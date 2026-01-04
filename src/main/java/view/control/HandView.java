package view.control;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import view.board.SquareView;

public class HandView extends StackPane {
	private Label handLabel;
	private Circle handVisual;
	private ImageView handImage = new ImageView();
	private Image handOpened = null;
	private Image handClosed = null;
	private Image handBetween = null;
	private int handCount = 0;
	private SquareView target = null;
	private final Timeline fromOpenToCloseAnimation = new Timeline();
	private final Timeline fromBetweenToCloseAnimation = new Timeline();
	private static final double HAND_ANIMATION_LENGTH = 300;
	public static final double HAND_RADIUS = 30;
	private boolean animationEnabled = true;
	public HandView() {
		handVisual = new Circle(HAND_RADIUS);
		handVisual.setFill(Color.SANDYBROWN);
		handVisual.setStroke(Color.BLACK);	
		
		handLabel = new Label("0");
		handLabel.setFont(new Font("System Bold", 14));
		handLabel.setTextFill(Color.WHITE);	
		
		handImage.setPreserveRatio(true);
		handImage.setFitWidth(2*HAND_RADIUS);
		
		try {
			handOpened = new Image(getClass().getResource("/image/hand_open.png").toExternalForm());
			handBetween = new Image(getClass().getResource("/image/hand_between.png").toExternalForm());
			handClosed = new Image(getClass().getResource("/image/hand_close.png").toExternalForm());
			handImage.setImage(handOpened);
			handVisual.setVisible(false);
			handLabel.setVisible(false);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			animationEnabled = false;
			handImage.setVisible(false);
			handVisual.setVisible(true);
			handLabel.setVisible(true);
		}
		
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
			System.out.println("HandView: Hand is empty now??");
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
			System.out.println("HandView: Where is hand's parent?");
			return;
		}
		Point2D p = getParent().sceneToLocal(sv.localToScene(sv.getWidth()/2, sv.getHeight()/2));
		setLayoutX(p.getX() - handVisual.getRadius()); setLayoutY(p.getY() - handVisual.getRadius());
	}
	public void reset() {
		target = null;
		handCount = 0;
		handLabel.setText("0");
		fromOpenToCloseAnimation.stop();
		fromBetweenToCloseAnimation.stop();
		hide();
	}
	public void show() {
		setVisible(true);
	}
	public void hide() {
		setVisible(false);
	}
	
	private void createFromOpenToCloseAnimation() {
		if (!fromOpenToCloseAnimation.getKeyFrames().isEmpty()) return;
		fromOpenToCloseAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(0), ev -> {
			handImage.setImage(handOpened);
		}));
		fromOpenToCloseAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(HAND_ANIMATION_LENGTH/2), ev -> {
			handImage.setImage(handBetween);
		}));
		fromOpenToCloseAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(HAND_ANIMATION_LENGTH), ev -> {
			handImage.setImage(handClosed);
		}));
	}
	private void createFromBetweenToCloseAnimation() {
		if (!fromBetweenToCloseAnimation.getKeyFrames().isEmpty()) return;
		fromBetweenToCloseAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(0), ev -> {
			handImage.setImage(handBetween);
		}));
		fromBetweenToCloseAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(HAND_ANIMATION_LENGTH), ev -> {
			handImage.setImage(handClosed);
		}));
	}
	public void animateFull() {
		if (!animationEnabled) return;
		fromOpenToCloseAnimation.play();
		fromBetweenToCloseAnimation.stop();
	}
	public void animateHalf() {
		if (!animationEnabled) return;
		fromBetweenToCloseAnimation.play();
		fromOpenToCloseAnimation.stop();
	}
	public void stopAnimate() {
		fromOpenToCloseAnimation.stop();
		fromBetweenToCloseAnimation.stop();
	}
	public double getHandAnimationLength() {
		return animationEnabled ? HAND_ANIMATION_LENGTH : 0;
	}
}
