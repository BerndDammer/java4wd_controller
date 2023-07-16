package java4wd_controller.can;

import java.util.LinkedList;
import java.util.List;

import java4wd_controller.gui.WebsocketStringServiceBroadcast;
import javafx.application.Platform;

public class SquidGame implements ICan.Tentacle, ICan.SquidBody {

	private class DemuxMap {
		private final ICan.Filter filter;
		private final ICan.Endpoint endpoint;

		public DemuxMap(CanMsgFilter filter, ICan.Endpoint endpoint) {
			this.filter = filter;
			this.endpoint = endpoint;
		}
	}

	private final WebsocketStringServiceBroadcast websocketStringServiceBroadcast;
	private final List<DemuxMap> demuxMap = new LinkedList<>();
	private final ICan.Monitoring out;
	private final ICan.Monitoring in;

	public SquidGame(WebsocketStringServiceBroadcast websocketStringServiceBroadcast, ICan.Monitoring out,
			ICan.Monitoring in) {
		this.websocketStringServiceBroadcast = websocketStringServiceBroadcast;
		this.out = out;
		this.in = in;
		websocketStringServiceBroadcast.setSquid(this);
	}

	@Override
	public void transmit(CanMsg canMsg) {
		out.add(canMsg);
		websocketStringServiceBroadcast.sendMsg(canMsg);
	}

	@Override
	public void register(CanMsgFilter canMsgFilter, ICan.Endpoint endpoint) {
		demuxMap.add(new DemuxMap(canMsgFilter, endpoint));
	}

	@Override
	public void push(CanMsg canMsg) {
		in.add(canMsg);
		for (DemuxMap dm : demuxMap) {
			if (dm.filter.is(canMsg)) {
				Platform.runLater(() -> dm.endpoint.onCanMsg(canMsg));
			}
		}
	}
}
