package model.events;

import model.players.Player;

public class CaptureEvent extends GameEvent {
	private int squareId;
	private int amountCaptured;
	private Player player;
	
	public CaptureEvent(int squareId, int amountCaptured, Player player) {
		this.squareId = squareId;
		this.amountCaptured = amountCaptured;
		this.player = player;
	}

	public int getSquareId() { return squareId; }
	public int getAmountCaptured() { return amountCaptured; }
	public Player getPlayer() { return player; }
}