package model.rules;

import model.entity.Board;
import model.entity.Player;
import model.enums.PlayerSide;

public class StandardRule implements GameRule {

    @Override
    public boolean isValidMove(Board board, int squareId, Player player) {
        if (player.getSide() == PlayerSide.BOTTOM_SIDE && (squareId < 0 || squareId > 4)) return false;
        if (player.getSide() == PlayerSide.TOP_SIDE && (squareId < 6 || squareId > 10)) return false;

        return board.getSquare(squareId).canBeMoved();
    }

    @Override
    public boolean isGameOver(Board board) {
        return board.areMandarinsEmpty();
    }
}