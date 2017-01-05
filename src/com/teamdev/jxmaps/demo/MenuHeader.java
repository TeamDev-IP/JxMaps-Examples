/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.demo;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class MenuHeader extends JPanel {
    private static final SampleIcon CLOSE_ICON = new SampleIcon(SampleDescriptor.class.getResource(SampleIcon.isRetina()? "res/close_retina.png" :  "res/close.png"));
    private static final SampleIcon CLOSE_ICON_BLACK = new SampleIcon(SampleDescriptor.class.getResource(SampleIcon.isRetina()? "res/close_black_retina.png" : "res/close_black.png"));

    private JTextField menuText;
    private JButton closeButton;

    public MenuHeader(String title, final ActionListener textEnterListener, final ActionListener closeListener) {
        setPreferredSize(new Dimension(1, 65));
        setBackground(Color.white);

        JLabel menuImageLabel = new JLabel(PopupMenu.MENU_ICON);
        menuImageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        menuText = new JTextField(title);
        menuText.setOpaque(false);
        menuText.setBorder(BorderFactory.createEmptyBorder());
        menuText.setEditable(false);
        menuText.addActionListener(textEnterListener);

        Font robotoBold18 = new Font("Roboto", Font.BOLD, 18);
        menuText.setFont(robotoBold18);
        menuText.setForeground(new Color(0x21, 0x21, 0x21));

        setLayout(new GridBagLayout());

        menuImageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (closeButton.isVisible()) {
                    closeListener.actionPerformed(null);
                }
            }
        });
        add(menuImageLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(19, 13, 12, 0), 0, 0));

        add(menuText, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(21, 32, 12, 15), 0, 0));

        closeButton = new JButton(CLOSE_ICON);
        closeButton.setToolTipText("Close");
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setUI(new BasicButtonUI());
        closeButton.setOpaque(false);
        closeButton.setRolloverIcon(CLOSE_ICON_BLACK);
        closeButton.addActionListener(closeListener);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setVisible(false);

        add(closeButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(22, 0, 12, 18), 0, 0));

        setBorder(new Shadow(4));
    }

    public void setText(String text) {
        menuText.setText(text);
    }

    public String getText() {
        return menuText.getText();
    }

    public void setTextEditable(boolean value) {
        menuText.setEditable(value);
    }

    public boolean getTextEditable() {
        return menuText.isEditable();
    }

    public void setCloseButtonVisible(boolean value) {
        closeButton.setVisible(value);
    }

    public boolean getCloseButtonVisible() {
        return closeButton.isVisible();
    }
}
