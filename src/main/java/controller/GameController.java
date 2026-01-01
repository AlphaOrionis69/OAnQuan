package controller;

import java.util.List;

import java.util.Map;

import model.events.*;
import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import model.game.Direction;
import model.game.Move;
import model.game.OAnQuanGame;
import model.players.PlayerSide;
import view.board.BoardView;
import view.board.CitizenSquareView;
import view.board.SquareView;
import view.component.StatusView;
import view.control.Arrow;
import view.control.DirectionControlView;
import view.control.HandView;
import view.overlay.MessageOverlay;

public class GameController {
	@FXML private AnchorPane rootPane;
	@FXML private BorderPane containerPane;
	@FXML private StackPane boardPane;
	private StatusView statusView;
	// hand
	private HandView handView;
	// overlay message
	private MessageOverlay msgOverlay;
	
	private BoardView boardView;
	private Map<Integer, SquareView> squareViews;
	// arrows
	private DirectionControlView arrowView;
	
	private OAnQuanGame game;
	
	private int selectedSquareId = -1;
	private int enteredSquareId = -1;
	private GameAnimator animator;
	
	@FXML
	private void initialize() {
		game = new OAnQuanGame();
		
		createStatusView(); createBoard(); createDirectionArrows();
		createHand(); createOverlay(); createAnimator();
		
		boardPane.getChildren().add(boardView);
		containerPane.setTop(statusView); 
		
		rootPane.layoutBoundsProperty().addListener((obs, o, n) -> {
			Platform.runLater(() -> {
				arrowView.updateArrowPosition();
				handView.updateHandPosition();
			});
		});

		rootPane.getChildren().add(arrowView);
		rootPane.getChildren().add(handView);
		
		rootPane.getChildren().add(msgOverlay);
		AnchorPane.setBottomAnchor(msgOverlay, 0.0); AnchorPane.setTopAnchor(msgOverlay, 0.0); 
		AnchorPane.setLeftAnchor(msgOverlay, 0.0); AnchorPane.setRightAnchor(msgOverlay, 0.0);
		
		statusView.syncScore(game); statusView.syncTurn(game); boardView.syncBoard(game);   
	}
	private void createStatusView() {
		statusView = new StatusView();
	}
	
	private void createBoard() {
		boardView = new BoardView();
		squareViews = boardView.getAllViews();
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
	}
	
	private void createHand() {
		handView = new HandView();
	}
	
	private void createOverlay() {
		msgOverlay = new MessageOverlay();
		msgOverlay.getButton().setOnAction(e -> msgOverlay.setVisible(false));
	}
	
	private void createAnimator() {
		animator = new GameAnimator(statusView, handView, msgOverlay, boardView, game);
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
		boolean isLeftDirection = arrow == arrowView.getLeftArrow(); Direction direction;
		if (selectedSquareId >= PlayerSide.BOTTOM.start() && selectedSquareId <= PlayerSide.BOTTOM.end()) {
			direction = Direction.fromBoolean(!isLeftDirection);
		}
		else direction = Direction.fromBoolean(isLeftDirection);
		processMove(direction);
	}
	
	private void handleSquareEnter(SquareView sv) {
		enteredSquareId = sv.getSquareId();
		if (animator.isAnimating() || game.isGameOver()) return;
		int squareId = sv.getSquareId();
		if (!game.getRule().isValidMove(game.getBoard(), squareId, game.getCurrentPlayer())) {
			return;
		}		
		sv.highlight(true);
	}
	private void handleSquareExit(SquareView sv) {
		enteredSquareId = -1;
		if (animator.isAnimating() || game.isGameOver()) return;
		int squareId = sv.getSquareId();
		if (!game.getRule().isValidMove(game.getBoard(), squareId, game.getCurrentPlayer())) {
			return;
		}	
		if (squareId != selectedSquareId) {
			sv.highlight(false);
		}
	}
	private void handleSquareClick(SquareView sv) {
		if (animator.isAnimating() || game.isGameOver()) return;
		int squareId = sv.getSquareId();
		// choose the same square
		if (selectedSquareId == squareId) {
			deselect();
			return;
		}
		// validate
		if (!game.getRule().isValidMove(game.getBoard(), squareId, game.getCurrentPlayer())) {
			return;
		}

		deselect(); 
		selectedSquareId = squareId;
		
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

	private void processMove(Direction direction) {
		if (selectedSquareId == -1) return;	
		Move move = new Move(selectedSquareId, direction);
		List<GameEvent> events = game.move(move);		
		deselect();
		animator.animate(events, () -> {
			if (enteredSquareId != -1) {
				handleSquareEnter(squareViews.get(enteredSquareId));
			}
		});
	}
	@FXML
	public void handleBack() {
		NavigationController.getInstance().showMainMenu();
	}
}