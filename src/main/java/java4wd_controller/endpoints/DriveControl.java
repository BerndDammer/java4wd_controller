package java4wd_controller.endpoints;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import java4wd_controller.can.CanMsg;
import java4wd_controller.can.ICan;
import java4wd_controller.gui.FXTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class DriveControl extends Canvas {
	Logger logger = Logger.getLogger(getClass().getName());
	private static final double W = 100.0;
	private final ICan.Tentacle tentacle;

	private final FXTimer fxTimer;
	private double mx;
	private double my;

	public DriveControl(ICan.Tentacle tentacle) {
		super(W, W);
		this.tentacle = tentacle;

		setOnMousePressed(this::onMouseClicked);
		setOnMouseDragged(this::onMouseDragged);
		setOnMouseReleased(this::onMouseReleased);
		setOnMouseExited(this::onMouseExited);

		setActiveColor(false);

		fxTimer = new FXTimer(this::ping);
		fxTimer.setRate(Duration.millis(300.0));
	}

	private void setActiveColor(final boolean active) {
		GraphicsContext gc = getGraphicsContext2D();
		gc.setFill(active ? Color.LIGHTGREEN : Color.LIGHTGRAY);
		gc.fillRect(0.0, 0.0, getWidth(), getHeight());
	}

	// TODO better tracking logic
	private void onMouseExited(MouseEvent mouseEvent) {
		logger.info("exit");
		deactivate();
	}

	private void onMouseReleased(MouseEvent mouseEvent) {
		logger.info("released");
		deactivate();
	}

	private void onMouseDragged(MouseEvent mouseEvent) {
		activate();
		mx = mouseEvent.getX();
		my = mouseEvent.getY();

		logger.info("X " + mx + "  Y " + my);
	}

	private void onMouseClicked(MouseEvent mouseEvent) {
		activate();

		mx = mouseEvent.getX();
		my = mouseEvent.getY();

		logger.info("X " + mx + "  Y " + my);
	}

	private void activate() {
		setActiveColor(true);
		fxTimer.setEnabled(true);
	}

	private void deactivate() {
		mx = W * 0.5;
		my = W * 0.5;
		setActiveColor(false);
		fxTimer.setEnabled(false);
	}

	private void ping() {
		double wx, wy, l, r, f;
		wx = mx / W * 2.0 - 1.0;
		wy = 1.0 - my / W * 2.0;

		l = wy + wx;
		r = wy - wx;
		f = 1.0;

		if (l < -1.0 && f < -l)
			f = -l;
		if (r < -1.0 && f < -r)
			f = -r;
		if (l > 1.0 && f < l)
			f = l;
		if (r > 1.0 && f < r)
			f = r;
		l /= f;
		r /= f;
		l *= 100.0;
		r *= 100.0;

		final CanMsg canMsg = new CanMsg();
		canMsg.id = CanMsg.CAN_ID_DRIVE;
		canMsg.len = 8;
		final ByteBuffer bb = canMsg.getData();
		bb.clear();
		bb.putShort((short) l);
		bb.putShort((short) r);
		bb.putShort((short) l);
		bb.putShort((short) r);
		tentacle.transmit(canMsg);
	}
}
