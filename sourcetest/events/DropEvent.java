package events;

public class DropEvent extends GameEvent {
	public int squareId;
	public int amountDrop;
	public DropEvent(int squareId, int amountDrop) {
		this.squareId = squareId;
		this.amountDrop = amountDrop;
	}
}