package view;

public class DirectionControlView {
	private Arrow arrowCW, arrowCCW;
	public DirectionControlView() {
		arrowCW = new Arrow();
		arrowCCW = new Arrow();
	}
	public Arrow getArrowCW() {
		return arrowCW;
	}
	public Arrow getArrowCCW() {
		return arrowCCW;
	}
}
