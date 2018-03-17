package sample;

import sample.model.NeuroProperties;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Random;

public class NeuralNetwork {
    private static final int COUNT_NEURON = 900;
    private static final int HIDDEN_LAYERS = 1;
    private static final int COUNT_NEURON_ON_HIDDEN_LAYERS = 1500;//Рандомно взял, но больше начального
    private static final int COUNT_OUTPUT_NEURON = 33;//Из-за кол-ва букв

    private int iteration; //Сколько раз нейросеть прошла по trainingSet
    private int[] epoch;

    private double[][][] weights;//[Номер слоя][Номер левого нейрона][Номер правого нейрона]

    private double[] input;// Входные значения
    private double[] output;// Выходные значения
    private int trainingSet;//Нужно рандомизировать подачу тренировочных данных
    private int verifyingSet;

    public void start() {
        if (NeuroProperties.isSaveProperty()) {
            NeuroProperties neuroProperties = NeuroProperties.loadProperty();
            weights = neuroProperties.getWeightsArray();
            iteration = neuroProperties.getIteration();
        } else {
            weights = new double[HIDDEN_LAYERS][COUNT_NEURON_ON_HIDDEN_LAYERS][COUNT_NEURON_ON_HIDDEN_LAYERS];
            NeuralNetwork.fillRandomDouble(weights);
            NeuroProperties.saveProperty(new NeuroProperties(weights, iteration));
        }
    }

    public void loadInput(double[] newInput) {
        input = new double[newInput.length];
        for (int i = 0; i < newInput.length; i++) {
            input[i] = activate(newInput[i]);//Специально использовал функцию активации, хоть значения 1 и 0, чтобы потом не забыть
        }
    }

    public double activate(double x)// Функция активации (Нормальзации значения [0;1])
    {
        return (1 / (1 + Math.pow(Math.E, -x)));//Сигмоид
    }

    public static void fillRandomDouble(double[][][] weightsArray) {
        double rangeMin = 0.0f;
        double rangeMax = 1.0f;
        Random r = new Random();
        for (int i = 0; i < weightsArray.length; i++) {
            for (int j = 0; j < weightsArray[i].length; j++) {
                for (int k = 0; k < weightsArray[i][j].length; k++) {
                    weightsArray[i][j][k] = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
                }
            }
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
            case 'A':
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

    public void study() {
        throw new NotImplementedException();
    }
}
