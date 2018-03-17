package sample.controller;

import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import sample.Main;
import sample.model.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


public class ListImagesController {
    @FXML
    private TableView<Image> imageTable;
    @FXML
    private TableColumn<Image, String> pathColumn;
    @FXML
    private TableColumn<Image, String> nameColumn;

    @FXML
    private ListView<Image> nameList;

    @FXML
    private ImageView imageView;

    @FXML
    private Canvas canvas;

    @FXML
    public Label frameRate;
    @FXML
    public Label nameLabel;

    @FXML
    public LineChart<Number, Number> convergence; // Сходимость

    @FXML
    NumberAxis xAxisIteration;
    @FXML
    NumberAxis yAxisError;


    private Main main;

    public ListImagesController() {
    }

    @FXML void initialize(){
        pathColumn.setCellValueFactory(cellData -> cellData.getValue().pathProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    }


    private int i = 0;
    public void testNext(){
        int[][] imageArray = imageTable.getItems().get(i).getImageArray();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for(int i = 0; i < imageArray.length; i++){
            for (int j = 0; j < imageArray[i].length; j++){
                if(imageArray[j][i] == 0) gc.setFill(Color.WHITE);
                else gc.setFill(Color.BLACK);
                gc.fillRect(20*i,20*j,20,20);
            }
        }
        if(i!=imageTable.getItems().size()-1) i++;
        else i=0;
    }

    public void setMain(Main main){
        this.main = main;

        imageTable.setItems(main.getImageData());

        //imageTable.setVisible(false);
        nameList.setItems(main.getImageData());
        nameList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        imageView.setSmooth(false);

        xAxisIteration.setLabel("Iteration");
        yAxisError.setLabel("Error");
        //.setLabel("Month");

        convergence.setTitle("Сходимость");
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Portfolio 1");

        series1.getData().add(new XYChart.Data(0.02, 0.1));
        series1.getData().add(new XYChart.Data(2, 14));
        series1.getData().add(new XYChart.Data(3, 15));
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Portfolio 2");

        series2.getData().add(new XYChart.Data(5, 6));
        series2.getData().add(new XYChart.Data(12, 7));
        series2.getData().add(new XYChart.Data(22, 8));
        convergence.getData().addAll(series1);
        convergence.getData().addAll(series2);
        //System.out.println(imageView.isSmooth());

        /*Timeline timelines = new Timeline(new KeyFrame(
                Duration.millis(17),
                ae -> testNext()));
        timelines.setCycleCount(Animation.INDEFINITE);
        timelines.play();*/

        /*Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Test");
                    }
                }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
*/

        nameList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Image image = imageTable.getItems().get(newValue.intValue());
                imageView.setImage(image.getImage());
                GraphicsContext gc = canvas.getGraphicsContext2D();

                int[][] imageArray = image.getImageArray();
                for(int i = 0; i < imageArray.length; i++){
                    for (int j = 0; j < imageArray[i].length; j++){
                        if(imageArray[j][i] == 0) gc.setFill(Color.WHITE);
                        else gc.setFill(Color.BLACK);
                        gc.fillRect(20*i,20*j,20,20);

                    }
                }
            }


        });
    }
}
