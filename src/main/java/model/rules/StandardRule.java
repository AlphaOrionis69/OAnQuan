package model.rules;

import model.board.Board;

import model.board.MandarinSquare;
import model.board.Square;
import model.game.OAnQuanGame;
import model.players.Player;
import model.players.PlayerSide;

public class StandardRule implements GameRule {
	// uncomment if run CLI version
	@Override
	public boolean isValidMove(Board board, int squareId, PlayerSide currentSide) {
		Square sq = board.getSquare(squareId);
		if (sq == null) {
			//System.out.println("You move from outside the board??");
			return false;
		}
		if (squareId < currentSide.start() || squareId > currentSide.end()) {
			return false;
		}
		
		if (!sq.canMove()) {
			//System.out.println("Wrong square");
			return false;
		}
		return true;
	}
	@Override
	public boolean isGameOver(OAnQuanGame game) {
		boolean firstScenario = game.getBoard().areMandarinsEmpty();
		boolean secondScenario = (game.getPlayer1().getScore() + game.getPlayer2().getScore() < 5) 
				&& game.getBoard().isSideEmpty(game.getCurrentPlayer().getSide());
		return firstScenario || secondScenario;
	}
}