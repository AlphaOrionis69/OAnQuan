package events;

public class DropEvent extends GameEvent {
	private int squareId;
	private int amountDropped;
	public int getSquareId() {
		return squareId;
	}
	public int getAmountDropped() {
		return amountDropped;
	}
	public DropEvent(int squareId, int amountDropped) {
		this.squareId = squareId;
		this.amountDropped = amountDropped;
	}
}