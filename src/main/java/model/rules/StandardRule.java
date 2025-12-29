package model.rules;

import model.board.Board;
import model.board.MandarinSquare;
import model.board.Square;
import model.players.Player;

public class StandardRule implements GameRule {
	// uncomment if run CLI version
	@Override
	public boolean isValidMove(Board board, int squareId, Player currentPlayer) {
		Square sq = board.getSquare(squareId);
		if (sq == null) {
			//System.out.println("You move from outside the board??");
			return false;
		}
		boolean playRightSide = squareId >= currentPlayer.getSide()*6 && squareId < (currentPlayer.getSide()+1)*6 - 1;
		if (!playRightSide) {
			//System.out.println("You play at the right side??");
			return false;
		}
			
		if (!sq.canMove()) {
			//System.out.println("Wrong square");
			return false;
		}
		return true;
	}
	@Override
	public boolean isGameOver(Board board) {
		return board.areMandarinsEmpty();
	}
}