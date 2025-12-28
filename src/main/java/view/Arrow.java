package view;

import javafx.scene.shape.Polygon;

public class Arrow extends Polygon {
	public static final double ARROW_SIZE = 20.0;
	private double size;
	public Arrow() {
		this(ARROW_SIZE);
	}
	public Arrow(double size) {
		super(0, -size/2, -size, size/2, size, size/2);
		this.size = size;
		
		setStyle("-fx-fill: yellow; -fx-stroke: black; -fx-cursor: hand;");
	}
	public double getSize() {
		return size;
	}
	public void highlight(boolean on) {
		if (on) {
			setStyle("-fx-fill: yellow; -fx-stroke: black; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, yellow, 15, 0.8, 0, 0);");
		}
		else {
			setStyle("-fx-fill: yellow; -fx-stroke: black; -fx-cursor: hand; -fx-effect: null");
		}
	}
}
