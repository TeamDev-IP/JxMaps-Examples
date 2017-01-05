/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.demo;

import javax.swing.border.AbstractBorder;
import java.awt.*;

class Shadow extends AbstractBorder {
    final private int shadowSize;

    public Shadow(int shadowSize) {
        this.shadowSize = shadowSize;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (g instanceof Graphics2D) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(239, 239, 239));
            Composite composite = g2d.getComposite();
            try {
                for (int i = 0; i < shadowSize; i++) {
                    float alpha = 1f / (float) (i * 2 + 1);
                    g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
                    int yPosition = height - shadowSize + i;
                    g.drawLine(0, yPosition, width, yPosition);
                }
            } finally {
                g2d.setComposite(composite);
            }
        }
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, shadowSize, 0);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.bottom = shadowSize;
        return insets;
    }
}
