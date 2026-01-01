package view.board;

import model.board.Board;
import model.board.Square;
import model.game.OAnQuanGame;
import model.players.PlayerSide;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardView extends GridPane {

	private Map<Integer, SquareView> squareViews = new HashMap<>();
	private final int spacing = 6;

	public BoardView() {
		setAlignment(Pos.CENTER);
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		setHgap(spacing); setVgap(spacing);
		setStyle("-fx-border-width: 0;");
	}

	public void init(List<Square> squares) {
		this.getChildren().clear();
		squareViews.clear();

		
		for (Square s : squares) {
			SquareView sv;
			if (s.getId() == Board.RIGHT_MANDARIN_ID || s.getId() == Board.LEFT_MANDARIN_ID) {
				sv = new MandarinSquareView(s.getId());
			} else {
				sv = new CitizenSquareView(s.getId());
			}
			squareViews.put(s.getId(), sv);
		}

		
		SquareView leftMan = squareViews.get(Board.LEFT_MANDARIN_ID);
		this.add(leftMan, 0, 0, 1, 2);

		
		int col = 1;
		for (int i = PlayerSide.TOP.end(); i >= PlayerSide.TOP.start(); i--) {
			this.add(squareViews.get(i), col++, 0);
		}

		
		col = 1;
		for (int i = PlayerSide.BOTTOM.start(); i <= PlayerSide.BOTTOM.end(); i++) {
			this.add(squareViews.get(i), col++, 1);
		}
		SquareView rightMan = squareViews.get(Board.RIGHT_MANDARIN_ID);
		this.add(rightMan, 6, 0, 1, 2); 
	}

	public void syncBoard(OAnQuanGame game) {
		for (SquareView sv : squareViews.values()) {
			sv.syncSquare(game);
		}
	}

	public Map<Integer, SquareView> getAllViews() {
		return squareViews;
	}
}
