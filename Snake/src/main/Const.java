package main;

import java.awt.Color;

public class Const {
	static final int Block = 25;
	static final int BOARD_HEIGHT = 1000; 
	static final int BOARD_WIDTH = 1000; 
	static final int ROWS = BOARD_HEIGHT / Block;
	static final int COLS = BOARD_WIDTH / Block;
	static final int START_X = 200;
	static final int START_Y = 400;
	enum BOARD_VALUE { SPACE , WALL, FOOD };
	enum DIRECTION { UP, DOWN, LEFT, RIGHT };

	static final Color FOOD_COLOR = Color.YELLOW;
	
	enum SNAKE_STATE { NORMAL, COLLISION, EAT };
	
	enum KEY { UP, DOWN, LEFT, RIGHT, SPACE, ENTER };
	
}
