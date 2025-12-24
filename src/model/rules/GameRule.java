package model.rules;

import model.entity.Board;
import model.entity.Player;

public interface GameRule {
    boolean isValidMove(Board board, int squareId, Player player);
    boolean isGameOver(Board board);
}