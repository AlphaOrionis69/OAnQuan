package model.events;

public class PickUpEvent extends GameEvent {
	private int squareId;
	private int amountPickedUp;
	
	public int getSquareId() {
		return squareId;
	}

	public int getAmountPickedUp() {
		return amountPickedUp;
	}

	public PickUpEvent(int squareId, int amountPickedUp) {
		this.squareId = squareId;
		this.amountPickedUp = amountPickedUp;
	}
}