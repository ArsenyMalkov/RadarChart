package sample;

import com.sun.javafx.scene.control.skin.LabeledText;
import javafx.beans.*;
import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.*;

public class RadarChart extends Pane {

    private double radarRadius = 100d;

    private ObservableMap<String, Double> data;

    private MapChangeListener<String, Double> mapChangeListener = new MapChangeListener<String, Double>() {
        @Override
        public void onChanged(Change<? extends String, ? extends Double> change) {
            if (change.wasAdded()) {
                dataItemAdded(change.getKey(), change.getValueAdded());
            }
            if (change.wasRemoved()) {
                dataItemRemoved(change.getKey(), change.getValueRemoved());
            }
        }
    };

    private void dataItemRemoved(String key, Double value) {

    }

    private void dataItemAdded(String key, Double value) {
        getChildren().clear();

        if (data.size() < 3) {
            return;
        }

        double centerX = radarRadius;
        double centerY = radarRadius;

        double offsetRadius = 20d;

        int sectorCount = 3;

        int currentAngle = 0;
        int sectorAngle = 360 / data.size();

        Polygon mainPolyline = new Polygon();
        mainPolyline.setStroke(Color.DARKBLUE);
        mainPolyline.setStrokeWidth(4d);
        mainPolyline.setOpacity(0.5d);
        mainPolyline.setFill(Color.BLUE);

        for (int sector = 0; sector < sectorCount; sector++) {
            Polyline sectorsPolyline = new Polyline();

            for (int angle = 0; angle <= 360; angle += sectorAngle) {
                double startX = centerX + Math.sin(Math.toRadians(angle)) * offsetRadius;
                double startY = centerY + Math.cos(Math.toRadians(angle)) * offsetRadius;
                double endX = centerX + Math.sin(Math.toRadians(angle)) * radarRadius;
                double endY = centerY + Math.cos(Math.toRadians(angle)) * radarRadius;

                double spokeRadius = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
                double currentSectorRadius = (spokeRadius / (sectorCount - 1)) * sector;

                sectorsPolyline.getPoints().addAll(
                        startX + Math.sin(Math.toRadians(angle)) * currentSectorRadius,
                        startY + Math.cos(Math.toRadians(angle)) * currentSectorRadius);
            }

            RadarChart.this.getChildren().addAll(sectorsPolyline);
        }

        for (String name : data.keySet()) {
            Polyline spokePolyline = new Polyline();

            double startX = centerX + Math.sin(Math.toRadians(currentAngle)) * offsetRadius;
            double startY = centerY + Math.cos(Math.toRadians(currentAngle)) * offsetRadius;
            double endX = centerX + Math.sin(Math.toRadians(currentAngle)) * radarRadius;
            double endY = centerY + Math.cos(Math.toRadians(currentAngle)) * radarRadius;

            spokePolyline.getPoints().addAll(
                    startX, startY,
                    endX, endY);
            RadarChart.this.getChildren().addAll(spokePolyline);

            double spokeRadius = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
            double currentSectorRadius = (spokeRadius / (sectorCount - 1)) * (data.get(name) - 1);

            Circle circle = new Circle();
            circle.setCenterX(startX + Math.sin(Math.toRadians(currentAngle)) * currentSectorRadius);
            circle.setCenterY(startY + Math.cos(Math.toRadians(currentAngle)) * currentSectorRadius);
            circle.setRadius(5d);
            circle.setStroke(Color.ORANGE);
            circle.setStrokeWidth(2d);
            circle.setFill(Color.GREEN);
            RadarChart.this.getChildren().add(circle);
            circle.toFront();

            mainPolyline.getPoints().addAll(
                    startX + Math.sin(Math.toRadians(currentAngle)) * currentSectorRadius,
                    startY + Math.cos(Math.toRadians(currentAngle)) * currentSectorRadius);

            Text text = new Text();
            text.setText(name);
            text.setX(endX);
            text.setY(endY);
            RadarChart.this.getChildren().add(text);

            currentAngle += sectorAngle;
        }
        RadarChart.this.getChildren().addAll(mainPolyline);
        mainPolyline.toBack();
    }

    public void setData(ObservableMap<String, Double> data) {
        this.data = data;
        this.data.addListener(mapChangeListener);
    }
}
