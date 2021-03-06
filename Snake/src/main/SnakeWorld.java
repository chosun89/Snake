package main;

import java.awt.Point;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import neuralnet.Population;

public class SnakeWorld{

	Node Board[][];
	Snake Snake;
	public static Food Food;
	Const.SNAKE_STATE SNAKE_STATE;
	Const.DIRECTION dirArr[] = { Const.DIRECTION.RIGHT, Const.DIRECTION.UP, 
			Const.DIRECTION.LEFT, Const.DIRECTION.DOWN };
	
	Population AllPopulations[];
	Snake AlphaPrime;
	
	boolean GameOver;
	boolean Pause;
	Integer Score;
	
	int GenerationNumber;

	// default c'tor for non-ai mode
	SnakeWorld() {
			Board = new Node[Const.ROWS][Const.COLS];
			for (int i = 0; i < Const.ROWS; i++) 
				for (int j = 0; j < Const.COLS; j++) 
					Board[i][j] =  new Node(Const.BOARD_VALUE.SPACE);

			Board[Const.START_Y][Const.START_X].Val = Const.BOARD_VALUE.HEAD;
			Board[Const.START_Y][Const.START_X-1].Val = Const.BOARD_VALUE.BODY;
			Board[Const.START_Y][Const.START_X-2].Val = Const.BOARD_VALUE.BODY;
			Board[Const.START_Y][Const.START_X-3].Val = Const.BOARD_VALUE.BODY;


			Snake = new Snake(false);
			Food = new Food();
	//		Food.x = Const.START_X - 4;
	//		Food.y = Const.START_Y;
			randomFood();
			
			SNAKE_STATE = Const.SNAKE_STATE.NORMAL;
			GameOver = false;
			Pause = false;
			Score = new Integer(0);
	}
	
	// arg c'tor for training NN
	SnakeWorld(int pop, int snakes) {
		GenerationNumber = 0;
		AllPopulations = new Population[pop];
		for (int i = 0; i < pop; i++) {
			AllPopulations[i] = new Population(snakes);
		}
		AlphaPrime = AllPopulations[0].getAlpha();
	}

	public boolean isValid(int x, int y) {
		return (x >= 0 && x < Const.COLS && y >= 0 && y < Const.ROWS
				&& Board[y][x].Val != Const.BOARD_VALUE.BODY);
	}

	// finds a new location for Food AND updates its location on the Board
	void randomFood() {
		Random rand = new Random(System.currentTimeMillis());

		int x, y;
		do {
			x = rand.nextInt(Const.COLS);
			y = rand.nextInt(Const.ROWS);
			
		} while (Snake.isCollision(new Point(x ,y)));

		Food.setLocation(x, y);
		updateFoodLocation(x,y);
	}
	
