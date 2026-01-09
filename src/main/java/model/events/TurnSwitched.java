package model.events;

import model.players.Player;
import model.players.PlayerSide;

public class TurnSwitched extends ModelChange {
	private String newPlayerName;
	public String getNewPlayerName() {
		return newPlayerName;
	}
	public TurnSwitched(String newPlayerName) {
		this.newPlayerName = newPlayerName;
	}
}