package neuralnet;

import java.util.Random;

import main.Const;
import main.Snake;

public class Population {
	
	int PopSize;
	public Snake AllSnakes[];
	int NumAlive;
	double TotalFitness;
	Snake Alpha;
	Snake BestAlive;
	int BestScore;
	
	public Population(int n) {
		PopSize = n;
		AllSnakes = new Snake[PopSize];
		
		for (int i = 0; i < n; i++) {
			AllSnakes[i] = new Snake(Const.TRAINING_MODE);
		}
		BestAlive = AllSnakes[0];
		NumAlive = n;
	}
	
	public void updatePopulation() {
		NumAlive = PopSize;
		double maxi = 0;
		for (int i = 0; i < PopSize; i++) {
			if (!AllSnakes[i].isAlive()) {
				NumAlive--;
			}
			else {
				if (AllSnakes[i].getScore() > BestScore) {
					BestScore = AllSnakes[i].getScore();
				}
				if (AllSnakes[i].calcFitness() > maxi) {
					BestAlive = AllSnakes[i];
					maxi = AllSnakes[i].getFitness();
				}
				AllSnakes[i].setInputLayer();
				AllSnakes[i].move();
			}
		}
	}
	
	public Snake getCurrentBest () {
		return BestAlive;
	}
	
	// returns true if all Snakes in the pop are in a dead state
	public int numAlive() {
		return NumAlive;
	}
	
	// compute fitness for each snake in pop
	private void calcFitness() {
		double sum = 0;
		for (int i = 0; i < PopSize; i++) 
			sum += AllSnakes[i].calcFitness();
		
		double maxi = -1;
		for (int i = 0; i < PopSize; i++) {
			if (AllSnakes[i].getFitness() > maxi) {
				maxi = AllSnakes[i].getFitness();
				Alpha = AllSnakes[i];
			}
			AllSnakes[i].normalizeFitness(sum);
		}
	}
	
	// find two best snakes and mate them to produce child snake
	// and puts it in the next generation
	private void naturalSelection() {
		
		Snake nextGen[] = new Snake[PopSize];
		
		for (int i = 0; i < PopSize; i++) {
			Snake Mom = selectMate();
			Snake Dad = selectMate();

			nextGen[i] = Mom.crossOver(Dad);
		}
		
		// reset the next generation
		NumAlive = PopSize;
		AllSnakes = nextGen;
	}
	
	// roulette wheel selection
	private Snake selectMate() {
		Random rand = new Random();
		double thresh = rand.nextDouble();

		double sum = 0;
		for (int i = 0; i < PopSize; i++) {
			sum += AllSnakes[i].getFitness();
			if (sum > thresh) {
				return AllSnakes[i];
			}
		}
		return AllSnakes[0];
	}
	
	public void geneticAlgo () {
		calcFitness();
		naturalSelection();
	}
	
	public int getBestScore() { return BestScore; }
	
	public Snake getAlpha() {
		return Alpha;
	}
	
	
	
	
	
	
	
	
	
	
	
}
