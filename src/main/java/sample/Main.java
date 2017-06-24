package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.HashMap;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();

        ObservableMap<String, Double> observableMap = FXCollections.observableHashMap();

        RadarChart radarChart = new RadarChart();
        radarChart.setData(observableMap);

        observableMap.put("Grapefruit", 1d);
        observableMap.put("Oranges", 2d);
        observableMap.put("Plums", 1d);
        observableMap.put("Pears", 2d);
        observableMap.put("Apples", 3d);

        radarChart.prefWidthProperty().bind(((GridPane) root).prefWidthProperty());
        radarChart.prefHeightProperty().bind(((GridPane) root).prefHeightProperty());

        ((GridPane) root).add(radarChart, 0, 0);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
