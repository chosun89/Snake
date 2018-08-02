package neuralnet;

import java.io.Serializable;

import main.Const;

public class NeuralNetwork implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5284113230493646192L;
	/**
	 * 
	 */
	private int inputLayers;
	private int hiddenLayers;
	private int outputLayers;
	
	Matrix ih;
	Matrix ho;

	public NeuralNetwork (int i, int h, int o) {
		inputLayers = i;
		hiddenLayers = h;
		outputLayers = o;
		
		ih = new Matrix (hiddenLayers, inputLayers + 1); // m x (n + 1)
		ho = new Matrix (outputLayers, hiddenLayers + 1); // 4 x (m + 1)
		
		ih.random();
		ho.random();
	}

	// main function passes input through neural network 
	public float[] getOutput(float inputarr[]) {
		if (inputarr.length != inputLayers) throw new RuntimeException("invalid input dimension");
		float res [] = new float[Const.OUTPUT_LAYERS]; // 1 x n
		Matrix input = Matrix.toColumnMatrix(inputarr); // n x 1
		Matrix inputbias = input.addBias(); // [(n + 1) x 1]
		
		Matrix middleoutput = ih.mul(inputbias); // [m x (n + 1)] * [(n + 1) x 1]
		middleoutput = middleoutput.activation().addBias(); // (m + 1) x 1

		Matrix output = ho.mul(middleoutput); // [4 x (m + 1)] * [(m + 1) x 1]
		res = output.toArray(); // 4 x 1 -> 1 x 4

		return res;
	}
	
	public NeuralNetwork crossOver(NeuralNetwork mate) {
		NeuralNetwork res = new NeuralNetwork(inputLayers, hiddenLayers, outputLayers);
		
		res.ih = this.ih.crossOver(mate.ih);
		res.ho = this.ho.crossOver(mate.ho);
		
		return res;
	}

	public void randomizeLayers() {
		ih = ih.random();
		ho = ho.random();
	}

	public NeuralNetwork clone() {
		NeuralNetwork res = new NeuralNetwork(inputLayers, hiddenLayers, outputLayers);
		res.ih = this.ih.clone();
		res.ho = this.ih.clone();

		return res;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
