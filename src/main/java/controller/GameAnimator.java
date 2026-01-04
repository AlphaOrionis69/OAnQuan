package controller;

import java.util.List;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import model.events.CaptureEvent;
import model.events.DistributeEvent;
import model.events.DropEvent;
import model.events.GameEvent;
import model.events.PickUpEvent;
import model.events.StopEvent;
import model.events.SwitchTurnEvent;
import model.game.OAnQuanGame;
import model.players.Player;
import view.board.BoardView;
import view.board.SquareView;
import view.component.StatusView;
import view.control.HandView;
import view.overlay.MessageOverlay;

public class GameAnimator {
	private final StatusView statusView;
	// hand
	private final HandView handView;
	// overlay message
	private final MessageOverlay msgOverlay;
	
	private final BoardView boardView;
	private final Map<Integer, SquareView> squareViews;
	private final OAnQuanGame game;
	private boolean isAnimating = false;
	private static final double DELAY_AFTER_MOVING_HAND = 200;
	private static final double DELAY_AFTER_PICKING_STONES = 200;
	private static final double DELAY_AFTER_DROPPING_STONES = 200;
	private static final double DELAY_AFTER_SWITCHING_TURN = 100;
	private static final double DELAY_AFTER_CAPTURING_STONES = 200;
	private static final double DELAY_BEFORE_DISTRIBUTING_STONES = 1500;
	private static final double DELAY_BEFORE_CALCULATING_FINAL_SCORES = 1500;
	private static final double DELAY_AFTER_CALCULATING_FINAL_SCORES = 1500;
	public GameAnimator(StatusView statusView, HandView handView, MessageOverlay msgOverlay,
			BoardView boardView, OAnQuanGame game) {
		this.statusView = statusView;
		this.handView = handView;
		this.msgOverlay = msgOverlay;
		this.boardView = boardView;
		this.squareViews = boardView.getAllViews();
		this.game = game;
	}
	public boolean isAnimating() {
		return isAnimating;
	}
	public void animate(List<GameEvent> events, Runnable onFinished) {
		if (events == null) return;
		Timeline timeline = new Timeline();
		double delayTime = 0;
		isAnimating = true;
		handView.show();
		//handView.open();
		for (GameEvent event : events) {	
			if (event instanceof PickUpEvent) {
				delayTime = animatePickUp((PickUpEvent)event, timeline, delayTime);		
			} else if (event instanceof DropEvent) {
				delayTime = animateDrop((DropEvent)event, timeline, delayTime);	
			} else if (event instanceof CaptureEvent) {
				delayTime = animateCapture((CaptureEvent)event, timeline, delayTime);		
			} else if (event instanceof DistributeEvent) {			
				delayTime = animateDistribute((DistributeEvent)event, timeline, delayTime);	
			} else if (event instanceof SwitchTurnEvent) {
				delayTime = animateSwitchTurn((SwitchTurnEvent)event, timeline, delayTime);		
			} else if (event instanceof StopEvent) {
				delayTime = animateStop((StopEvent)event, timeline, delayTime);	
			}		
		}
		timeline.setOnFinished(ev -> {
			isAnimating = false;
			handView.hide();
			onFinished.run();
		});
		timeline.play();
	}
	private double animatePickUp(PickUpEvent e, Timeline timeline, double delayTime) {
		SquareView sv = squareViews.get(e.getSquareId());
		KeyFrame kf1 = new KeyFrame(Duration.millis(delayTime), ev -> {
			
			handView.moveHandTo(sv);
			handView.animateFull();
		});
		delayTime += DELAY_AFTER_MOVING_HAND;
		
		KeyFrame kf2 = new KeyFrame(Duration.millis(delayTime), ev -> {
			
			handView.addAmount(e.getAmountPickedUp());
			sv.clearVisualStones();
		});
		delayTime += DELAY_AFTER_PICKING_STONES;
		
		timeline.getKeyFrames().addAll(kf1, kf2);
		return delayTime;
	}
	private double animateDrop(DropEvent e, Timeline timeline, double delayTime) {
		SquareView sv = squareViews.get(e.getSquareId());
		KeyFrame kf1 = new KeyFrame(Duration.millis(delayTime), ev -> {
			
			handView.moveHandTo(sv);
			handView.animateHalf();
		});
		delayTime += DELAY_AFTER_MOVING_HAND;
		
		KeyFrame kf2 = new KeyFrame(Duration.millis(delayTime), ev -> {
			
			handView.decreaseAmount(e.getAmountDropped());
			sv.addVisualStone();
		});
		delayTime += DELAY_AFTER_DROPPING_STONES;
		
		timeline.getKeyFrames().addAll(kf1, kf2);
		return delayTime;
	}
	private double animateCapture(CaptureEvent e, Timeline timeline, double delayTime) {
		SquareView sv = squareViews.get(e.getSquareId());
		
		KeyFrame kf1 = new KeyFrame(Duration.millis(delayTime), ev -> {
			
			handView.moveHandTo(sv);
			handView.animateFull();
			sv.highlight();
		});
		delayTime += DELAY_AFTER_MOVING_HAND;
		
		KeyFrame kf2 = new KeyFrame(Duration.millis(delayTime), ev -> {
			sv.clearVisualStones();
			sv.clearHighlight();
			statusView.updateScore(e.getAmountCaptured(), e.getPlayer());
		});
		delayTime += DELAY_AFTER_CAPTURING_STONES;
		
		timeline.getKeyFrames().addAll(kf1, kf2);
		return delayTime;
	}
	private double animateDistribute(DistributeEvent e, Timeline timeline, double delayTime) {
		KeyFrame kf1 = new KeyFrame(Duration.millis(delayTime), ev -> {
			msgOverlay.setTitle("Distribution");
			if (e.isLending()) msgOverlay.setContent(e.getPlayer().getName() + " lends stones from other player to distribute");
			else msgOverlay.setContent(e.getPlayer().getName() + " distributes stones.");
			msgOverlay.setVisible(true);
		});
		
		delayTime += DELAY_BEFORE_DISTRIBUTING_STONES;
		KeyFrame kf2 = new KeyFrame(Duration.millis(delayTime), ev -> {
			msgOverlay.setVisible(false);
			handView.hide();
			int start = e.getPlayer().getSide().start();
			int end = e.getPlayer().getSide().end();
			for (int i = start; i <= end; i++) {
				squareViews.get(i).addVisualStone();
			}
			Player otherPlayer = e.getPlayer() == game.getPlayer1() ? game.getPlayer2() : game.getPlayer1();
			statusView.updateScore(-e.getAmountLent(), otherPlayer);			
			statusView.updateScore(-(5*e.getAmountPerSquare() - e.getAmountLent()), e.getPlayer());
		});
		
		timeline.getKeyFrames().addAll(kf1, kf2);
		return delayTime;
	}
	private double animateSwitchTurn(SwitchTurnEvent e, Timeline timeline, double delayTime) {
		KeyFrame kf = new KeyFrame(Duration.millis(delayTime), ev -> {
			statusView.updateTurn(e.getNewPlayer());
		});
		delayTime += DELAY_AFTER_SWITCHING_TURN;
		
		timeline.getKeyFrames().add(kf);
		return delayTime;
	}
	private double animateStop(StopEvent e, Timeline timeline, double delayTime) {
		KeyFrame kf = new KeyFrame(Duration.millis(delayTime), ev -> {
			handView.reset();
			handView.hide();
			//boardView.syncBoard(game); statusView.syncScore(game); statusView.syncTurn(game);
		});
		timeline.getKeyFrames().add(kf);
		if (e.getLastSquareId() == StopEvent.GAME_OVER) {
			delayTime = animateGameOver(timeline, delayTime);
		}
		return delayTime;
	}
	private double animateGameOver(Timeline timeline, double delayTime) {
		KeyFrame kf1 = new KeyFrame(Duration.millis(delayTime), ev -> {
			msgOverlay.setTitle("Game Over");
			msgOverlay.setContent("Calculating final scores...");
			msgOverlay.setVisible(true);
		});
		
		delayTime += DELAY_BEFORE_CALCULATING_FINAL_SCORES;
		KeyFrame kf2 = new KeyFrame(Duration.millis(delayTime), ev -> {
			msgOverlay.setVisible(false);
			boardView.syncBoard(game); statusView.syncScore(game); statusView.syncTurn(game);
		});
		
		delayTime += DELAY_AFTER_CALCULATING_FINAL_SCORES;
		KeyFrame kf3 = new KeyFrame(Duration.millis(delayTime), ev -> {
			msgOverlay.setTitle("Game Over");
			msgOverlay.setContent("Winner: " + (game.getPlayer1().getScore() > game.getPlayer2().getScore() ? "Player 1" : "Player 2"));
			msgOverlay.setVisible(true);
		});
		timeline.getKeyFrames().addAll(kf1, kf2, kf3);

		return delayTime;
	}
}
