package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Deque;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class GameWindow extends JPanel implements ActionListener{

	private SnakeWorld World;
	private Timer Timer;

	GameWindow() {
		World = new SnakeWorld();
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(Const.BOARD_WIDTH, Const.BOARD_HEIGHT));
		setFocusable(true);
		
		
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0),"space" );
		getActionMap().put("space", new KeyAction("space"));

		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),"enter" );
		getActionMap().put("enter", new KeyAction("enter"));

		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),"left" );
		getActionMap().put("left", new KeyAction("left"));

		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),"right" );
		getActionMap().put("right", new KeyAction("right"));

		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),"up" );
		getActionMap().put("up", new KeyAction("up"));

		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),"down" );
		getActionMap().put("down", new KeyAction("down"));

		Timer = new Timer(Const.Ticks, this);
		Timer.start();
	}

	private class KeyAction extends AbstractAction {

		String Key ;
		public KeyAction (String s) {
			Key = s;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (Key == "space" ) {
				if (Timer.isRunning())
					Timer.stop();
				else Timer.start();
			}

			if (Key == "enter") {
				if (World.isGameOver()) {
					World = new SnakeWorld();
					Timer.start();
				}
			}
			
			Const.DIRECTION curr = World.getSnakeDirection();
			if (Key == "up" && curr != Const.DIRECTION.DOWN) {
				World.setSnakeDirection(Const.DIRECTION.UP);
			}
			if (Key == "down" && curr != Const.DIRECTION.UP) {
				World.setSnakeDirection(Const.DIRECTION.DOWN);
			}
			if (Key == "left" && curr != Const.DIRECTION.RIGHT) {
				World.setSnakeDirection(Const.DIRECTION.LEFT);
			}
			if (Key == "right" && curr != Const.DIRECTION.LEFT) {
				World.setSnakeDirection(Const.DIRECTION.RIGHT);
			}
		}
		
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Snake Snake = World.getSnake();

		g.setColor(Color.GREEN);
		Deque<Point> body = Snake.getBody();
		for (Point p : body) {
			g.fillRect(p.x*Const.Block, p.y*Const.Block, Const.Block-1,  Const.Block-1);
		}

		g.setColor(Color.RED);
		g.fillRect(Snake.getHead().x*Const.Block, Snake.getHead().y*Const.Block -1 , Const.Block -1, Const.Block-1);
		
		Food Food = World.getFood();
		g.setColor(Food.getColor());
		g.fillRect((int)Food.getX()*Const.Block, (int)Food.getY()*Const.Block, Const.Block, Const.Block);
		
		String Score = World.getScore();
		g.setColor(Color.WHITE);
		g.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		g.drawString(Score, Const.Block*2, Const.Block);

		Toolkit.getDefaultToolkit().sync();
	}
	
	@Override 
	public void actionPerformed(ActionEvent e) {
		// if collision is detected, stop
		if (World.isGameOver() == true) {
			Timer.stop();
		}
		else if (World.isPaused()) {
		}
		else {
			World.move();
			repaint();
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
