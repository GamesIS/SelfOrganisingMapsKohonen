package sample.controller;

import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Main;
import sample.NeuralNetwork;
import sample.model.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class ListImagesController {
    private static ListImagesController controller;

    @FXML
    private TableView<Image> imageTable;
    @FXML
    private TableColumn<Image, String> pathColumn;
    @FXML
    private TableColumn<Image, String> nameColumn;

    @FXML
    private ListView<Image> nameList;

    @FXML
    private ListView<String> resultList;

    @FXML
    private ImageView imageView;

    @FXML
    private Canvas canvas;

    @FXML
    private Canvas drawCanvas;

    @FXML
    public Label percent;

    @FXML
    public Label nameLabel;

    @FXML
    public Button studying;
    @FXML
    public javafx.scene.image.Image image;

    @FXML
    public LineChart<Number, Number> convergence;

    @FXML
    NumberAxis xAxisIteration;
    @FXML
    NumberAxis yAxisError;

    private static ObservableList<String> obResList;

    @FXML
    public void initialize(){
        convergence.setTitle("Ошибка от эпохи");

        studying.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                main.getNeuralNetwork().study(main.getChars());
                main.getNeuralNetwork().save();
            }
        });


        series1 = new XYChart.Series();
        series1.setName("Portfolio 1");

        convergence.getData().addAll(series1);
    }

    public XYChart.Series series1;

    private Main main;

    public ListImagesController() {
        controller = this;
    }

    public static ListImagesController getController() {
        return controller;
    }
    /*    @FXML void initialize(){
        pathColumn.setCellValueFactory(cellData -> cellData.getValue().pathProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    }*/


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

    private void initDraw(GraphicsContext gc){
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.setFill(Color.LIGHTGRAY);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);

        gc.fill();
        gc.strokeRect(
                0,              //x of the upper left corner
                0,              //y of the upper left corner
                canvasWidth,    //width of the rectangle
                canvasHeight);  //height of the rectangle

        gc.setFill(Color.RED);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);
    }

    public void setMain(Main main){
        this.main = main;

        imageTable.setItems(main.getImageData());

        imageTable.setVisible(false);
        nameList.setItems(main.getImageData());
        nameList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        imageView.setSmooth(false);


        xAxisIteration.setLabel("Epoch");
        yAxisError.setLabel("Error");

        drawCanvas.setVisible(false);

        GraphicsContext gc = drawCanvas.getGraphicsContext2D();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>(){

                    @Override
                    public void handle(MouseEvent event) {
                        gc.beginPath();
                        gc.moveTo(event.getX(), event.getY());
                        gc.stroke();
                    }
                });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>(){

                    @Override
                    public void handle(MouseEvent event) {
                        gc.lineTo(event.getX(), event.getY());
                        gc.stroke();
                    }
                });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>(){

                    @Override
                    public void handle(MouseEvent event) {

                    }
                });


        //.setLabel("Month");



        /*XYChart.Series series2 = new XYChart.Series();
        series2.setName("Portfolio 2");

        series2.getData().add(new XYChart.Data(5, 6));
        series2.getData().add(new XYChart.Data(12, 7));
        series2.getData().add(new XYChart.Data(22, 8));*/
        //convergence.getData().addAll(series2);
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
        resultList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

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
                main.getNeuralNetwork().calculate(imageArray);

                DecimalFormat decimalFormatter = new DecimalFormat("###.###");
                decimalFormatter.setMinimumIntegerDigits(2);
                decimalFormatter.setMinimumFractionDigits(3);

                obResList = FXCollections.observableArrayList();
                for(int i = 0; i <= 32; i++){
                    obResList.add("" + NeuralNetwork.getOutputChar(i) + " = "+ decimalFormatter.format(main.getNeuralNetwork().getError(i)) +"%");
                }
                resultList.setItems(obResList);

            }
        });
    }
}
