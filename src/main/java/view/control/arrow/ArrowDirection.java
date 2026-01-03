package view.control.arrow;

public enum ArrowDirection {
	LEFT, RIGHT;
	public ArrowDirection opposite() {
		return this == ArrowDirection.LEFT ? ArrowDirection.RIGHT : ArrowDirection.LEFT;
	}
	public boolean toBoolean() {
		return this == ArrowDirection.LEFT;
	}
}
