/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.examples;

import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.swing.MapView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This example demonstrates how to process MAP_EVENTS from map.
 *
 * @author Vitaly Eremenko
 * @author Sergei Piletsky
 */
public class EventsExample extends JComponent implements ControlPanel {

    private static final Color SELECTION_COLOR = new Color(0x99, 0xCC, 0xFF);
    private static final Color BACKGROUND_COLOR = Color.white;
    private static final Color FOREGROUND_COLOR = new Color(0x21, 0x21, 0x21);

    static class MapEvent extends JLabel {
        private static final double PI2 = Math.PI / 2;
        private static final double STEP = 0.1;
        private final Timer updateTimer;
        private double transparency;
        private double x;

        public MapEvent(String name) {
            setText(name);

            updateTimer = new Timer(50, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    transparency = Math.cos(x);
                    x += STEP;
                    if (x > PI2) {
                        transparency = 0.0f;
                        Timer source = (Timer) e.getSource();
                        source.stop();
                    }
                    repaint();
                }
            });
        }

        void highlight() {
            x = -PI2;
            if (updateTimer.isRunning()) {
                updateTimer.restart();
            } else {
                updateTimer.start();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (g instanceof Graphics2D) {
                Graphics2D g2d = (Graphics2D) g;

                Composite composite = g2d.getComposite();
                try {
                    g2d.setComposite(AlphaComposite.SrcOver.derive((float) transparency));
                    g2d.setColor(getBackground());
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                } finally {
                    g2d.setComposite(composite);
                }
            }
            super.paintComponent(g);
        }
    }

    private static final MapEvent[] MAP_EVENTS = new MapEvent[]{
        new MapEvent("bounds_changed"),
        new MapEvent("center_changed"),
        new MapEvent("click"),
        new MapEvent("dblclick"),
        new MapEvent("drag"),
        new MapEvent("dragend"),
        new MapEvent("dragstart"),
        new MapEvent("heading_changed"),
        new MapEvent("idle"),
        new MapEvent("maptypeid_changed"),
        new MapEvent("mousemove"),
        new MapEvent("mouseout"),
        new MapEvent("mouseover"),
        new MapEvent("projection_changed"),
        new MapEvent("resize"),
        new MapEvent("rightclick"),
        new MapEvent("tilesloaded"),
        new MapEvent("tilt_changed"),
        new MapEvent("zoom_changed"),
    };

    private final MapView mapView = new MapView();
    private final JComponent controlPanel;

    public EventsExample() {
        setLayout(new BorderLayout());
        add(mapView, BorderLayout.CENTER);

        // Setting of a ready handler to MapView object. onMapReady will be called when map initialization is done and
        // the map object is ready to use. Current implementation of onMapReady customizes the map object.
        mapView.setOnMapReadyHandler(new MapReadyHandler() {
            @Override
            public void onMapReady(MapStatus status) {
            // Check if the map is loaded correctly
            if (status == MapStatus.MAP_STATUS_OK) {
                // Getting the associated map object
                final Map map = mapView.getMap();
                // Setting the map center
                map.setCenter(new LatLng(-25.363, 131.044));
                // Setting initial zoom value
                map.setZoom(4.0);
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

                for (final MapEvent event : MAP_EVENTS) {
                    // Registering event listener for each event.
                    map.addEventListener(event.getText(), new com.teamdev.jxmaps.MapEvent() {
                        // onEvent is called when event with event is fired
                        @Override
                        public void onEvent() {
                            event.highlight();
                        }
                    });
                }
            }
            }
        });

        controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBackground(BACKGROUND_COLOR);
        int row = 0;
        for (MapEvent mapEvent : MAP_EVENTS) {
            mapEvent.setForeground(FOREGROUND_COLOR);
            mapEvent.setBackground(SELECTION_COLOR);
            mapEvent.setFont(new Font(Font.MONOSPACED, 0, 15));

            controlPanel.add(mapEvent, new GridBagConstraints(0, row++, 1, 1, 1.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
        }
        controlPanel.add(Box.createVerticalBox(), new GridBagConstraints(0, row, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    @Override
    public void configureControlPanel() {
        final Font monospaced15 = new Font(Font.MONOSPACED, 0, 15);

        controlPanel.removeAll();
        int row = 0;
        for (MapEvent mapEvent : MAP_EVENTS) {
            mapEvent.setFont(monospaced15);
            mapEvent.setForeground(FOREGROUND_COLOR);
            mapEvent.setBackground(SELECTION_COLOR);
            mapEvent.setBorder(BorderFactory.createEmptyBorder(5, 65, 5, 5));

            controlPanel.add(mapEvent, new GridBagConstraints(0, row++, 1, 1, 1.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
        }
        controlPanel.add(Box.createVerticalBox(), new GridBagConstraints(0, row, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    @Override
    public JComponent getControlPanel() {
        return controlPanel;
    }

    @Override
    public int getPreferredHeight() {
        return getHeight();
    }

    private void configureView() {
        add(controlPanel, BorderLayout.EAST);
    }

    private static void loadAndRegisterCustomFonts() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, EventsExample.class.getResourceAsStream("res/Roboto-Bold.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, EventsExample.class.getResourceAsStream("res/Roboto-Medium.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, EventsExample.class.getResourceAsStream("res/Roboto-Regular.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, EventsExample.class.getResourceAsStream("res/Roboto-Thin.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, EventsExample.class.getResourceAsStream("res/Roboto-Light.ttf")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        loadAndRegisterCustomFonts();

        EventsExample eventsExample = new EventsExample();
        eventsExample.configureView();
        JFrame frame = new JFrame("Events");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(eventsExample, BorderLayout.CENTER);
        frame.setSize(700, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
