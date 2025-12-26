package model.players;

public class Move {
	private int squareId;
	private boolean isClockwise;

	public Move(int squareId, boolean isClockwise) {
		this.squareId = squareId;
		this.isClockwise = isClockwise;
	}

	public int getSquareId() {
		return squareId;
	}

	public boolean isClockwise() {
		return isClockwise;
	}
	
	
}