package players;

import board.Board;

public abstract class Player {
	private String name;
	private int score;
	private int side;
	
	public Player(String name, int side) {
		this.name = name;
		this.side = side;
		this.score = 0;
	}
	@Override
	public String toString() {
		return String.format("%s (%d) score: %d", getName(), getSide() + 1, getScore());
	}
	public String getName() {
		return name;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getSide() {
		return side;
	}
	
	public void addScore(int points) {
		this.score += points;
	}
	
	public void decreaseScore(int points) {
		this.score -= points;
	}
	public void setScore(int points) {
		this.score= points;
	}
	public void resetScore() {
		this.score = 0;
	}
	
	public abstract Move makeMove(Board board);
}