package view;

import model.board.Square;
import model.game.OAnQuanGame;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardView extends HBox {
	private Map<Integer, SquareView> squareViews = new HashMap<>();
	private int spacing = 6;
	public BoardView() {
		this.setAlignment(Pos.CENTER);
		this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		this.setStyle("-fx-border-width: 0;");
		this.setSpacing(spacing);
	}
	
	public void init(List<Square> squares) {
		this.getChildren().clear();
		squareViews.clear();
		
		
		for (Square s : squares) {
			SquareView sv;
			if (s.getId() == 5 || s.getId() == 11) {
				sv = new MandarinSquareView(s.getId());
			} else {
				sv = new CitizenSquareView(s.getId());
			}
			squareViews.put(s.getId(), sv);
		}
		
		SquareView leftMan = squareViews.get(11);
		
		VBox centerGrid = new VBox(); centerGrid.setSpacing(spacing);		
		HBox topRow = new HBox(); topRow.setSpacing(spacing);
		for (int i = 10; i >= 6; i--) {
			topRow.getChildren().add(squareViews.get(i));
		}		
		HBox bottomRow = new HBox(); bottomRow.setSpacing(spacing);
		for (int i = 0; i <= 4; i++) {
			bottomRow.getChildren().add(squareViews.get(i));
		}
		centerGrid.getChildren().addAll(topRow, bottomRow);
		
		SquareView rightMan = squareViews.get(5);
		
		this.getChildren().addAll(leftMan, centerGrid, rightMan);
	}
	public void syncBoard(OAnQuanGame game) {
		for (Map.Entry<Integer, SquareView> entry : squareViews.entrySet()) {
			entry.getValue().syncSquare(game);
		}
	}	
	public Map<Integer, SquareView> getAllViews() {
		return squareViews;
	}
}