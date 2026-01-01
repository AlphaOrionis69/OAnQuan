package model.game;

public enum Direction {
	CLOCKWISE(1),
	COUNTER_CLOCKWISE(-1);
	
	private final int step;
	Direction(int step) {
		this.step = step;
	}
	public int step() {
		return step;
	}
	public Direction opposite() {
		return this == Direction.CLOCKWISE ? Direction.COUNTER_CLOCKWISE : Direction.CLOCKWISE;
	}
	public static Direction fromBoolean(boolean isClockwise) {
		return isClockwise ? Direction.CLOCKWISE : Direction.COUNTER_CLOCKWISE;
	}
	@Override
	public String toString() {
		return this == Direction.CLOCKWISE ? "cw" : "ccw";
	}
}
