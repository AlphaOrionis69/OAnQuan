package events;

public class StopEvent extends GameEvent {
	public int lastSquareId;  
	public StopEvent(int lastSquareId) {
		this.lastSquareId = lastSquareId;
	}
}