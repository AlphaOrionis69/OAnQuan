package view;

import model.board.Square;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import java.util.Random;

public abstract class SquareView extends Pane {
	private Square square;
	private int currentValue = 0;
	protected Label countLabel;
	protected Pane stoneLayer; 
	protected Pane backgroundLayer; 
	protected Shape backgroundShape;
	public static final double CELL_WIDTH = 120.0;
	public static final double CELL_HEIGHT = 120.0;
	public static final double CONTAINER_SIZE = 90.0;
	public static final double BIG_STONE_RADIUS = 12.0;
	public static final double SMALL_STONE_RADIUS = 5.0;
	private Random random = new Random();
	private static final int MAX_REROLL = 30;
	public SquareView(Square square) {
		this.square = square;
		this.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
		
		backgroundLayer = new Pane();
		backgroundLayer.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
		backgroundLayer.setMouseTransparent(true); 
		
		stoneLayer = new Pane();
		stoneLayer.setPrefSize(CONTAINER_SIZE, CONTAINER_SIZE);
		stoneLayer.setLayoutX((this.getPrefWidth() - stoneLayer.getPrefWidth()) / 2);
		stoneLayer.setLayoutY((this.getPrefHeight() - stoneLayer.getPrefHeight()) / 2);
		stoneLayer.setMouseTransparent(true);
		
		
		countLabel = new Label("0");
		countLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14px; -fx-effect: dropshadow(one-pass-box, black, 2, 0, 0, 0);");
		countLabel.setMouseTransparent(true);
		
		try {
			this.getStylesheets().add(getClass().getResource("/css/square_style.css").toExternalForm());
		}
		catch (Exception e) {
			e.printStackTrace();
			this.setStyle("-fx-border-width: 0; -fx-background-color: transparent;");
			drawBackground();
		}
		
		this.getChildren().addAll(backgroundLayer, stoneLayer, countLabel);
		
		
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
		double radius = isBig ? BIG_STONE_RADIUS : SMALL_STONE_RADIUS;
		
		
		for (int turn = 0; turn <= MAX_REROLL; turn++) {
			double maxPos = stoneLayer.getPrefWidth() - (radius * 2);
			double x = (random.nextDouble() * maxPos) + radius;
			double y = (random.nextDouble() * maxPos) + radius;
			
			boolean ok = true;
			for (Node stone : stoneLayer.getChildren()) {
				if (stone instanceof Circle) {
					Circle circle = (Circle)stone;
					Point2D p = new Point2D(circle.getLayoutX() + circle.getRadius(), circle.getLayoutY() + circle.getRadius());
					if (p.distance(x, y) < circle.getRadius() + radius - 2) {
						ok = false; break;
					}
				}
			}
			if (ok || turn == MAX_REROLL) {
				Circle c = new Circle(radius);
				try {
					if (isBig) {
						c.getStyleClass().add("big-stone-shape");
					}
					else {
						c.getStyleClass().add("stone-shape");
					}
				}
				catch (Exception e) {
					c.setFill(isBig ? Color.GOLD : Color.WHITE);
					c.setStroke(Color.BLACK);
				}
				c.setLayoutX(x);
				c.setLayoutY(y);
				
				stoneLayer.getChildren().add(c);
				break;
			}	
		}
		
		
		
	}
	
	public void highlight(boolean on) {
		if (on) {
			try {
				if (!this.getStyleClass().contains("square-hover")) {
					this.getStyleClass().add("square-hover");
				}
			}
			catch (Exception e) {
				backgroundShape.setStroke(Color.YELLOW);
			}
			
		} else {
			try {
				if (this.getStyleClass().contains("square-hover")) {
					this.getStyleClass().remove("square-hover");
				}
			}
			catch (Exception e) {
				backgroundShape.setStroke(Color.BLACK);
			}
			
		}
	}
	protected abstract void drawBackground();
}