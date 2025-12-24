package events;

import players.Player;

public class SwitchTurnEvent extends GameEvent {
	public Player newPlayer;
	public SwitchTurnEvent(Player newPlayer) {
		this.newPlayer = newPlayer;
	}
}