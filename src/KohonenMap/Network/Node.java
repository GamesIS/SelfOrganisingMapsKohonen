package KohonenMap.Network;

public class Node {//Узел

    private double[] weights;// Веса
    private int x, y;

    public Node(int numWeights) {

        weights = new double[numWeights];

        for (int i = 0; i < numWeights; i++) {
            weights[i] = Math.random();//Инициализация рандомных весов
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double[] getWeights() {
        return weights;
    }

    // eta - скорость обучения
    // theta - влияние радиуса
    public void adjustWeights(double[] input, double eta, double theta)
    {
        for (int w = 0; w < weights.length; w++) {
            weights[w] += theta * eta * (input[w] - weights[w]);
        }
    }
}
