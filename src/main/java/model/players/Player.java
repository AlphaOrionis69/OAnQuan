package model.players;

public class Player {
	private String name;
	private int score;
	private PlayerSide side;
	
	public Player(String name, PlayerSide side) {
		this.name = name;
		this.side = side;
		this.score = 0;
	}
	@Override
	public String toString() {
		return String.format("%s score: %d side: ", getName(), getScore()) + getSide();
	}
	public String getName() { return name; }
	public int getScore() { return score; }
	public PlayerSide getSide() { return side; }
	
	public void addScore(int points) {
		this.score += points;
	}
	public void decreaseScore(int points) {
		this.score -= points;
	}
	public void resetScore() {
		this.score = 0;
	}
	
}