package sample;

import sample.model.NeuroProperties;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NeuralNetwork {
    private static final int HIDDEN_LAYERS = 1;

    private static final int COUNT_HIDDEN_NEURON = 1500 + 1;//Рандомно взял, но больше начального, из-за нейрона смещения + 1
    private static final int COUNT_INPUT_NEURON = 900 + 1;//Из-за нейрона смещения + 1
    private static final int COUNT_OUTPUT_NEURON = 33;//Из-за кол-ва букв

    private static final int COUNT_LAYERS = HIDDEN_LAYERS + 2;// Скрытые слои + входной и выходной слой

    private static final int COUNT_EDGE = COUNT_LAYERS - 1;// Количество слоев ребер = Количество слоев нейронов - 1

    private static final int COUNT_EPOCH = 30;// Количество слоев ребер = Количество слоев нейронов - 1

    private static final double BIAS_NEURON = 1;// Нейрон смещения /*их можно размещать на входном слое и всех скрытых слоях, но никак не на выходном слое*/

    private static final double d = 0.002; // параметр наклона сигмоидальной функции
    private static final double n = 0.1; // коэффициент скорости обучения

    private static final double SENSITIVITY = 0.02;

    private int iteration; //Сколько раз нейросеть прошла по trainingSet

    private double[][][] weights;//[Количество слоев ребер][Номер левого нейрона][Номер правого нейрона]

    private double[][] neurons;//[Номер слоя][Номер нейрона]
    private double[] input;// Входные значения
    private double[] output;// Выходные значения
    private int [] layersLength;// Массив хранящий длину каждого слоя

    private int trainingSet;//Нужно рандомизировать подачу тренировочных данных
    private int verifyingSet;

    public NeuralNetwork() {
        layersLength = new int[COUNT_LAYERS];
        layersLength[0] = COUNT_INPUT_NEURON;
        for (int i = 1; i < HIDDEN_LAYERS + 1; i++){
            layersLength[i] = COUNT_HIDDEN_NEURON;
        }
        layersLength[layersLength.length - 1] = COUNT_OUTPUT_NEURON;
    }

    public void load() {
        if (NeuroProperties.isSaveProperty()) {
            NeuroProperties neuroProperties = NeuroProperties.loadProperty();
            weights = neuroProperties.getWeightsArray();
            iteration = neuroProperties.getIteration();
        } else {
            weights = new double[COUNT_EDGE][maxLengthLayers()][maxLengthLayers()];
            NeuralNetwork.fillRandomDouble(weights, layersLength);
            NeuroProperties.saveProperty(new NeuroProperties(weights, iteration));
        }
    }

    private static int maxLengthLayers(){
        return COUNT_HIDDEN_NEURON>=COUNT_INPUT_NEURON?COUNT_HIDDEN_NEURON:COUNT_INPUT_NEURON;
    }

    public void calculate(int [][] imageArray){
        double[] tmpArray = convertBinaryArrayToSingle(imageArray);
        loadInput(tmpArray);//Инициированы входные значения, там пропускаем через функцию активации
        neurons = new double[COUNT_LAYERS][maxLengthLayers()];
        neurons[0] = input;
        int lastTmp = layersLength[0]; //Количество нейронов рассматриваемого слоя
        int firstTmp; //сохраним сюда индекс первого нейрона рассматриваемого слоя
        double res; // Переменная для подсчета взвешенной суммы
        for (int i = 1; i < layersLength.length; i++ ) {// Пробегаемся по всем слоям, начиная со 2
            neurons[i-1][0] = BIAS_NEURON;
            for(int j = 0; j < layersLength[i]; j++){//Пробегаемся по всем нейронам слоя
                res = 0;
                /**
                 * Считаем для данного нейрона взвешенную сумму
                 * пробегаясь по нейронам левого слоя
                 * */
                //TODO еще раз разобраться с нейроном смещения
                for(int x = 0; x < layersLength[i-1]; x++){
                    res += neurons[i-1][x]*weights[i-1][x][j];
                }
                /**
                 * Из-за того что res получался всегда 1
                 * нужна использовать нормализацию*/
                //double norm = normalization(res, i);
                neurons[i][j]= activate(res);//Прогоняем результат через активационную функцию
            }
        }
        //System.out.println();
    }

    /**
     * Используется метод обратного распространения //Возможно их несколько вариантов
     * */
    public void study(List<Char> chars) {
        for (int epoch = 0; epoch < COUNT_EPOCH; epoch++){
            for(Char ch: chars){
                calculate(ch.getImageArray());//TODO тут возможна ошибка
                int numSym = getOutputNumber(ch.getName());
                int lastLayer = layersLength.length - 1;// номер последнего слоя
                double expectedResult;//Ожидаемый результат
                double error;//Ошибка(Ожидаемый минус текущий)
                double weights_delta;//Дельта весов (ошибка*производную активационной функции)
                double derivative;//производная сигмоида = sigmoid(x)-(1-sigmoid(x))
                double tmp;//временная переменная для хранения значения активационной функции

                double newWeight;
                /**
                 * смысл в том, что мы сначала рассчитываем ошибку для конечного выходного нейрона, а потом передаем ее обратно
                 * рассчитывая значения для предыдущих нейронов, на основе этого изменяем веса
                 * */
                double[] errorsRightLayer = new double[layersLength[lastLayer]];
                double[] errorsLeftLayer = new double[layersLength[lastLayer-1]];
                //boolean res;

                //double difOut;
                for(int j = lastLayer; j >= 0; j--) {//Начинаем с конечного слоя. Текущий (правый) слой
                    //if(j != lastLayer) errorsLeftLayer = new double[layersLength[j]];
                    for(int i = 0; i < layersLength[j]; i++){//Пробегаемся по всем нейронам СНАЧАЛА текущего(правого) ПОТОТМ ЛЕВОГО слоя
                        if(j == lastLayer){
                            if(i == numSym){expectedResult = 1.0D;}
                            else {expectedResult = 0.0D;}
                            //errorsRightLayer[i] = Math.abs(neurons[lastLayer][i] - expectedResult);// Здесь рассчиытваем ошибки нейронов //TODO ВОЗМОЖНО АКТИВАЦИОННУЮ ФУНКЦИЮ НАДО ПРИМЕНЯТЬ
                            errorsRightLayer[i] = (expectedResult - neurons[lastLayer][i]) * neurons[lastLayer][i] * (1 - neurons[lastLayer][i]);// TODO ВОЗМОЖНО НУЖНА ОТДЕЛЬНАЯ ФУНКЦИЯ
                            /**
                             * ошибка_левого_нейрона_версии_2 = (ожидаемый - текущий) * текущий * (1 - текущий)
                             * текущий - результат уже пропущенный через активационную функцию для нейрона у которого рассчитываем ошибку
                             * http://microtechnics.ru/obuchenie-nejronnoj-seti-algoritm-obratnogo-rasprostraneniya-oshibok/?fdx_switcher=true
                             * */
                            if(Math.abs(errorsRightLayer[i]) <= SENSITIVITY) break;// Если верно не корректируем веса TODO ОШИБКА СЧИТАЕТСЯ ОБЩАЯ У МЕНЯ ОШИБКА ЭТО ДЕЛЬТА ЭТО УСЛОВИЕ ДОЛЖНО БЫТЬ ВЫШЕ
                        }
                        else {
                            /**
                             * ошибка для внутренних нейронов рассчитывается
                             * error = вес_ребра_до_нейрона_справа(1)*ошибка_нейрона_справа(1) ... + вес_ребра_до_нейрона_справа(n)*ошибка_нейрона_справа(n)
                             * http://robocraft.ru/blog/algorithm/560.html
                             *
                             * ошибка_левого_нейрона_версии_2 =  сумма_их_всех((ошибка_нейрона_справа_K*вес_до_нейрона_справа_К)) * текущий * (1 - текущий)
                             * http://microtechnics.ru/obuchenie-nejronnoj-seti-algoritm-obratnogo-rasprostraneniya-oshibok/?fdx_switcher=true
                             * дельта_весов = коэффициент_скорости_обучения * ошибка_правого_нейрона_версии_2 * результат_левого_нейрона_после_активационной_функции(текущий)
                             * новый вес = вес + дельта_весов;
                             * */
                            if (j!=0){
                                for(int k = 0; k < errorsRightLayer.length; k++){
                                    errorsLeftLayer[i] += errorsRightLayer[k] * weights[j][i][k];
                                    //errorsLeftLayer[i] += errorsRightLayer[k] * weights[j][i][k];//TODO ВОЗМОЖНО J-1 НЕВЕРНО
                                }
                                errorsLeftLayer[i] *= neurons[j][i] * (1 - neurons[j][i]);
                            }
                            //if(j < lastLayer-1) errorsRightLayer = errorsLeftLayer;//TODO УСЛОВИЕ БЫЛО ЗДЕСЬ
                            for(int k = 0; k < errorsRightLayer.length; k++){
                        /*tmp = activate(neurons[j+1][k]);
                        derivative = tmp * (1 - tmp);//производная сигмоида = sigmoid(x)*(1-sigmoid(x))*/
                                weights_delta = n * errorsRightLayer[k] * neurons[j][i];//TODO И ПОХОЖЕ КОРРЕКТИРОВКУ ВЕСОВ МЕНЯТЬ
                                weights[j][i][k] +=  weights_delta;
                                //System.out.println(weights[j][i][k]);
                                //double[][][] weights;//[Количество слоев ребер][Номер левого нейрона][Номер правого нейрона]
                                /**
                                 *формула для расчета нового веса   newWeight = oldWeight + коэффициент_скорости_обучения * значения_выходного_нейрона(слева) * дельту_весов(нейрона справа)
                                 * */
                            }
                        }
                    }
                    if(j < lastLayer) {
                        errorsRightLayer = errorsLeftLayer;
                        if(j - 1 > 0) {
                            errorsLeftLayer = new double[layersLength[j-1]];
                        }
                    }
                    //System.out.println();
                }
            }
            Collections.shuffle(chars);
        }
    }

    private void loadInput(double[] newInput) {
        //input = new double[newInput.length];
        input = newInput;
        for (int i = 0; i < newInput.length; i++) {
            input[i] = activate(newInput[i]);//Специально использовал функцию активации, хоть значения 1 и 0, чтобы потом не забыть

        }
    }

    public double normalization(double x, int numLayer){
        return x/(layersLength[numLayer]);
    }

    public double activate(double x)// Функция активации (Нормальзации значения [0;1])
    {
        return (1 / (1 + Math.pow(Math.E, -(d*x))));//Сигмоид
    }

    private double[] convertBinaryArrayToSingle(int [][] imageArray){
        int k = 0;
        double[] tmpArray = new double[COUNT_INPUT_NEURON];//Возможно заменить на imageArray.length * imageArray[0].length
        //tmpArray[0] = BIAS_NEURON;//Инициализация нейрона смещения
        for (int i = 0; i < imageArray.length; i++){
            for(int j = 0; j < imageArray[i].length; j++){
                tmpArray[++k] = (double) imageArray[i][j];
            }
        }
        return tmpArray;
    }

    public static void fillRandomDouble(double[][][] weightsArray, int[] layersLength) {
        double rangeMin = 0.0f;
        double rangeMax = 1.0f;
        Random r = new Random();
        for (int i = 0; i < COUNT_LAYERS - 1; i++) {//Бежим по слоям
            for (int j = 0; j < layersLength[i]; j++) {
                for (int k = 0; k < layersLength[i+1]; k++) {
                    weightsArray[i][j][k] = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
                    //System.out.print(weightsArray[i][j][k]);
                }
                //System.out.println();
            }
            /*System.out.println();
            System.out.println();
            System.out.println();*/
        }
    }

    public static char getOutputChar(int number) {
        switch (number) {
            case 0:
                return 'А';
            case 1:
                return 'Б';
            case 2:
                return 'В';
            case 3:
                return 'Г';
            case 4:
                return 'Д';
            case 5:
                return 'Е';
            case 6:
                return 'Ё';
            case 7:
                return 'Ж';
            case 8:
                return 'З';
            case 9:
                return 'И';
            case 10:
                return 'Й';
            case 11:
                return 'К';
            case 12:
                return 'Л';
            case 13:
                return 'М';
            case 14:
                return 'Н';
            case 15:
                return 'О';
            case 16:
                return 'П';
            case 17:
                return 'Р';
            case 18:
                return 'С';
            case 19:
                return 'Т';
            case 20:
                return 'У';
            case 21:
                return 'Ф';
            case 22:
                return 'Х';
            case 23:
                return 'Ц';
            case 24:
                return 'Ч';
            case 25:
                return 'Ш';
            case 26:
                return 'Щ';
            case 27:
                return 'Ъ';
            case 28:
                return 'Ы';
            case 29:
                return 'Ь';
            case 30:
                return 'Э';
            case 31:
                return 'Ю';
            case 32:
                return 'Я';
        }
        throw new IllegalArgumentException("Выход за пределы алфавита");
    }

    public static char getOutputNumber(char ch) {
        switch (ch) {
            case 'А':
                return 0;
            case 'Б':
                return 1;
            case 'В':
                return 2;
            case 'Г':
                return 3;
            case 'Д':
                return 4;
            case 'Е':
                return 5;
            case 'Ё':
                return 6;
            case 'Ж':
                return 7;
            case 'З':
                return 8;
            case 'И':
                return 9;
            case 'Й':
                return 10;
            case 'К':
                return 11;
            case 'Л':
                return 12;
            case 'М':
                return 13;
            case 'Н':
                return 14;
            case 'О':
                return 15;
            case 'П':
                return 16;
            case 'Р':
                return 17;
            case 'С':
                return 18;
            case 'Т':
                return 19;
            case 'У':
                return 20;
            case 'Ф':
                return 21;
            case 'Х':
                return 22;
            case 'Ц':
                return 23;
            case 'Ч':
                return 24;
            case 'Ш':
                return 25;
            case 'Щ':
                return 26;
            case 'Ъ':
                return 27;
            case 'Ы':
                return 28;
            case 'Ь':
                return 29;
            case 'Э':
                return 30;
            case 'Ю':
                return 31;
            case 'Я':
                return 32;
        }
        throw new IllegalArgumentException("Нет такой буквы");
    }

    public void save(){
        NeuroProperties.saveProperty(new NeuroProperties(weights, iteration));
    }

    public void writeResultsForSym(char ch, int[][] imageArray){
        calculate(imageArray);//TODO тут возможна ошибка
        for (int i = 0; i < COUNT_OUTPUT_NEURON; i++){
            System.out.println(getOutputChar(i) + " " + neurons[COUNT_LAYERS - 1][i]);
        }
    }
}
