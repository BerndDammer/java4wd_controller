package java4wd_controller.gui;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.logging.Logger;

import java4wd_controller.can.CanMsg;
import java4wd_controller.can.ICan;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

// TODO add logger
public class WebsocketStringServiceBroadcast extends Service<Void> {
	private static final Logger logger = Logger.getLogger(WebsocketStringServiceBroadcast.class.getName());

	static class Ticker {

		private long nextEvent;
		private final long delayMs;
		private long thisTimeout;
		
		Ticker(int delayMs) {
			this.delayMs = (long) delayMs;
			long now = System.currentTimeMillis();
			nextEvent = delayMs + now;
			thisTimeout = now - nextEvent;
		}

		void test() {
			thisTimeout = nextEvent - System.currentTimeMillis();
		}

		boolean isBehind() {
			return thisTimeout <= 0l;
		}

		long nextTimeout() {
			return thisTimeout;
		}

		void tick() {
			nextEvent += delayMs;
		}
	}

	// TODO better Sync
	private DatagramChannel dc = null;
	private ICan.SquidBody squid;

	public class WebsocketTask extends Task<Void> {

		public WebsocketTask() {
		}

		@Override
		protected Void call() throws Exception {
			updateTitle("Broadcaster");
			updateMessage("Starting");
			int counter = 0;

			dc = DatagramChannel.open(StandardProtocolFamily.INET);
			dc.bind(new InetSocketAddress(General.RECEIVE_PORT));
			dc.setOption(StandardSocketOptions.SO_BROADCAST, true);

			Selector selector = Selector.open();
			dc.configureBlocking(false);
			SelectionKey key = dc.register(selector, SelectionKey.OP_READ);
			ByteBuffer byteBuffer = ByteBuffer.allocate(General.BUFFER_SIZE);

			Ticker ticker = new Ticker(General.SELECT_TIMEOUT);
			updateMessage("Running");
			while (!isCancelled()) {
				int channels;
				ticker.test();
				if (ticker.isBehind()) {
					channels = selector.selectNow();
				} else {
					channels = selector.select(ticker.nextTimeout());
				}
				if (channels == 0) {
					//
				} else {
					for (SelectionKey skey : selector.selectedKeys()) {
						skey.channel();
						byteBuffer.clear();
						SocketAddress isa = dc.receive(byteBuffer);
						byteBuffer.flip();
						{
							int size = byteBuffer.limit() - byteBuffer.position();
							if (size != 16) {
								logger.info("Receive Size " + size);
							}
						}
						{
							final CanMsg canMsg = new CanMsg();
							canMsg.fromBB(byteBuffer);
							squid.push(canMsg);
						}
						updateMessage("Got data from: " + isa + "  Count: " + counter++);
						selector.selectedKeys().remove(skey); // nercessary ????
					}
				}
			}
			selector.close();
			key.cancel();

			dc.close();

			updateMessage("Bye!");
			return null;
		}
	}

	public WebsocketStringServiceBroadcast() {
	}

	@Override
	protected Task<Void> createTask() {

		return new WebsocketTask();
	}

	// TODO best Way to transmitt
	public void sendMsg(CanMsg canMsg) {
		ByteBuffer bb = ByteBuffer.allocate(16);
		canMsg.toBB(bb);
		bb.flip();
		try {
			// TODO check dc valid
			dc.send(bb, General.FINAL_DESTINATION);
		} catch (Exception e) {
			logger.warning(e.getMessage());
		}
	}

	public ICan.SquidBody getSquid() {
		return squid;
	}

	public void setSquid(ICan.SquidBody squid) {
		this.squid = squid;
	}
}
