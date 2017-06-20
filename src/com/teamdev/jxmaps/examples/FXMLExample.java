package com.teamdev.jxmaps.examples;

/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */

import com.teamdev.jxmaps.javafx.MapView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * This example demonstrates how to create a MapView instance,
 * display it in JavaFX application and open a simple map.
 *
 * @author Vitaly Eremenko
 */
public class FXMLExample extends Application {

    @Override
    public void init() throws Exception {
        // Initializing of JavaFX engine
        MapView.InitJavaFX();
    }


    @Override
    public void start(final Stage primaryStage) throws IOException {

        BorderPane pane = FXMLLoader.load(getClass().getResource("res/main.fxml"));

        primaryStage.setTitle("FXMLSample");
        primaryStage.setScene(new Scene(pane, 800, 600));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
