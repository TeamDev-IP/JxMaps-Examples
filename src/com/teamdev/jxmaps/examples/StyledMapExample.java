/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.examples;

import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.MapTypeId;
import com.teamdev.jxmaps.MapTypeStyle;
import com.teamdev.jxmaps.MapTypeStyleElementType;
import com.teamdev.jxmaps.MapTypeStyleFeatureType;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeStyler;
import com.teamdev.jxmaps.StyledMapType;
import com.teamdev.jxmaps.swing.MapView;

import javax.swing.*;
import java.awt.*;

/**
 * This example demonstrates how to customize a map. Customization defines custom colour and visibility of roads,
 * parks, bodies of water etc.
 *
 * @author Vitaly Eremenko
 */
public class StyledMapExample extends MapView {

    public StyledMapExample() {
        // Setting of a ready handler to MapView object. onMapReady will be called when map initialization is done and
        // the map object is ready to use. Current implementation of onMapReady customizes the map object.
        setOnMapReadyHandler(new MapReadyHandler() {
            @Override
            public void onMapReady(MapStatus status) {
                //Check if the map is loaded correctly
                if (status == MapStatus.MAP_STATUS_OK)
                    //Initializing the map
                    initMap(getMap());
            }
        });
    }

    /**
     * Create a custom colored style with a specified name and hue values
     */
    private StyledMapType createStyledMap(Map map, String name, String hue) {

        // Creating a map type style object for all elements
        MapTypeStyle style = new MapTypeStyle();
        // Styling will be applied to all elements
        style.setElementType(MapTypeStyleElementType.ALL);
        // Styling will be applied to all features
        style.setFeatureType(MapTypeStyleFeatureType.ALL);

        // Creating a map type styler object
        MapTypeStyler styler = new MapTypeStyler();

        // Setting a new hue value for the map types styler object
        styler.setHue(hue);
        // Setting a new saturation value the map types styler object
        styler.setSaturation(-20);
        // Setting a new visibility type value the map types styler object
        styler.setVisibility("simplified");
        // Setting a new gamma value the map types styler object
        styler.setGamma(0.5);
        // Setting a new weight value the map types styler object
        styler.setWeight(0.5);

        // Applying the map type styler to the map style object
        style.setStylers(new MapTypeStyler[]{styler});

        // Creating a map type style object for roads
        MapTypeStyle roadStyle = new MapTypeStyle();
        // Styling will be applied to geometry elements
        roadStyle.setElementType(MapTypeStyleElementType.GEOMETRY);
        // Styling will be applied to roads
        roadStyle.setFeatureType(MapTypeStyleFeatureType.ROAD);

        // Creating a road styler
        MapTypeStyler roadStyler = new MapTypeStyler();
        // Setting a new lightness value the road styler object
        roadStyler.setLightness(100);
        // Setting a new visibility type value the road styler object
        roadStyler.setVisibility("simplified");

        // Applying the road styler to road style object
        roadStyle.setStylers(new MapTypeStyler[]{roadStyler});

        MapTypeStyle[] styles = {style, roadStyle};

        // Creating a styled map type with the previously created styles
        StyledMapType styledMap = new StyledMapType(map, styles);
        // Setting a style name
        styledMap.setName(name);

        return styledMap;
    }

    private void initMap(Map map) {
        // Creating a map options object
        MapOptions options = new MapOptions();
        // Creating a map type control options object
        MapTypeControlOptions mapTypeOptions = new MapTypeControlOptions();
        // Changing position of the map type control
        mapTypeOptions.setPosition(ControlPosition.TOP_RIGHT);
        //Creation of a pink styled map object
        StyledMapType pinkMap = createStyledMap(map, "Pink", "#890000");
        // Creation of a teal styled map object
        StyledMapType tealMap = createStyledMap(map, "Teal", "#00ffe6");
        // Enabling the map type control on the map
        options.setMapTypeControl(true);
        // Setting the map type control options
        options.setMapTypeControlOptions(mapTypeOptions);
        // Setting the map center
        options.setCenter(new LatLng(41.9027835, 12.496365500000024));
        // Setting initial zoom value
        options.setZoom(11.0);

        MapTypeId[] ids = {new MapTypeId("teal"), new MapTypeId("pink"), MapTypeId.ROADMAP};
        // Setting available styles
        mapTypeOptions.setMapTypeIds(ids);

        // Association of map names with styles
        map.mapTypes().set("pink", pinkMap);
        map.mapTypes().set("teal", tealMap);

        // Setting map options
        map.setOptions(options);

        // Setting initial map type id to teal
        map.setMapTypeId(new MapTypeId("teal"));
    }

    public static void main(String[] args) {
        final StyledMapExample mapView = new StyledMapExample();

        JFrame frame = new JFrame("Styled map");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(mapView, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
