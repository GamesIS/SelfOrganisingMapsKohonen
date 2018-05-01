package KohonenMap.gui;

import KohonenMap.Network.Data;
import KohonenMap.Network.Grid;
import KohonenMap.Network.Trainer;
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

    public Main main;



    @FXML
    public void initialize() {
        trainButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                trainer.start();
                //trainer.run();
            }
        });
        reset.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
            }
        });
        property.setOnAction((e) -> {
            HexDrawer.repaint(trainer.getGrid(), property.getSelectionModel().getSelectedIndex()/*mapProp.getSelectedIndex(), currentPoint*/);
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

    private static final int gridHeight = 40;
    private static final int gridWidth = (int) ((double) gridHeight / 9 * 16);
    private static final int hexHeight = 13;

    public void startNetwork(){
        trainer = new Trainer(Data.testData());
        trainer.setGrid(new Grid(gridWidth, gridHeight, trainer.getInputs().getData()[0].length));

        HexDrawer.setProperty(grid, gridWidth, gridHeight, hexHeight, true);
        ObservableList<String> properties = FXCollections.observableArrayList();

        properties.addAll(trainer.getInputs().getName());

        property.setItems(properties);
        property.getSelectionModel().selectFirst();

        HexDrawer.paint(trainer.getGrid(), 0);
    }


    public void setMain(Main main) {
        this.main = main;
    }
}
