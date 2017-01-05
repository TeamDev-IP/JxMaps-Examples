/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.demo;

import javax.swing.*;
import javax.swing.plaf.metal.MetalScrollBarUI;
import java.awt.*;
import java.awt.image.BufferedImage;

class MenuScrollbarUI extends MetalScrollBarUI {

    private Image imageThumb, imageTrack;

    private JButton button = new JButton() {
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(0, 0);
        }

    };

    static public Image create(int w, int h, Color c) {
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.setPaint(c);
        g2d.fillRect(0, 0, w, h);
        g2d.dispose();
        return bi;
    }

    MenuScrollbarUI() {
        imageThumb = create(16, 16, new Color(0xbd, 0xbd, 0xbd));
        imageTrack = create(16, 16, new Color(0xf5, 0xf5, 0xf5));
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        g.setColor(Color.gray);
        g.drawImage(imageThumb, r.x, r.y, r.width, r.height, null);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        g.drawImage(imageTrack, r.x, r.y, r.width, r.height, null);
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return button;
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return button;
    }
}
