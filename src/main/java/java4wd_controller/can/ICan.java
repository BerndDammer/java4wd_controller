package java4wd_controller.can;


public interface ICan {
	// listing in and out messages
	public interface Monitoring {
		public void add(final CanMsg canMsg);
	}

	// where endpoints are conneted to
	// Squid offers to endpoints
	public interface Tentacle {
		public void register(CanMsgFilter canMsgFilter, ICan.Endpoint iCanMsgSink);

		public void transmit(final CanMsg canMsg);
	}

	// from node to mux / demux
	public interface Squid {
		public void push(CanMsg canMsg);
	}

	// Message from Squid to Endpoint
	public interface Endpoint {
		public void onCanMsg(CanMsg canMsg);
	}
}
