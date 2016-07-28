/*
 * Copyright (c) 2000-2016 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.demo;

import com.teamdev.jxmaps.examples.ControlPanel;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.net.URL;

class AboutPanel extends JPanel implements ControlPanel {

    public AboutPanel() {
        setBackground(Color.white);
        setLayout(new BorderLayout());
        JTextPane aboutText = new JTextPane();
        aboutText.setContentType("text/html");
        aboutText.setText("<html><font size=\"3\" face=\"Roboto>\">" +
                "JxMaps Demo<br><br>" +
                "Version 1.1<br><br>" +
                "This application is created for demonstration purposes only.<br>" +
                "&copy; 2016 TeamDev Ltd. All rights reserved.<br><br>" +
                "Powered by <font color=\"#3d82f8\"><a href=\"https://www.teamdev.com/jxmaps\" style=\"text-decoration:none\">JxMaps</a>. <a href=\"https://www.teamdev.com/jxmaps-licence-agreement\" style=\"text-decoration:none\">See terms of use.</a></font><br>" +
                "Created using <font color=\"#3d82f8\"><a href=\"https://developers.google.com/maps/terms\" style=\"text-decoration:none\">Google Maps JavaScript API.</a></font><br>" +
                "Based on <font color=\"#3d82f8\"><a href=\"http://www.chromium.org/\" style=\"text-decoration:none\">Chromium project</a></font>. See <font color=\"#3d82f8\"><a href=\"http://jxmaps-support.teamdev.com/documentation/open-source-components\" style=\"text-decoration:none\">full list</a></font> of Chromium<br>components, used in the current JxMaps version.<br>"+
                "This demo uses <font color=\"#3d82f8\"><a href=\"https://www.google.com/fonts/specimen/Roboto\" style=\"text-decoration:none\">WebKit</a></font> project under LGPL. </font><br>" +
                "This demo uses <font color=\"#3d82f8\"><a href=\"https://www.google.com/fonts/specimen/Roboto\" style=\"text-decoration:none\">Roboto</a></font> font, distributed under <font color=\"#3d82f8\"><a href=\"http://www.apache.org/licenses/LICENSE-2.0.html\" style=\"text-decoration:none\">Apache 2.0</a></font><br>licence, and <font color=\"#3d82f8\"><a href=\"https://design.google.com/icons/\" style=\"text-decoration:none\">Material icons.</a></font><br>" +
                "</font><</html>");

        Font robotoPlain13 = new Font("Roboto", 0, 12);
        aboutText.setFont(robotoPlain13);
        aboutText.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        aboutText.setEditable(false);
        aboutText.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                    URL url = e.getURL();
                    try {
                        Desktop desktop = java.awt.Desktop.getDesktop();
                        desktop.browse(url.toURI());
                    } catch (Exception e1) {
                        throw new RuntimeException(e1);
                    }
                }
            }
        });

        add(aboutText, BorderLayout.CENTER);
    }

    @Override
    public JComponent getControlPanel() {
        return this;
    }

    @Override
    public void configureControlPanel() {
    }

    @Override
    public int getPreferredHeight() {
        return 340;
    }
}
