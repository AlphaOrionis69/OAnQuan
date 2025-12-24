package game;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.CitizenSquare;
import board.MandarinSquare;
import board.Square;
import events.*;
import players.HumanPlayer;
import players.Move;
import players.Player;
import rules.GameRule;
import rules.StandardRule;

public class OAnQuanGame {
	private Board board;
	private Player player1;
	private Player player2;
	private Player currentPlayer;
	private GameRule rule;
	private boolean isGameOver;
	private int penalty = 0; 
	
	public OAnQuanGame() {
		this.board = new Board();
		this.rule = new StandardRule();
		this.isGameOver = false;
	}
	
	public void startNewGame(String p1Name, String p2Name) {
		player1 = new HumanPlayer(p1Name, 0);
		player2 = new HumanPlayer(p2Name, 1);
		currentPlayer = player1;
		isGameOver = false;		
		player1.setScore(0);
		player2.setScore(0);
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
			boolean lending = false;
			int amountLent = 0;
			
			if (currentPlayer.getScore() >= stonesNeeded) {
				currentPlayer.decreaseScore(stonesNeeded);
			} else {
				amountLent = stonesNeeded - currentPlayer.getScore();
				lending = true;
				if (currentPlayer == player1) {
					penalty -= amountLent;
					player2.decreaseScore(amountLent);
				} else {
					penalty += amountLent;
					player1.decreaseScore(amountLent);
				}
				currentPlayer.setScore(0);
			}
			
			events.add(new DistributeEvent(currentPlayer, lending, amountLent, 1));
			
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
			events.add(new StopEvent(-1));
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
				
				if (!nextSquare.isEmpty()) {
					if (nextSquare instanceof MandarinSquare) {
						events.add(new StopEvent(currentIdx));
						return events;
					} else {
						hand = nextSquare.pickUpStones();
						currentIdx = nextIdx;
						events.add(new PickUpEvent(currentIdx, hand));
					}
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
							if (!board.getSquare(checkIdx).isEmpty()) break;
							emptyIdx = checkIdx;
						}
					}
					events.add(new StopEvent(currentIdx));
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