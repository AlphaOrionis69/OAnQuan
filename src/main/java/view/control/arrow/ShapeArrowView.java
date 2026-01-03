package view.control.arrow;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;

public class ShapeArrowView implements HighlightableArrowView {
	private final Polygon arrowVisual;
	private final ArrowDirection direction;
	private static final double SHAPE_ARROW_SIZE = 20;
	public ShapeArrowView(ArrowDirection direction) {
		arrowVisual = createPolygonArrow();
		this.direction = direction;
		arrowVisual.setRotate(direction == ArrowDirection.LEFT ? -90 : 90);		
	}
	private Polygon createPolygonArrow() {
		Polygon output = new Polygon(-SHAPE_ARROW_SIZE, 0, 0, -SHAPE_ARROW_SIZE, SHAPE_ARROW_SIZE, 0,
				SHAPE_ARROW_SIZE/2, 0, SHAPE_ARROW_SIZE/2, SHAPE_ARROW_SIZE, -SHAPE_ARROW_SIZE/2, SHAPE_ARROW_SIZE,
				-SHAPE_ARROW_SIZE/2, 0);
		output.setStroke(Color.BLACK); output.setFill(Color.LIMEGREEN);
		return output;
	}
	@Override 
	public Node getNode() { return arrowVisual; }
	@Override
	public ArrowDirection getArrowDirection() { return direction; }
	@Override
	public void centerAround(double centerX, double centerY) {
		arrowVisual.setLayoutX(centerX);
		arrowVisual.setLayoutY(centerY);
	}
	@Override
	public void highlight() {
		arrowVisual.setStroke(Color.YELLOW);
		arrowVisual.setStrokeWidth(3);
	}
	@Override
	public void clearHighlight() {
		arrowVisual.setStroke(Color.BLACK);
		arrowVisual.setStrokeWidth(1);
	}
}
