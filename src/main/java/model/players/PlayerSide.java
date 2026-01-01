package model.players;

import model.game.Direction;

public enum PlayerSide {
	BOTTOM(0, 4), TOP(6, 10);
	private final int start;
	private final int end;
	PlayerSide(int start, int end) {
		this.start = start;
		this.end = end;
	}
	public int start() {
		return start;
	}
	public int end() {
		return end;
	}
	public PlayerSide opposite() {
		return this == PlayerSide.BOTTOM ? PlayerSide.TOP : PlayerSide.BOTTOM;
	}
	public static PlayerSide fromBoolean(boolean isTopSide) {
		return isTopSide ? PlayerSide.TOP : PlayerSide.BOTTOM;
	}
	@Override
	public String toString() {
		return this == PlayerSide.BOTTOM ? "bottom" : "top";
	}
}
