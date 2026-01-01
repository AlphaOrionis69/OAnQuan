package view.control;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;

public class Arrow extends ImageView {
	public Arrow(String imagePath) {
		super(imagePath);
		this.setPreserveRatio(true);
		this.setFitHeight(30); this.setFitWidth(50);
		setStyle("-fx-fill: yellow; -fx-stroke: black; -fx-cursor: hand;");
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
