package model.events;

public class MoveEnded extends ModelChange {
	private int lastSquareId;  
	public MoveEnded(int lastSquareId) {
		this.lastSquareId = lastSquareId;
	}
	public int getLastSquareId() { return lastSquareId; }
}