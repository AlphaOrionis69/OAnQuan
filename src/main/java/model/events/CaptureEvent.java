package model.events;

import model.players.PlayerSide;

public class CaptureEvent extends GameEvent {
	private int squareId;
	private int amountCaptured;
	private PlayerSide side;
	
	public CaptureEvent(int squareId, int amountCaptured, PlayerSide side) {
		this.squareId = squareId;
		this.amountCaptured = amountCaptured;
		this.side = side;
	}

	public int getSquareId() { return squareId; }
	public int getAmountCaptured() { return amountCaptured; }
	public PlayerSide getSide() { return side; }
}