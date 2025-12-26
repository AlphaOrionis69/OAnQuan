package view;

import model.board.Square;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class CitizenSquareView extends SquareView {
	
	public CitizenSquareView(Square square) {
		super(square);
		countLabel.layoutXProperty().bind(this.widthProperty().multiply(0.05));
		countLabel.layoutYProperty().bind(this.heightProperty().multiply(0.75));
	}

	
	@Override
	protected void drawBackground() {
		backgroundShape = new Rectangle(CELL_WIDTH, CELL_HEIGHT);
		backgroundShape.setFill(Color.BURLYWOOD);
		backgroundShape.setStroke(Color.BLACK);
		backgroundLayer.getChildren().add(backgroundShape);
	}

}