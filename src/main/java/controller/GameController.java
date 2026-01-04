package controller;

import java.util.List;
import java.util.Map;
import model.events.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
import view.control.DirectionControlView;
import view.control.HandView;
import view.control.arrow.ArrowDirection;
import view.control.arrow.HighlightableArrowView;
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
		
		wireArrow(arrowView.getLeftArrow());
		wireArrow(arrowView.getRightArrow());
		
		arrowView.setOnKeyPressed(ev -> handleKeyPressedForArrow(ev));
	}
	private void wireArrow(HighlightableArrowView arrow) {
		arrow.getNode().setOnMouseClicked(e -> handleArrowClick(arrow));
		arrow.getNode().setOnMouseEntered(e -> handleArrowEnter(arrow));
		arrow.getNode().setOnMouseExited(e -> handleArrowExit(arrow));
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
	
	private void handleArrowEnter(HighlightableArrowView arrow) {
		arrow.highlight();
	}
	private void handleArrowExit(HighlightableArrowView arrow) {
		arrow.clearHighlight();
	}
	private void handleArrowClick(HighlightableArrowView arrow) {
		if (selectedSquareId == -1) return;
		Direction direction = interpretDirectionInput(selectedSquareId, arrow.getArrowDirection());
		if (direction != null) processMove(direction);
	}
	// this function interpret the left right direction for the model to understand
	private Direction interpretDirectionInput(int squareId, ArrowDirection arrowDirection) {
		if (squareId == -1) return null; 
		Direction direction;
		if (squareId >= PlayerSide.BOTTOM.start() && squareId <= PlayerSide.BOTTOM.end()) {
			direction = Direction.fromBoolean(arrowDirection.opposite().toBoolean());
		}
		else direction = Direction.fromBoolean(arrowDirection.toBoolean());
		return direction;		
	}
	private void handleSquareEnter(SquareView sv) {	
		int squareId = sv.getSquareId();
		enteredSquareId = squareId;
		if (!canInteractWithSquare(squareId)) return;
		sv.highlight();
	}
	private void handleSquareExit(SquareView sv) {
		enteredSquareId = -1;
		int squareId = sv.getSquareId();
		if (!canInteractWithSquare(squareId)) return;
		if (squareId != selectedSquareId) {
			sv.clearHighlight();
		}
	}
	private void handleSquareClick(SquareView sv) {
		int squareId = sv.getSquareId();
		if (!canInteractWithSquare(squareId)) return;
		// choose the same square
		if (selectedSquareId == squareId) {
			deselect();
			return;
		}
		deselect(); 
		selectedSquareId = squareId;
		
		sv.highlight();
		arrowView.attachTo(sv);
		arrowView.show();
		
		arrowView.requestFocus();
	}
	private void handleKeyPressedForArrow(KeyEvent event) {
		if (selectedSquareId != -1 && !animator.isAnimating() && arrowView.isVisible()) {
			if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
				processMove(interpretDirectionInput(selectedSquareId, ArrowDirection.LEFT));
			}
			else if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
				processMove(interpretDirectionInput(selectedSquareId, ArrowDirection.RIGHT));
			}
			else if (event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.ENTER) {
				deselect();
			}
		}
	}
	private void deselect() {
		if (selectedSquareId != -1) {
			if (selectedSquareId != enteredSquareId) squareViews.get(selectedSquareId).clearHighlight();
			selectedSquareId = -1;
		}
		
		arrowView.detach();
		arrowView.hide();
	}

	private void processMove(Direction direction) {
		if (selectedSquareId == -1) return;
		
		Move move = new Move(selectedSquareId, direction);		
		deselect(); if (enteredSquareId != -1) squareViews.get(enteredSquareId).clearHighlight();
		List<GameEvent> events = game.move(move);		
		animator.animate(events, () -> {
			if (enteredSquareId != -1) {
				handleSquareEnter(squareViews.get(enteredSquareId));
			}
		});
	}
	private boolean canInteractWithSquare(int squareId) {
		if (animator.isAnimating()) return false;
		if (game.isGameOver()) return false;
		if (!game.getRule().isValidMove(game.getBoard(), squareId, game.getCurrentPlayer())) return false;
		return true;
	}
	@FXML
	public void handleBack() {
		NavigationController.getInstance().showMainMenu();
	}
}