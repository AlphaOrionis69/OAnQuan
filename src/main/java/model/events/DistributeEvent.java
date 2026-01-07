package model.events;

import model.players.PlayerSide;

public class DistributeEvent extends GameEvent {
	private PlayerSide side;
	private String name;
	private boolean isLending;
	private int amountLent;
	private int amountPerSquare;
	
	public DistributeEvent(PlayerSide side, String name, boolean isLending, int amountLent, int amountPerSquare) {
		this.side = side;
		this.name = name;
		this.isLending = isLending;
		this.amountLent = amountLent;
		this.amountPerSquare = amountPerSquare;
	}

	public PlayerSide getSide() { return side; }
	public String getName() { return name; }
	public boolean isLending() { return isLending; }
	public int getAmountLent() { return amountLent; }
	public int getAmountPerSquare() { return amountPerSquare; }
}