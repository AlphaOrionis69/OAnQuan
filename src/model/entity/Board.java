package model.entity;

import model.enums.Direction;
import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int TOTAL_SQUARES = 12;
    private List<Square> squares;

    public Board() {
        squares = new ArrayList<>();
        initBoard();
    }

    private void initBoard() {
        for (int i = 0; i < TOTAL_SQUARES; i++) {
            if (i == 5 || i == 11) {
                squares.add(new MandarinSquare(i, 10)); 
            } else {
                squares.add(new CitizenSquare(i, 5));   
            }
        }
    }

    public Square getSquare(int id) {
        if (id < 0 || id >= TOTAL_SQUARES) return null;
        return squares.get(id);
    }

   
    public int getNextIndex(int currentIndex, Direction direction) {
        if (direction == Direction.CLOCKWISE) {
            return (currentIndex + 1) % TOTAL_SQUARES;
        } else {
            return (currentIndex - 1 + TOTAL_SQUARES) % TOTAL_SQUARES;
        }
    }

    public boolean areMandarinsEmpty() {
        return squares.get(5).isEmpty() && squares.get(11).isEmpty();
    }
    
    
    public List<Square> getSquares() { return squares; }
}