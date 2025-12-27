package controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import model.board.Square;
import model.events.*;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import model.game.OAnQuanGame;
import model.players.Move;
import model.players.Player;
import view.Arrow;
import view.BoardView;
import view.CitizenSquareView;
import view.DirectionControlView;
import view.HandView;
import view.SquareView;

public class GameController {
	@FXML private AnchorPane rootPane;
	@FXML private StackPane boardPane;
	@FXML private Label lblP1Score, lblP2Score, lblTurn;
	private int p1Score = 0, p2Score = 0; // helper
	
	// hand
	private HandView handView;
	// overlay message
	@FXML private StackPane msgOverlay;
	@FXML private Label msgTitle, msgContent;
	
	private BoardView boardView;
	private Map<Integer, SquareView> squareViews;
	// arrows
	private DirectionControlView arrowView;
	
	private OAnQuanGame game;
	
	private int selectedSquareId = -1;
	private boolean isAnimating = false;
	
	private final double DELAY_AFTER_MOVING_HAND = 100;
	private final double DELAY_AFTER_PICKING_STONES = 100;
	private final double DELAY_AFTER_DROPPING_STONES = 100;
	private final double DELAY_AFTER_SWITCHING_TURN = 100;
	private final double DELAY_AFTER_CAPTURING_STONES = 100;
	private final double DELAY_BEFORE_DISTRIBUTING_STONES = 1500;
	
	private Queue<GameEvent> eventQueue = new LinkedList<>();
	
	@FXML
	private void initialize() {
		game = new OAnQuanGame();
		game.startNewGame("Player 1", "Player 2");
		boardView = new BoardView();
		squareViews = boardView.getAllViews();
		boardPane.getChildren().add(boardView);
		//boardView.setAlignment(Pos.CENTER);
		drawBoard();
		createDirectionArrows();
		createHand();
		rootPane.layoutBoundsProperty().addListener((obs, o, n) -> {
			Platform.runLater(() -> {
				arrowView.updateArrowPosition();
				handView.updateHandPosition();
			});
		});
		if (rootPane.getChildren().contains(msgOverlay)) {
			rootPane.getChildren().remove(msgOverlay);
			rootPane.getChildren().add(msgOverlay);
		}
		syncScore(); syncTurn(); syncBoard();   
	}

	
	private void drawBoard() {
		boardView.init(game.getBoard().getSquares());	
		for (Map.Entry<Integer, SquareView> entry : squareViews.entrySet()) {
			SquareView sv = entry.getValue();
			if (sv instanceof CitizenSquareView) {
				sv.setOnMouseClicked(e -> handleSquareClick((SquareView)e.getSource()));
				sv.setOnMouseEntered(e -> handleSquareEnter((SquareView)e.getSource()));
				sv.setOnMouseExited(e -> handleSquareExit((SquareView)e.getSource()));
			}
		}
	}

	private void createDirectionArrows() {
		arrowView = new DirectionControlView();
		arrowView.getArrowCCW().setOnMouseClicked(e -> processMove(false));
		arrowView.getArrowCW().setOnMouseClicked(e -> processMove(true));
		
		rootPane.getChildren().addAll(arrowView.getArrowCCW(), arrowView.getArrowCW());
	}
	
