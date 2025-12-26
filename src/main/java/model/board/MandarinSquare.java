package model.board;

public class MandarinSquare extends Square { 
	public MandarinSquare(int id, int stones) {
		super(id, 0, stones);   
	}
	
	@Override
	public boolean canMove() {
		return false;
	}
}