package model.board;

import java.util.ArrayList;
import java.util.List;

public class Board {
	private List<Square> squares;

	public Board() {
		this.squares = new ArrayList<>();
		initBoard();
	}

	public void initBoard() {
		for (int i = 0; i < 5; i++) {
			squares.add(new CitizenSquare(i, 5));
		}
		
		squares.add(new MandarinSquare(5, 1));
		
		for (int i = 6; i < 11; i++) {
			squares.add(new CitizenSquare(i, 5));
		}
		
		squares.add(new MandarinSquare(11, 1));
	}

	public Square getSquare(int id) {
		if (id >= 0 && id < squares.size()) {
			return squares.get(id);
		}
		return null;
	}

	public int getNextIdx(int currentId, boolean isClockwise) {
		if (isClockwise) {
			return (currentId + 1) % 12;
		}
		else {
			return (currentId - 1 + 12) % 12;
		}
	}

	public int getTotalStones() {
		int total = 0;
		for (Square s : squares) {
			total += s.calculatePoints();
		}
		return total;
	}
	
	public List<Square> getSquares() {
		return squares;
	}
	
	@Override
	public String toString() {
		StringBuffer output = new StringBuffer();
		output.append(new String("====================================================\n"));
		output.append(new String("	 "));
		for (int i = 10; i >= 6; i--) {
			output.append(getSquare(i).toString());
			if (i != 6) output.append(" - ");
			else output.append("\n");
		}
		output.append(new String("  " + getSquare(11).toString() + "							 " + getSquare(5).toString()) + "\n");
		output.append(new String("	 "));
		for (int i = 0; i <= 4; i++) {
			output.append(getSquare(i).toString());
			if (i != 4) output.append(" - ");
			else output.append("\n");
		}
		output.append(new String("====================================================\n"));
		return output.toString();
	}
}