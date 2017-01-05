/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.demo;

import com.teamdev.jxmaps.examples.ControlPanel;

import javax.swing.*;
import java.awt.*;

class ContentView extends JPanel implements MenuListener {
    private JComponent sampleView;

    public ContentView() {
        setLayout(new GridLayout());
    }

    @Override
    public void sampleSelected(SampleDescriptor descriptor) {
        if (sampleView != null) {
            remove(sampleView);
            validate();
        }

        Dimension size = getSize();
        sampleView = descriptor.createInstance(size);

        add(sampleView, BorderLayout.CENTER);
        validate();
    }

    public ControlPanel getControlPanel() {
        if (sampleView instanceof ControlPanel) {
            return (ControlPanel) sampleView;
        } else {
            return null;
        }
    }
}
