package java4wd_controller;

import javafx.collections.ObservableList;

public class CanMsgSink implements ICanMsgSink {
	ObservableList<CanMsg> poutgoing;
	private final WebsocketStringServiceBroadcast websocketService;

	CanMsgSink(ObservableList<CanMsg> poutgoing, WebsocketStringServiceBroadcast websocketService) {
		this.poutgoing = poutgoing;
		this.websocketService = websocketService;
	}

	@Override
	public void transmit(CanMsg canMsg) {
		poutgoing.add(canMsg);
		websocketService.sendMsg(canMsg);
	}
}
