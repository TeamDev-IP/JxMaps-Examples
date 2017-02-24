/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.demo;
import com.teamdev.jxmaps.examples.*;

import javax.swing.*;
import java.awt.*;

public class JxMapsDemo implements MenuListener {
    private JFrame frame;
    private ContentView contentView;
    private PopupMenu menu;

    private static void initEnvironment() throws Exception {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "JxMaps Demo");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    }

    private static void loadAndRegisterCustomFonts() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, JxMapsDemo.class.getResourceAsStream("res/Roboto-Bold.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, JxMapsDemo.class.getResourceAsStream("res/Roboto-Medium.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, JxMapsDemo.class.getResourceAsStream("res/Roboto-Regular.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, JxMapsDemo.class.getResourceAsStream("res/Roboto-Thin.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, JxMapsDemo.class.getResourceAsStream("res/Roboto-Light.ttf")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initAndDisplayUI() {
        contentView = new ContentView();

        frame = new JFrame("JxMaps Demo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(contentView, BorderLayout.CENTER);
        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon(JxMapsDemo.class.getResource("res/jxmaps32x32.png")).getImage());
        frame.setVisible(true);

        String osName = System.getProperty("os.name");
        if (osName.startsWith("Mac OS")) {
            AboutDialog.initHandler(frame);
        }

        SampleDescriptor samples[] = new SampleDescriptor[]{
                new SampleDescriptor("ic_simple_sample", "Simple Map", "Adding a map to a Java application is easy.", MapExample.class),
                new SampleDescriptor("ic_styled_sample", "Styled Map", "Define colour and visibility of roads, parks, bodies of water etc.", StyledMapExample.class),
                new SampleDescriptor("ic_markers_sample", "Markers", "Add markers to a map on click.", MarkersExample.class),
                new SampleDescriptor("ic_directions_sample", "Directions", "Calculate and display the route between two points.", DirectionsExample.class),
                new SampleDescriptor("ic_options_sample", "Map Options", "Customize the map view.", MapOptionsExample.class),
                new SampleDescriptor("ic_infowindow_sample", "Info Window", "Display information about the marked location.", InfoWindowExample.class),
                new SampleDescriptor("ic_geocoding_sample", "Geocoding", "Define coordinates based on the address and vice versa.", GeocoderExample.class),
                new SampleDescriptor("ic_streetview_sample", "Street View", "Display panoramas.", StreetViewExample.class),
                new SampleDescriptor("ic_elevation_sample", "Elevation", "Calculate and display elevation value at a given point.", ElevationExample.class),
                new SampleDescriptor("ic_places_sample", "Places Search", "Find places near a given point.", PlacesSearchExample.class),
                new SampleDescriptor("ic_polygon_sample", "Polygon", "Draw polygons on the map.", PolygonExample.class),
                new SampleDescriptor("ic_polyline_sample", "Polyline", "Draw a custom route on the map.", PolylineExample.class),
                new SampleDescriptor("ic_traffic_sample", "Traffic Layer", "Display information about traffic on the map.", TrafficLayerExample.class),
                new SampleDescriptor("ic_events_sample", "Events", "The pane shows you events that are triggered when you interact with the map.", EventsExample.class),
        };

        menu = new PopupMenu(frame, samples, new Point(0, 0));
        menu.addListener(this);

        sampleSelected(samples[0]);
    }

    @Override
    public void sampleSelected(SampleDescriptor descriptor) {
        frame.setTitle("JxMaps Demo - " + descriptor.getTitle());
        contentView.sampleSelected(descriptor);

        ControlPanel controlPanel = contentView.getControlPanel();
        if (controlPanel != null) {
            controlPanel.configureControlPanel();
        }
        menu.setControlPanel(controlPanel);
    }

    static {
        try {
            initEnvironment();
        } catch (Exception e) {
      //      e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        initEnvironment();
        loadAndRegisterCustomFonts();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JxMapsDemo().initAndDisplayUI();
            }
        });
    }
}
