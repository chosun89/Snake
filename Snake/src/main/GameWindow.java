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
import java.text.DecimalFormat;
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
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(Const.BOARD_WIDTH, Const.BOARD_HEIGHT));
		setFocusable(true);

		if (Const.TRAINING_MODE) {
			World = new SnakeWorld(Const.NUMBER_OF_POPULATIONS, Const.SNAKES_PER_POP);
		}
	
		else { 
			World = new SnakeWorld();
		}
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
		Timer = new Timer(Const.DELAY, this);
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
	
		if (Const.TRAINING_MODE) {
			g.setColor(Color.WHITE);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, Const.SCOREFONTSIZE));
			g.drawString("Generation: " + World.getGeneration(), 25, 25);

			for (int i = 0; i < World.AllPopulations.length; i++) {
				String Alive = Integer.toString((World.AllPopulations[i].numAlive()));
				String BestScore = Integer.toString(World.AllPopulations[i].getBestScore());
				g.drawString("Number Alive: " + Alive, 25, 50);
				g.drawString("Best Score: " + BestScore, 25, 75);
			}
			// always paints the current best snake
			Snake s = World.findCurrentBest();
			drawSnake(g, s);
			drawFood(g, s.getFood());

	
		} else {
			Snake Snake = World.getSnake();
			
			drawSnake(g, Snake);

			Food Food = World.getFood();
			drawFood(g, Food);
			
			String Score = Integer.toString(Snake.getBody().size() + 1);
			g.setColor(Color.WHITE);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, Const.SCOREFONTSIZE));
			g.drawString("Score: " + Score, 25, 25);
			
			String LifeTime = Integer.toString(Snake.getLifeTime());
			g.drawString("Game ticks: " + LifeTime, 25, 50);

			DecimalFormat df = new DecimalFormat("#.##");
			String Time = df.format(Snake.calcFitness());
			g.drawString("Fitness: " + Time, 25, 75);
		}

		Toolkit.getDefaultToolkit().sync();
	}
	
	@Override 
	public void actionPerformed(ActionEvent e) {
		if (Const.TRAINING_MODE) {
			if (World.getGeneration() < Const.GENERATIONS) {
				if (World.allPopulationsDead()) {
					World.geneticAlgo();
				}
				else {
					World.go();
					repaint();
				}
			}
		}
		else {
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

	private void drawSnake(Graphics g, Snake s) {
			g.setColor(Const.SNAKE_COLOR);
			Deque<Point> body = s.getBody();
			String str = Integer.toString(s.getScore());
			for (Point p : body) {
				g.fillRect(p.x*Const.Block, p.y*Const.Block, Const.Block-1,  Const.Block-1);
			}
			g.setColor(Const.SNAKE_HEAD_COLOR);
			g.fillRect(s.getHead().x*Const.Block, s.getHead().y*Const.Block , Const.Block -1, Const.Block-1);
			//g.drawString("Fitness: " + s.calcFitness(), 600, 25);
			g.drawString("Score: " + str, 600, 25);
	}
	
	private void drawFood(Graphics g, Food f) {
			g.setColor(f.getColor());
			g.fillRect((int)f.getX()*Const.Block, (int)f.getY()*Const.Block, Const.Block, Const.Block);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
