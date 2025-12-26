package view;

import model.board.Square;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import java.util.Random;

public abstract class SquareView extends Pane {
	private Square square;
	protected int currentValue = 0;
	protected Label countLabel;
	protected Pane stoneLayer; 
	protected Pane backgroundLayer; 
	protected Shape backgroundShape;
	public static final double CELL_WIDTH = 90.0;
	public static final double CELL_HEIGHT = 90.0;
	public static final double CONTAINER_SIZE = 60.0;
	private Random random = new Random();
	
	public SquareView(Square square) {
		this.square = square;
		this.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
		
		this.setStyle("-fx-border-width: 0; -fx-background-color: transparent;");
		
		backgroundLayer = new Pane();
		backgroundLayer.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
		backgroundLayer.setMouseTransparent(true); 
		
		stoneLayer = new Pane();
		stoneLayer.setPrefSize(CONTAINER_SIZE, CONTAINER_SIZE);
		stoneLayer.setLayoutX((CELL_WIDTH - CONTAINER_SIZE) / 2);
		stoneLayer.setLayoutY((CELL_HEIGHT - CONTAINER_SIZE) / 2);
		stoneLayer.setMouseTransparent(true);
		
		
		countLabel = new Label("0");
		countLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14px; -fx-effect: dropshadow(one-pass-box, black, 2, 0, 0, 0);");
		countLabel.setMouseTransparent(true);
		
		this.getChildren().addAll(backgroundLayer, stoneLayer, countLabel);
		
		drawBackground();
		syncSquare();
	}
	
	public Square getSquare() { return square; }
	
	
	public void syncSquare() {
		stoneLayer.getChildren().clear();
		int small = square.getSmallStones();
		int big = square.getBigStones();
		
		for (int i = 0; i < big; i++) {
			drawStones(true);
		}
		for (int i = 0; i < small; i++) {
			drawStones(false);
		}
		currentValue = square.calculatePoints();
		countLabel.setText(String.valueOf(square.calculatePoints()));
	}
	
	public void addVisualStone() {
		drawStones(false);		
		countLabel.setText(String.valueOf(currentValue + 1));
		++currentValue;		
	}
	
	public void clearVisualStones() {
		stoneLayer.getChildren().clear();
		currentValue = 0;
		countLabel.setText("0");
	}
	
	private void drawStones(boolean isBig) {
		double radius = isBig ? 12 : 5;
		Circle c = new Circle(radius);
		c.setFill(isBig ? Color.GOLD : Color.WHITE);
		c.setStroke(Color.BLACK);
		
		double maxPos = stoneLayer.getPrefWidth() - (radius * 2);
		double x = (random.nextDouble() * maxPos) + radius;
		double y = (random.nextDouble() * maxPos) + radius;
		
		c.setLayoutX(x);
		c.setLayoutY(y);
		
		stoneLayer.getChildren().add(c);
	}
	
	public void highlight(boolean on) {
		if (on) {
			backgroundShape.setStroke(Color.YELLOW);
		} else {
			backgroundShape.setStroke(Color.BLACK);
		}
	}
	protected abstract void drawBackground();
}