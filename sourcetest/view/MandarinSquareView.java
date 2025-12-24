package view;

import board.Square;
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
			countLabel.layoutXProperty().bind(this.widthProperty().multiply(0.85)); 
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
		((Arc)backgroundShape).setRadiusX(CELL_WIDTH + 2);
		((Arc)backgroundShape).setRadiusY(h / 2 + 2);
		((Arc)backgroundShape).setType(ArcType.CHORD);
		((Arc)backgroundShape).setFill(Color.BURLYWOOD);
		((Arc)backgroundShape).setStroke(Color.BLACK);
		
		if (getSquare().getId() == 11) { 
			((Arc)backgroundShape).setCenterX(CELL_WIDTH); 
			((Arc)backgroundShape).setCenterY(h / 2 + 1);
			((Arc)backgroundShape).setStartAngle(90);
			((Arc)backgroundShape).setLength(180);
		} else { 
			((Arc)backgroundShape).setCenterX(0);
			((Arc)backgroundShape).setCenterY(h / 2 + 1);
			((Arc)backgroundShape).setStartAngle(270);
			((Arc)backgroundShape).setLength(180);
		}
		
		backgroundLayer.getChildren().add(backgroundShape);
	}

}