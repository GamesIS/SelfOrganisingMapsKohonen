package KohonenMap.Network;

public class Grid {// Решетка

    private int width, height;
    private Node[][] matrix;//Матрица нейронов(узлов)

    public Grid(int width, int height, int numWeights) {

        this.width = width;
        this.height = height;

        matrix = new Node[this.width][this.height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                matrix[i][j] = new Node(numWeights);
                matrix[i][j].setX(i);
                matrix[i][j].setY(j);
            }
        }
    }

    public Node getNode(int x, int y) {
        return matrix[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Node getBMU(double[] inputVector) {// Находим нейрон победитель(Наиболее близкий к входному вектору)

        Node bmu = matrix[0][0];
        double curDist, bestDist = Trainer.euclideanDist(inputVector, bmu.getWeights());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                curDist = Trainer.euclideanDist(inputVector, matrix[x][y].getWeights());

                if (curDist < bestDist) {
                    bmu = matrix[x][y];
                    bestDist = curDist;
                }
            }
        }

        return bmu;
    }
}
