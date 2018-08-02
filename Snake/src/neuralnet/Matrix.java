package neuralnet;
import java.io.Serializable;
import java.util.Random;

public class Matrix implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3592842365922491908L;
	private int rows;
	private int cols;
	private float matrix[][];

	public Matrix(int r, int c) {
		rows = r;
		cols = c;
		matrix = new float[r][c];
	}
	
	public Matrix(float m [][]) {

		rows = m.length;
		cols = m[0].length;
		matrix = new float [rows][cols];
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) 
				matrix[i][j] = m[i][j];
	}

	public Matrix (Matrix rhs) {
		this.rows = rhs.rows;
		this.cols = rhs.cols;
		this.matrix = new float[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				this.matrix [i][j] = rhs.matrix[i][j];
			}
		}
	}
	
	public Matrix add(Matrix rhs) {
		if(this.rows != rhs.rows || this.cols != rhs.cols) throw new RuntimeException("illegal dimensions -- addition");
		Matrix res = new Matrix(rows, cols);
		for (int i = 0; i < rows; i++) 
			for (int j = 0; j < cols; j++)
				res.matrix[i][j] = matrix[i][j] + rhs.matrix[i][j];
		return res;
	}
	
	// matrix multiplication 
	public Matrix mul(Matrix rhs) {
		if (this.cols != rhs.rows) throw new RuntimeException("illegal dimensions -- multiplication");
		
		Matrix res = new Matrix(this.rows, rhs.cols);
		int j;
		for (int i = 0; i < rows; i++) {
			for (j = 0; j < rhs.cols; j++) {
				for (int k = 0; k < cols; k++) {
					res.matrix[i][j] += matrix[i][k] * rhs.matrix[k][j];
				}
			}
		}
		return res;
	}
	
	public Matrix subtract(Matrix rhs) {
		if(this.rows != rhs.rows || this.cols != rhs.cols) throw new RuntimeException("illegal dimensions -- addition");
		Matrix res = new Matrix(rows, cols);
		for (int i = 0; i < rows; i++) 
			for (int j = 0; j < cols; j++)
				res.matrix[i][j] = matrix[i][j] - rhs.matrix[i][j];
		return res;
	}

	public Matrix scale(float k) {
		Matrix res = new Matrix(rows, cols);
		for (int i = 0; i < rows; i++) 
			for (int j = 0; j < cols; j++)
				res.matrix[i][j] = k*matrix[i][j];
		return res;
	}
	
	public Matrix transpose () {
		Matrix res = new Matrix(cols, rows);
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) {
				res.matrix[j][i] = matrix[i][j];
			}
		return res;
	}

	private float sigmoid(float x, boolean derivative) {
		if (derivative)
			return sigmoid(x, false) * (1 - sigmoid(x, false));
		else {
			return (float)(1.0/(1.0 + Math.exp(-x)));
		}
	}

	public Matrix activation() {
		Matrix res = new Matrix(rows, cols);
		for (int i = 0; i < rows; i++) 
			for (int j = 0; j < cols; j++) 
				res.matrix[i][j] = sigmoid(matrix[i][j], false);
		return res;
	}
	
	public Matrix random() {
		Matrix res = new Matrix(rows, cols);
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				res.matrix[i][j] = (float)Math.random();
		return res;
	}

	public Matrix crossOver(Matrix rhs) {
		Matrix child = new Matrix(rows, cols);
		Random rand = new Random();
		int randrow = rand.nextInt(rows);
		int randcol = rand.nextInt(cols);
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (i <= randrow && j <= randcol)
					child.matrix[i][j] = matrix[i][j];
				else child.matrix[i][j] = rhs.matrix[i][j];
			}
		}
		return child;
	}

	private void swap (int i, int j) {
		float temp[] = matrix[i];
		matrix[i] = matrix[j];
		matrix[j] = temp;
	}

	public Matrix solve(Matrix rhs) {
		if (cols != rows || rhs.rows != rows || rhs.cols != 1) throw new RuntimeException ("invalid dimensions -- solve");
		Matrix x = new Matrix(cols, 1);
		Matrix A = new Matrix(this);
		Matrix b = new Matrix(rhs);

		// Gauss elim, lower triangle
		for (int i = 0; i < rows; i++) {
			int idx = i;
			for (int j = i + 1; j < rows; j++) 
				if (Math.abs(A.matrix[idx][i]) < Math.abs(A.matrix[j][i]))
					idx = j;

			A.swap(i, idx);
			b.swap(i, idx);
			
			for (int j = i + 1; j < rows; j++) {
				float m = A.matrix[j][i] / A.matrix[i][i];
				for (int k = i; k < cols; k++) {
					A.matrix[j][k] -= m * A.matrix[i][k];
				}
				b.matrix[j][0] -= m * b.matrix[i][0];
			}
		}
		// back sub, upper-tri
		for (int i = rows - 1; i >= 0; i--) {
			for (int j = i + 1; j < cols; j++) 
					b.matrix[i][0] -= x.matrix[j][0] * A.matrix[i][j];
			x.matrix[i][0] = (b.matrix[i][0])/A.matrix[i][i];
		}
		return x;
	}
	
	public float[] toArray() {
		float res[] = new float[cols * rows];
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) 
				res[i*cols + j] = matrix[i][j];

		return res;
	}

	public Matrix addBias() {
		if (cols != 1 ) throw new RuntimeException ("Can only add bias to a column matrix");
		Matrix res = new Matrix(rows + 1, 1);
		for (int i = 0; i < rows; i++)
			res.matrix[i][0] = matrix[i][0];
		res.matrix[rows][0] = 1;
		return res;
	}

	public Matrix removeLastRow() {
		Matrix res = new Matrix (rows - 1, cols);
		for (int i = 0; i < rows - 1; i++)
			for (int j = 0; j < cols; j++)
				res.matrix[i][j] = matrix[i][j];
		return res;
	}

	public static Matrix toColumnMatrix(float vec []) {
		Matrix res = new Matrix(vec.length, 1);
		for (int i = 0; i < vec.length; i++)
			res.matrix[i][0] = vec[i];
		return res;
	}

	public void mutate(float mutationrate) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Random rand = new Random();
				if (rand.nextDouble() < mutationrate) { // if within mutation rate
					matrix[i][j] += rand.nextGaussian()/5; // rescale to 1/5 normal dist.
				}
				if (matrix[i][j] > 1) matrix[i][j] = 1;
				else if (matrix[i][j] < -1) matrix [i][j] = -1;
			}
		}
	}

	public Matrix clone() {
		Matrix res = new Matrix(rows, cols);
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				res.matrix[i][j] = matrix[i][j];
		return res;
	}
	
	void print() {
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < cols; j++)
				System.out.print(matrix[i][j] + " ");
			System.out.println();
		}
	}
	
}
