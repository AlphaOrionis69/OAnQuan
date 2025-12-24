package events;

import players.Player;

public class DistributeEvent extends GameEvent {
	public Player player;
	public boolean isLending;
	public int amountLent;
	public int amountPerSquare;
	
	public DistributeEvent(Player player, boolean isLending, int amountLent, int amountPerSquare) {
		this.player = player;
		this.isLending = isLending;
		this.amountLent = amountLent;
		this.amountPerSquare = amountPerSquare;
	}
}