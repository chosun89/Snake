package main;

import java.awt.Point;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import neuralnet.NeuralNetwork;

public class Snake {

	NeuralNetwork Brain;
	float InputLayer[]; // input layer
	Point Head;
	Deque<Point> Body;
	Set<Point> BodySet;
	boolean Alive;
	Const.DIRECTION Direction;
	Food Food;
	int LifeTime = 0; // Time alive in game ticks
	float Fitness = 0;
	int MovesToLive = Const.MOVES_TO_LIVE; // Countdown timer for snake's life
	int Grow = 0;
	int Score = 0;

	public Snake(boolean nnmode) {
		Head = new Point(Const.START_X, Const.START_Y);
		Body = new LinkedList<>();
		BodySet = new HashSet<>();
		Alive = true;
		
		for (int i = 1; i <= Const.START_LENGTH; i++) {
			Point p1 = new Point(Const.START_X + i, Const.START_Y);
			Body.offer(p1);
			BodySet.add(p1);
		}

		Direction = Const.START_DIRECTION; 
		if (nnmode) {
			Food = new Food();
			Brain = new NeuralNetwork(Const.INPUT_LAYERS, Const.HIDDEN_LAYERS, Const.OUTPUT_LAYERS);
			Brain.randomizeLayers();
			InputLayer = new float[Const.INPUT_LAYERS];
			randomFood();
		}
	}
	
	public void setInputLayer() {
		InputLayer = new float[Const.INPUT_LAYERS];
		// left
		float left[] = lookInDirection(Const.DIRECTION.LEFT);
		InputLayer[0] = left[0];
		InputLayer[1] = left[1];
		InputLayer[2] = left[2];
		// right
		float right[] = lookInDirection(Const.DIRECTION.RIGHT);
		InputLayer[3] = right[0];
		InputLayer[4] = right[1];
		InputLayer[5] = right[2];
	
		// up
		float up[] = lookInDirection(Const.DIRECTION.UP);
		InputLayer[6] = up[0];
		InputLayer[7] = up[1];
		InputLayer[8] = up[2];

		// down
		float down[] = lookInDirection(Const.DIRECTION.DOWN);
		InputLayer[9] = down[0];
		InputLayer[10] = down[1];
		InputLayer[11] = down[2];
		
		int bx = Food.x/Const.BIG_COLS/Const.COLS;
		int by = Food.y/Const.BIG_ROWS/Const.ROWS;
		
		// tell snake which subarray food exists
		InputLayer[12 + bx*Const.BIG_COLS + by] = 1;

		InputLayer[Const.INPUT_LAYERS - 1] = (float)(distance(Food, Head)/(Const.ROWS + Const.COLS));

	}
	
	// no arg move method for ai
	public void move() {
		if (Alive == false) throw new RuntimeException("Trying to move a dead snake");
		LifeTime++;
		MovesToLive--;
		
		if (MovesToLive <= 0) {
			Alive = false;
			return;
		}
		
		// if food is found
		if (Food.x == Head.x && Food.y == Head.y) {
			Score++;
			grow(true);
			randomFood();
			
			// give more time if snake is long
			// less time if snake is short
			if (BodySet.size() <= Const.ROWS)
				MovesToLive = Const.MOVES_TO_LIVE;
			else 
				MovesToLive += Const.ROWS*Const.COLS/2;
		} else {
			grow(false);
		}

		// move head based on output layer
		float output[] = Brain.getOutput(InputLayer);
		float maxi = output[0];
		int id = 0;
		for (int i = 0; i < output.length; i++) {
			if (output[i] > maxi) {
				maxi = output[i];
				id = i;
			}
		}
		
		// move in direction of max 
		//Const.DIRECTION d;
		// left
		if (id == 0 ) {
			//d = getRealtiveLeft(Direction);
			Direction = Const.DIRECTION.LEFT;
		} 
		// right
		else if(id == 1) {
			//d = getRealtiveRight(Direction);
			Direction = Const.DIRECTION.RIGHT;
		} 
		// up 
		else if (id == 2) {
			Direction = Const.DIRECTION.UP;
		}
		else {
			Direction = Const.DIRECTION.DOWN;
		}
		
		Head.x += Direction.x;
		Head.y += Direction.y;
		
		if (checkIfValid()) {
			Alive = false;
		}
	}

	private float[] lookInDirection(Const.DIRECTION d) {
		int dx = d.getX(); int dy = d.getY();
		// add once in direction before starting. this is to prevent isCollision from returning true 
		// at the start of the loop every time
		int currx = Head.x + dx; 
		int curry = Head.y + dy; 
		boolean foodFound = false;
		boolean bodyFound = false;
		float distance = 1;
		// [0] == food (0 or 1)
		// [1] == inv dist to body 
		// [2] == inv dist to wall
		float res[] = new float[3];

		// loop in current direction until wall is hit
		while (currx >= 0 && currx < Const.COLS && curry >= 0 && curry < Const.ROWS) {
			if (!foodFound && Food.x == currx && Food.y == curry) {
				res[0] = -100;
				foodFound = true;
			}
			if (!bodyFound && isCollision(new Point(currx, curry))) {
				res[1] = -1/distance;
				bodyFound = true;
			}
			currx+=dx;
			curry+=dy;
			distance++;
		}
		
		// inv proportional to distance from WALL
		res[2] = -1/distance;
		return res;
	}
	
