package view.control;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import view.board.SquareView;
import view.control.arrow.ArrowDirection;
import view.control.arrow.HighlightableArrowView;
import view.control.arrow.ImageArrowView;
import view.control.arrow.ShapeArrowView;

public class DirectionControlView extends Pane{
	private HighlightableArrowView leftArrow, rightArrow;
	private SquareView target = null;
	public static final double GAP_BETWEEN_ARROW_AND_SQUARE = 25; // approximately
	public DirectionControlView() {
		try {
			ImageArrowView newLeftArrow = new ImageArrowView("/image/left-arrow.png", ArrowDirection.LEFT);
			ImageArrowView newRightArrow = new ImageArrowView("/image/right-arrow.png", ArrowDirection.RIGHT);
			leftArrow = newLeftArrow;
			rightArrow = newRightArrow;
		}
		catch (Exception e) {
			System.out.println("Image Error: " + e.getMessage());
			leftArrow = new ShapeArrowView(ArrowDirection.LEFT);
			rightArrow = new ShapeArrowView(ArrowDirection.RIGHT);
		}
		this.getChildren().addAll(leftArrow.getNode(), rightArrow.getNode());		
		this.setPickOnBounds(false);
		hide();
	}
	
	public HighlightableArrowView getLeftArrow() {
		return leftArrow;
	}
	public HighlightableArrowView getRightArrow() {
		return rightArrow;
	}
	public void hide() {
		this.setVisible(false);
	}
	public void show() {
		this.setVisible(true);
	}
	public void attachTo(SquareView sv) {
		target = sv;
		updateArrowPosition();
	}
	public void detach() {
		target = null;
	}
	public void updateArrowPosition() {
		if (target == null) {
			return;
		}
		Point2D p = this.sceneToLocal(target.localToScene(target.getWidth()/2, target.getHeight()/2));
		leftArrow.centerAround(p.getX() - target.getWidth()/2 - GAP_BETWEEN_ARROW_AND_SQUARE, p.getY());
		rightArrow.centerAround(p.getX() + target.getWidth()/2 + GAP_BETWEEN_ARROW_AND_SQUARE, p.getY());
	}
}
