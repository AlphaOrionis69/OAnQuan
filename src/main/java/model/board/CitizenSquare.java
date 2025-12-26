package model.board;

public class CitizenSquare extends Square {	
	public CitizenSquare(int id, int stones) {
		super(id, stones, 0);
	}
	
	@Override
	public boolean canMove() {
		return !isEmpty();
	}
	
}