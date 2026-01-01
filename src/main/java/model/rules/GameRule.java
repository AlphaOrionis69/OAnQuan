package model.rules;

import model.board.Board;
import model.game.OAnQuanGame;
import model.players.Player;

public interface GameRule {
	boolean isValidMove(Board board, int squareId, Player currentPlayer);
	boolean isGameOver(OAnQuanGame game);
}