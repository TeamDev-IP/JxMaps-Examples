/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.examples;

import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.DirectionsRequest;
import com.teamdev.jxmaps.DirectionsResult;
import com.teamdev.jxmaps.DirectionsRouteCallback;
import com.teamdev.jxmaps.DirectionsStatus;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.TravelMode;
import com.teamdev.jxmaps.swing.MapView;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This example demonstrates how to calculate a route between two locations.
 *
 * @author Vitaly Eremenko
 */
public class DirectionsExample extends MapView implements ControlPanel {

    private static final Color FOREGROUND_COLOR = new Color(0xBB, 0xDE, 0xFB);

    private JTextField fromField;
    private JTextField toField;

    private JPanel controlPanel;

    public DirectionsExample() {
        controlPanel = new JPanel();

        fromField = new JTextField("Chicago, il");
        toField = new JTextField("Winona, az");

        configureControlPanel();

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
                    map.setCenter(new LatLng(41.85, -87.65));
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

                    calculateDirection();
                }
            }
        });
    }

    @Override
    public JComponent getControlPanel() {
        return controlPanel;
    }

    @Override
    public void configureControlPanel() {
        controlPanel.setBackground(Color.white);
        controlPanel.setLayout(new BorderLayout());

        JPanel demoControlPanel = new JPanel(new GridBagLayout());
        demoControlPanel.setBackground(new Color(61, 130, 248));

        Font robotoPlain13 = new Font("Roboto", 0, 13);
        fromField.setForeground(FOREGROUND_COLOR);
        toField.setForeground(FOREGROUND_COLOR);

        fromField.setFont(robotoPlain13);
        toField.setFont(robotoPlain13);

        fromField.setOpaque(false);
        toField.setOpaque(false);

        fromField.setBorder(new UnderscoreBorder());
        toField.setBorder(new UnderscoreBorder());

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateDirection();
            }
        };
        fromField.addActionListener(listener);
        toField.addActionListener(listener);

        JLabel fromIcon = new JLabel(new ImageIcon(DirectionsExample.class.getResource("res/from.png")));
        JLabel dotsIcon = new JLabel(new ImageIcon(DirectionsExample.class.getResource("res/dots.png")));
        JLabel toIcon = new JLabel(new ImageIcon(DirectionsExample.class.getResource("res/to.png")));
        JLabel changeIcon = new JLabel(new ImageIcon(DirectionsExample.class.getResource("res/change.png")));
        changeIcon.setToolTipText("Reverse starting point and destination");
        changeIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changeIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String from = fromField.getText();
                String to = toField.getText();
                fromField.setText(to);
                toField.setText(from);
                calculateDirection();
            }
        });

        demoControlPanel.add(fromIcon, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(22, 30, 0, 0), 0, 0));
        demoControlPanel.add(dotsIcon, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 33, 0, 0), 0, 0));
        demoControlPanel.add(toIcon, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(6, 30, 25, 0), 0, 0));

        demoControlPanel.add(fromField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(19, 22, 0, 0), 0, 0));
        demoControlPanel.add(toField, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(3, 22, 0, 0), 0, 0));

        demoControlPanel.add(changeIcon, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 22, 0, 22), 0, 0));

        controlPanel.add(demoControlPanel, BorderLayout.NORTH);
    }

    @Override
    public int getPreferredHeight() {
        return 169;
    }

    class UnderscoreBorder extends AbstractBorder {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(FOREGROUND_COLOR);
            g.drawLine(0, height - 1, width, height - 1);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(0, 0, 5, 0);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.bottom = 5;
            return insets;
        }
    }

    private void calculateDirection() {
        // Getting the associated map object
        final Map map = getMap();
        // Creating a directions request
        DirectionsRequest request = new DirectionsRequest();
        // Setting of the origin location to the request
        request.setOriginString(fromField.getText());
        // Setting of the destination location to the request
        request.setDestinationString(toField.getText());
        // Setting of the travel mode
        request.setTravelMode(TravelMode.DRIVING);
        // Calculating the route between locations
        getServices().getDirectionService().route(request, new DirectionsRouteCallback(map) {
            @Override
            public void onRoute(DirectionsResult result, DirectionsStatus status) {
                // Checking of the operation status
                if (status == DirectionsStatus.OK) {
                    // Drawing the calculated route on the map
                    map.getDirectionsRenderer().setDirections(result);
                } else {
                    JOptionPane.showMessageDialog(DirectionsExample.this, "Error. Route cannot be calculated.\nPlease correct input data.");
                }
            }
        });
    }

    private static void loadAndRegisterCustomFonts() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, DirectionsExample.class.getResourceAsStream("res/Roboto-Bold.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, DirectionsExample.class.getResourceAsStream("res/Roboto-Medium.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, DirectionsExample.class.getResourceAsStream("res/Roboto-Regular.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, DirectionsExample.class.getResourceAsStream("res/Roboto-Thin.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, DirectionsExample.class.getResourceAsStream("res/Roboto-Light.ttf")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        loadAndRegisterCustomFonts();

        JFrame frame = new JFrame("Directions");
        final DirectionsExample sample = new DirectionsExample();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(sample, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        new OptionsWindow(sample, new Dimension(300, 100)) {
            @Override
            public void initContent(JWindow contentWindow) {
                contentWindow.add(sample.controlPanel);
            }
        };
    }
}
