package main;

import java.awt.Color;

public class Const {
	public static final int Block = 25;
	public static final int BOARD_HEIGHT = 950; 
	public static final int BOARD_WIDTH = 950; 
	public static final int ROWS = BOARD_HEIGHT / Block;
	public static final int COLS = BOARD_WIDTH / Block;
	
	public static final int BIG_ROWS = 4;
	public static final int BIG_COLS = 4;

	public static final int START_X = 200 / Block;
	public static final int START_Y = 400 / Block;
	public static final DIRECTION START_DIRECTION = DIRECTION.RIGHT;
	public static final int START_LENGTH = 5;
	public static final int GROW_LENGTH = 3;
	public static final boolean TRAINING_MODE = true;
	public static final boolean BFS_MODE = true;

	// 12 for 4 Directions x 3 types (food,body,wall)
	// + BRxBC for subarray food
	// + 1 for distance to food
	public static final int INPUT_LAYERS = 12 + BIG_COLS*BIG_COLS + 1;
	public static final int HIDDEN_LAYERS = 13;
	public static final int OUTPUT_LAYERS = 4;
	
	public static final int GENERATIONS = 200;
	public static final int SNAKES_PER_POP = 7000;
	public static final int NUMBER_OF_POPULATIONS = 1;

	public static final int MOVES_TO_LIVE = COLS*4; 
	static final int DELAY = 0; // in milli seconds


	public enum BOARD_VALUE { SPACE , HEAD, BODY, FOOD };
	public enum DIRECTION { 

		UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT (1, 0);
		
		int x;
		int y;

		DIRECTION(int x , int y) {
			this.x = x;
			this.y = y;
		}
		
		int getX() { return x; }
		int getY() { return y; }
		
		
	};

	public static final int SCOREFONTSIZE = 20;
	public static final Color FOOD_COLOR = Color.YELLOW;
	public static final Color SNAKE_HEAD_COLOR = Color.RED;
	public static final Color SNAKE_COLOR = Color.GREEN;
	
	enum SNAKE_STATE { NORMAL, COLLISION, EAT };
	
	enum KEY { UP, DOWN, LEFT, RIGHT, SPACE, ENTER };
}
