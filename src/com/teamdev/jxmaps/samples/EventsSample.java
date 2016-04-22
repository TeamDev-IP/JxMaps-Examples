/*
 * Copyright (c) 2000-2016 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */

package com.teamdev.jxmaps.samples;

import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapEvent;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.swing.MapView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * This example demonstrates how to process events from map.
 *
 * @author Vitaly Eremenko
 * @author Sergei Piletsky
 */
public class EventsSample extends JComponent implements ControlPanel {

    private static final String[] events = {"bounds_changed", "center_changed", "click", "dblclick", "drag", "dragend",
            "dragstart", "heading_changed", "idle", "maptypeid_changed", "mousemove", "mouseout", "mouseover", "projection_changed",
            "resize", "rightclick", "tilesloaded", "tilt_changed", "zoom_changed"};

    private final MapView mapView = new MapView();
    private final JList<String> eventsList = new JList<String>(events);
    private final HashMap<Integer, Timer> hideTimers = new HashMap<Integer, Timer>();

    private abstract static class EventListener extends MapEvent {
        private String eventId;
        EventListener(String eventId) {
            this.eventId = eventId;
        }
    }

    public EventsSample() {
        for (int i = 0; i < eventsList.getModel().getSize(); i++) {
            final int itemId = i;

            Timer timer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectItem(itemId, false);
                }
            });
            timer.setRepeats(false);
            hideTimers.put(i, timer);
        }

        eventsList.setLayoutOrientation(JList.VERTICAL);
        eventsList.setPreferredSize(new Dimension(200, 1));
        eventsList.setEnabled(false);

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
                    map.setCenter(new LatLng(map, -25.363, 131.044));
                    // Setting initial zoom value
                    map.setZoom(4.0);
                    // Creating a map options object
                    MapOptions options = new MapOptions(map);
                    // Creating a map type control options object
                    MapTypeControlOptions controlOptions = new MapTypeControlOptions(map);
                    // Changing position of the map type control
                    controlOptions.setPosition(ControlPosition.TOP_RIGHT);
                    // Setting map type control options
                    options.setMapTypeControlOptions(controlOptions);
                    // Setting map options
                    map.setOptions(options);

                    for (final String eventId : events) {
                        // Registering event listener for each event.
                        map.addEventListener(eventId, new EventListener(eventId) {
                            // onEvent is called when event with eventId is fired
                            @Override
                            public void onEvent() {
                                highlightEvent(eventId);
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void configureControlPanel() {
        final Font robotoPlain13 = new Font(Font.MONOSPACED, 0, 15);
        final Color foregroundColor = new Color(0x21, 0x21, 0x21);
        final Color selectionColor = new Color(0x99, 0xCC, 0xFF);
        final JLabel text = new JLabel();

        text.setBorder(new EmptyBorder(5, 70, 6, 0));
        text.setFont(robotoPlain13);
        text.setForeground(foregroundColor);
        text.setOpaque(true);

        ListCellRenderer cellRenderer = new ListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                text.setText(value.toString());
                if (isSelected) {
                    text.setBackground(selectionColor);
                } else {
                    text.setBackground(Color.white);
                }
                return text;
            }
        };
        eventsList.setCellRenderer(cellRenderer);
    }

    @Override
    public JComponent getControlPanel() {
        return eventsList;
    }

    @Override
    public int getPreferredHeight() {
        return getHeight();
    }

    private void configureView() {
        add(eventsList, BorderLayout.EAST);
    }

    private void highlightEvent(String eventId) {
        ListModel<String> model = eventsList.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            String text = model.getElementAt(i);
            if (eventId.equals(text)) {
                selectItem(i, true);
                Timer timer = hideTimers.get(i);
                if (timer != null) {
                    timer.restart();
                }
                break;
            }
        }
    }

    private void selectItem(int index, boolean select) {
        if (select)
            eventsList.addSelectionInterval(index, index);
        else
            eventsList.removeSelectionInterval(index, index);
    }

    public static void main(String[] args) {
        EventsSample eventsSample = new EventsSample();
        eventsSample.configureView();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(eventsSample, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
