package view.control.arrow;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageArrowView implements HighlightableArrowView {
	private final ImageView arrowVisual;
	private final ArrowDirection direction;
	private final boolean isImageValid;
	public ImageArrowView(String imagePath, ArrowDirection direction) {
		arrowVisual = new ImageView();
		Image arrowImage = new Image(imagePath);
		isImageValid = !arrowImage.isError();
		if (isImageValid) {
			arrowVisual.setImage(arrowImage);
			arrowVisual.setFitHeight(30);
			arrowVisual.setPreserveRatio(true);
		}
		arrowVisual.setMouseTransparent(false);
		arrowVisual.setPickOnBounds(true);
		this.direction = direction;
	}
	@Override 
	public Node getNode() { return arrowVisual; }
	@Override
	public ArrowDirection getArrowDirection() { return direction; }
	@Override
	public void centerAround(double centerX, double centerY) {
		arrowVisual.setLayoutX(centerX - arrowVisual.getLayoutBounds().getWidth()/2);
		arrowVisual.setLayoutY(centerY - arrowVisual.getLayoutBounds().getHeight()/2);
	}
	@Override
	public void highlight() {
		arrowVisual.setStyle("-fx-effect: dropshadow(gaussian, yellow, 15, 0.8, 0, 0);");
	}
	@Override
	public void clearHighlight() {
		arrowVisual.setStyle("");
	}
}
