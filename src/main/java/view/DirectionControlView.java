package view;

import javafx.geometry.Point2D;

public class DirectionControlView {
	private Arrow arrowCW, arrowCCW;
	private SquareView target;
	private final double GAP_BETWEEN_ARROW_AND_SQUARE = 8;
	public DirectionControlView() {
		arrowCW = new Arrow();
		arrowCCW = new Arrow();
		hide();
	}
	
	public DirectionControlView(double size) {
		arrowCW = new Arrow(size);
		arrowCCW = new Arrow(size);
		hide();
	}
	public DirectionControlView(double arrowCWsize, double arrowCCWsize) {
		arrowCW = new Arrow(arrowCWsize);
		arrowCCW = new Arrow(arrowCCWsize);
		hide();
	}
	public Arrow getArrowCW() {
		return arrowCW;
	}
	public Arrow getArrowCCW() {
		return arrowCCW;
	}
	public void hide() {
		arrowCCW.setVisible(false);
		arrowCW.setVisible(false);
	}
	public void show() {
		arrowCCW.setVisible(true);
		arrowCW.setVisible(true);
	}
	public void attachTo(SquareView sv) {
		target = sv;
		updateArrowPosition();
	}
	public void detach() {
		target = null;
	}
	public void updateArrowPosition() {
//		Arrow arrowCCW = arrowView.getArrowCCW();
//		Arrow arrowCW = arrowView.getArrowCW();
		if (arrowCW.getParent() == null) {
			System.out.println("Where is arrow's parent");
			return;
		}
		if (target == null) {
			return;
		}
		Point2D p = arrowCW.getParent().sceneToLocal(target.localToScene(target.getWidth()/2, target.getHeight()/2));
		int id = target.getSquare().getId();
		if (id >= 6 && id <= 10) {
			arrowCW.setRotate(-90);
			arrowCCW.setRotate(90);
			
			arrowCW.setLayoutX(p.getX() - target.getWidth()/2 - arrowCW.getSize()/2 - GAP_BETWEEN_ARROW_AND_SQUARE);
			arrowCW.setLayoutY(p.getY());
			
			arrowCCW.setLayoutX(p.getX() + target.getWidth()/2 + arrowCW.getSize()/2 + GAP_BETWEEN_ARROW_AND_SQUARE);
			arrowCCW.setLayoutY(p.getY());
			
		}
		else {
			arrowCW.setRotate(90);
			arrowCCW.setRotate(-90);
			
			arrowCCW.setLayoutX(p.getX() - target.getWidth()/2 - arrowCW.getSize()/2 - GAP_BETWEEN_ARROW_AND_SQUARE);
			arrowCCW.setLayoutY(p.getY());
			
			arrowCW.setLayoutX(p.getX() + target.getWidth()/2 + arrowCW.getSize()/2 + GAP_BETWEEN_ARROW_AND_SQUARE);
			arrowCW.setLayoutY(p.getY());
		}
	}
}
