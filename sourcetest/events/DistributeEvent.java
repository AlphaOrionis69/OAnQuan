package events;

import players.Player;

public class DistributeEvent extends GameEvent {
	private Player player;
	private boolean isLending;
	private int amountLent;
	private int amountPerSquare;
	
	public DistributeEvent(Player player, boolean isLending, int amountLent, int amountPerSquare) {
		this.player = player;
		this.isLending = isLending;
		this.amountLent = amountLent;
		this.amountPerSquare = amountPerSquare;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isLending() {
		return isLending;
	}

	public int getAmountLent() {
		return amountLent;
	}

	public int getAmountPerSquare() {
		return amountPerSquare;
	}
}