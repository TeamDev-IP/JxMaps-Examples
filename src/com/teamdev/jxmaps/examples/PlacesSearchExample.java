/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.examples;

import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.GeocoderCallback;
import com.teamdev.jxmaps.GeocoderRequest;
import com.teamdev.jxmaps.GeocoderResult;
import com.teamdev.jxmaps.GeocoderStatus;
import com.teamdev.jxmaps.Icon;
import com.teamdev.jxmaps.InfoWindow;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapEvent;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.MapViewOptions;
import com.teamdev.jxmaps.Marker;
import com.teamdev.jxmaps.PhotoOptions;
import com.teamdev.jxmaps.PlaceDetailsCallback;
import com.teamdev.jxmaps.PlaceDetailsRequest;
import com.teamdev.jxmaps.PlaceNearbySearchCallback;
import com.teamdev.jxmaps.PlacePhoto;
import com.teamdev.jxmaps.PlaceResult;
import com.teamdev.jxmaps.PlaceSearchPagination;
import com.teamdev.jxmaps.PlaceSearchRequest;
import com.teamdev.jxmaps.PlacesService;
import com.teamdev.jxmaps.PlacesServiceStatus;
import com.teamdev.jxmaps.swing.MapView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;


/**
 * This example demonstrates how to find places on the map.
 * Also it demonstrates how to get details about selected place.
 *
 * @author Vitaly Eremenko
 * @author Sergei Piletsky
 */
public class PlacesSearchExample extends MapView implements EditableTextControlPanel  {
    static final MapViewOptions mapOptions;

    static {
        // initializing a map view options
        mapOptions = new MapViewOptions();
        // enabling usage of places library
        mapOptions.importPlaces();
    }

    private final boolean standalone;

       static final Icon restarantIcon = new Icon();
    static final Icon barIcon = new Icon();
    static final Icon hotelIcon = new Icon();
    static final Icon restarantIconHover = new Icon();
    static final Icon barIconHover = new Icon();
    static final Icon hotelIconHover = new Icon();

    private OptionsWindow optionsWindow;
    private JTextField addressEdit;
    private JList placesList;
    private ExtendedMarker[] markers;
    private Marker mainMarker;
    private JPanel controlPanel;

    static class ExtendedMarker extends Marker {

        private Icon hoverIcon;
        private Icon normalIcon;
        private final Map map;
        private final PlacesService placesService;

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        private String placeId;

        public ExtendedMarker(Map map, PlacesService placesService) {
            super(map);
            this.map = map;
            this.placesService = placesService;

            addEventListener("mouseover", new MapEvent() {
                @Override
                public void onEvent() {
                    if (hoverIcon != null) {
                        setIcon(hoverIcon);
                    }
                }
            });
            addEventListener("mouseout", new MapEvent() {
                @Override
                public void onEvent() {
                    if (normalIcon != null) {
                        setIcon(normalIcon);
                    }
                }
            });
            addEventListener("click", new MapEvent() {
                @Override
                public void onEvent() {
                    showInfoWindow();
                }
            });
        }

        /**
         * Showing info window for certain place
         */
        private void showInfoWindow() {
            // Checking if placeId value is set
            if (placeId != null) {
                // Creating place details request
                PlaceDetailsRequest request = new PlaceDetailsRequest();
                // Setting placeId to search request
                request.setPlaceId(placeId);
                // Requesting details for the place by provided placeId
                placesService.getDetails(request, new PlaceDetailsCallback(map) {
                        @Override
                        public void onComplete (PlaceResult result, PlacesServiceStatus status){
                            // Checking operation status
                            if (status == PlacesServiceStatus.OK) {
                                // Creating a info window
                                InfoWindow window = new InfoWindow(map);
                                // Getting details from result and setting it as info window context
                                window.setContent(getContentByResult(result));
                                // Setting info window position
                                window.setPosition(getPosition());
                                // Showing information about place
                                window.open(map, this);
                            }
                        }
                    }
                );
            }
        }

        private String getContentByResult(PlaceResult result) {

            String textContent = "<p><b>" + result.getName() + "</b></p>";
            textContent += "<p>" + result.getFormattedAddress() + "</p>";

            // Creating a photo options object
            PhotoOptions option = new PhotoOptions();
            // Setting maximum photo height
            option.setMaxHeight(64);
            // Setting maximum photo width
            option.setMaxWidth(64);

            // Getting photos from result
            PlacePhoto[] photos = result.getPhotos();

            if ((photos != null) && (photos.length > 0)) {
                PlacePhoto photo = photos[0];
                String imageContent = "<table cellspacing=\"0\" cellpadding=\"5\"><tr><td><img src=\"" + photo.getUrl(option);
                imageContent += "\" /></td><td>";
                textContent = imageContent + textContent;
                textContent += "</td></tr></table>";
            }

            return textContent;
        }

