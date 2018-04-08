package sample;

public class Char {
    private int [][] imageArray;
    private char name;
    private int num;

    public Char(int[][] imageArray, char name) {
        this.imageArray = imageArray;
        this.name = name;
        this.num = NeuralNetwork.getOutputNumber(name);
    }

    public int[][] getImageArray() {
        return imageArray;
    }

    public char getName() {
        return name;
    }

    public int getNum() {
        return num;
    }
}
