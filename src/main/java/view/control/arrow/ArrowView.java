package view.control.arrow;

import javafx.scene.Node;

public interface ArrowView {
	public Node getNode();
	public ArrowDirection getArrowDirection();
	public void centerAround(double centerX, double centerY);
}
