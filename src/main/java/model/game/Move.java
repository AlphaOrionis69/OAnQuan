package model.game;

public class Move {
	private int squareId;
	private Direction direction;
	public Move(int squareId, Direction direction) {
		this.squareId = squareId;
		this.direction = direction;
	}
	public int getSquareId() { return squareId; }
	public Direction getDirection() { return direction; }	
}