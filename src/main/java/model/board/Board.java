package model.board;

import java.util.ArrayList;
import java.util.List;

import model.game.Direction;
import model.players.PlayerSide;

public class Board {
	private final List<Square> squares;
	public static final int TOTAL_SQUARES = 12;
	public static final int LEFT_MANDARIN_ID = 11;
	public static final int RIGHT_MANDARIN_ID = 5;
	public Board() {
		this.squares = new ArrayList<>();
		initBoard();
	}

	private void initBoard() {
		for (int i = PlayerSide.BOTTOM.start(); i <= PlayerSide.BOTTOM.end(); i++) {
			squares.add(new CitizenSquare(i, 5));
		}
		squares.add(new MandarinSquare(RIGHT_MANDARIN_ID, 1));
		
		for (int i = PlayerSide.TOP.start(); i <= PlayerSide.TOP.end(); i++) {
			squares.add(new CitizenSquare(i, 5));
		}
		squares.add(new MandarinSquare(LEFT_MANDARIN_ID, 1));
	}

	public Square getSquare(int id) {
		if (id >= 0 && id < squares.size()) {
			return squares.get(id);
		}
		System.out.println("Board: The square did not exist?");
		return null;
	}

	public int getNextIdx(int currentId, Direction direction) {
		return (currentId + direction.step() + TOTAL_SQUARES) % TOTAL_SQUARES;
	}

	public int getTotalStones() {
		int total = 0;
		for (Square s : squares) {
			total += s.calculatePoints();
		}
		return total;
	}
	public boolean areMandarinsEmpty() {
		return squares.get(RIGHT_MANDARIN_ID).calculatePoints() == 0 && squares.get(LEFT_MANDARIN_ID).calculatePoints() == 0;
	}
	public boolean isSideEmpty(PlayerSide side) {
		int startIdx = side.start(), endIdx = side.end();
		for (int i = startIdx; i <= endIdx; i++) {
			if (getSquare(i) == null) {
				System.out.println("Board: Side is not compatible with board");
				continue;
			}
			if (!squares.get(i).isEmpty()) return false;
		}
		return true;
	}
	public int pickUpStonesOnSide(PlayerSide side) {
		int picked = 0;
		for (int i = side.start(); i <= side.end(); i++) {
			if (getSquare(i) == null) {
				System.out.println("Board: Side is not compatible with board");
				continue;
			}
			picked += getSquare(i).pickUpStones();
		}
		return picked;
	}
	public void addStonesOnSide(int amountPerSquare, PlayerSide side) {
		for (int i = side.start(); i <= side.end(); i++) {
			if (getSquare(i) == null) {
				System.out.println("Board: Side is not compatible with board");
				continue;
			}
			getSquare(i).addStones(amountPerSquare);
		}
	}
	public List<Square> getSquares() {
		return new ArrayList<>(squares);
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("====================================================\n");
		output.append("	 ");
		for (int i = PlayerSide.TOP.end(); i >= PlayerSide.TOP.start(); i--) {
			output.append(getSquare(i).toString());
			if (i != PlayerSide.TOP.start()) output.append(" - ");
			else output.append("\n");
		}
		output.append("  " + getSquare(11).toString() + "							 " + getSquare(5).toString() + "\n");
		output.append("	 ");
		for (int i = PlayerSide.BOTTOM.start(); i <= PlayerSide.BOTTOM.end(); i++) {
			output.append(getSquare(i).toString());
			if (i != PlayerSide.BOTTOM.end()) output.append(" - ");
			else output.append("\n");
		}
		output.append("====================================================\n");
		return output.toString();
	}
}