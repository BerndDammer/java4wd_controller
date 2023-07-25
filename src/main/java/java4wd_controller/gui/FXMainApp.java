package java4wd_controller.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMainApp extends Application {
	private static final boolean MAX = true;

	public void start(Stage stage) throws IOException {
		final MainFrame mfc = new MainFrame();
		final Scene scene = new Scene(mfc);
		stage.setScene(scene);
		if (MAX) {
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("CAN Broadcast Test");
			stage.setMaximized(true);
			stage.setAlwaysOnTop(true);
			stage.setFullScreen(false);
		} else {
			stage.setTitle("CAN Broadcast Test");
			stage.centerOnScreen();
			stage.sizeToScene();
		}
		stage.show();
	}
}