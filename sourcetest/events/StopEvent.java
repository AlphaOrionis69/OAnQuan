package events;

public class StopEvent extends GameEvent {
	private int lastSquareId;  
	public StopEvent(int lastSquareId) {
		this.lastSquareId = lastSquareId;
	}
	public int getLastSquareId() {
		return lastSquareId;
	}
}