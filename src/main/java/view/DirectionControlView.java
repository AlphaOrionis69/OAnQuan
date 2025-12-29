package view;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;


public class DirectionControlView extends Pane{
	private Arrow leftArrow, rightArrow;
	private SquareView target;
	private final double GAP_BETWEEN_ARROW_AND_SQUARE = 25;
	public DirectionControlView() {
		try {
			leftArrow = new Arrow("/image/left-arrow.png");
			rightArrow = new Arrow("/image/right-arrow.png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		this.getChildren().addAll(leftArrow, rightArrow);
		
		this.setPickOnBounds(false);
		hide();
	}
	
//	public DirectionControlView(double size) {
//		leftArrow = new Arrow(size);
//		rightArrow = new Arrow(size);
//		hide();
//	}
//	public DirectionControlView(double leftArrowSize, double rightArrowSize) {
//		leftArrow = new Arrow(leftArrowSize);
//		rightArrow = new Arrow(rightArrowSize);
//		hide();
//	}
	public Arrow getLeftArrow() {
		return leftArrow;
	}
	public Arrow getRightArrow() {
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
		//leftArrow.setRotate(-90);
		//rightArrow.setRotate(90);
		leftArrow.setLayoutX(p.getX() - target.getWidth()/2 - leftArrow.getLayoutBounds().getWidth()/2 - GAP_BETWEEN_ARROW_AND_SQUARE);
		leftArrow.setLayoutY(p.getY() - leftArrow.getLayoutBounds().getHeight()/2);
		
		rightArrow.setLayoutX(p.getX() + target.getWidth()/2 - rightArrow.getLayoutBounds().getWidth()/2 + GAP_BETWEEN_ARROW_AND_SQUARE);
		rightArrow.setLayoutY(p.getY() - rightArrow.getLayoutBounds().getHeight()/2);
	}
}
