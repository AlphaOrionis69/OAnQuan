package view;

import javafx.scene.shape.Polygon;

public class Arrow extends Polygon {
	public static final double ARROW_SIZE = 10.0;
	private double size;
	public Arrow() {
		super(0, -ARROW_SIZE/2, -ARROW_SIZE, ARROW_SIZE/2, ARROW_SIZE, ARROW_SIZE/2);
		size = ARROW_SIZE;
		setStyle("-fx-fill: yellow; -fx-stroke: black; -fx-cursor: hand;");
	}
	public Arrow(double size) {
		super(0, -size/2, -size, size/2, size, size/2);
		this.size = size;
		setStyle("-fx-fill: yellow; -fx-stroke: black; -fx-cursor: hand;");
	}
	public double getSize() {
		return size;
	}
}
