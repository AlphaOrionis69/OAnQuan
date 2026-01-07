package model.events;

import model.players.Player;
import model.players.PlayerSide;

public class SwitchTurnEvent extends GameEvent {
	private String newPlayerName;
	public String getNewPlayerName() {
		return newPlayerName;
	}
	public SwitchTurnEvent(String newPlayerName) {
		this.newPlayerName = newPlayerName;
	}
}