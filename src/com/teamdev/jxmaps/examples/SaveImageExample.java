package com.teamdev.jxmaps.examples;

import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.swing.MapView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

/**
 * This example demonstrates how to save displayed map to PNG file.
 *
 * @author Vitaly Eremenko
 */
public class SaveImageExample extends MapView {
    public SaveImageExample() {

        // Creation of MapViewer with specifying LIGHTWEIGHT mode. getImage method of MapView class working only in the this mode.
        super(new MapViewOptions(MapComponentType.LIGHTWEIGHT));

        // Setting of a ready handler to MapView object. onMapReady will be called when map initialization is done and
        // the map object is ready to use. Current implementation of onMapReady customizes the map object.
        setOnMapReadyHandler(new MapReadyHandler() {
            @Override
            public void onMapReady(MapStatus status) {
                // Check if the map is loaded correctly
                if (status == MapStatus.MAP_STATUS_OK) {
                    // Getting the associated map object
                    final Map map = getMap();
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
                    // Setting initial zoom value
                    map.setZoom(2.0);
                    // Setting the map center
                    map.setCenter(new LatLng(35.91466, 10.312499));

                    // Adding of idle event listener
                    map.addEventListener("idle", new MapEvent() {
                        @Override
                        public void onEvent()  {
                            // Getting of current map image
                            Image image = getImage();
                            // Saving of image of the displayed map into a PNG file.
                            try {
                                ImageIO.write((RenderedImage) image, "PNG", new File("map-image.png"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    public static void main(String[] args) {
        SaveImageExample sample = new SaveImageExample();

        JFrame frame = new JFrame("Save Image Example");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(sample, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
