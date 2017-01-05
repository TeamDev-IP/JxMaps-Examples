/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.examples;

import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.ElevationCallback;
import com.teamdev.jxmaps.ElevationResult;
import com.teamdev.jxmaps.ElevationService;
import com.teamdev.jxmaps.ElevationStatus;
import com.teamdev.jxmaps.InfoWindow;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.LocationElevationRequest;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapMouseEvent;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.MapTypeId;
import com.teamdev.jxmaps.MouseEvent;
import com.teamdev.jxmaps.swing.MapView;

import javax.swing.*;
import java.awt.*;

/**
 * This example demonstrates how to calculate elevation by location.
 *
 * @author Vitaly Eremenko
 */
public class ElevationExample extends MapView {

    InfoWindow infoWindow;

    public ElevationExample() {
        // Setting of a ready handler to MapView object. onMapReady will be called when map initialization is done and
        // the map object is ready to use. Current implementation of onMapReady customizes the map object.
        setOnMapReadyHandler(new MapReadyHandler() {
            @Override
            public void onMapReady(MapStatus status) {
                // Check if the map is loaded correctly
                if (status == MapStatus.MAP_STATUS_OK) {
                    // Getting the associated map object
                    final Map map = getMap();
                    // Setting the map center
                    map.setCenter(new LatLng(60.85026, -147.57934999999998));
                    // Setting initial zoom value
                    map.setZoom(7.0);
                    // Setting initial map type
                    map.setMapTypeId(MapTypeId.HYBRID);
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

                    showElevationInfo(map, map.getCenter(), true);

                    // Adding of event listener to the click event
                    map.addEventListener("click", new MapMouseEvent() {
                        @Override
                        public void onEvent(final MouseEvent mouseEvent) {
                            showElevationInfo(map, mouseEvent.latLng(), false);
                        }
                    });
                }
            }
        });
    }

    private void showElevationInfo(final Map map, final LatLng latLng, final boolean initial) {
        // Getting the elevation service instance
        final ElevationService elevationService = getServices().getElevationService();

        // Checking if info window has already opened
        if (infoWindow != null) {
            // Close info window
            infoWindow.close();
        }

        // Creating an elevation request
        LocationElevationRequest request = new LocationElevationRequest();
        LatLng[] locations = {latLng};
        // Setting location to the elevation request
        request.setLocations(locations);

        // Evaluating of the elevation for a location
        elevationService.getElevationForLocations(request, new ElevationCallback(map) {
            @Override
            public void onComplete(ElevationResult[] result, ElevationStatus status) {
                // Checking operation status
                if (status == ElevationStatus.OK) {
                    // Creating an info window
                    infoWindow = new InfoWindow(map);
                    String content = "The elevation at this point is <b>" + (int) result[0].getElevation() + "</b> meters. ";
                    if (initial)
                        content += "<br><br>Click anywhere on the map to get the elevation data at that point.";
                    // Setting content of the info window
                    infoWindow.setContent(content);
                    // Moving the info window to the source location
                    infoWindow.setPosition(latLng);
                    // Showing the info window
                    infoWindow.open(map);
                }
            }
        });
    }

    public static void main(String[] args) {
        final ElevationExample sample = new ElevationExample();

        JFrame frame = new JFrame("Elevation");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(sample, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
