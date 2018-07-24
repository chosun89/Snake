package main;

import java.awt.Color;
import java.awt.Point;

@SuppressWarnings("serial")
public class Food extends Point {

	Color Color;
	
	Food() {
		Color = Const.FOOD_COLOR;
	}
	
	public Color getColor() { return Color; }
}
