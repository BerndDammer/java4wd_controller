package java4wd_controller.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMainApp extends Application {
    public void start(Stage stage) throws IOException {
        final MainFrame mfc = new MainFrame();
        Scene scene = new Scene(mfc);
        stage.setTitle("UDP Multicast Test");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.show();
    }
}