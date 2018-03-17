package sample;

public class Neuron {
    String name;
    int [][] input;
    int output;
    int [][] memory;

    public Neuron(String name, int[][] input, int output) {
        this.name = name;
        this.input = input;
        this.output = output;
    }
}
