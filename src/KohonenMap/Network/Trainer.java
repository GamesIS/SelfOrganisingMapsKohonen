package KohonenMap.Network;

import KohonenMap.gui.HexDrawer;
import KohonenMap.gui.ListImagesController;

import java.util.concurrent.ThreadLocalRandom;

public class Trainer implements Runnable {

    private static final double _ETA            = 0.1;
    private static final int    _EPOCH          = 1000;
    private static final int    ITERATION_DELAY = 5;

    private Grid grid;
    private Data inputs;

    private static int iteration = 0;
    private static boolean running = false;

    public Trainer(Data inputs) {
        this.inputs = inputs;
    }

    public static boolean isRunning() {
        return running;
    }

    public Data getInputs() {
        return inputs;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }



    public void start() {
        running = true;
        Thread thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        running = false;
    }

    public void stop() {
        running = false;
        iteration = 0;
    }

    private void train() {

        int gridWidth = grid.getWidth();
        int gridHeight = grid.getHeight();

        double _SIGMA = Math.max(gridWidth, gridHeight) / 2;
        double _LAMBDA = _EPOCH / Math.log(_SIGMA);

        double eta, sigma, theta;

        for ( ; iteration < _EPOCH && running; iteration++) {

            double[] curInput = inputs.getNormalized()[ThreadLocalRandom.current().nextInt(0, inputs.getData().length)];//Рандомный вектор берем
            Node bmu = grid.getBMU(curInput);

            eta = _ETA * Math.exp(-(double) iteration / _EPOCH);
            sigma = _SIGMA * Math.exp(-iteration / _LAMBDA);

            for (int x = 0; x < gridWidth; x++) {
                for (int y = 0; y < gridHeight; y++) {

                    Node temp = grid.getNode(x, y);
                    theta = Math.exp(-Trainer.euclideanDist(bmu, temp) / (2 * sigma * sigma));
                    temp.adjustWeights(curInput, eta, theta);
                }
            }

            try {
                Thread.sleep(ITERATION_DELAY);
            } catch (InterruptedException ignored) {
                ignored.printStackTrace();
            }
        }
        synchronized (grid){
            synchronized (ListImagesController.getController().property){
                HexDrawer.repaint(grid, ListImagesController.getController().property.getSelectionModel().getSelectedIndex());
            }
        }
        running = false;
    }

    public static double euclideanDist(double[] vector1, double[] vector2) {

        if (vector1.length != vector2.length)
            return -1;

        double summation = 0, temp;
        for (int i = 0; i < vector1.length; i++) {
            temp = vector1[i] - vector2[i];
            temp *= temp;
            summation += temp;
        }

        return summation;
    }

    public static double euclideanDist(Node node1, Node node2) {

        double diff1 = node1.getX() - node2.getX();
        diff1 *= diff1;
        double diff2 = node1.getY() - node2.getY();
        diff2 *= diff2;

        return diff1 + diff2;
    }


    @Override
    public void run() {
        running = true;
        train();
    }
}
