package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Deque;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class GameWindow extends JPanel implements ActionListener{

	private SnakeWorld World;
	private Timer Timer;
	private static final int Ticks = 100;

	GameWindow() {
		World = new SnakeWorld();
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(Const.BOARD_WIDTH, Const.BOARD_WIDTH));
		addKeyListener(new KeyboardInput());
		setFocusable(true);

		Timer = new Timer(Ticks, this);
		Timer.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		
		Snake Snake = World.getSnake();

		g.setColor(Color.GREEN);
		Deque<Point> body = Snake.getBody();
		for (Point p : body) {
			g.fillRect(p.x, p.y, Const.Block,  Const.Block);
		}

		g.setColor(Color.RED);
		g.fillRect(Snake.getHead().x, Snake.getHead().y, Const.Block, Const.Block);
		
		Food Food = World.getFood();
		g.setColor(Food.getColor());
		g.fillRect((int)Food.getX(), (int)Food.getY(), Const.Block, Const.Block);
		
		Toolkit.getDefaultToolkit().sync();
	}
	
	@Override 
	public void actionPerformed(ActionEvent e) {
	
		// if collision is detected, stop
		if (World.isGameOver() == true) {
			//Timer.stop();
		}
		else if (World.isPaused()) {
			Timer.stop();
		}
		else {
			Timer.start();
			World.move();
			repaint();
		}
	}

	private class KeyboardInput extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_LEFT && World.getSnakeDirection() != Const.DIRECTION.RIGHT) {
				World.setSnakeDirection(Const.DIRECTION.LEFT);
			}
			if(key == KeyEvent.VK_RIGHT && World.getSnakeDirection() != Const.DIRECTION.LEFT) {
				World.setSnakeDirection(Const.DIRECTION.RIGHT);
			}
			if(key == KeyEvent.VK_UP && World.getSnakeDirection() != Const.DIRECTION.DOWN) {
				World.setSnakeDirection(Const.DIRECTION.UP);
			}
			if(key == KeyEvent.VK_DOWN && World.getSnakeDirection() != Const.DIRECTION.UP) {
				World.setSnakeDirection(Const.DIRECTION.DOWN);
			}
			if (key == KeyEvent.VK_ENTER && World.isGameOver()) {
				World = new SnakeWorld();
			}
			if (key == KeyEvent.VK_SPACE) {
				World.flipPause();
			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