	void updateSnakeState() {
		int x = Snake.Head.x;
		int y = Snake.Head.y;

		// if the snake touches itself or the boundaries set GameOver = true
		if (Snake.getBody().contains(new Point(x,y))){
			SNAKE_STATE = Const.SNAKE_STATE.COLLISION;
			GameOver = true;
			return;
		}
		if (x < 0 || x + 1 > Const.COLS) {
			SNAKE_STATE = Const.SNAKE_STATE.COLLISION;
			GameOver = true;
			return;
		}
		if (y < 0 || y + 1 > Const.ROWS) {
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

	void updateFoodLocation(int x, int y) {
		Board[y][x].Val = Const.BOARD_VALUE.FOOD;
	}
	
	
	public void go() {
		for (int i = 0; i < Const.NUMBER_OF_POPULATIONS; i++) {
			if (AllPopulations[i].numAlive() != 0) {
				AllPopulations[i].updatePopulation();
			}
		}
	}
	
	void setSnakeDirection(Const.DIRECTION d) { Snake.setDirection(d); }
	 
	boolean isGameOver() { return GameOver; }
	 
	void flipPause() { Pause = !Pause; }
	 
	Const.DIRECTION getSnakeDirection() { return Snake.getDirection(); }
	 
	Snake getSnake() { return Snake; }
	 
	Food getFood() { return Food; }
	 
	boolean isPaused() { return Pause; }
	 
	String getScore() { return Integer.toString(Score); }
	
	boolean allPopulationsDead() { 
		for (int i = 0; i < Const.NUMBER_OF_POPULATIONS; i++) {
			if (AllPopulations[i].numAlive() != 0)
				return false;
		}
		
		return true;
	}
	
	public Snake findCurrentBest() {
		return AllPopulations[0].getCurrentBest();
	}

	public void geneticAlgo() {
		double maxi = 0;
		for (int i = 0; i < Const.NUMBER_OF_POPULATIONS; i++){
			AllPopulations[i].geneticAlgo();
			if (maxi < AllPopulations[i].getAlpha().getFitness()) {
				maxi = AllPopulations[i].getAlpha().getFitness();
				AlphaPrime = AllPopulations[i].getAlpha();
			}
		}
		GenerationNumber++;
		
		try {
			FileOutputStream file = new FileOutputStream(Const.FILENAME);
			ObjectOutputStream out = new ObjectOutputStream(file);
			
			out.writeObject(AlphaPrime.getBrain());
			out.close();
			file.close();

			System.out.println("Generation: " + GenerationNumber + "AlphaPrime successfully written");
		} catch (IOException e) {
			System.out.println(e + "Error writing file to alphaprime");
		}
	}
	
	int getGeneration() { return GenerationNumber; }
	 
	void printBoard() {
		for (int i = 0; i < Const.ROWS; i++) {
			for (int j = 0; j < Const.COLS; j++) {
				//System.out.print(Board[i][j].toString());
				System.out.print(Board[i][j].getDist() + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	// if bfs is true, Snake moves in bfs manner
	// single player mode and bfs mode only
	void move() {
		int x, y;
		Point head = Snake.getHead();
		x = head.x;
		y = head.y;
		Board[y][x].Val = Const.BOARD_VALUE.BODY;

		if (SNAKE_STATE == Const.SNAKE_STATE.NORMAL) {
			// no elongation
			Point tail = Snake.getTail();
			x = tail.x;
			y = tail.y;
			Board[y][x].Val = Const.BOARD_VALUE.SPACE;
			Snake.grow(false);
		}
		// snake eats food, elongate
		else if (SNAKE_STATE == Const.SNAKE_STATE.EAT) {
			Snake.grow(true);
			randomFood();
			Score++;
			SNAKE_STATE = Const.SNAKE_STATE.NORMAL;
		}
		
		bfs();
		// head must be moved after moving body
		if (Const.BFS_MODE){
			head = Snake.getHead();
			x = head.x;
			y = head.y;
			int mini = 0x3f3f3f3f;
			int dir[] = { 1, 0, -1, 0, 1 }; // r, u, l, d 
			Const.DIRECTION opt = Snake.getDirection();

			opt.getX();
			for (int i = 0; i < 4; i++) {
				int dx = x + dir[i];
				int dy = y + dir[i + 1];
				boolean isValid = isValid(dx,dy);
				if (isValid && mini > Board[dy][dx].Dist) {
					mini = Board[dy][dx].Dist;
					opt = dirArr[i];
				}
			}
			if (opt == Const.DIRECTION.RIGHT) {
				Snake.Head.x++;
			}
			else if (opt == Const.DIRECTION.LEFT) {
				Snake.Head.x--;
			}
			else if (opt == Const.DIRECTION.UP) {
				Snake.Head.y--;
			}
			else {
				Snake.Head.y++;
			}
			
		}
		else  Snake.moveHead();

		updateSnakeState();
		if (isGameOver()) return;
		// update snake needs to be placed here to prevent o-o-bounds error below
		x = Snake.Head.x;
		y = Snake.Head.y;
		Board[y][x].Val = Const.BOARD_VALUE.HEAD;
	}

	void bfs() {
		boolean visited[][] = new boolean[Const.ROWS][Const.COLS];
		for (int i = 0; i < Const.ROWS; i++) 
			for (int j = 0; j < Const.COLS; j++) 
				visited[i][j] = false;

		Queue<Point> q = new LinkedList<>();
		Board[Food.y][Food.x].Dist = 0;
		q.offer(Food);
		
		while (!q.isEmpty()) {
			Point top = q.poll();
			int x = top.x;
			int y = top.y;
			int curr = Board[y][x].Dist;
			visited[y][x] = true;
			
			// left neighbor
			if (isValid(x-1,y) && !visited[y][x-1]) {
				visited[y][x-1] = true;
				Board[y][x-1].Dist = curr + 1;
				q.offer(new Point(x-1,y));
			}
			// right
			if (isValid(x+1,y) && !visited[y][x+1]) {
				visited[y][x+1] = true;
				Board[y][x+1].Dist = curr + 1;
				q.offer(new Point(x+1,y));
			}
			// top 
			if (isValid(x,y-1) && !visited[y-1][x]) {
				visited[y-1][x] = true;
				Board[y-1][x].Dist = curr + 1;
				q.offer(new Point(x,y-1));
			}
			// down
			if (isValid(x,y+1) && !visited[y+1][x]) {
				visited[y+1][x] = true;
				Board[y+1][x].Dist = curr + 1;
				q.offer(new Point(x,y+1));
			}
		}
		//printBoard();
	}
	
}