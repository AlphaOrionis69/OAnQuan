package view.board;

import model.game.OAnQuanGame;
import view.common.Highlightable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;

import java.util.Random;

public abstract class SquareView extends Pane implements Highlightable{
	private int squareId;
	private int currentValue = 0;
	private Label countLabel;
	private Pane stoneLayer; 
	private Pane backgroundLayer; 
	protected Shape backgroundShape;
	public static final double CELL_WIDTH = 120.0;
	public static final double CELL_HEIGHT = 120.0;
	public static final double CONTAINER_SIZE = 90.0;
	public static final double BIG_STONE_RADIUS = 12.0;
	public static final double SMALL_STONE_RADIUS = 5.0;
	private Random random = new Random();
	private static final int MAX_REROLL = 100;
	private String cssURL = null; 
	public SquareView(int squareId) {
		this.squareId = squareId;
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
		countLabel.setMouseTransparent(true);
		
		try {
			cssURL = getClass().getResource("/css/square_style.css").toExternalForm();
			this.getStylesheets().add(cssURL);
			countLabel.getStyleClass().add("score-text");
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			
			drawBackground();
			countLabel.setTextFill(Color.WHITE);
			countLabel.setFont(new Font("System Bold", 14));
		}
		
		this.getChildren().addAll(backgroundLayer, stoneLayer, countLabel);
		
	}
	
	public int getSquareId() { return squareId; }
	
	
	public void syncSquare(OAnQuanGame game) {
		stoneLayer.getChildren().clear();
		int small = game.getBoard().getSquare(squareId).getSmallStones();		
		int big = game.getBoard().getSquare(squareId).getBigStones();
		
		for (int i = 0; i < big; i++) {
			drawStones(true);
		}
		for (int i = 0; i < small; i++) {
			drawStones(false);
		}
		currentValue = game.getBoard().getSquare(squareId).calculatePoints();
		countLabel.setText(String.valueOf(game.getBoard().getSquare(squareId).calculatePoints()));
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
		
		double bestX = 0, bestY = 0, bestDistance = -1;
		for (int turn = 0; turn <= MAX_REROLL; turn++) {
			double maxPos = stoneLayer.getPrefWidth() - (radius * 2);
			double x = (random.nextDouble() * maxPos) + radius;
			double y = (random.nextDouble() * maxPos) + radius;
			
			boolean ok = true;
			double curDistance = Double.MAX_VALUE;
			for (Node stone : stoneLayer.getChildren()) {
				if (stone instanceof Circle) {
					Circle circle = (Circle)stone;
					Point2D p = new Point2D(circle.getLayoutX() + circle.getRadius(), circle.getLayoutY() + circle.getRadius());
					if (p.distance(x, y) < circle.getRadius() + radius + 1) {
						curDistance = Math.min(curDistance, p.distance(x, y));
						ok = false;
					}
				}
			}
			if (bestDistance < curDistance) {
				bestDistance = curDistance;
				bestX = x;
				bestY = y;
			}
			if (ok || turn == MAX_REROLL) {
				if (!ok) { 
					x = bestX; y = bestY; 
				}
				Circle c = new Circle(radius);
				if (this.getStylesheets().contains(cssURL)) {
					c.getStyleClass().add(isBig ? "big-stone-shape" : "stone-shape");
					//System.out.println("YES!!");
				}
				else {
					// fallback to default
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
	@Override
	public void highlight() {
		if (this.getStylesheets().contains(cssURL)) {		
			if (!this.getStyleClass().contains("square-hover")) {
				this.getStyleClass().add("square-hover");
			}
		}
		else {
			// fallback to default
			backgroundShape.setStroke(Color.YELLOW);
		}
		
	}
	@Override
	public void clearHighlight() {
		if (this.getStylesheets().contains(cssURL)) {		
			this.getStyleClass().remove("square-hover");		
		}
		else {
			// fallback to default
			backgroundShape.setStroke(Color.BLACK);
		}
	}
	protected abstract void drawBackground();

	protected Label getCountLabel() { return countLabel; }
	protected Pane getStoneLayer() { return stoneLayer; }
	protected Pane getBackgroundLayer() { return backgroundLayer; }
	protected String getCssURL() { return cssURL; }
}