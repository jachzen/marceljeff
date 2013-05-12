
/**
 * Represents a buy or sell order that has been placed.
 */
public abstract class Order {

	public enum State {
		OPEN, PENDING, DONE
	}

	private String reference;
	private OrderType orderType = OrderType.DO_NOTHING;;
	private State state = State.DONE;
	private double volume;

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

}
