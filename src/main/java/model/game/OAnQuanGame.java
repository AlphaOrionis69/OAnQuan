package model.game;

import java.util.ArrayList;

import java.util.List;
import model.board.*;
import model.events.*;
import model.players.Player;
import model.players.PlayerSide;
import model.rules.GameRule;
import model.rules.StandardRule;

public class OAnQuanGame {
	private Board board;
	private Player player1;
	private Player player2;
	private Player currentPlayer;
	private GameRule rule;
	private boolean isGameOver;
	private int penalty = 0; 
	
	public OAnQuanGame() {
		startNewGame("Player 1", "Player 2");
	}
	
	public void startNewGame(String p1Name, String p2Name) {
		board = new Board();
		rule = new StandardRule();
		player1 = new Player(p1Name, PlayerSide.BOTTOM);
		player2 = new Player(p2Name, PlayerSide.TOP);
		player1.setScore(0);
		player2.setScore(0);
		currentPlayer = player1;
		isGameOver = false;		
		
		penalty = 0;
	}
	
	public void endGame() {
		if (!isGameOver) return;
		player1.addScore(penalty);
		player2.decreaseScore(penalty);
		penalty = 0;
		for (int i = PlayerSide.BOTTOM.start(); i <= PlayerSide.BOTTOM.end(); i++) player1.addScore(board.getSquare(i).pickUpStones());
		for (int i = PlayerSide.TOP.start(); i <= PlayerSide.TOP.end(); i++) player2.addScore(board.getSquare(i).pickUpStones());
	}
	
	public List<GameEvent> checkAndDistribute() {
		if (isGameOver) return new ArrayList<>();
		
		int startIdx = currentPlayer.getSide().start();
		int endIdx = currentPlayer.getSide().end();
		
		List<GameEvent> events = new ArrayList<>();
		if (board.isSideEmpty(currentPlayer.getSide())) {
			int stonesNeeded = 5;
			boolean isLending = false;
			int amountLent = 0;
			if (currentPlayer.getScore() >= stonesNeeded) {
				currentPlayer.decreaseScore(stonesNeeded);
			} else {
				amountLent = stonesNeeded - currentPlayer.getScore();
				isLending = true;
				if (currentPlayer == player1) {
					penalty -= amountLent;
					player2.decreaseScore(amountLent);
				} else {
					penalty += amountLent;
					player1.decreaseScore(amountLent);
				}
				currentPlayer.setScore(0);
			}
			
			events.add(new DistributeEvent(currentPlayer, isLending, amountLent, 1));
			
			for (int i = startIdx; i <= endIdx; i++) {
				board.getSquare(i).addStones(1);
			}
		}
		return events;
	}
	
	public List<GameEvent> move(Move move) {
		if (!rule.isValidMove(board, move.getSquareId(), currentPlayer)) return null;
		
		List<GameEvent> events = new ArrayList<>();
		
		events.addAll(performMoveLogic(move.getSquareId(), move.getDirection()));
		
		// here to avoid mixing game-over reasons, push the check after switching turn, better to return reasons instead of booleans
		if (currentPlayer == player1) currentPlayer = player2;
		else currentPlayer = player1;
		
		events.add(new SwitchTurnEvent(currentPlayer));
		
		if (rule.isGameOver(this)) {
			isGameOver = true;
			events.add(new StopEvent(StopEvent.GAME_OVER));
			endGame();
			return events;
		}
		
		events.addAll(checkAndDistribute());
		
		return events;
	}
	
	private List<GameEvent> performMoveLogic(int startId, Direction direction) {
		List<GameEvent> events = new ArrayList<>();
		int currentIdx = startId;
		
		int hand = board.getSquare(startId).pickUpStones();
		events.add(new PickUpEvent(currentIdx, hand));
		
		while (hand > 0) {
			currentIdx = board.getNextIdx(currentIdx, direction);
			board.getSquare(currentIdx).addStones(1);
			hand--;
			events.add(new DropEvent(currentIdx, 1));
			
			if (hand == 0) {
				int nextIdx = board.getNextIdx(currentIdx, direction);
				Square nextSquare = board.getSquare(nextIdx);
				
				if (nextSquare instanceof MandarinSquare) {
					events.add(new StopEvent(currentIdx));
					return events;
				}
				if (!nextSquare.isEmpty()) {		
					hand = nextSquare.pickUpStones();
					currentIdx = nextIdx;
					events.add(new PickUpEvent(currentIdx, hand));
					
				} else {
					
					int emptyIdx = nextIdx;
					while (true) {
						int targetIdx = board.getNextIdx(emptyIdx, direction);
						Square targetSquare = board.getSquare(targetIdx);
						
						if (targetSquare.isEmpty()) {
							break;
						} else {
							int captured = targetSquare.pickUpStones();
							currentPlayer.addScore(captured);
							events.add(new CaptureEvent(targetIdx, captured, currentPlayer));
							
							int checkIdx = board.getNextIdx(targetIdx, direction);
							if (board.getSquare(checkIdx) instanceof MandarinSquare) break;
							if (!board.getSquare(checkIdx).isEmpty()) break;
							emptyIdx = checkIdx;
						}
					}
					events.add(new StopEvent(board.getNextIdx(emptyIdx, direction.opposite())));
					return events;
				}
			}
		}
		return events;
	}
	
	public Board getBoard() { return board; }
	public Player getPlayer1() { return player1; }
	public Player getPlayer2() { return player2; }
	public Player getCurrentPlayer() { return currentPlayer; }
	public boolean isGameOver() { return isGameOver; }
	public GameRule getRule() { return rule; }
}