	private void createHand() {
		handView = new HandView();
		rootPane.getChildren().add(handView);
	}
	private void handleSquareEnter(SquareView sv) {
		if (isAnimating || game.isGameOver()) return;
		Square s = sv.getSquare();
		if (!game.getRule().isValidMove(game.getBoard(), s.getId(), game.getCurrentPlayer())) {
			return;
		}
		sv.highlight(true);
	}
	private void handleSquareExit(SquareView sv) {
		if (isAnimating || game.isGameOver()) return;
		Square s = sv.getSquare();
		if (!game.getRule().isValidMove(game.getBoard(), s.getId(), game.getCurrentPlayer())) {
			return;
		}
		if (s.getId() != selectedSquareId) {
			sv.highlight(false);
		}
	}
	private void handleSquareClick(SquareView sv) {
		if (isAnimating || game.isGameOver()) return;
		Square s = sv.getSquare();
		// choose the same square
		if (selectedSquareId == s.getId()) {
			deselect();
			return;
		}
		// validate
		if (!game.getRule().isValidMove(game.getBoard(), s.getId(), game.getCurrentPlayer())) {
			return;
		}

		deselect(); 
		selectedSquareId = s.getId();
		
		sv.highlight(true);
		arrowView.attachTo(sv);
		arrowView.show();
	}
	private void deselect() {
		if (selectedSquareId != -1) {
			squareViews.get(selectedSquareId).highlight(false);
			selectedSquareId = -1;
		}
		arrowView.detach();
		arrowView.hide();
	}

	private void processMove(boolean clockwise) {
		if (selectedSquareId == -1) return;
		
		Move move = new Move(selectedSquareId, clockwise);
		List<GameEvent> events = game.move(move);
		
		deselect();
		
		if (events != null) {
			eventQueue.addAll(events);
			playNextEvent();
		}
		
	}

	private void playNextEvent() {
		if (eventQueue.isEmpty()) {
			isAnimating = false;
			
			handView.reset();
			handView.hide();
			
			//syncBoard(); syncScore(); syncTurn();
			checkGameOver();
			return;
		}

		handView.show();
		isAnimating = true;
		GameEvent event = eventQueue.poll();

		if (event instanceof PickUpEvent) {
			PickUpEvent e = (PickUpEvent) event;
			SquareView sv = squareViews.get(e.getSquareId());

			handView.moveHandTo(sv);
			PauseTransition afterMovingHand = new PauseTransition(Duration.millis(DELAY_AFTER_MOVING_HAND));
			afterMovingHand.setOnFinished(ev -> {
				handView.addAmount(e.getAmountPickedUp());
				sv.clearVisualStones();
			});
			
			PauseTransition afterUpdatingOneEvent = new PauseTransition(Duration.millis(DELAY_AFTER_PICKING_STONES));
			afterUpdatingOneEvent.setOnFinished(ev -> playNextEvent());
			
			SequentialTransition sq = new SequentialTransition(afterMovingHand, afterUpdatingOneEvent);
			sq.play();
			return; 
			
		} else if (event instanceof DropEvent) {
			DropEvent e = (DropEvent) event;
			SquareView sv = squareViews.get(e.getSquareId());

			handView.moveHandTo(sv);
			PauseTransition afterMovingHand = new PauseTransition(Duration.millis(DELAY_AFTER_MOVING_HAND));
			afterMovingHand.setOnFinished(ev -> {
				handView.decreaseAmount(e.getAmountDropped());
				sv.addVisualStone();
			});
			
			PauseTransition afterUpdatingOneEvent = new PauseTransition(Duration.millis(DELAY_AFTER_DROPPING_STONES));
			afterUpdatingOneEvent.setOnFinished(ev -> playNextEvent());
			
			SequentialTransition sq = new SequentialTransition(afterMovingHand, afterUpdatingOneEvent);
			sq.play();
			return;

		} else if (event instanceof CaptureEvent) {
			CaptureEvent e = (CaptureEvent) event;
			SquareView sv = squareViews.get(e.getSquareId());
			
			handView.moveHandTo(sv);
			sv.highlight(true);
			
			PauseTransition afterMovingHand = new PauseTransition(Duration.millis(DELAY_AFTER_MOVING_HAND));
			afterMovingHand.setOnFinished(ev -> {
				sv.clearVisualStones();
				sv.highlight(false);
				updateScore(e.getAmountCaptured(), e.getPlayer());
			});
			
			PauseTransition afterUpdatingOneEvent = new PauseTransition(Duration.millis(DELAY_AFTER_CAPTURING_STONES));
			afterUpdatingOneEvent.setOnFinished(ev -> playNextEvent());
			
			SequentialTransition sq = new SequentialTransition(afterMovingHand, afterUpdatingOneEvent);
			sq.play();
			return;

		} else if (event instanceof DistributeEvent) {
			DistributeEvent e = (DistributeEvent) event;
			msgTitle.setText("Distribution");
			msgContent.setText(e.getPlayer().getName() + " distributes stones.");
			msgOverlay.setVisible(true);
			
			PauseTransition pt = new PauseTransition(Duration.millis(DELAY_BEFORE_DISTRIBUTING_STONES));
			pt.setOnFinished(ev -> {
				msgOverlay.setVisible(false);
				int start = (e.getPlayer().getSide() == 0) ? 0 : 6;
				int end = (e.getPlayer().getSide() == 0) ? 4 : 10;
				for (int i = start; i <= end; i++) {
					squareViews.get(i).addVisualStone();
				}
				Player otherPlayer = e.getPlayer() == game.getPlayer1() ? game.getPlayer2() : game.getPlayer1();
				updateScore(-e.getAmountLent(), otherPlayer);
				
				updateScore(-(5*e.getAmountPerSquare() - e.getAmountLent()), e.getPlayer());
				
				playNextEvent();
			});
			pt.play();
			return;

		} else if (event instanceof SwitchTurnEvent) {
			SwitchTurnEvent e = (SwitchTurnEvent) event;
			updateTurn(e.getNewPlayer());
			PauseTransition pt = new PauseTransition(Duration.millis(DELAY_AFTER_SWITCHING_TURN));
			pt.setOnFinished(ev -> playNextEvent());
			pt.play();
			return;

		} else if (event instanceof StopEvent) {
			// maybe add something more here
			playNextEvent();
			return;
		}

		// unknown one??
		playNextEvent();
	}
	
