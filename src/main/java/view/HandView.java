package view;

import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class HandView extends StackPane {
	private Label handLabel;
	private Circle handVisual;
	private int handCount = 0;
	private SquareView target = null;
	public static double RADIUS = 30;
	public HandView() {
		handVisual = new Circle(RADIUS);
		handVisual.setFill(Color.SANDYBROWN);
		handVisual.setStroke(Color.BLACK);
		
		handLabel = new Label("0");
		handLabel.setStyle("-fx-font-weight:bold;");
		handLabel.setTextFill(Color.WHITE);
		
		this.getChildren().addAll(handVisual, handLabel);
		hide();
	}
	public void addAmount(int amount) {
		handCount += amount;
		handLabel.setText(String.valueOf(handCount));
	}
	public void decreaseAmount(int amount) {
		if (handCount < amount) {
			System.out.println("Hand is empty now??");
		}
		handCount -= amount;
		handLabel.setText(String.valueOf(handCount));
	}
	public void updateHandPosition() {
		if (target == null) {
			return;
		}
		moveHandTo(target);
	}
	public void moveHandTo(SquareView sv) {
		target = sv;
		if (getParent() == null) {
			System.out.println("Where is hand's parent?");
			return;
		}
		Point2D p = getParent().sceneToLocal(sv.localToScene(sv.getWidth()/2, sv.getHeight()/2));
		setLayoutX(p.getX() - handVisual.getRadius()); setLayoutY(p.getY() - handVisual.getRadius());
	}
	public void reset() {
		target = null;
		handCount = 0;
		handLabel.setText("0");
	}
	public void show() {
		setVisible(true);
	}
	public void hide() {
		setVisible(false);
	}
}
