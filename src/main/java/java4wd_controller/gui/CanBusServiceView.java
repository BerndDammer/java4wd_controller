package java4wd_controller.gui;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CanBusServiceView extends GridPane2 {

	private final WebsocketStringServiceBroadcast websocketStringServiceBroadcast;

	final Button startButton = new Button("Start");
	final Button stopButton = new Button("Stop");
	final Label message = new Label("XXXXXXXX");
	final Label title = new Label("WWWWWW");
	final Label exception = new Label("");

	public CanBusServiceView(WebsocketStringServiceBroadcast websocketStringServiceBroadcast) {
		super(true);
		this.websocketStringServiceBroadcast = websocketStringServiceBroadcast;

		// colum row
		add(new Label("Title"), 0, 0);
		add(title, 1, 0);

		add(new Label("Message"), 0, 1);
		add(message, 1, 1);

		add(new Label("Exception"), 0, 2);
		add(exception, 1, 2);

		add(startButton, 0, 3, INSERTING.CENTER);
		add(stopButton, 1, 3, INSERTING.CENTER);

		message.textProperty().bind(websocketStringServiceBroadcast.messageProperty());
		title.textProperty().bind(websocketStringServiceBroadcast.titleProperty());

		exception.textProperty().bind(websocketStringServiceBroadcast.exceptionProperty().asString());

		startButton.onActionProperty().setValue(this::onStart);
		stopButton.onActionProperty().setValue(this::onStop);

		websocketStringServiceBroadcast.stateProperty().addListener(this::onNewWorkerState);
	}

	public void onStart(ActionEvent event) {
		websocketStringServiceBroadcast.reset();
		websocketStringServiceBroadcast.start();
	}

	public void onStop(ActionEvent event) {
		websocketStringServiceBroadcast.cancel();
	}

	void onNewWorkerState(ObservableValue<? extends Worker.State> observable, Worker.State oldValue,
			Worker.State newValue) {

        // TODO make enum choice
		// stateWorker.setValue(newValue);
		switch (newValue) {
		case FAILED: {
			Throwable t = websocketStringServiceBroadcast.getException();
			t.printStackTrace();
			startButton.disableProperty().setValue(false);
		}
			break;
		case CANCELLED:
		case READY:
		case SCHEDULED:
		case SUCCEEDED:
			startButton.disableProperty().setValue(false);
			break;
		case RUNNING:
			startButton.disableProperty().setValue(true);
			break;
		}
	}
}
