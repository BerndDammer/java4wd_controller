package java4wd_controller;

public class Heartbeat implements ICanEndpoint.ICanMsgSink {
	private final ICanEndpoint iCanEndpoint;

	public Heartbeat(ICanEndpoint iCanEndpoint) {

		final CanMsgFilter canMsgFilter = new CanMsgFilter();
		this.iCanEndpoint = iCanEndpoint;
		iCanEndpoint.register(canMsgFilter);
	}

	@Override
	public void onCanMsg(CanMsg canMsg) {
	}
	
	public void onTimeout()
	{
		final CanMsg canMsg = new CanMsg();
		iCanEndpoint.transmit(canMsg);
	}
}