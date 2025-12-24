package model.entity;

public abstract class Square {
	protected int id;
    protected int stones;

    public Square(int id, int stones) {
        this.id = id;
        this.stones = stones;
    }

    public int getId() { return id; }
    public int getStones() { return stones; }
    
    public void addStones(int amount) {
        this.stones += amount;
    }

    public int pickUpStones() {
        int temp = this.stones;
        this.stones = 0;
        return temp;
    }

    public boolean isEmpty() {
        return this.stones == 0;
    }

    public abstract boolean canBeMoved();
    public abstract int getScoreValue(); 
}
