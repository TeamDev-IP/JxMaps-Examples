/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.examples;

import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.swing.MapView;

import javax.swing.*;
import java.awt.*;

/**
 * This example demonstrates how to customize street view panorama layout.
 *
 * @author Vitaly Eremenko
 */
public class StreetViewLayoutExample extends MapView {

    public static void main(String[] args) {

        final MapViewOptions option = new MapViewOptions();
        option.streetViewLayout().setSize(70);
        option.streetViewLayout().setPosition(StreetViewLayout.Position.BOTTOM);
        final MapView view = new MapView(option);

        // Setting of a ready handler to MapView object. onMapReady will be called when map initialization is done and
        // the map object is ready to use. Current implementation of onMapReady customizes the map object.
        view.setOnMapReadyHandler(new MapReadyHandler() {
            @Override
            public void onMapReady(MapStatus status) {
                // Check if the map is loaded correctly
                if (status == MapStatus.MAP_STATUS_OK) {
                    // Getting the associated map object
                    Map map = view.getMap();
                    // Creating a map options object
                    MapOptions mapOptions = new MapOptions();
                    // Creating a map type control options object
                    MapTypeControlOptions controlOptions = new MapTypeControlOptions();
                    // Changing position of the map type control
                    controlOptions.setPosition(ControlPosition.TOP_RIGHT);
                    // Setting map type control options
                    mapOptions.setMapTypeControlOptions(controlOptions);
                    // Setting map options
                    map.setOptions(mapOptions);
                    // Setting the map center
                    map.setCenter(new LatLng(51.500871, -0.1222632));
                    // Setting initial zoom value
                    map.setZoom(13.0);
                    // Creating a street view panorama options object
                    StreetViewPanoramaOptions options = new StreetViewPanoramaOptions();
                    // Creating a street view address control options object
                    StreetViewAddressControlOptions svControlOptions = new StreetViewAddressControlOptions();
                    // Changing position of the address control on the panorama
                    svControlOptions.setPosition(ControlPosition.TOP_RIGHT);
                    // Setting address control options
                    options.setAddressControlOptions(svControlOptions);
                    // Setting street view panorama options
                    view.getPanorama().setOptions(options);
                    // Setting initial position of the street view
                    view.getPanorama().setPosition(map.getCenter());
                    // Creating point of view object
                    StreetViewPov pov = new StreetViewPov();
                    // Setting heading for the point of view
                    pov.setHeading(270);
                    // Setting pitch for the point of view
                    pov.setPitch(20);
                    // Applying the point of view to the panorama object
                    view.getPanorama().setPov(pov);
                }
            }
        });

        JFrame frame = new JFrame("Street View Layout");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }
}
