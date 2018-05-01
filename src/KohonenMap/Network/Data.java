package KohonenMap.Network;

public class Data {//Данные

    private String[] name; // Массив парамтеров параметра
    private double[][] data; //[номер параметр][номер входного вектора]
    private double[][] normilized; //[номер параметр][номер входного вектора]

    private static double[] as;//временные переменные для денормализации данных
    private static double[] bs;//

    public Data(String[] name, double[][] data) {
        this.name = name;
        this.data = data;

        as = new double[data.length];
        bs = new double[data.length];

        normilized = new double[data.length][data[0].length];

        normalize();
    }

    public String[] getName() {
        return name;
    }

    public double[][] getData() {
        return data;
    }

    public double[][] getNormalized() {
        return normilized;
    }

    public static double denormalise(double value, int j) {
        return (value - bs[j]) / as[j];
    }// Денормализация данных

    private double[] getColumn(int j) {

        double[] values = new double[data.length];

        for (int i = 0; i < data.length; i++) {
            values[i] = data[i][j];
        }

        return values;
    }

    private void normalize() {// Нормализация данных

        double min, max;

        for (int j = 0; j < data[0].length; j++) {

            min = data[0][j];
            max = data[0][j];
            double[] column = getColumn(j);

            for (int i = 1; i < column.length; i++) {
                if (data[i][j] > max) {
                    max = data[i][j];
                } else if (data[i][j] < min) {
                    min = data[i][j];
                }
            }

            as[j] = 1 / (max - min);
            bs[j] = -min / (max - min);

            for (int i = 0; i < column.length; i++) {
                normilized[i][j] = data[i][j] * as[j] + bs[j];
            }
        }
    }


    public static Data testData() {
        return new Data(
                new String[]  {// Параметры
                        "Масса",
                        "Длина",
                        "Ширина",
                        "Мощность двигателя",
                        "Скорость"
                },
                new double[][]{// Входные вектора
                        {5.4,  4020, 2060, 57,  37}, // (легкий)  PzKpfw I
                        {8.9,  4810, 2223, 140, 40}, // (легкий)  PzKpfw II
                        {19.5, 5380, 2910, 285, 60}, // (легкий)  PzKpfw III
                        {13.8, 5200, 2470, 300, 60}, // (легкий)  Т-50
                        {9.2,  4285, 2348, 140, 42}, // (легкий)  Т-70
                        {25,   5890, 2880, 300, 40}, // (средний) PzKpfw IV
                        {44.8, 6870, 3270, 700, 55}, // (тяж/ср)  «Пантера»
                        {56,   6316, 3705, 700, 44}, // (тяжелый) «Тигр»
                        {68,   7380, 3755, 700, 38}, // (тяжелый) «Королевский тигр»
                        {30,   5920, 3000, 500, 54}, // (средний) Т-34
                        {31.8, 6070, 3180, 500, 60}, // (средний) Т-44
                        {47.5, 6675, 3320, 600, 34}, // (тяжелый) КВ-1
                        {44.2, 6770, 3070, 520, 37}, // (тяжелый) ИС-1
                        {46,   6770, 3070, 520, 37}, // (тяжелый) ИС-2
                        {49,   6900, 3150, 520, 40}, // (тяжелый) ИС-3
                }
        );
    }
}
