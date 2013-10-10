package helpAndEnums;

/**
 * Matrix class handles some of the basic matrix functions and represents 
 * data in a matrix format
 *
 * @author Mikko Hilpinen.
 *         Created 19.9.2013.
 * @deprecated This class is no longer supported or updated.
 */
@Deprecated
public class DoubleMatrix
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private double[][] matrix;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new doublematrix with the given size. All the values are 
	 * initialized as 0;
	 *
	 * @param xsize How many instances the matrix holds horizontally
	 * @param ysize How many instances the matrix holds vertically
	 */
	public DoubleMatrix(int xsize, int ysize)
	{
		// Initializes attributes
		this.matrix = new double[xsize][ysize];
		
		for (int x = 0; x < xsize; x++)
		{
			for (int y = 0; y < ysize; y++)
			{
				this.matrix[x][y] = 0;
			}
		}
	}
	
	/**
	 * A copy constructor for the doublematrix class. Makes the new matrix a 
	 * copy of the given matrix.
	 *
	 * @param other The doublematrix which will be copied
	 */
	public DoubleMatrix(DoubleMatrix other)
	{
		// Copies the values from the other matrix
		this.matrix = new double[other.getXSize()][other.getYSize()];
		
		for (int x = 0; x < other.getXSize(); x++)
		{
			for (int y = 0; y < other.getYSize(); y++)
			{
				this.matrix[x][y] = other.getValue(x, y);
			}
		}
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * Changes the value in a certain position in the matrix
	 *
	 * @param xindex The horizontal index of the new value (starts from 0)
	 * @param yindex The vertical index of the new value (starts from 0)
	 * @param value The new value in the matrix
	 */
	public void setValue(int xindex, int yindex, double value)
	{
		// Checks the indexes
		if (xindex < 0 || yindex < 0 || xindex >= this.matrix.length || 
				yindex >= this.matrix[0].length)
			return;
		
		// Changes the value
		this.matrix[xindex][yindex] = value;
	}
	
	/**
	 * Returns a value from a certain position in the matrix
	 *
	 * @param xindex The horizontal index of the value (starts from 0)
	 * @param yindex The vertical index of the value (starts from 0)
	 * @return The value from the given index
	 */
	public double getValue(int xindex, int yindex)
	{
		// Checks the indexes
		if (xindex < 0 || yindex < 0 || xindex >= getXSize() || 
				yindex >= getYSize())
			return 0;
		
		// Returns the value
		return this.matrix[xindex][yindex];
	}
	
	/**
	 * @return the horizontal size of the matrix
	 */
	public int getXSize()
	{
		return this.matrix.length;
	}
	
	/**
	 * @return the horizontal size of the matrix
	 */
	public int getYSize()
	{
		if (getXSize() >= 0)
			return this.matrix[0].length;
		return 0;
	}
	
	/**
	 * Makes the matrix an identifier matrix. Doesn't change the values ofther 
	 * than the ones in the center.
	 * 
	 * @warning The matrix needs to have equal width and height.
	 */
	protected void makeIdentifierMatrix()
	{
		// Doesn't work with matrices that don't have egual width and height
		if (getXSize() != getYSize())
			return;
		
		// Adds the identifier
		for (int i = 0; i < getXSize(); i++)
		{
			setValue(i, i, 1);
		}
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Calculates and returns the multiplication matrix of two double matrices
	 *
	 * @param first The first matrix
	 * @param second The second matrix
	 * @return The multiplication of the two matrices
	 */
	public static DoubleMatrix getMultiplicationMatrix(DoubleMatrix first, 
			DoubleMatrix second)
	{
		// Checks the arguments
		if (first.getXSize() != second.getYSize())
		{
			System.err.println("Failed to multiply the matrixes since their " +
					"sizes are wrong. XSize of the first (" + 
					first.getXSize() + ") should be equal to the YSize of " +
					"the second (" + second.getYSize() + ")!");
			return null;
		}
		
		// Creates the new empty matrix
		DoubleMatrix newMatrix = new DoubleMatrix(second.getXSize(), 
				first.getYSize());
		
		// Goes through all horizontal lines in the first matrix
		for (int y1 = 0; y1 < first.getYSize(); y1++)
		{	
			// Goes through each of the vertical lines in the second matrix
			for (int x2 = 0; x2 < second.getXSize(); x2++)
			{
				double value = 0;
				
				// Goes through the indexes in the current lines and adds them 
				// together
				for (int i = 0; i < first.getXSize(); i++)
				{
					value += first.getValue(i, y1) * second.getValue(x2, i);
				}
				
				// Adds the value to the new matrix
				newMatrix.setValue(x2, y1, value);
			}
		}
		
		return newMatrix;
	}
	
	/**
	 * Creates and returns a new identifier matrix witht eh given size
	 *
	 * @param size The size of the matrix (the matrix will be size * size large)
	 * @return A new identifier matrix with the given size
	 */
	public static DoubleMatrix getIdentifierMatrix(int size)
	{
		// Creates a new empty matrix
		DoubleMatrix newmatrix = new DoubleMatrix(size, size);
		
		// Adds the identifier
		newmatrix.makeIdentifierMatrix();
		
		return newmatrix;
	}
}
