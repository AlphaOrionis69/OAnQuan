package model.board;

public class MandarinSquare extends Square { 
	public MandarinSquare(int id, int stones) {
		super(id, 0, stones);   
	}
	public MandarinSquare(int id, int smallStones, int bigStones) {
		super(id, smallStones, bigStones);
	}
	
	@Override
	public boolean canMove() {
		return false;
	}
}