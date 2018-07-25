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

		Point p1 = new Point(Const.START_X - 1, Const.START_Y);
		Point p2 = new Point(Const.START_X - 2, Const.START_Y);
		Point p3 = new Point(Const.START_X - 3, Const.START_Y);
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
	
	// true means food is eaten, so elongate
	void move(boolean food) {
		
		Body.offerFirst(new Point(Head.x, Head.y));
		BodySet.add(new Point(Head.x, Head.y));
		Point last;
		if (!food) {
			last = Body.pollLast();
			BodySet.remove(last);
		}
	}
	
	void moveHead() {
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

	void setDirection(Const.DIRECTION d) {
		Direction = d;
	}
	Deque<Point> getBody() { return Body; }
	Set<Point> getBodySet() { return BodySet; }
	Const.DIRECTION getDirection() { return Direction; }
	Point getHead() { return Head; }
	Point getFirst() { return Body.peekFirst(); }
	Point getTail() { return Body.peekLast(); }
}
