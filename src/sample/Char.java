package sample;

public class Char {
    private int [][] imageArray;
    private char name;

    public Char(int[][] imageArray, char name) {
        this.imageArray = imageArray;
        this.name = name;
    }

    public int[][] getImageArray() {
        return imageArray;
    }

    public char getName() {
        return name;
    }
}
