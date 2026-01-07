package view.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.game.OAnQuanGame;
import model.players.Player;
import model.players.PlayerSide;

public class StatusView extends StackPane {
	private Label lblP1Score, lblP2Score, lblTurn;
	private int p1Score = 0, p2Score = 0;
	public static final double PREF_HEIGHT = 80.0;
	private static final double LEFT_RIGHT_PADDING = 160.0;
	private static final double FONT_SIZE = 25.0;
	public StatusView() {
		this.setPrefHeight(PREF_HEIGHT); this.setAlignment(Pos.CENTER);
		
		lblP1Score = new Label("P1: 0"); lblP1Score.setFont(new Font("System Bold", FONT_SIZE)); lblP1Score.setTextFill(Color.web("#7a2e2e"));
		lblP2Score = new Label("P2: 0"); lblP2Score.setFont(new Font("System Bold", FONT_SIZE)); lblP2Score.setTextFill(Color.web("#3f5f4a"));
		lblTurn = new Label("Turn: Player 1"); lblTurn.setFont(new Font("System Bold", FONT_SIZE)); lblTurn.setTextFill(Color.web("#2f4a63"));
		this.getChildren().addAll(lblP1Score, lblTurn, lblP2Score);
		
		StackPane.setAlignment(lblP1Score, Pos.CENTER_LEFT);
		StackPane.setAlignment(lblTurn, Pos.CENTER);
		StackPane.setAlignment(lblP2Score, Pos.CENTER_RIGHT);
		this.setPadding(new Insets(0, LEFT_RIGHT_PADDING, 0, LEFT_RIGHT_PADDING));;
	}
	public void updateScore(int amount, PlayerSide side) {	
		if (side == PlayerSide.BOTTOM) {
			lblP1Score.setText("P1: " + (p1Score + amount));
			p1Score += amount;
		}
		else {
			lblP2Score.setText("P2: " + (p2Score + amount));
			p2Score += amount;
		}
	}
	public void updateTurn(String newPlayerName) {
		lblTurn.setText("Turn: " + newPlayerName);
	}
	public void syncScore(OAnQuanGame game) {
		lblP1Score.setText("P1: " + game.getPlayer1().getScore());
		p1Score = game.getPlayer1().getScore();
		
		lblP2Score.setText("P2: " + game.getPlayer2().getScore());
		p2Score = game.getPlayer2().getScore();
	}
	public void syncTurn(OAnQuanGame game) {
		lblTurn.setText("Turn: " + game.getCurrentPlayer().getName());
	}
	
}
