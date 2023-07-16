package java4wd_controller.endpoints;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import java4wd_controller.can.CanMsg;
import java4wd_controller.can.ICan;
import java4wd_controller.gui.FXTimer;
import java4wd_controller.gui.GridPane2;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class LightsControl extends GridPane2 {
	Logger logger = Logger.getLogger(getClass().getName());
	private final ICan.Tentacle tentacle;

	private final FXTimer fxTimer;

	// TODO more messages
	// TODO light show
	public LightsControl(ICan.Tentacle tentacle) {
		super(true);
		this.tentacle = tentacle;

		fxTimer = new FXTimer();
		fxTimer.setAction(this::onTimer, Duration.millis(300.0));

		final Button l1Button = new Button("L1");
		final Button l2Button = new Button("L2");
		final Button l3Button = new Button("L3");
		final Button l4Button = new Button("L4");

		l1Button.onActionProperty().setValue(this::onl1);
		l2Button.onActionProperty().setValue(this::onl2);
		l3Button.onActionProperty().setValue(this::onl3);
		l4Button.onActionProperty().setValue(this::onl4);

		add(l1Button, 0, 0, INSERTING.HGROW);
		add(l2Button, 0, 1, INSERTING.HGROW);
		add(l3Button, 0, 2, INSERTING.HGROW);
		add(l4Button, 0, 3, INSERTING.HGROW);
	}

	private void onl1(ActionEvent event) {
		final CanMsg canMsg = new CanMsg();
		canMsg.id = CanMsg.CAN_ID_LIGHTS;
		canMsg.len = 4;
		final ByteBuffer bb = canMsg.getData();
		bb.clear();
		bb.put((byte) 3);
		bb.put((byte) 20);
		bb.put((byte) 0);
		bb.put((byte) 0);
		tentacle.transmit(canMsg);
	}

	private void onl2(ActionEvent event) {
		final CanMsg canMsg = new CanMsg();
		canMsg.id = CanMsg.CAN_ID_LIGHTS;
		canMsg.len = 4;
		final ByteBuffer bb = canMsg.getData();
		bb.clear();
		bb.put((byte) 4);
		bb.put((byte) 0);
		bb.put((byte) 20);
		bb.put((byte) 0);
		tentacle.transmit(canMsg);
	}

	private void onl3(ActionEvent event) {
		final CanMsg canMsg = new CanMsg();
		canMsg.id = CanMsg.CAN_ID_LIGHTS;
		canMsg.len = 4;
		final ByteBuffer bb = canMsg.getData();
		bb.clear();
		bb.put((byte) 5);
		bb.put((byte) 0);
		bb.put((byte) 0);
		bb.put((byte) 20);
		tentacle.transmit(canMsg);
	}

	private void onl4(ActionEvent event) {
		final CanMsg canMsg = new CanMsg();
		canMsg.id = CanMsg.CAN_ID_LIGHTS;
		canMsg.len = 4;
		final ByteBuffer bb = canMsg.getData();
		bb.clear();
		bb.put((byte) 6);
		bb.put((byte) 0);
		bb.put((byte) 10);
		bb.put((byte) 10);
		tentacle.transmit(canMsg);
	}

	private void onTimer() {

	}
}
