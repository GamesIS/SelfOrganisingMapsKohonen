package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.controller.ListImagesController;
import sample.model.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;

public class Main extends Application {
    public static final String RESOURCES_PATH = new File("resources").getAbsolutePath();
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Image> imageData = FXCollections.observableArrayList();
    private NeuralNetwork neuralNetwork;

    public Main() {
        neuralNetwork = new NeuralNetwork();
        neuralNetwork.load();
        System.out.println(neuralNetwork.activate(35));
        //System.exit(1);
        loadImages();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public ObservableList<Image> getImageData() {
        return imageData;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("NeuralNetwork");

        initRootLayout();

        showListImages();

    }

    public void loadImages(){
        //ObservableList<Image> list = FXCollections.observableArrayList();
        File dir = new File(RESOURCES_PATH);
        if(dir.isDirectory())
        {
            // получаем все вложенные объекты в каталоге
            for(File item : dir.listFiles()){

                if(!item.isDirectory()){
                    //System.out.println(item.getName() + "  \tфайл");
                    try {
                        BufferedImage bufferedImage = ImageIO.read(new File(item.getAbsolutePath()));
                        int [][] tmp = imageToArray(bufferedImage);
                        imageData.add(new Image(item.getPath(), item.getName(), tmp/*imageToArray(bufferedImage)*/));
                        neuralNetwork.calculate(tmp);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else{
                    //System.out.println(item.getName() + "\tкаталог");
                }
            }
        }

        FXCollections.sort(imageData, new Comparator<Image>() {
            public int compare(Image o1, Image o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    public int[][] imageToArray(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if(image.getRGB(col,row) < -1){
                    result[row][col] = 1;
                }
                else {
                    result[row][col] = 0;
                }
            }
        }

        /*for(int i = 0; i < result.length; i++){ // ВЫВОД Ч/Б ИЗОБРАЖЕНИЙ В ВИДЕ ТЕКСТА
            for (int j = 0; j < result[i].length; j++){
                System.out.print(" " + result[i][j]);
            }
            System.out.println();
        }*/
        return result;
    }

    public void /*int[][]*/ imageToArray2(BufferedImage image){
        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        for(int i = 0; i < result.length; i++){
            for (int j = 0; j < result[i].length; j++){
                System.out.print(result[i][j]);
            }
            System.out.println();
        }
        //return result;
    }


    public void initRootLayout() {
        try {
            // Загружаем корневой макет из fxml файла.
            //FXMLLoader loader = new FXMLLoader();
            //loader.setLocation(Main.class.getResource("sample.fxml"));
            rootLayout = (BorderPane) FXMLLoader.load(getClass().getResource("sample.fxml"));

            // Отображаем сцену, содержащую корневой макет.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showListImages(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("listimages.fxml"));
            AnchorPane listImage = (AnchorPane) loader.load();

            rootLayout.setCenter(listImage);

            ListImagesController controller = loader.getController();
            controller.setMain(this);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

}
