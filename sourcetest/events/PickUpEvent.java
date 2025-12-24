package events;

public class PickUpEvent extends GameEvent {
	public int squareId;
	public int amount;
	
	public PickUpEvent(int squareId, int amount) {
		this.squareId = squareId;
		this.amount = amount;
	}
}