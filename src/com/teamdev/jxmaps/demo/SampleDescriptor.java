/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.demo;

import com.teamdev.jxmaps.swing.MapView;

import javax.swing.*;
import java.awt.*;

class SampleDescriptor {
    private final SampleIcon icon;
    private final String title;
    private final String description;
    private Class sampleClass;

    public SampleDescriptor(String iconPath, String title, String description, Class sampleClass) {
        this.icon = new SampleIcon(SampleDescriptor.class.getResource("res/" + iconPath + (SampleIcon.isRetina() ? "_retina.png" : ".png")));
        this.title = title;
        this.description = description;
        this.sampleClass = sampleClass;
    }

    public SampleIcon getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public JComponent createInstance(Dimension size) {
        try {
            JComponent view = (JComponent) sampleClass.newInstance();
            if (view instanceof MapView) {
                ((MapView) view).setMapSize(size);
            }
            return view;
        } catch (Exception e) {
            return null;
        }
    }
}
