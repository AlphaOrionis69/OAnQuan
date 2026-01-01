package model.events;

public class StopEvent extends GameEvent {
	private int lastSquareId;  
	public static final int GAME_OVER = -1;
	public StopEvent(int lastSquareId) {
		this.lastSquareId = lastSquareId;
	}
	public int getLastSquareId() { return lastSquareId; }
}