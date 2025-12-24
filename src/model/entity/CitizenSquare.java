package model.entity;

public class CitizenSquare extends Square {
    public CitizenSquare(int id, int stones) {
        super(id, stones);
    }

    @Override
    public boolean canBeMoved() {
        return !isEmpty(); 
    }

    @Override
    public int getScoreValue() {
        return 0; 
    }
}