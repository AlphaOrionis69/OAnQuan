package model.game;

import model.entity.Board;
import model.entity.Player;
import model.entity.Square;
import model.enums.Direction;
import model.enums.PlayerSide;
import model.rules.GameRule;
import model.rules.StandardRule;

public class OAnQuanGame {
    private Board board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private GameRule rule;
    private boolean isGameOver;

    public OAnQuanGame() {
        initGame();
    }

    public void initGame() {
        this.board = new Board();
        this.player1 = new Player("Player 1", PlayerSide.BOTTOM_SIDE);
        this.player2 = new Player("Player 2", PlayerSide.TOP_SIDE);
        this.currentPlayer = player1;
        this.rule = new StandardRule();
        this.isGameOver = false;
    }

    
    public boolean play(int squareId, boolean isClockwise) {
        if (isGameOver) return false;

      
        if (!rule.isValidMove(board, squareId, currentPlayer)) {
            return false;
        }

        Direction direction = Direction.fromBoolean(isClockwise);

        performMoveLogic(squareId, direction);

        if (rule.isGameOver(board)) {
            isGameOver = true;
            calculateFinalScore(); 
        } else {
            switchTurn();
            checkAndRefillEmptySquares(); 
        }

        return true;
    }

    private void performMoveLogic(int startId, Direction direction) {
        int currentId = startId;
        Square currentSq = board.getSquare(currentId);
        int hand = currentSq.pickUpStones(); 

        while (hand > 0) {

            currentId = board.getNextIndex(currentId, direction);
            board.getSquare(currentId).addStones(1);
            hand--;

            if (hand == 0) {
                int nextId = board.getNextIndex(currentId, direction);
                Square nextSq = board.getSquare(nextId);

                if (nextSq.canBeMoved()) {
                    hand = nextSq.pickUpStones();
                    currentId = nextId;
                }
                else if (nextSq.isEmpty()) {
                    handleCapture(nextId, direction);
                    break; 
                }

                else {
                    break;
                }
            }
        }
    }

    private void handleCapture(int emptySquareId, Direction direction) {
        int currentEmptyId = emptySquareId;
        
        while (true) {

            int targetId = board.getNextIndex(currentEmptyId, direction);
            Square targetSq = board.getSquare(targetId);

            if (!targetSq.isEmpty()) {
                int points = targetSq.pickUpStones() + targetSq.getScoreValue();
                currentPlayer.addScore(points);
                
                int nextOfTarget = board.getNextIndex(targetId, direction);
                if (board.getSquare(nextOfTarget).isEmpty()) {
                     currentEmptyId = nextOfTarget; 
                } else {
                    break; 
                }
            } else {
                break; 
            }
        }
    }

    private void switchTurn() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    private void checkAndRefillEmptySquares() {
        boolean allEmpty = true;
        int start = (currentPlayer.getSide() == PlayerSide.BOTTOM_SIDE) ? 0 : 6;
        int end = (currentPlayer.getSide() == PlayerSide.BOTTOM_SIDE) ? 4 : 10;

        for (int i = start; i <= end; i++) {
            if (!board.getSquare(i).isEmpty()) {
                allEmpty = false;
                break;
            }
        }

        if (allEmpty) {

            if (currentPlayer.getScore() >= 5) { 
                currentPlayer.minusScore(5);
                for (int i = start; i <= end; i++) {
                    board.getSquare(i).addStones(1);
                }
            } else {

                currentPlayer.minusScore(5);
                for (int i = start; i <= end; i++) {
                    board.getSquare(i).addStones(1);
                }
            }
        }
    }

    private void calculateFinalScore() {

        for (int i = 0; i <= 4; i++) {
            player1.addScore(board.getSquare(i).pickUpStones());
        }
        for (int i = 6; i <= 10; i++) {
            player2.addScore(board.getSquare(i).pickUpStones());
        }
    }

    public Board getBoard() { return board; }
    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public boolean isGameOver() { return isGameOver; }
}