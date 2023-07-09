package java4wd_controller.can;

public enum CanMsgFilter implements ICan.Filter {

	HEARTBEAT(0XFFFFFF00, 0X81041300), //
	DRIVE(0XFFFFFF00, 0X82050100), //
	LIGHTS(0XFFFFFF00, 0X82060100);

	private final int mask;
	private final int val;

	CanMsgFilter(final int mask, final int val) {
		this.mask = mask;
		this.val = val;
	}

	@Override
	public boolean is(CanMsg canMsg) {
		boolean result;
		result = (canMsg.id & mask) == val;
		return result;
	}
}
