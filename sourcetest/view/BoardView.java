package view;

import board.Square;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardView extends HBox {
	
	private Map<Integer, SquareView> squareViews = new HashMap<>();
	
	public BoardView() {
		this.setAlignment(Pos.CENTER);
		this.setSpacing(0);
		this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		this.setStyle("-fx-border-width: 0;");
		this.setSpacing(4);
	}
	
	public void init(List<Square> squares) {
		this.getChildren().clear();
		squareViews.clear();
		
		// Create Views
		for (Square s : squares) {
			SquareView sv;
			if (s.getId() == 5 || s.getId() == 11) {
				sv = new MandarinSquareView(s);
			} else {
				sv = new CitizenSquareView(s);
			}
			squareViews.put(s.getId(), sv);
		}
		
		SquareView leftMan = squareViews.get(11);
		
		
		VBox centerGrid = new VBox(); centerGrid.setSpacing(4);
		
		HBox topRow = new HBox(); topRow.setSpacing(4);
		for (int i = 10; i >= 6; i--) {
			topRow.getChildren().add(squareViews.get(i));
		}
		
		HBox bottomRow = new HBox(); bottomRow.setSpacing(4);
		for (int i = 0; i <= 4; i++) {
			bottomRow.getChildren().add(squareViews.get(i));
		}
		
		centerGrid.getChildren().addAll(topRow, bottomRow);
		
		
		SquareView rightMan = squareViews.get(5);
		
		this.getChildren().addAll(leftMan, centerGrid, rightMan);
	}
	
	
	public Map<Integer, SquareView> getAllViews() {
		return squareViews;
	}
}