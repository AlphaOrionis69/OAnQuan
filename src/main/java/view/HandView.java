package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class HandView extends StackPane {
	private Label handLabel;
	private Circle handVisual;
	private ImageView handImage = new ImageView();
	private Image handOpened = null;
	private Image handClosed = null;
	private Image handBetween = null;
	private int handCount = 0;
	private SquareView target = null;
	public static final double RADIUS = 30;
	public HandView() {
		handVisual = new Circle(RADIUS);
		handVisual.setFill(Color.SANDYBROWN);
		handVisual.setStroke(Color.BLACK);	
		
		handLabel = new Label("0");
		handLabel.setStyle("-fx-font-weight:bold;");
		handLabel.setTextFill(Color.BLACK);	
		try {
			handOpened = new Image(getClass().getResource("/image/hand_open.png").toExternalForm());
			handBetween = new Image(getClass().getResource("/image/hand_between.png").toExternalForm());
			handClosed = new Image(getClass().getResource("/image/hand_close.png").toExternalForm());
			handVisual.setVisible(false);
			handLabel.setVisible(false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
				
		handImage.setPreserveRatio(true);
		handImage.setFitWidth(2*RADIUS);
		this.getChildren().addAll(handVisual, handImage, handLabel);
		
		createFromOpenToCloseAnimation();
		createFromBetweenToCloseAnimation();
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
	private Timeline fromOpenToCloseAnimation = new Timeline();
	private Timeline fromBetweenToCloseAnimation = new Timeline();
	private final double ANIMATION_LENGTH = 300;
	private void createFromOpenToCloseAnimation() {
		fromOpenToCloseAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(0), ev -> {
			handImage.setImage(handOpened);
		}));
		fromOpenToCloseAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(ANIMATION_LENGTH/2), ev -> {
			handImage.setImage(handBetween);
		}));
		fromOpenToCloseAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(ANIMATION_LENGTH), ev -> {
			handImage.setImage(handClosed);
		}));
	}
	private void createFromBetweenToCloseAnimation() {
		fromBetweenToCloseAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(0), ev -> {
			handImage.setImage(handBetween);
		}));
		fromBetweenToCloseAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(ANIMATION_LENGTH), ev -> {
			handImage.setImage(handClosed);
		}));
	}
	public void animateFull() {
		fromOpenToCloseAnimation.play();
	}
	public void animateHalf() {
		fromBetweenToCloseAnimation.play();
	}
//	public void open() {
//		handImage.setImage(handOpened);
//	}
}
