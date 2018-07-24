package main;

import java.awt.Point;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Snake {

	Point Head;
	Deque<Point> Body;
	Set<Point> BodySet;
	Const.DIRECTION Direction;

	Snake() {
		Head = new Point(Const.START_X, Const.START_Y);
		Body = new LinkedList<>();
		BodySet = new HashSet<>();

		Point p1 = new Point(Const.START_X - Const.Block, Const.START_Y);
		Point p2 = new Point(Const.START_X - 2*Const.Block, Const.START_Y);
		Point p3 = new Point(Const.START_X - 3*Const.Block, Const.START_Y);
		/*Point p4 = new Point(Const.START_X - 4*Const.Block, Const.START_Y);
		Point p5 = new Point(Const.START_X - 5*Const.Block, Const.START_Y);
		*/

		Body.offer(p1);
		Body.offer(p2);
		Body.offer(p3);
		/*Body.offer(p4);
		Body.offer(p5);*/

		//BodySet.add(Head);
		BodySet.add(p1);
		BodySet.add(p2);
		BodySet.add(p3);
		/*BodySet.add(p4);
		BodySet.add(p5);*/

		Direction = Const.DIRECTION.RIGHT;
	}
	
	void moveHead(Const.DIRECTION d) {
		
		if (d == Const.DIRECTION.UP) {
			Head.y -= Const.Block;
		}
		else if (d == Const.DIRECTION.DOWN) {
			Head.y += Const.Block;
		}
		else if (d == Const.DIRECTION.LEFT) {
			Head.x -= Const.Block;
		}
		else {
			Head.x += Const.Block;
		}
	}

	void moveBody(boolean elongate) {
		Body.offerFirst(new Point(Head.x, Head.y));
		BodySet.add(new Point(Head.x, Head.y));
		Point last;
		if (!elongate) {
			last = Body.pollLast();
			BodySet.remove(last);
		}
		
	}

	void elongate() {
	}

	void setDirection(Const.DIRECTION d) {
		Direction = d;
	}

	Point getHead() { return Head; }
	Deque<Point> getBody() { return Body; }
	Set<Point> getBodySet() { return BodySet; }
	Const.DIRECTION getDirection() { return Direction; }
}
