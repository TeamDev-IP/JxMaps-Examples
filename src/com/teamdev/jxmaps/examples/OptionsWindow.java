/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.examples;

import com.teamdev.jxmaps.swing.MapView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Base class for windows with options. Some examples can have options.
 *
 * @author Vitaly Eremenko
 * @author Sergei Piletsky
 */

abstract class OptionsWindow {
    protected JFrame parentFrame;
    protected JWindow contentWindow;
    private Dimension size;

    public OptionsWindow(MapView parentWindow, Dimension size) {
        this.size = size;
        Container parent = parentWindow.getParent();
        while (parent != null) {
            if (parent instanceof JFrame) {
                parentFrame = (JFrame) parent;
                break;
            }
            parent = parent.getParent();
        }

        parentFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                updatePosition();
            }

            @Override
            public void componentResized(ComponentEvent e) {
                updatePosition();
            }
        });

        contentWindow = new JWindow(parentFrame);
        contentWindow.setVisible(true);
        contentWindow.setFocusable(true);
        contentWindow.setFocusableWindowState(true);

        parentFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowIconified(WindowEvent e) {
                contentWindow.setVisible(false);
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                contentWindow.setVisible(true);
            }
        });

        initContent(contentWindow);
        updatePosition();
    }

    abstract public void initContent(JWindow contentWindow);

    protected void updatePosition() {
        Rectangle bounds = new Rectangle();
        bounds.setLocation((int) (parentFrame.getX() + (parentFrame.getWidth() - size.getWidth()) / 2), (int) (parentFrame.getY() + parentFrame.getHeight() - size.getHeight() - 20));
        bounds.setSize(size);

        contentWindow.setBounds(bounds);
    }

    public void dispose() {
        contentWindow.dispose();
    }
}
