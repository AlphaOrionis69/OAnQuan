package model.test;

import java.util.List;

import java.util.Scanner;

import model.events.GameEvent;
import model.game.OAnQuanGame;
import model.players.Move;

public class GameTest {

	public static void main(String[] args) {
		OAnQuanGame game = new OAnQuanGame();
		game.startNewGame("First", "Second");
		Scanner s = new Scanner(System.in);
		while (!game.isGameOver()) {
			System.out.println(game.getBoard());
			game.checkAndDistribute();
			System.out.println("Current score: " + game.getPlayer1().getScore() + " " + game.getPlayer2().getScore());
			System.out.println("This is " +  game.getCurrentPlayer() + " turn");
			System.out.println("Input your move here, as a pair of integer and ccw/cw, ex: \"1 cw\"");
			while (true) {
				try {
					int id = s.nextInt(); String turn = s.next(); Move move;
					if (turn.equals("ccw")) {
						// wrap it
						move = new Move(id, false);
					}
					else if (turn.equals("cw")) {
						move = new Move(id, true);
					}
					else {
						System.out.println("Invalid direction");
						continue;
					}
					List<GameEvent> events = game.move(move);
					if (events == null) {
						System.out.println("Wrong move"); continue;
					}
					break;
				}
				catch (Exception e) {
					e.printStackTrace();
					while (!s.hasNextInt()) System.out.println("Trash: " + s.next()); // read trash
				}
				
			}
		}
		game.endGame();
	}

}