	private void checkGameOver() {
		game.endGame();
		if (game.isGameOver()) {
			PauseTransition beforeCalculatingFinalScore = new PauseTransition(Duration.millis(200));
			beforeCalculatingFinalScore.setOnFinished(ev -> {
				syncBoard(); syncScore(); syncTurn();
			});
			
			PauseTransition afterCalculatingFinalScore = new PauseTransition(Duration.millis(500));
			afterCalculatingFinalScore.setOnFinished(ev -> {
				msgTitle.setText("Game Over");
				msgContent.setText("Winner: " + (game.getPlayer1().getScore() > game.getPlayer2().getScore() ? "Player 1" : "Player 2"));
				msgOverlay.setVisible(true);
			});
			SequentialTransition sq = new SequentialTransition(beforeCalculatingFinalScore, afterCalculatingFinalScore);
			sq.play();
		}
	}
	
	private void syncBoard() {
		for (SquareView sv : squareViews.values()) {
			sv.syncSquare();
		}
		
	}
	private void syncScore() {
		lblP1Score.setText("P1: " + game.getPlayer1().getScore());
		p1Score = game.getPlayer1().getScore();
		
		lblP2Score.setText("P2: " + game.getPlayer2().getScore());
		p2Score = game.getPlayer2().getScore();
	}
	private void syncTurn() {
		lblTurn.setText("Turn: " + game.getCurrentPlayer().getName());
	}
	
	private void updateScore(int amount, Player player) {	
		if (player == game.getPlayer1()) {
			lblP1Score.setText("P1: " + (p1Score + amount));
			p1Score += amount;
		}
		else {
			lblP2Score.setText("P2: " + (p2Score + amount));
			p2Score += amount;
		}
	}
	private void updateTurn(Player player) {
		lblTurn.setText("Turn: " + player.getName());
	}
	@FXML
	public void closeOverlay() {
		msgOverlay.setVisible(false);
	}
	
	@FXML
	public void handleBack() {
		// just for sure
		isAnimating = false;
		eventQueue.clear();
		
		NavigationController.getInstance().navigateTo("/view/MainMenu.fxml");
	}
}