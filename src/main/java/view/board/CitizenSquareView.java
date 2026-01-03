package view.board;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CitizenSquareView extends SquareView {
	
	public CitizenSquareView(int squareId) {
		super(squareId);
		if (getStylesheets().contains(getCssURL())) {
			this.getStyleClass().add("citizen-square");
		}
		getCountLabel().layoutXProperty().bind(this.widthProperty().multiply(0.05));
		getCountLabel().layoutYProperty().bind(this.heightProperty().multiply(0.80));
	}

	
	@Override
	protected void drawBackground() {
		backgroundShape = new Rectangle(CELL_WIDTH, CELL_HEIGHT);
		backgroundShape.setFill(Color.BURLYWOOD);
		backgroundShape.setStroke(Color.BLACK);
		getBackgroundLayer().getChildren().add(backgroundShape);
		
	}

}