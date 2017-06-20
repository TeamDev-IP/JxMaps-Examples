package com.teamdev.jxmaps.examples;

import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.javafx.MapView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Represents FXML control with MapViewer instance.
 */
public class MapViewFXControl implements Initializable {
    @FXML
    private MapView mapView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Creation of a JavaFX map view
        //final MapView mapView = new MapView();

        // Setting of a ready handler to MapView object. onMapReady will be called when map initialization is done and
        // the map object is ready to use. Current implementation of onMapReady customizes the map object.
        mapView.setOnMapReadyHandler(new MapReadyHandler() {
            @Override
            public void onMapReady(MapStatus status) {
                // Check if the map is loaded correctly
                if (status == MapStatus.MAP_STATUS_OK) {
                    // Getting the associated map object
                    final Map map = mapView.getMap();
                    // Creating a map options object
                    MapOptions options = new MapOptions();
                    // Creating a map type control options object
                    MapTypeControlOptions controlOptions = new MapTypeControlOptions();
                    // Changing position of the map type control
                    controlOptions.setPosition(ControlPosition.TOP_RIGHT);
                    // Setting map type control options
                    options.setMapTypeControlOptions(controlOptions);
                    // Setting map options
                    map.setOptions(options);
                    // Setting the map center
                    map.setCenter(new LatLng(35.91466, 10.312499));
                    // Setting initial zoom value
                    map.setZoom(2.0);
                }
            }
        });
    }
};