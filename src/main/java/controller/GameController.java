package controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import model.board.Square;
import model.events.*;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
	private int enteredSquareId = -1;
	private boolean isAnimating = false;
	
	private static final double DELAY_AFTER_MOVING_HAND = 100;
	private static final double DELAY_AFTER_PICKING_STONES = 500;
	private static final double DELAY_AFTER_DROPPING_STONES = 100;
	private static final double DELAY_AFTER_SWITCHING_TURN = 100;
	private static final double DELAY_AFTER_CAPTURING_STONES = 300;
	private static final double DELAY_BEFORE_DISTRIBUTING_STONES = 1500;
	private static final double DELAY_BEFORE_CALCULATING_FINAL_SCORES = 200;
	private static final double DELAY_AFTER_CALCULATING_FINAL_SCORES = 1500;
	
	@FXML
	private void initialize() {
		game = new OAnQuanGame();
		//game.startNewGame("Player 1", "Player 2");
		boardView = new BoardView();
		squareViews = boardView.getAllViews();
		boardPane.getChildren().add(boardView);
		//boardView.setAlignment(Pos.CENTER);
		createBoard();
		createDirectionArrows();
		createHand();
		
		
		rootPane.layoutBoundsProperty().addListener((obs, o, n) -> {
			Platform.runLater(() -> {
				arrowView.updateArrowPosition();
				handView.updateHandPosition();
			});
		});
		// add again to make the overlay on top of all (wrap it in the future)
		if (rootPane.getChildren().contains(msgOverlay)) {
			rootPane.getChildren().remove(msgOverlay);
			rootPane.getChildren().add(msgOverlay);
		}
		syncScore(); syncTurn(); syncBoard();   
	}

	
	private void createBoard() {
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
		
		arrowView.getRightArrow().setOnMouseClicked(e -> handleArrowClick(e));
		arrowView.getLeftArrow().setOnMouseClicked(e -> handleArrowClick(e));
		
		arrowView.getRightArrow().setOnMouseEntered(e -> handleArrowEnter(e));
		arrowView.getLeftArrow().setOnMouseEntered(e -> handleArrowEnter(e));
		
		arrowView.getRightArrow().setOnMouseExited(e -> handleArrowExit(e));
		arrowView.getLeftArrow().setOnMouseExited(e -> handleArrowExit(e));
		
		rootPane.getChildren().addAll(arrowView);
	}
	
	private void createHand() {
		handView = new HandView();
		rootPane.getChildren().add(handView);
	}
	
	private void handleArrowEnter(MouseEvent e) {
		((Arrow)(e.getSource())).highlight(true);
	}
	private void handleArrowExit(MouseEvent e) {
		((Arrow)(e.getSource())).highlight(false);
	}
	private void handleArrowClick(MouseEvent e) {
		if (selectedSquareId == -1) return;
		Arrow arrow = (Arrow)(e.getSource());
		boolean isLeftDirection = arrow == arrowView.getLeftArrow(); boolean isClockwise;
		if (selectedSquareId >= 0 && selectedSquareId <= 4) isClockwise = !isLeftDirection;
		else isClockwise = isLeftDirection;
		processMove(isClockwise);
	}
	
	private void handleSquareEnter(SquareView sv) {
		enteredSquareId = sv.getSquare().getId();
		if (isAnimating || game.isGameOver()) return;
		Square s = sv.getSquare();
		if (!game.getRule().isValidMove(game.getBoard(), s.getId(), game.getCurrentPlayer())) {
			return;
		}		
		sv.highlight(true);
	}
	private void handleSquareExit(SquareView sv) {
		enteredSquareId = -1;
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

	private void processMove(boolean isClockwise) {
		if (selectedSquareId == -1) return;	
		Move move = new Move(selectedSquareId, isClockwise);
		List<GameEvent> events = game.move(move);		
		deselect();
		stimulateAnimation(events);
	}

	
	private void stimulateAnimation(List<GameEvent> events) {
		if (events == null) return;
		Timeline timeline = new Timeline();
		double delayTime = 0;
		isAnimating = true;
		handView.show();
		for (GameEvent event : events) {	
			if (event instanceof PickUpEvent) {
				delayTime = animatePickUp((PickUpEvent)event, timeline, delayTime);
				
			} else if (event instanceof DropEvent) {
				delayTime = animateDrop((DropEvent)event, timeline, delayTime);
				
			} else if (event instanceof CaptureEvent) {
				delayTime = animateCapture((CaptureEvent)event, timeline, delayTime);
				
			} else if (event instanceof DistributeEvent) {			
				delayTime = animateDistribute((DistributeEvent)event, timeline, delayTime);
				
			} else if (event instanceof SwitchTurnEvent) {
				delayTime = animateSwitchTurn((SwitchTurnEvent)event, timeline, delayTime);
				
			} else if (event instanceof StopEvent) {
				delayTime = animateStop((StopEvent)event, timeline, delayTime);
				
			}
			
		}
		timeline.setOnFinished(ev -> {
			if (enteredSquareId != -1) {
				handleSquareEnter(squareViews.get(enteredSquareId));
			}
		});
		timeline.play();
	}
	private double animatePickUp(PickUpEvent e, Timeline timeline, double delayTime) {
		SquareView sv = squareViews.get(e.getSquareId());
		KeyFrame kf1 = new KeyFrame(Duration.millis(delayTime), ev -> {
			handView.moveHandTo(sv);
		});
		delayTime += DELAY_AFTER_MOVING_HAND;
		
		KeyFrame kf2 = new KeyFrame(Duration.millis(delayTime), ev -> {
			handView.addAmount(e.getAmountPickedUp());
			sv.clearVisualStones();
		});
		delayTime += DELAY_AFTER_PICKING_STONES;
		
		timeline.getKeyFrames().addAll(kf1, kf2);
		return delayTime;
	}
	private double animateDrop(DropEvent e, Timeline timeline, double delayTime) {
		SquareView sv = squareViews.get(e.getSquareId());
		KeyFrame kf1 = new KeyFrame(Duration.millis(delayTime), ev -> {
			handView.moveHandTo(sv);
		});
		delayTime += DELAY_AFTER_MOVING_HAND;
		
		KeyFrame kf2 = new KeyFrame(Duration.millis(delayTime), ev -> {
			handView.decreaseAmount(e.getAmountDropped());
			sv.addVisualStone();
		});
		delayTime += DELAY_AFTER_DROPPING_STONES;
		
		timeline.getKeyFrames().addAll(kf1, kf2);
		return delayTime;
	}
	private double animateCapture(CaptureEvent e, Timeline timeline, double delayTime) {
		SquareView sv = squareViews.get(e.getSquareId());
		
		KeyFrame kf1 = new KeyFrame(Duration.millis(delayTime), ev -> {
			handView.moveHandTo(sv);
			sv.highlight(true);
		});
		delayTime += DELAY_AFTER_MOVING_HAND;
		
		KeyFrame kf2 = new KeyFrame(Duration.millis(delayTime), ev -> {
			sv.clearVisualStones();
			sv.highlight(false);
			updateScore(e.getAmountCaptured(), e.getPlayer());
		});
		delayTime += DELAY_AFTER_CAPTURING_STONES;
		
		timeline.getKeyFrames().addAll(kf1, kf2);
		return delayTime;
	}
	private double animateDistribute(DistributeEvent e, Timeline timeline, double delayTime) {
		KeyFrame kf1 = new KeyFrame(Duration.millis(delayTime), ev -> {
			msgTitle.setText("Distribution");
			if (e.isLending()) msgContent.setText(e.getPlayer().getName() + " lends stones from other player to distribute");
			else msgContent.setText(e.getPlayer().getName() + " distributes stones.");
			msgOverlay.setVisible(true);
		});
		
		delayTime += DELAY_BEFORE_DISTRIBUTING_STONES;
		KeyFrame kf2 = new KeyFrame(Duration.millis(delayTime), ev -> {
			msgOverlay.setVisible(false);
			handView.hide();
			int start = (e.getPlayer().getSide() == 0) ? 0 : 6;
			int end = (e.getPlayer().getSide() == 0) ? 4 : 10;
			for (int i = start; i <= end; i++) {
				squareViews.get(i).addVisualStone();
			}
			Player otherPlayer = e.getPlayer() == game.getPlayer1() ? game.getPlayer2() : game.getPlayer1();
			updateScore(-e.getAmountLent(), otherPlayer);			
			updateScore(-(5*e.getAmountPerSquare() - e.getAmountLent()), e.getPlayer());
		});
		
		timeline.getKeyFrames().addAll(kf1, kf2);
		return delayTime;
	}
	private double animateSwitchTurn(SwitchTurnEvent e, Timeline timeline, double delayTime) {
		KeyFrame kf = new KeyFrame(Duration.millis(delayTime), ev -> {
			updateTurn(e.getNewPlayer());
		});
		delayTime += DELAY_AFTER_SWITCHING_TURN;
		
		timeline.getKeyFrames().add(kf);
		return delayTime;
	}
	private double animateStop(StopEvent e, Timeline timeline, double delayTime) {
		KeyFrame kf = new KeyFrame(Duration.millis(delayTime), ev -> {
			isAnimating = false;
			handView.reset();
			handView.hide();
			//syncBoard(); syncScore(); syncTurn();
		});
		timeline.getKeyFrames().add(kf);
		if (e.getLastSquareId() == -1) {
			delayTime = animateGameOver(timeline, delayTime);
		}
		return delayTime;
	}
	private double animateGameOver(Timeline timeline, double delayTime) {
		delayTime += DELAY_BEFORE_CALCULATING_FINAL_SCORES;
		KeyFrame kf1 = new KeyFrame(Duration.millis(delayTime), ev -> {
			syncBoard(); syncScore(); syncTurn();
		});
		
		delayTime += DELAY_AFTER_CALCULATING_FINAL_SCORES;
		KeyFrame kf2 = new KeyFrame(Duration.millis(delayTime), ev -> {
			msgTitle.setText("Game Over");
			msgContent.setText("Winner: " + (game.getPlayer1().getScore() > game.getPlayer2().getScore() ? "Player 1" : "Player 2"));
			msgOverlay.setVisible(true);
		});
		timeline.getKeyFrames().addAll(kf1, kf2);

		return delayTime;
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
		NavigationController.getInstance().showMainMenu();
	}
}