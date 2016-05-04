package com.teamdev.jxmaps.demo;


import javax.swing.*;
import java.awt.*;

class AboutDialog extends JDialog{
    private static Component parentFrame;

    public AboutDialog() {
        AboutPanel panel = new AboutPanel();
        setModal(true);
        setResizable(false);
        setTitle("About JxMaps Demo");
        setSize(new Dimension(400,panel.getPreferredHeight() - 60));

        setLayout(new BorderLayout());
        add(panel);
    }

    public static void initHandler(Component frame) {
        parentFrame = frame;
        com.apple.eawt.Application.getApplication().setDockIconImage(Toolkit.getDefaultToolkit().getImage(SampleDescriptor.class.getResource("res/jxmaps32x32.png")));
        com.apple.eawt.Application.getApplication().setAboutHandler(new com.apple.eawt.AboutHandler() {
            @Override
            public void handleAbout(com.apple.eawt.AppEvent.AboutEvent aboutEvent) {
                AboutDialog dlg = new AboutDialog();
                dlg.setLocationRelativeTo(parentFrame);
                dlg.setVisible(true);
            }
        });

    }
}
