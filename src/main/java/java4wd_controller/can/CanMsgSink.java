package java4wd_controller.can;

import java4wd_controller.WebsocketStringServiceBroadcast;
import java4wd_controller.can.ICanEndpoint.ICanMsgSource;
import javafx.collections.ObservableList;

public class CanMsgSink implements ICanEndpoint.ICanMsgSource {
	public ObservableList<CanMsg> poutgoing;
	private final WebsocketStringServiceBroadcast websocketService;

	public CanMsgSink(ObservableList<CanMsg> poutgoing, WebsocketStringServiceBroadcast websocketService) {
		this.poutgoing = poutgoing;
		this.websocketService = websocketService;
	}

	@Override
	public void transmit(CanMsg canMsg) {
		poutgoing.add(canMsg);
		websocketService.sendMsg(canMsg);
	}
}
