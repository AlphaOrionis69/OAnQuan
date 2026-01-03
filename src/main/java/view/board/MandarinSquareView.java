package view.board;

import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import model.board.Board;


public class MandarinSquareView extends SquareView {
	
	public MandarinSquareView(int squareId) {
		super(squareId);
		double doubleHeight = CELL_HEIGHT * 2;
		this.setPrefHeight(doubleHeight);
		getBackgroundLayer().setPrefHeight(doubleHeight);	
		getStoneLayer().setLayoutY((doubleHeight - getStoneLayer().getPrefHeight()) / 2);
		if (getStylesheets().contains(getCssURL())) {
			this.getStyleClass().add(squareId == Board.LEFT_MANDARIN_ID ? "mandarin-left" : "mandarin-right");
		}
		if (squareId == Board.LEFT_MANDARIN_ID) { 
			getCountLabel().layoutXProperty().bind(this.widthProperty().multiply(0.85)); 
			getCountLabel().layoutYProperty().bind(this.heightProperty().multiply(0.025)); 
		} else { 
			getCountLabel().layoutXProperty().bind(this.widthProperty().multiply(0.05)); 
			getCountLabel().layoutYProperty().bind(this.heightProperty().multiply(0.9));
		}
	}
	
	@Override
	protected void drawBackground() {
		
		double h = CELL_HEIGHT * 2;
		
		backgroundShape = new Arc();
		Arc holder = (Arc)backgroundShape;
		holder.setRadiusX(CELL_WIDTH + 2);
		holder.setRadiusY(h / 2 + 2);
		holder.setType(ArcType.CHORD);
		holder.setFill(Color.BURLYWOOD);
		holder.setStroke(Color.BLACK);
		
		if (getSquareId() == 11) { 
			holder.setCenterX(CELL_WIDTH); 
			holder.setCenterY(h / 2 + 1);
			holder.setStartAngle(90);
			holder.setLength(180);
		} else { 
			holder.setCenterX(0);
			holder.setCenterY(h / 2 + 1);
			holder.setStartAngle(270);
			holder.setLength(180);
		}
		
		getBackgroundLayer().getChildren().add(backgroundShape);
	}

}