	// true means food is eaten, so elongate
	public void grow(boolean food) {
		LifeTime++;
		Body.offerFirst(new Point(Head.x, Head.y));
		BodySet.add(new Point(Head.x, Head.y));
		Point last;
		if (food) 
			if (Body.size() < 20)
				Grow += Const.GROW_LENGTH;
			else Grow++;

		if (Grow > 0) {
			Grow--;
		} else {
			last = Body.pollLast();
			BodySet.remove(last);
		}
	}
	
	// non-ai mode controller for snake head movement
	public void moveHead() {
		/// move head after moving body
		if (Direction == Const.DIRECTION.UP) {
			Head.y--;
		}
		else if (Direction == Const.DIRECTION.DOWN) {
			Head.y++;
		}
		else if (Direction == Const.DIRECTION.LEFT) {
			Head.x--;
		}
		else {
			Head.x++;
		}
	}

	public float calcFitness() {
		if (Body.size() < 10) 
			Fitness = (float)(LifeTime*LifeTime * Math.pow(2, getSize()));
		else {
			Fitness = (float)(LifeTime*LifeTime * Math.pow(2, 10));
			Fitness *= getSize()-9;
		}
		
		return Fitness;
	}

	public Snake crossOver(Snake mate) {
		Snake res = new Snake (true);
		res.Brain = Brain.crossOver(mate.Brain);
		return res;
	}

	// returns true if a given point p overlaps with body or head
	public boolean isCollision (Point p) {
		return (BodySet.contains(p) || (p.x == Head.x && p.y == Head.y));
	}
	
	// reset everything about snake Except its brain
	public void resetSnake() {
		Head = new Point(Const.START_X, Const.START_Y);
		Body = new LinkedList<>();
		BodySet = new HashSet<>();
		Alive = true;
		
		for (int i = 1; i <= Const.START_LENGTH; i++) {
			Point p1 = new Point(Const.START_X - i, Const.START_Y);
			Body.offer(p1);
			BodySet.add(p1);
		}

		Direction = Const.DIRECTION.RIGHT;
		
	}
	
	private void randomFood() {
		Random rand = new Random(System.currentTimeMillis());
		int x, y;
		do {
			x = rand.nextInt(Const.COLS);
			y = rand.nextInt(Const.ROWS);
			
		} while (isCollision(new Point(x ,y)));

		Food.setLocation(x, y);
	}

	// includes head
	public int getSnakeSize() { return BodySet.size() + 1; }
	 
	public void setDirection(Const.DIRECTION d) { Direction = d; }
	 
	public boolean isAlive() { return Alive; }

	public Deque<Point> getBody() { return Body; }

	public Const.DIRECTION getDirection() { return Direction; }

	public Point getHead() { return Head; }

	public Point getFirst() { return Body.peekFirst(); }

	public Point getTail() { return Body.peekLast(); }

	public int getLifeTime() { return LifeTime; }

	public int getSize() { return Body.size(); }
	
	public float getFitness() { return Fitness; }

	public void normalizeFitness(double sum) { Fitness /= sum; }

	public float[] getInputLayer() {
		return InputLayer;
	}
	
	public int getScore() { return Score; }

	// returns true if snake is in illegal zones
	private boolean checkIfValid() {
		return (BodySet.contains(new Point(Head.x, Head.y)) || Head.x  < 0 || Head.x >= Const.ROWS || Head.y < 0 || Head.y >= Const.COLS);
	}

	private int distance(Point a, Point b) {
		int dx = Math.abs(a.x - b.x);
		int dy = Math.abs(a.y - b.y);
		return (dx + dy);
	}

	Const.DIRECTION getRealtiveLeft(Const.DIRECTION d) {
		if (d == Const.DIRECTION.UP) {
			return Const.DIRECTION.LEFT;
		}
		else if (d == Const.DIRECTION.DOWN) {
			return Const.DIRECTION.RIGHT;
		}
		else if (d == Const.DIRECTION.RIGHT) {
			return Const.DIRECTION.UP;
		}
		else return Const.DIRECTION.DOWN;
		
	}

	Const.DIRECTION getRealtiveRight(Const.DIRECTION d) {
		if (d == Const.DIRECTION.UP) {
			return Const.DIRECTION.RIGHT;
		}
		else if (d == Const.DIRECTION.DOWN) {
			return Const.DIRECTION.LEFT;
		}
		else if (d == Const.DIRECTION.RIGHT) {
			return Const.DIRECTION.DOWN;
		}
		else return Const.DIRECTION.UP;
		
	}
	public Food getFood() { return Food; }
}

