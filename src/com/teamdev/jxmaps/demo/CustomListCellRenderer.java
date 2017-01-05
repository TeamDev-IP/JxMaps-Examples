/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.demo;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


class CustomListCellRenderer extends JPanel implements ListCellRenderer {
    private final Color SELECTION_BACKGROUND = new Color(0xFA, 0xFA, 0xFA);

    private JLabel image = new JLabel();
    private JLabel title = new JLabel();
    private JTextArea description = new JTextArea();

    private int highlightedIndex = -1;

    public CustomListCellRenderer(final JList list) {
        list.addPropertyChangeListener(PopupMenu.HIGHLIGHTED_INDEX, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                highlightedIndex = (Integer) evt.getNewValue();
                list.repaint();
            }
        });

        setAutoscrolls(true);
        setLayout(new GridBagLayout());

        Font robotoPlain15 = new Font("Roboto", 0, 15);
        Font robotoPlain13 = new Font("Roboto", 0, 13);

        title.setFont(robotoPlain15);
        title.setForeground(new Color(0x21, 0x21, 0x21));

        description.setFont(robotoPlain13);
        description.setForeground(new Color(0x75, 0x75, 0x75));

        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setPreferredSize(new Dimension(1, 34));
        description.setBorder(null);
        description.setOpaque(false);

        add(image, new GridBagConstraints(0, 0, 1, 3, 0.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(10, 14, 5, 10), 0, 0));

        add(title, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 22, 0, 0), 0, 0));

        add(description, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0
                , GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(5, 22, 5, 0), 0, 0));

    }

    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        if (isSelected || index == highlightedIndex) {
            setBackground(SELECTION_BACKGROUND);
        } else {
            setBackground(list.getBackground());
        }
        SampleDescriptor sample = (SampleDescriptor) value;

        image.setIcon(sample.getIcon());
        title.setText(sample.getTitle());
        description.setText(sample.getDescription());
        return this;
    }
}
