package java4wd_controller.drive;

import java.nio.ByteBuffer;

import java4wd_controller.CanMsg;
import java4wd_controller.ICanMsgSink;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

public class DriveControl extends Canvas {
	private static final double W = 100.0;
	private final ICanMsgSink iCanMsgSink;

	public DriveControl(ICanMsgSink iCanMsgSink) {
		super(W, W);
		this.iCanMsgSink = iCanMsgSink;
		setOnMouseClicked( this::onMouseClicked);
	}
	
	private void onMouseClicked(MouseEvent mouseEvent)
	{
		double x = mouseEvent.getX();
		double y = mouseEvent.getY();
		
		final CanMsg canMsg = new CanMsg();
		canMsg.id = CanMsg.CAN_ID_DRIVE;
		canMsg.len = 8;
		final ByteBuffer bb = canMsg.getData();
		bb.clear();
		bb.putShort( (short)x);
		bb.putShort( (short)y);
		bb.putShort( (short)0);
		bb.putShort( (short)0);
		iCanMsgSink.transmit(canMsg);
	}
}
