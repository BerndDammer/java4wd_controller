package java4wd_controller.can;

public interface ICanEndpoint {
	interface ICanMsgSink
	{
		public void onCanMsg(CanMsg canMsg);
	}

	public interface ICanMsgSource {	
		public void transmit(final CanMsg canMsg);
	}

	public void register(CanMsgFilter canMsgFilter);
	public void setSink(ICanMsgSink iCanMsgSink);
	public void transmit(final CanMsg canMsg);
}
