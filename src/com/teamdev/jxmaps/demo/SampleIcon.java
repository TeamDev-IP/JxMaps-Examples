/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.demo;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.lang.reflect.Field;
import java.net.URL;

class SampleIcon extends ImageIcon {

    static final boolean isRetinaDisplay = isRetinaDisplay();

    public static boolean isRetina() {
        return isRetinaDisplay;
    }

    private static boolean isRetinaDisplay() {
        boolean isRetina = false;
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        try {
            Field field = graphicsDevice.getClass().getDeclaredField("scale");
            if (field != null) {
                field.setAccessible(true);
                Object scale = field.get(graphicsDevice);
                if (scale instanceof Integer && (Integer) scale == 2) {
                    isRetina = true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return isRetina;
    }

    public SampleIcon(URL resource) {
        super(resource);
    }

    @Override
    public int getIconWidth() {
        return isRetinaDisplay ? (super.getIconWidth() / 2) : super.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return isRetinaDisplay ? (super.getIconHeight() / 2) : super.getIconHeight();
    }

    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {

        ImageObserver observer = getImageObserver();

        Image image = getImage();
        int width = image.getWidth(observer);
        int height = image.getHeight(observer);
        final Graphics2D g2d = (Graphics2D) g.create(x, y, width, height);

        if (isRetinaDisplay) {
            g2d.scale(0.5, 0.5);
        }

        g2d.drawImage(image, 0, 0, observer);
        g2d.scale(1, 1);
        g2d.dispose();
    }
}
