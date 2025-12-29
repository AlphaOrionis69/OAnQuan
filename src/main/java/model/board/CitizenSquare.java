package model.board;

public class CitizenSquare extends Square {	
	public CitizenSquare(int id, int stones) {
		super(id, stones, 0);
	}
	public CitizenSquare(int id, int smallStones, int bigStones) {
		super(id, smallStones, bigStones);
	}
	@Override
	public boolean canMove() {
		return !isEmpty();
	}
	
}