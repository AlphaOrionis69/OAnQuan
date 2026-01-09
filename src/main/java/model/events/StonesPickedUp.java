package model.events;

public class StonesPickedUp extends ModelChange {
	private int squareId;
	private int amountPickedUp;
	
	public int getSquareId() { return squareId; }
	public int getAmountPickedUp() { return amountPickedUp; }

	public StonesPickedUp(int squareId, int amountPickedUp) {
		this.squareId = squareId;
		this.amountPickedUp = amountPickedUp;
	}
}