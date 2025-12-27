package view;

import model.board.Square;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Shape;

public class MandarinSquareView extends SquareView {
	
	public MandarinSquareView(Square square) {
		super(square);
		double doubleHeight = CELL_HEIGHT * 2;
		this.setPrefHeight(doubleHeight);
		backgroundLayer.setPrefHeight(doubleHeight);	
		stoneLayer.setLayoutY((doubleHeight - stoneLayer.getPrefHeight()) / 2);
		
		if (square.getId() == 11) { 
			countLabel.layoutXProperty().bind(this.widthProperty().multiply(0.8)); 
			countLabel.layoutYProperty().bind(this.heightProperty().multiply(0.025)); 
		} else { 
			countLabel.layoutXProperty().bind(this.widthProperty().multiply(0.05)); 
			countLabel.layoutYProperty().bind(this.heightProperty().multiply(0.85));
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
		
		if (getSquare().getId() == 11) { 
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
		
		backgroundLayer.getChildren().add(backgroundShape);
	}

}