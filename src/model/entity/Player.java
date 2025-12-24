package model.entity;

import model.enums.PlayerSide;

public class Player {
    private String name;
    private int score;
    private PlayerSide side;

    public Player(String name, PlayerSide side) {
        this.name = name;
        this.side = side;
        this.score = 0;
    }

    public void addScore(int points) {
        this.score += points;
    }
    
    public void minusScore(int points) {
        this.score -= points;
    }

    // Getters
    public String getName() { return name; }
    public int getScore() { return score; }
    public PlayerSide getSide() { return side; }
}