package sample.controller;

import KohonenMap.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import sample.Main;


public class ListImagesController {
    private static ListImagesController controller;

    @FXML
    public Button reset;

    @FXML
    public ComboBox property;

    @FXML
    public Button clear;

    @FXML
    public Button trainButton;

    @FXML
    public Pane grid;

    @FXML
    public Label info;

    private Main main;


    private static ObservableList<String> obResList;

    @FXML
    public void initialize() {
        trainButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //Thread myThready = new Thread(trainer);	//Создание потока "myThready"
                //myThready.start();
                //trainer.start();
                trainer.run();
                //HexDrawer.paint(trainer.getLattice(), property.getSelectionModel().getSelectedIndex()/*mapProp.getSelectedIndex(), currentPoint*/);
            }
        });
        reset.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
            }
        });
        property.setOnAction((e) -> {
            //HexDrawer.repaint(trainer.getLattice(), property.getSelectionModel().getSelectedIndex()/*mapProp.getSelectedIndex(), currentPoint*/);
            HexDrawer.paint(trainer.getLattice(), property.getSelectionModel().getSelectedIndex()/*mapProp.getSelectedIndex(), currentPoint*/);
        });
        grid.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!trainer.isRunning()){
                    HexDrawer.increasedHex((int)event.getX(), (int)event.getY(), trainer, property.getSelectionModel().getSelectedIndex());
                }
            }
        });

    }

    public ListImagesController() {
        controller = this;
    }

    public static ListImagesController getController() {
        return controller;
    }


    private Trainer trainer;

    private final static Color COLOUR_BACKGROUND =  Color.WHITE;
    private final static int BORDERS = 15;
    private static int gridHeight = 20;
    private static int gridWidth = (int) ((double) gridHeight / 9 * 16);
    private static int hexHeight = 16;

    public void STARTTHISBULLSHIT(){
       trainer = new Trainer(Data.testData());

        trainer.setLattice(new Lattice(gridWidth, gridHeight, trainer.getInputs().getData()[0].length));

        HexDrawer.setContainer(grid);
        HexDrawer.setHeight(hexHeight);
        HexDrawer.setBorder(true);
        HexDrawer.setSize(gridWidth, gridHeight);


        ObservableList<String> properties = FXCollections.observableArrayList();

        properties.addAll(trainer.getInputs().getName());

        property.setItems(properties);
        property.getSelectionModel().selectFirst();
        //properties.sel

        HexDrawer.paint(trainer.getLattice(), 0/*mapProp.getSelectedIndex(), currentPoint*/);
    }


    public void setMain(Main main) {
        this.main = main;
    }
}
