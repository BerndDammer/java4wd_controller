package java4wd_controller.gui;

import java4wd_controller.can.SqidGame;
import java4wd_controller.can.CanMsgView2;
import java4wd_controller.endpoints.DriveControl;
import java4wd_controller.endpoints.Heartbeat;
import java4wd_controller.endpoints.LightsControl;

public class MainFrame extends GridPane2 {

	public MainFrame() {
		super(true);

		final CanMsgView2 incomming = new CanMsgView2();
		final CanMsgView2 outgoing = new CanMsgView2();

		final WebsocketStringServiceBroadcast websocketService = new WebsocketStringServiceBroadcast();
		final SqidGame squidGame = new SqidGame(websocketService, outgoing, incomming);

		add(new DriveControl(squidGame), 1, 0, INSERTING.CENTER);
		add(new LightsControl(squidGame), 0, 1, INSERTING.CENTER);
		add(new CanBusServiceView(websocketService), 1, 1, INSERTING.CENTER);
		add(new Heartbeat(squidGame), 0,0);
		
		add(incomming, 0, 2, INSERTING.FILL);
		add(outgoing, 1, 2, INSERTING.FILL);
		
		websocketService.setSquid(squidGame);
	}
}
