/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.examples;

import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.ZoomControlOptions;
import com.teamdev.jxmaps.ZoomControlStyle;
import com.teamdev.jxmaps.swing.MapView;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This example demonstrates how to customize a Map.
 *
 * @author Vitaly Eremenko
 * @author Sergey Piletsky
 */
public class MapOptionsExample extends MapView implements ControlPanel  {
    private static final Color FOREGROUND_COLOR = new Color(0xBB, 0xDE, 0xFB);
    private static final Color FOREGROUND_COLOR_SELECTED = new Color(0xFE, 0xFE, 0xFE);

    private JPanel controlPanel;
    private JCheckBox defaultUiCheck;
    private JCheckBox dblClickCheck;
    private JCheckBox draggingCheck;
    private JCheckBox scrollWheelCheck;

    public MapOptionsExample() {
        // Setting of a ready handler to MapView object. onMapReady will be called when map initialization is done and
        // the map object is ready to use. Current implementation of onMapReady customizes the map object.
        setOnMapReadyHandler(new MapReadyHandler() {
            @Override
            public void onMapReady(MapStatus status) {
                // Check if the map is loaded correctly
                if (status == MapStatus.MAP_STATUS_OK) {
                    // Initializing map
                    initMap(getMap());
                }
            }
        });

        controlPanel = new JPanel(new GridLayout(4, 1));

        defaultUiCheck = new JCheckBox("Show controls");
        defaultUiCheck.setSelected(true);

        // Setting a handler on clicking "Show controls" checkbox
        defaultUiCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Getting current map options
                MapOptions options = new MapOptions();
                // Updating visibility of the zoom control
                options.setZoomControl(defaultUiCheck.isSelected());
                // Updating visibility of the map type control
                options.setMapTypeControl(defaultUiCheck.isSelected());
                // Updating visibility of the street view control
                options.setStreetViewControl(defaultUiCheck.isSelected());
                // Applying updated options
                getMap().setOptions(options);
            }
        });

        dblClickCheck = new JCheckBox("Zoom on double click");
        dblClickCheck.setSelected(true);

        // Setting a handler on clicking "Zoom on double click" checkbox
        dblClickCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Getting current map options
                MapOptions options = new MapOptions();
                // Updating DisableDoubleClickZoom property value
                options.setDisableDoubleClickZoom(!dblClickCheck.isSelected());
                // Applying updated options
                getMap().setOptions(options);
            }
        });

        draggingCheck = new JCheckBox("Enable dragging");
        draggingCheck.setSelected(true);

        // Setting a handler on clicking "Enable dragging" checkbox
        draggingCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Getting current map options
                MapOptions options = new MapOptions();
                // Updating Draggable property value
                options.setDraggable(draggingCheck.isSelected());
                // Applying updated options
                getMap().setOptions(options);
            }
        });

        scrollWheelCheck = new JCheckBox("Enable ScrollWheel");
        scrollWheelCheck.setSelected(true);

        // Setting a handler on clicking "Enable ScrollWheel" checkbox
        scrollWheelCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Getting current map options
                MapOptions options = new MapOptions();
                // Updating the ScrollWheel property value
                options.setScrollWheel(scrollWheelCheck.isSelected());
                // Applying updated options
                getMap().setOptions(options);
            }
        });

        configureControlPanel();
    }

    @Override
    public JComponent getControlPanel() {
        return controlPanel;
    }

    @Override
    public void configureControlPanel() {
        controlPanel.removeAll();
        controlPanel.setBackground(Color.white);
        controlPanel.setLayout(new BorderLayout());

        JPanel demoControlPanel = new JPanel(new GridBagLayout());
        demoControlPanel.setBackground(new Color(61, 130, 248));

        customizeCheckBox(defaultUiCheck);
        customizeCheckBox(dblClickCheck);
        customizeCheckBox(draggingCheck);
        customizeCheckBox(scrollWheelCheck);

        demoControlPanel.add(defaultUiCheck, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(18, 8, 0, 0), 0, 0));
        demoControlPanel.add(dblClickCheck, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 8, 0, 0), 0, 0));
        demoControlPanel.add(draggingCheck, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 8, 0, 0), 0, 0));
        demoControlPanel.add(scrollWheelCheck, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 8, 18, 0), 0, 0));

        controlPanel.add(demoControlPanel, BorderLayout.NORTH);
    }

    @Override
    public int getPreferredHeight() {
        return 210;
    }

    private void customizeCheckBox(final JCheckBox checkBox) {
        Font robotoPlain13 = new Font("Roboto", 0, 13);
        checkBox.setUI(new BasicButtonUI() {
            @Override
            protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
                textRect.translate(28, 0);
                super.paintText(g, b, textRect, text);
            }
        });
        checkBox.setFont(robotoPlain13);
        checkBox.setForeground(checkBox.isSelected()?  FOREGROUND_COLOR_SELECTED : FOREGROUND_COLOR);
        checkBox.setOpaque(false);
        checkBox.setIcon(new ImageIcon(MapOptionsExample.class.getResource("res/checkbox_0.png")));
        checkBox.setSelectedIcon(new ImageIcon(MapOptionsExample.class.getResource("res/checkbox_1.png")));
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBox.setForeground(checkBox.isSelected()?  FOREGROUND_COLOR_SELECTED : FOREGROUND_COLOR);
            }
        });
    }

    private void initMap(Map map) {
        // Creating a map options object
        MapOptions options = new MapOptions();
        // Setting visibility of the map type control
        options.setMapTypeControl(true);
        // Setting coordinates of the map center
        options.setCenter(new LatLng(34.0522342, -118.2436849)); // LA, ca
        // Setting visibility of the map type control
        options.setZoomControl(true);
        // Setting initial zoom value
        options.setZoom(9.0);
        // Creating a map type control options object
        MapTypeControlOptions controlOptions = new MapTypeControlOptions();
        // Changing position of the map type control
        controlOptions.setPosition(ControlPosition.TOP_RIGHT);
        // Setting the map type control options
        options.setMapTypeControlOptions(controlOptions);
        // Creating a zoom control options object
        ZoomControlOptions zoomOptions = new ZoomControlOptions();
        // Changing position of the zoom control
        zoomOptions.setPosition(ControlPosition.LEFT_CENTER);
        // Changing the style of the zoom control
        zoomOptions.setStyle(ZoomControlStyle.LARGE);
        // Setting zoom control options
        options.setZoomControlOptions(zoomOptions);
        // Setting map options
        map.setOptions(options);

        dblClickCheck.doClick();
        draggingCheck.doClick();
        scrollWheelCheck.doClick();
        controlPanel.revalidate();
        controlPanel.repaint();
    }

    private static void loadAndRegisterCustomFonts() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, MapOptionsExample.class.getResourceAsStream("res/Roboto-Bold.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, MapOptionsExample.class.getResourceAsStream("res/Roboto-Medium.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, MapOptionsExample.class.getResourceAsStream("res/Roboto-Regular.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, MapOptionsExample.class.getResourceAsStream("res/Roboto-Thin.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, MapOptionsExample.class.getResourceAsStream("res/Roboto-Light.ttf")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        loadAndRegisterCustomFonts();

        JFrame frame = new JFrame("Map Options");
        final MapOptionsExample sample = new MapOptionsExample();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(sample, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        OptionsWindow optionsWindow = new OptionsWindow(sample, new Dimension(300, 140)) {
            @Override
            public void initContent(JWindow contentWindow) {
                contentWindow.add(sample.controlPanel);
            }
        };
    }
}
