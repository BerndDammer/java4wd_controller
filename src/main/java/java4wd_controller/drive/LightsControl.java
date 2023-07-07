package java4wd_controller.drive;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import java4wd_controller.FXTimer;
import java4wd_controller.GridPane2;
import java4wd_controller.can.CanMsg;
import java4wd_controller.can.ICanEndpoint;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class LightsControl extends GridPane2 {
	Logger logger = Logger.getLogger(getClass().getName());
	private final ICanEndpoint.ICanMsgSource iCanMsgSink;

	private final FXTimer fxTimer;

	public LightsControl(ICanEndpoint.ICanMsgSource iCanMsgSink) {
		super(true);
		this.iCanMsgSink = iCanMsgSink;

		fxTimer = new FXTimer(this::onTimer);
		fxTimer.setRate(Duration.millis(300.0));

		final Button l1Button = new Button("L1");
		final Button l2Button = new Button("L2");
		final Button l3Button = new Button("L3");
		final Button l4Button = new Button("L4");

		l1Button.onActionProperty().setValue(this::onl1);
		l2Button.onActionProperty().setValue(this::onl2);
		l3Button.onActionProperty().setValue(this::onl3);
		l4Button.onActionProperty().setValue(this::onl4);

		add(l1Button, 0,0, INSERTING.HGROW);
		add(l2Button, 0,1, INSERTING.HGROW);
		add(l3Button, 0,2, INSERTING.HGROW);
		add(l4Button, 0,3, INSERTING.HGROW);
	}

	private void onl1(ActionEvent event) {
		final CanMsg canMsg = new CanMsg();
		canMsg.id = CanMsg.CAN_ID_LIGHTS;
		canMsg.len = 4;
		final ByteBuffer bb2 = canMsg.getData();
		bb2.clear();
		bb2.put((byte) 3);
		bb2.put((byte) 20);
		bb2.put((byte) 0);
		bb2.put((byte) 0);
		iCanMsgSink.transmit(canMsg);
	}
	private void onl2(ActionEvent event) {
		final CanMsg canMsg = new CanMsg();
		canMsg.id = CanMsg.CAN_ID_LIGHTS;
		canMsg.len = 4;
		final ByteBuffer bb2 = canMsg.getData();
		bb2.clear();
		bb2.put((byte) 4);
		bb2.put((byte) 0);
		bb2.put((byte) 20);
		bb2.put((byte) 0);
		iCanMsgSink.transmit(canMsg);
	}
	private void onl3(ActionEvent event) {
		final CanMsg canMsg = new CanMsg();
		canMsg.id = CanMsg.CAN_ID_LIGHTS;
		canMsg.len = 4;
		final ByteBuffer bb2 = canMsg.getData();
		bb2.clear();
		bb2.put((byte) 5);
		bb2.put((byte) 0);
		bb2.put((byte) 0);
		bb2.put((byte) 20);
		iCanMsgSink.transmit(canMsg);
	}
	private void onl4(ActionEvent event) {	
		final CanMsg canMsg = new CanMsg();
		canMsg.id = CanMsg.CAN_ID_LIGHTS;
		canMsg.len = 4;
		final ByteBuffer bb2 = canMsg.getData();
		bb2.clear();
		bb2.put((byte) 6);
		bb2.put((byte) 0);
		bb2.put((byte) 10);
		bb2.put((byte) 10);
		iCanMsgSink.transmit(canMsg);
	}
	private void onTimer() {	
		
	}
}
