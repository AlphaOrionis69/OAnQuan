
package model.board;

public abstract class Square {
	private final int id;
	private int smallStones;
	private int bigStones;
	public Square(int id, int smallStones, int bigStones) {
		this.id = id;
		this.smallStones = smallStones;
		this.bigStones = bigStones;
	}	
	
	public int getId() { return id; }	
	public int getSmallStones() { return smallStones; }
	public int getBigStones() { return bigStones; }
	
	public void addStones(int amount) {
		this.smallStones += amount;
	}
	@Override
	public String toString() {
		return String.format("[%d]", calculatePoints());
	}
	
	public int pickUpStones() {
		int picked = calculatePoints();
		smallStones = bigStones = 0;
		return picked;
	}
	
	public boolean isEmpty() {
		return smallStones == 0 && bigStones == 0;
	}
	
	public abstract boolean canMove();
	
	public int calculatePoints() {
		return smallStones + bigStones * 5;
	}
}