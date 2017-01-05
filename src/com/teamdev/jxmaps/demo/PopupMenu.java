/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.demo;

import com.teamdev.jxmaps.examples.ControlPanel;
import com.teamdev.jxmaps.examples.EditableTextControlPanel;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

class PopupMenu {
    static final SampleIcon MENU_ICON = new SampleIcon(SampleDescriptor.class.getResource("res/menu" + (SampleIcon.isRetina() ? "_retina.png" : ".png")));
    static final SampleIcon MENU_BACKGROUND = new SampleIcon(SampleDescriptor.class.getResource("res/menu-background" + (SampleIcon.isRetina() ? "_retina.png" : ".png")));

    final static Color TRANSPARENT_BACKGROUND = new Color(1, 1, 1, 1);
    final static String HIGHLIGHTED_INDEX = "highlightedIndex";
    final static String FEATURES = "Features";
    final static int MENU_WIDTH = 400;

    private final JFrame parentWindow;
    private final JWindow menuButtonWindow;
    private final JWindow menuOptionsWindow;
    private MenuHeader menuHeader;
    private JScrollPane scrollPane;
    private JList<SampleDescriptor> featureList;
    private boolean shouldHideMenu = true;
    private ControlPanel controlPanel;
    private JLabel aboutLabel;
    private AboutPanel about;

    private final java.util.List<MenuListener> menuListeners = new LinkedList<MenuListener>();

