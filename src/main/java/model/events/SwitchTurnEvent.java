package model.events;

import model.players.Player;

public class SwitchTurnEvent extends GameEvent {
	private Player newPlayer;
	public Player getNewPlayer() {
		return newPlayer;
	}
	public SwitchTurnEvent(Player newPlayer) {
		this.newPlayer = newPlayer;
	}
}