/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.examples;

import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.InfoWindow;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.Marker;
import com.teamdev.jxmaps.swing.MapView;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * This example demonstrates how to display info windows on the map.
 *
 * @author Vitaly Eremenko
 */
public class InfoWindowExample extends MapView {

    private static String convertImageStreamToString(InputStream is) {
        String result;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[10240];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            result = "data:image/png;base64," + DatatypeConverter.printBase64Binary(buffer.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private static String getBase64ImageString() {
        InputStream is = GeocoderExample.class.getResourceAsStream("res/Paris.png");
        return convertImageStreamToString(is);
    }

    private final static String contentString = "<table cellpadding=\"5\"><tr><td><img src=\"" + getBase64ImageString() + "\" /></td><td valign='top'><p><b>Paris</b></p>" +
            "<p>Paris is the home of the most visited art museum in the world.</p>" +
            "<p style=\"color:#757575\">Use InfoWindow to display custom information, related to a point on a map. InfoWindow layout can be formatted using HTML.</p>" +
            "</td></tr></table>";


    public InfoWindowExample() {
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
                    map.setCenter(new LatLng(48.856614, 2.3522219000000177));
                    // Setting initial zoom value
                    map.setZoom(7.0);
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
                    // Creating a marker object
                    final Marker marker = new Marker(map);
                    // Moving marker to the map center
                    marker.setPosition(map.getCenter());
                    // Creating an information window
                    final InfoWindow window = new InfoWindow(map);
                    // Setting html content to the information window
                    window.setContent(contentString);
                    // Showing the information window on marker
                    window.open(map, marker);
                }
            }
        });

    }

    public static void main(String[] args) {
        final InfoWindowExample sample = new InfoWindowExample();

        JFrame frame = new JFrame("Info window");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(sample, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
