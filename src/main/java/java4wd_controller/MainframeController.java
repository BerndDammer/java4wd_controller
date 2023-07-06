package java4wd_controller;

import java4wd_controller.WebsocketStringServiceBroadcast.NonFXThreadEventReciever;
import java4wd_controller.can.CanMsg;
import java4wd_controller.can.CanMsgHeartbeat;
import java4wd_controller.can.CanMsgSink;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class MainframeController extends MainframeParameter implements NonFXThreadEventReciever {

	private final FXTimer transmittWorker = new FXTimer(this::onTransmitt);
	private final WebsocketStringServiceBroadcast websocketService = new WebsocketStringServiceBroadcast(this);
	private final GridPane rootNode;
	private final CanMsgHeartbeat canMsgHeartbeat = new CanMsgHeartbeat();

	public MainframeController() {
		final CanMsgSink canMsgSink = new CanMsgSink(outgoing, websocketService);
		rootNode = new MainframeLoader(this, canMsgSink);

		//////////////////////////////
		/////////// Action Connections
		onActionPropertyStartButton.setValue(this::onStart);
		onActionPropertyStopButton.setValue(this::onStop);
//        onActionPropertyDriveButton.setValue(this::onDrive);

		message.bind(websocketService.messageProperty());
		title.bind(websocketService.titleProperty());
		websocketService.stateProperty().addListener(this::onNewWorkerState);
	}

	public void onStart(ActionEvent event) {
		websocketService.reset();
		websocketService.start();
		transmittWorker.setRate(Duration.millis(2300));
		transmittWorker.setEnabled(true);
	}

	public void onStop(ActionEvent event) {
		websocketService.cancel();
		transmittWorker.setEnabled(false);
	}

	public void onDrive(ActionEvent event) {
		final CanMsg canMsg = new CanMsg();
		canMsg.id = CanMsg.CAN_ID_DRIVE;
		canMsg.len = 8;
		canMsg.data.clear();
		canMsg.data.putShort((short) 60);
		canMsg.data.putShort((short) 60);
		canMsg.data.putShort((short) 60);
		canMsg.data.putShort((short) 60);
		outgoing.add(canMsg);
		websocketService.sendMsg(canMsg);
	}

	void onNewWorkerState(ObservableValue<? extends Worker.State> observable, Worker.State oldValue,
			Worker.State newValue) {

		// stateWorker.setValue(newValue);
		switch (newValue) {
		case FAILED: {
			Throwable t = websocketService.getException();
			t.printStackTrace();
			disablePropertyStartButton.setValue(false);
		}
			break;
		case CANCELLED:
		case READY:
		case SCHEDULED:
		case SUCCEEDED:
			disablePropertyStartButton.setValue(false);
			break;
		case RUNNING:
			disablePropertyStartButton.setValue(true);
			break;
		}
	}

	public GridPane getRootNode() {
		return rootNode;
	}

	private void onTransmitt() {
		CanMsg canMsg = canMsgHeartbeat.get();
		if (outgoing.size() > General.LOG_AUTODELETE) {
			outgoing.clear();
		}
		outgoing.add(canMsg);
		websocketService.sendMsg(canMsg);
	}

	@Override
	public void xonNewText(final CanMsg canMsg) {
		Platform.runLater(() -> {
			if (incomming.size() > General.LOG_AUTODELETE) {
				incomming.clear();
			}
			incomming.add(canMsg);
			canMsgHeartbeat.got(canMsg);
		});
	}
}