        public void setIcons(Icon normalIcon, Icon hoverIcon) {
            this.normalIcon = normalIcon;
            this.hoverIcon = hoverIcon;

            setIcon(this.normalIcon);
        }
    }

    public PlacesSearchExample() {
        this(false);
    }

    public PlacesSearchExample(boolean standalone) {
        super(mapOptions);
        this.standalone = standalone;

        restarantIcon.loadFromStream(PlacesSearchExample.class.getResourceAsStream("res/restaurants_for_map.png"), "png");
        barIcon.loadFromStream(PlacesSearchExample.class.getResourceAsStream("res/bars_and_pubs_for_map.png"), "png");
        hotelIcon.loadFromStream(PlacesSearchExample.class.getResourceAsStream("res/hotels_for_map.png"), "png");
        restarantIconHover.loadFromStream(PlacesSearchExample.class.getResourceAsStream("res/restaurants_for_map.png"), "png");
        barIconHover.loadFromStream(PlacesSearchExample.class.getResourceAsStream("res/bars_and_pubs_for_map.png"), "png");
        hotelIconHover.loadFromStream(PlacesSearchExample.class.getResourceAsStream("res/hotels_for_map.png"), "png");

        // Setting of a ready handler to MapView object. onMapReady will be called when map initialization is done and
        // the map object is ready to use. Current implementation of onMapReady customizes the map object.
        setOnMapReadyHandler(new MapReadyHandler() {
            @Override
            public void onMapReady(MapStatus status) {
                // Check if the map is loaded correctly
                if (status == MapStatus.MAP_STATUS_OK) {
                    init();
                }
            }
        });

        controlPanel = new JPanel();

        addressEdit = new JTextField("London, Baker str., 221b");
        addressEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                geocodePlace(addressEdit.getText());
            }
        });

        placesList = new JList<String>(new String[]{"Restaurants", "Hotels", "Bars and pubs"});
        placesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                findPlaces();
            }
        });

        configureControlPanel();
    }

    @Override
    public JComponent getControlPanel() {
        return controlPanel;
    }

    @Override
    public int getPreferredHeight() {
        return 233;
    }

    class PlaceOption {
        final ImageIcon icon;
        final String name;

        public PlaceOption(ImageIcon icon, String name) {
            this.icon = icon;
            this.name = name;
        }

        public ImageIcon getIcon() {
            return icon;
        }

        public String getName() {
            return name;
        }
    }

    class PlaceOptionsRenderer extends JPanel implements ListCellRenderer {
        private final Color SELECTION_BACKGROUND = new Color(0xFA, 0xFA, 0xFA);

        private final JLabel imageLabel;
        private final JLabel text;

        public PlaceOptionsRenderer() {
            setLayout(new GridBagLayout());

            imageLabel = new JLabel();
            imageLabel.setPreferredSize(new Dimension(18, 18));
            text = new JLabel();

            Font robotoPlain13 = new Font("Roboto", 0, 13);
            text.setFont(robotoPlain13);
            text.setForeground(new Color(0x21, 0x21, 0x21));

            add(imageLabel, new GridBagConstraints(0, 0, 1, 3, 0.0, 0.0
                    , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(20, 22, 20, 22), 0, 0));

            add(text, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
                    , GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(20, 8, 20, 22), 0, 0));
        }
        public Component getListCellRendererComponent(JList list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {
            if (isSelected) {
                setBackground(SELECTION_BACKGROUND);
            } else {
                setBackground(list.getBackground());
            }
            PlaceOption placeOption = (PlaceOption) value;
            imageLabel.setIcon(placeOption.getIcon());
            text.setText(placeOption.getName());
            return this;
        }
    }

    @Override
    public void configureControlPanel() {
        controlPanel.removeAll();
        controlPanel.setBackground(Color.white);
        controlPanel.setLayout(new BorderLayout());

        placesList = new JList<PlaceOption>(new PlaceOption[]{
                new PlaceOption(new ImageIcon(PlacesSearchExample.class.getResource("res/restaurants.png")), "Restaurants"),
                new PlaceOption(new ImageIcon(PlacesSearchExample.class.getResource("res/hotels.png")), "Hotels"),
                new PlaceOption(new ImageIcon(PlacesSearchExample.class.getResource("res/bars_and_pubs.png")), "Bars and pubs"),
        });

        placesList.setCellRenderer(new PlaceOptionsRenderer());
        placesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                findPlaces();
            }
        });
        placesList.setOpaque(false);
        placesList.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (standalone) {
            controlPanel.add(addressEdit, BorderLayout.NORTH);
        }

        controlPanel.add(placesList, BorderLayout.CENTER);
    }

    @Override
    public void onTextEntered(String value) {
        geocodePlace(value);
    }

    @Override
    public String getInitialText() {
        return addressEdit.getText();
    }

    private void findPlaces() {

        if (placesList.getSelectedIndex() < 0)
            return;

        // Getting the associated map object
        final Map map = getMap();
        // Creating places search request
        final PlaceSearchRequest request = new PlaceSearchRequest();
        // Setting start point for places search
        request.setLocation(map.getCenter());
        // Setting radius for places search
        request.setRadius(500.0);

        final int imageType = placesList.getSelectedIndex();

        final Icon iconUrl[] = new Icon[2];

        switch (imageType) {
            case 0:
                iconUrl[0] = restarantIcon;
                iconUrl[1] = restarantIconHover;

                request.setTypes(new String[]{"restaurant"});
                break;
            case 1:
                iconUrl[0] = hotelIcon;
                iconUrl[1] = hotelIconHover;

                request.setTypes(new String[]{"hotels"});
                break;
            case 2:
                iconUrl[0] = barIcon;
                iconUrl[1] = barIconHover;

                request.setTypes(new String[]{"bar"});
                break;
        }

        // Searching places near specified location
        getServices().getPlacesService().nearbySearch(request, new PlaceNearbySearchCallback(map) {
            @Override
            public void onComplete(PlaceResult[] results, PlacesServiceStatus status, PlaceSearchPagination pagination) {
                // Checking operation status
                if (status == PlacesServiceStatus.OK) {
                    clearMarkers();

                    // Creating markers for each place
                    markers = new ExtendedMarker[results.length];
                    for (int i=0; i< results.length; ++i) {
                        PlaceResult result = results[i];
                        // Creating a marker for place found
                        markers[i] = new ExtendedMarker(map, getServices().getPlacesService());
                        // Associating the marker with placeId (will be used on place details search)
                        markers[i].setPlaceId(result.getPlaceId());
                        // Moving the marker to place location
                        LatLng location = result.getGeometry() != null ? result.getGeometry().getLocation() : null;
                        // Setting icons for the marker
                        markers[i].setIcons(iconUrl[0],iconUrl[1]);
                        if (location != null)
                            // Setting the marker position
                            markers[i].setPosition(location);
                    }
                }
            }
        });

    }

    private void geocodePlace(String address) {
        if (mainMarker != null)
            mainMarker.setVisible(false);
        mainMarker = null;
        clearMarkers();

        // Getting the associated map object
        final Map map = getMap();

        // Creating geocoder request
        GeocoderRequest request = new GeocoderRequest();
        // Set address for request
        request.setAddress(address);
        // Geocoding a position by address
        getServices().getGeocoder().geocode(request, new GeocoderCallback(map) {
            @Override
            public void onComplete(GeocoderResult[] results, GeocoderStatus status) {
                // Checking operation status
                if ((status == GeocoderStatus.OK) && (results.length > 0)) {
                    // Getting first result
                    GeocoderResult result = results[0];
                    // Getting location (coords)
                    LatLng location = result.getGeometry().getLocation();
                    // Centering map to result location
                    map.setCenter(location);
                    // Initializing main marker
                    mainMarker = new Marker(map);
                    // Moving marker to result location
                    mainMarker.setPosition(location);
                    // Showing marker on map
                    mainMarker.setVisible(true);

                    findPlaces();
                }
            }
        });
    }

    private void clearMarkers() {
        if (markers == null)
            return;
        for (Marker marker : markers) {
            marker.setVisible(false);
        }
        markers = null;
    }

    private void init() {
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
        map.setZoom(15.0);

        geocodePlace(addressEdit.getText());
    }

    private static void loadAndRegisterCustomFonts() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, PlacesSearchExample.class.getResourceAsStream("res/Roboto-Bold.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, PlacesSearchExample.class.getResourceAsStream("res/Roboto-Medium.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, PlacesSearchExample.class.getResourceAsStream("res/Roboto-Regular.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, PlacesSearchExample.class.getResourceAsStream("res/Roboto-Thin.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, PlacesSearchExample.class.getResourceAsStream("res/Roboto-Light.ttf")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        loadAndRegisterCustomFonts();

        final PlacesSearchExample sample = new PlacesSearchExample(true);

        JFrame frame = new JFrame("Places Search");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(sample, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        new OptionsWindow(sample, new Dimension(350, 200)) {
            @Override
            public void initContent(JWindow contentWindow) {
                contentWindow.add(sample.controlPanel);
            }
        };
    }
}
