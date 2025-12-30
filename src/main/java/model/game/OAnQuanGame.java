package model.game;

import java.util.ArrayList;

import java.util.List;
import model.board.*;
import model.events.*;
import model.players.Move;
import model.players.Player;
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
		player1 = new Player(p1Name, 0);
		player2 = new Player(p2Name, 1);
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
		for (int i = 0; i <= 4; i++) player1.addScore(board.getSquare(i).pickUpStones());
		for (int i = 6; i <= 10; i++) player2.addScore(board.getSquare(i).pickUpStones());
	}
	
	public List<GameEvent> checkAndDistribute() {
		if (isGameOver) return new ArrayList<>();
		
		int startIdx = (currentPlayer.getSide() == 0) ? 0 : 6;
		int endIdx = (currentPlayer.getSide() == 0) ? 4 : 10;
		
		boolean sideEmpty = true;
		for (int idx = startIdx; idx <= endIdx; idx++) {
			if (!board.getSquare(idx).isEmpty()) {
				sideEmpty = false;
				break;
			}
		}
		
		List<GameEvent> events = new ArrayList<>();
		if (sideEmpty) {
			int stonesNeeded = 5;
			boolean isLending = false;
			int amountLent = 0;
			if (player1.getScore() + player2.getScore() < stonesNeeded) {
				// special case: no one can help continue the game
				events.add(new StopEvent(StopEvent.GAME_OVER));
				isGameOver = true;
				endGame();
				return events;
			}
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
		
		events.addAll(performMoveLogic(move.getSquareId(), move.isClockwise()));
		
		if (rule.isGameOver(board)) {
			isGameOver = true;
			events.add(new StopEvent(StopEvent.GAME_OVER));
			endGame();
			return events;
		}
		if (currentPlayer == player1) currentPlayer = player2;
		else currentPlayer = player1;
		
		events.add(new SwitchTurnEvent(currentPlayer));
		events.addAll(checkAndDistribute());
		
		return events;
	}
	
	private List<GameEvent> performMoveLogic(int startId, boolean isClockwise) {
		List<GameEvent> events = new ArrayList<>();
		int currentIdx = startId;
		
		int hand = board.getSquare(startId).pickUpStones();
		events.add(new PickUpEvent(currentIdx, hand));
		
		while (hand > 0) {
			currentIdx = board.getNextIdx(currentIdx, isClockwise);
			board.getSquare(currentIdx).addStones(1);
			hand--;
			events.add(new DropEvent(currentIdx, 1));
			
			if (hand == 0) {
				int nextIdx = board.getNextIdx(currentIdx, isClockwise);
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
						int targetIdx = board.getNextIdx(emptyIdx, isClockwise);
						Square targetSquare = board.getSquare(targetIdx);
						
						if (targetSquare.isEmpty()) {
							break;
						} else {
							int captured = targetSquare.pickUpStones();
							currentPlayer.addScore(captured);
							events.add(new CaptureEvent(targetIdx, captured, currentPlayer));
							
							int checkIdx = board.getNextIdx(targetIdx, isClockwise);
							if (board.getSquare(checkIdx) instanceof MandarinSquare) break;
							if (!board.getSquare(checkIdx).isEmpty()) break;
							emptyIdx = checkIdx;
						}
					}
					events.add(new StopEvent(board.getNextIdx(emptyIdx, !isClockwise)));
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