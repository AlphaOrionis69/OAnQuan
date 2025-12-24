package rules;

import board.Board;
import players.Player;

public interface GameRule {
	boolean isValidMove(Board board, int squareId, Player currentPlayer);
	boolean isGameOver(Board board);
}