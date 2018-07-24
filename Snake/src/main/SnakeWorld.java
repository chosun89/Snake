package main;

import java.awt.Point;
import java.util.Random;

public class SnakeWorld{

	Snake Snake;
	Food Food;
	Const.SNAKE_STATE SNAKE_STATE;
	boolean GameOver;
	boolean Pause;
	int Score;

	SnakeWorld() {
		Snake = new Snake();
		Food = new Food();
		SNAKE_STATE = Const.SNAKE_STATE.NORMAL;
		GameOver = false;
		Pause = false;
		Score = 0;

		randomFood();
	}
	
	void init() {
	}
	
	void randomFood() {
		Random rand = new Random(System.currentTimeMillis());
		int x = rand.nextInt(Const.COLS)*Const.Block;
		int y = rand.nextInt(Const.ROWS)*Const.Block;
		
		if (Snake.getBodySet().contains(new Point(x, y))) {
			randomFood();
		}
		
		Food.setLocation(x, y);
	}

	void move() {
		updateSnakeState();
		Const.DIRECTION d = Snake.getDirection();
		if (SNAKE_STATE == Const.SNAKE_STATE.NORMAL) {
			// order matters, body takes the place of current head so it must be moved first
			Snake.moveBody(false);
			Snake.moveHead(d);
		}
		else if (SNAKE_STATE == Const.SNAKE_STATE.EAT) {
			Snake.moveBody(true);
			Snake.moveHead(d);
			Score++;
			randomFood();
			SNAKE_STATE = Const.SNAKE_STATE.NORMAL;
		}
	}
	
	void updateSnakeState() {
		int x = (int)Snake.getHead().getX();
		int y = (int)Snake.getHead().getY();

		// if the snake touches itself or the boundaries set GameOver = true
		if (Snake.getBodySet().contains(new Point(x,y))){
			SNAKE_STATE = Const.SNAKE_STATE.COLLISION;
			GameOver = true;
			return;
		}
		if (x < 0 || x + Const.Block > Const.BOARD_WIDTH) {
			SNAKE_STATE = Const.SNAKE_STATE.COLLISION;
			GameOver = true;
			return;
		}
		if (y < 0 || y + Const.Block > Const.BOARD_HEIGHT) {
			SNAKE_STATE = Const.SNAKE_STATE.COLLISION;
			GameOver = true;
			return;
		}
		
		int fx = (int)Food.getX();
		int fy = (int)Food.getY();
		if (x == fx && y == fy) {
			SNAKE_STATE = Const.SNAKE_STATE.EAT;
		}
	}
	

	void setSnakeDirection(Const.DIRECTION d) {
		Snake.setDirection(d);
	}
	boolean isGameOver() { return GameOver; }
	void flipPause() { Pause = !Pause; }
	Const.DIRECTION getSnakeDirection() { return Snake.getDirection(); }
	Snake getSnake() { return Snake; }
	Food getFood() { return Food; }
	boolean isPaused() { return Pause; }
}