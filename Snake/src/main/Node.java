package main;

import java.awt.Point;

@SuppressWarnings("serial")
public class Node extends Point {
	
	Const.BOARD_VALUE Val;
	int Dist;

	Node(Const.BOARD_VALUE val) {
		Val = val;
		Dist = -1;
	}
	
	public int getDist() { return Dist; }
	public String toString() {
		if (Val == Const.BOARD_VALUE.HEAD) return "H";
		else if (Val == Const.BOARD_VALUE.BODY) return "1";
		else if (Val == Const.BOARD_VALUE.FOOD) return "F";
		else return "0";
	}

}