    public PopupMenu(JFrame parentWindow, SampleDescriptor[] samples, final Point relativeLocation) {
        this.parentWindow = parentWindow;
        parentWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowIconified(WindowEvent e) {
                menuButtonWindow.setVisible(false);
                menuOptionsWindow.setVisible(false);
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                menuButtonWindow.setVisible(true);
            }
        });

        parentWindow.getContentPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (controlPanel != null) {
                    menuOptionsWindow.setSize(MENU_WIDTH, controlPanel.getPreferredHeight());
                    menuOptionsWindow.getContentPane().revalidate();
                }
            }
        });

        menuButtonWindow = new JWindow(parentWindow);

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (event.getID() == MouseEvent.MOUSE_EXITED) {
                    if (menuOptionsWindow.isVisible() && !menuOptionsWindow.getBounds().contains(((MouseEvent) event).getLocationOnScreen())) {
                        if (shouldHideMenu) {
                            menuOptionsWindow.setVisible(false);
                        }
                    }
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);

        menuButtonWindow.setBackground(TRANSPARENT_BACKGROUND);
        menuButtonWindow.setSize(62, 62);
        menuButtonWindow.setLocation(relativeLocation);

        final Container contentPane = menuButtonWindow.getContentPane();
        contentPane.setLayout(new BorderLayout());

        final JButton menuButton = new JButton(MENU_ICON);
        menuButton.setSize(new Dimension(62, 62));
        menuButton.setBorder(BorderFactory.createEmptyBorder());
        menuButton.setToolTipText("Menu");
        menuButton.setOpaque(false);
        menuButton.setUI(new BasicButtonUI());
        menuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                showMenu();
            }
        });

        JComponent menuPanel = new JComponent() {
            @Override
            public void paintComponent(Graphics g) {
                Image image = MENU_BACKGROUND.getImage();
                g.drawImage(image, 0, 0, null);
            }
        };
        menuPanel.add(menuButton, BorderLayout.CENTER);

        contentPane.add(menuPanel, BorderLayout.CENTER);

        updateMenuLocation(relativeLocation);
        menuButtonWindow.setVisible(true);

        menuOptionsWindow = new JWindow(parentWindow);
        menuOptionsWindow.setLocation(relativeLocation);

        final Container contentPane1 = menuOptionsWindow.getContentPane();

        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(true);

        contentPane1.add(mainPanel);

        mainPanel.setLayout(new GridBagLayout());

        featureList = new JList<SampleDescriptor>(samples);
        featureList.setCursor(new Cursor(Cursor.HAND_CURSOR));
        scrollPane = new JScrollPane(featureList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        featureList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectSampleAndHideMenu();
            }
        });

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (event.getID() == MouseEvent.MOUSE_MOVED) {
                    Point cursorPosition = ((MouseEvent) event).getLocationOnScreen();
                    cursorPosition.translate(0, scrollPane.getViewport().getY());
                    if (menuOptionsWindow.isVisible() && menuOptionsWindow.getBounds().contains(cursorPosition)) {
                        SwingUtilities.convertPointFromScreen(cursorPosition, featureList);
                        int index = featureList.locationToIndex(cursorPosition);
                        featureList.firePropertyChange(HIGHLIGHTED_INDEX, -1, index);
                    }
                }
            }
        }, AWTEvent.MOUSE_MOTION_EVENT_MASK);

        featureList.setCellRenderer(new CustomListCellRenderer(featureList));
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 1));
        MenuScrollbarUI scrollbar = new MenuScrollbarUI();
        scrollPane.getVerticalScrollBar().setUI(scrollbar);

        ActionListener closeSampleListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restoreMenu();
            }
        };
        ActionListener textEnterListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredText = menuHeader.getText();
                if (controlPanel instanceof EditableTextControlPanel) {
                    ((EditableTextControlPanel) controlPanel).onTextEntered(enteredText);
                }
            }
        };
        menuHeader = new MenuHeader(FEATURES, textEnterListener, closeSampleListener);
        mainPanel.add(menuHeader, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        mainPanel.add(scrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));


        Font robotoPlain11 = new Font("Roboto", 0, 11);
        aboutLabel = new JLabel("About JxMaps Demo");
        aboutLabel.setFont(robotoPlain11);
        aboutLabel.setForeground(new Color(0x75, 0x75, 0x75));
        aboutLabel.setBackground(new Color(0xFA, 0xFA, 0xFA));
        aboutLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        aboutLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        aboutLabel.setHorizontalAlignment(SwingConstants.CENTER);
        aboutLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showAbout();
            }
        });

        mainPanel.add(scrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        mainPanel.add(aboutLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        parentWindow.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                updateMenuLocation(relativeLocation);
            }
        });

        about = new AboutPanel();
    }

    private void showAbout() {
        setControlPanel(about);
    }

    private void selectSampleAndHideMenu() {
        SampleDescriptor sampleDescriptor = featureList.getSelectedValue();
        for (MenuListener listener : menuListeners)
            listener.sampleSelected(sampleDescriptor);

        if (shouldHideMenu) {
            menuOptionsWindow.setVisible(false);
        }
    }

    private void restoreMenu() {
        menuOptionsWindow.setSize(MENU_WIDTH, parentWindow.getContentPane().getHeight());

        scrollPane.getViewport().remove(controlPanel.getControlPanel());
        scrollPane.setViewportView(featureList);
        scrollPane.getViewport().revalidate();
        menuHeader.setText(FEATURES);
        menuHeader.setCloseButtonVisible(false);
        menuHeader.setTextEditable(false);
        aboutLabel.setVisible(true);
        shouldHideMenu = true;
    }

    public void setControlPanel(ControlPanel controlPanel) {
        if (controlPanel != null) {
            aboutLabel.setVisible(false);
            this.controlPanel = controlPanel;
            scrollPane.getViewport().remove(featureList);

            if (controlPanel instanceof AboutPanel) {
                menuHeader.setText(aboutLabel.getText());
            } else {
                SampleDescriptor descriptor = featureList.getSelectedValue();
                menuHeader.setText(descriptor.getTitle());
            }
            menuHeader.setCloseButtonVisible(true);
            menuOptionsWindow.setVisible(true);
            scrollPane.setViewportView(controlPanel.getControlPanel());
            scrollPane.getViewport().revalidate();
            if (controlPanel instanceof EditableTextControlPanel) {
                menuHeader.setTextEditable(true);
                menuHeader.setText(((EditableTextControlPanel) controlPanel).getInitialText());
            }
            menuOptionsWindow.setSize(MENU_WIDTH, controlPanel.getPreferredHeight());
            shouldHideMenu = false;
        }
    }

    private void showMenu() {
        updateMenuOptionsLocation();
        menuOptionsWindow.setSize(MENU_WIDTH, parentWindow.getContentPane().getHeight());
        menuOptionsWindow.setVisible(true);
    }

    private void updateMenuLocation(Point relativeLocation) {
        Point newLocation = PopupMenu.this.parentWindow.getContentPane().getLocationOnScreen();
        newLocation.translate(relativeLocation.x, relativeLocation.y);
        menuButtonWindow.setLocation(newLocation);

        if (menuOptionsWindow != null && menuOptionsWindow.isVisible()) {
            updateMenuOptionsLocation();
        }
    }

    private void updateMenuOptionsLocation() {
        Point location = parentWindow.getContentPane().getLocationOnScreen();
        menuOptionsWindow.setLocation(location.x, location.y);
    }

    public void addListener(MenuListener menuListener) {
        menuListeners.add(menuListener);
    }
}
