package events;

import players.Player;

public class CaptureEvent extends GameEvent {
	public int squareId;
	public int amount;
	public Player player;
	
	public CaptureEvent(int squareId, int amount, Player player) {
		this.squareId = squareId;
		this.amount = amount;
		this.player = player;
	}
}