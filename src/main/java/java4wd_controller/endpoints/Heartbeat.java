package java4wd_controller.endpoints;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

import java4wd_controller.can.CanMsg;
import java4wd_controller.can.CanMsgFilter;
import java4wd_controller.can.ICan;
import java4wd_controller.can.ICan.Tentacle;
import java4wd_controller.gui.FXTimer;
import java4wd_controller.gui.General;
import java4wd_controller.gui.GridPane2;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class Heartbeat extends GridPane2 implements ICan.Endpoint {
	private static final Logger logger = Logger.getLogger(Heartbeat.class.getName());

	private FXTimer fxTimer = new FXTimer();
	private final ICan.Tentacle tentacle;
	private static final long VALID_TIMEOUT_MS = 3000l;
	private short counter = 1;
	private long lastRec = 0l;
	private short lastSeen;

	private final CheckBox hEnable;
	// TODO more informative status
	private final Label status = new Label("XXXXXXXX");

	public Heartbeat(Tentacle tentacle) {
		super(true);
		this.tentacle = tentacle;
		tentacle.register(CanMsgFilter.HEARTBEAT, this);
		fxTimer.setAction(this::onTimeout, General.HEARTBEAT_DELAY_MS);
		hEnable = new CheckBox("enable");
		hEnable.setOnAction(this::onEnable);
		add(hEnable, 0, 0, INSERTING.HGROW);
		add(status, 0, 1, INSERTING.HGROW);
	}

	private void onEnable(ActionEvent ae) {
		if (hEnable.isSelected()) {
			fxTimer.setEnabled(true);
		} else {
			fxTimer.setEnabled(false);
			counter = 0;
		}
	}

	@Override
	public void onCanMsg(CanMsg canMsg) {
		final ByteBuffer bb = canMsg.data;
		if (canMsg.getId() != CanMsg.CAN_ID_HEARTBEAT) {
			logger.warning("Wrong CAN ID");
		}
		if (canMsg.getLen() != 8) {
			logger.warning("Wrong CAN LEN");
		}

		bb.clear();
		lastSeen = bb.getShort();
		int echo = bb.getShort();
		lastRec = System.currentTimeMillis();
		status.setText(echo != -1 ? "OK" : "Reaction Failure");
	}

	public void onTimeout() {

		counter++;
		CanMsg result = new CanMsg();
		result.id = CanMsg.CAN_ID_HEARTBEAT;
		result.len = 8;
		result.data.clear();
		result.data.order(ByteOrder.LITTLE_ENDIAN);

		final ByteBuffer bb = result.data;
		bb.putShort(counter);
		if (System.currentTimeMillis() <= lastRec + VALID_TIMEOUT_MS) {
			bb.putShort(lastSeen);
		} else {
			bb.putShort((short) 0XFFFF);
			status.setText("Echo Timeout");
		}
		bb.putShort((short) 0);
		bb.putShort((short) 0);
		tentacle.transmit(result);
	}
}
