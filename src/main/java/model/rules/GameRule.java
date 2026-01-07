package model.rules;

import model.board.Board;
import model.game.OAnQuanGame;
import model.players.PlayerSide;

public interface GameRule {
	boolean isValidMove(Board board, int squareId, PlayerSide currentSide);
	boolean isGameOver(OAnQuanGame game);
}