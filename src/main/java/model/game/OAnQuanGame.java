package model.game;

import java.util.ArrayList;

import java.util.List;
import model.board.*;
import model.events.*;
import model.players.Player;
import model.players.PlayerSide;
import model.rules.GameRule;
import model.rules.StandardRule;

/*
 * Changing to List<Player> now is still hard to expand to 3-4 players game, since we also need to modify board, possible player sides, rule, ...
 */
public class OAnQuanGame {
	private Board board;
	private Player player1;
	private Player player2;
	private Player currentPlayer;
	private GameRule rule;
	private boolean isGameOver;
	private int penalty = 0; // > 0 => player2 need to pay stone back
	private static final int AMOUNT_DISTRIBUTED_PER_SQUARE = 1;
	public OAnQuanGame() {
		startNewGame();
	}
	
	private void startNewGame() {
		board = new Board();
		rule = new StandardRule();
		player1 = new Player("Player 1", PlayerSide.BOTTOM);
		player2 = new Player("Player 2", PlayerSide.TOP);
		player1.resetScore();
		player2.resetScore();
		currentPlayer = player1;
		isGameOver = false;		
		
		penalty = 0;
	}
	
	private void endGame() {
		if (!isGameOver) return;
		player1.addScore(penalty); player1.addScore(board.pickUpStonesOnSide(player1.getSide()));
		player2.decreaseScore(penalty); player2.addScore(board.pickUpStonesOnSide(player2.getSide()));
		penalty = 0;
	}
	
	private List<ModelChange> checkAndDistribute() {
		if (isGameOver) return new ArrayList<>();
		
		List<ModelChange> events = new ArrayList<>();
		if (board.isSideEmpty(currentPlayer.getSide())) {
			int stonesNeeded = (currentPlayer.getSide().end() - currentPlayer.getSide().start() + 1)*AMOUNT_DISTRIBUTED_PER_SQUARE;
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
				currentPlayer.resetScore();
			}
			
			events.add(new SideRefilled(currentPlayer.getSide(), currentPlayer.getName(), isLending, amountLent, AMOUNT_DISTRIBUTED_PER_SQUARE));
			
			board.addStonesOnSide(AMOUNT_DISTRIBUTED_PER_SQUARE, currentPlayer.getSide());
		}
		return events;
	}
	
	public List<ModelChange> move(Move move) {
		if (!rule.isValidMove(board, move.getSquareId(), currentPlayer.getSide()) || move.getDirection() == null) return null;
		
		List<ModelChange> events = new ArrayList<>();
		
		events.addAll(performMoveLogic(move.getSquareId(), move.getDirection()));
		
		events.addAll(switchTurn());
		
		if (rule.isGameOver(this)) {
			isGameOver = true;
			events.add(new GameEnded());
			endGame();
			return events;
		}
		
		events.addAll(checkAndDistribute());
		
		return events;
	}
	
	private List<ModelChange> performMoveLogic(int startId, Direction direction) {
		List<ModelChange> events = new ArrayList<>();
		int currentIdx = startId;
		
		int hand = board.getSquare(startId).pickUpStones();
		events.add(new StonesPickedUp(currentIdx, hand));
		
		while (hand > 0) {
			currentIdx = board.getNextIdx(currentIdx, direction);
			board.getSquare(currentIdx).addStones(1);
			hand--;
			events.add(new StonesDropped(currentIdx, 1));
			
			if (hand == 0) {
				int nextIdx = board.getNextIdx(currentIdx, direction);
				Square nextSquare = board.getSquare(nextIdx);
				
				if (nextSquare instanceof MandarinSquare) {
					events.add(new MoveEnded(currentIdx));
					return events;
				}
				if (!nextSquare.isEmpty()) {		
					hand = nextSquare.pickUpStones();
					currentIdx = nextIdx;
					events.add(new StonesPickedUp(currentIdx, hand));
					
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
							events.add(new StonesCaptured(targetIdx, captured, currentPlayer.getSide()));
							
							int checkIdx = board.getNextIdx(targetIdx, direction);
							if (board.getSquare(checkIdx) instanceof MandarinSquare) break;
							if (!board.getSquare(checkIdx).isEmpty()) break;
							emptyIdx = checkIdx;
						}
					}
					events.add(new MoveEnded(board.getNextIdx(emptyIdx, direction.opposite())));
					return events;
				}
			}
		}
		return events;
	}
	private List<ModelChange> switchTurn() {
		currentPlayer = currentPlayer == player1 ? player2 : player1;
		List<ModelChange> output = new ArrayList<>(); output.add(new TurnSwitched(currentPlayer.getName()));
		return output;
	}
	public Board getBoard() { return board; }
	public Player getPlayer1() { return player1; }
	public Player getPlayer2() { return player2; }
	public Player getCurrentPlayer() { return currentPlayer; }
	public boolean isGameOver() { return isGameOver; }
	public GameRule getRule() { return rule; }
}