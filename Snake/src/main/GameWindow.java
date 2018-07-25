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

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class GameWindow extends JPanel implements ActionListener{

	private SnakeWorld World;
	private Timer Timer;
	private static final int Ticks = 100;

	GameWindow() {
		World = new SnakeWorld();
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(Const.BOARD_WIDTH, Const.BOARD_HEIGHT));
		//addKeyListener(new KeyboardInput());
		setFocusable(true);

		Timer = new Timer(Ticks, this);
		Timer.start();
		
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0),"space" );
		getActionMap().put("space", new KeyAction("space"));

		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),"enter" );
		getActionMap().put("enter", new KeyAction("enter"));
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
				}
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
		g.fillRect(Snake.getHead().x*Const.Block, Snake.getHead().y*Const.Block, Const.Block-1, Const.Block-1);
		
		Food Food = World.getFood();
		g.setColor(Food.getColor());
		g.fillRect((int)Food.getX()*Const.Block, (int)Food.getY()*Const.Block, Const.Block, Const.Block);
		
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
			World.move(true);
			repaint();
		}
	}

//	private class KeyboardInput extends KeyAdapter {
//		@Override
//		public void keyPressed(KeyEvent e) {
//			int key = e.getKeyCode();
//			if(key == KeyEvent.VK_LEFT && World.getSnakeDirection() != Const.DIRECTION.RIGHT) {
//				World.setSnakeDirection(Const.DIRECTION.LEFT);
//			}
//			if(key == KeyEvent.VK_RIGHT && World.getSnakeDirection() != Const.DIRECTION.LEFT) {
//				World.setSnakeDirection(Const.DIRECTION.RIGHT);
//			}
//			if(key == KeyEvent.VK_UP && World.getSnakeDirection() != Const.DIRECTION.DOWN) {
//				World.setSnakeDirection(Const.DIRECTION.UP);
//			}
//			if(key == KeyEvent.VK_DOWN && World.getSnakeDirection() != Const.DIRECTION.UP) {
//				World.setSnakeDirection(Const.DIRECTION.DOWN);
//			}

//		}
// 	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
