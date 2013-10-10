package helpAndEnums;

/**
 * AffineTransformationDoubleMatrix is a doublematrix that has special functions 
 * for the basic affine transformations (translate, rotate, scale and shear).
 *
 * @author Mikko Hilpinen.
 *         Created 10.10.2013.
 * @deprecated This class should not be used. Use AffineTransform instead
 */
@Deprecated
public class AffineMatrix extends DoubleMatrix
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new matrix with no transformations added
	 */
	public AffineMatrix()
	{
		super(3, 3);
		makeIdentifierMatrix();
	}
	
	
	// STATIC METHODS	-------------------------------------------------
	
	/**
	 * Multiplies two affine matrices together and returns the multiplication
	 *
	 * @param first The first affineMatrix
	 * @param second The second affineMatrix
	 * @return The multiplication of the two matrices
	 */
	public static AffineMatrix getMultiplicationMatrix(AffineMatrix first, 
			AffineMatrix second)
	{
		return (AffineMatrix) getMultiplicationMatrix(first, 
				(DoubleMatrix) second);
	}
	
	/**
	 * Returns an affine transformation matrix that translates points
	 *
	 * @param htrans The horizontal translation
	 * @param vtrans The vertical translation
	 * @return The matrix with the given translations
	 */
	public static AffineMatrix getTranslationMatrix(
			double htrans, double vtrans)
	{
		// Creates the matrix
		AffineMatrix newmatrix = 
				new AffineMatrix();
		newmatrix.setValue(2, 0, htrans);
		newmatrix.setValue(2, 1, vtrans);
		
		return newmatrix;
	}
	
	/**
	 * Returns an affine transformation matrix that scales points with the given 
	 * values
	 *
	 * @param hscale The horizontal scaling
	 * @param vscale The vertical scaling
	 * @return The matrix with the given scaling
	 */
	public static AffineMatrix getScalingMatrix(double hscale, double vscale)
	{
		// Creates the matrix
		AffineMatrix newmatrix = 
				new AffineMatrix();
		newmatrix.setValue(0, 0, hscale);
		newmatrix.setValue(1, 1, vscale);
		
		return newmatrix;
	}
	
	/**
	 * Translates the given affinematrix horizontally and / or vertically
	 *
	 * @param m The matrix that will be translated
	 * @param htrans The horizontal translation
	 * @param vtrans The vertical translation
	 * @return The new translated matrix
	 */
	public static AffineMatrix translateMatrix(AffineMatrix m, double htrans, 
			double vtrans)
	{
		return getMultiplicationMatrix(m, getTranslationMatrix(htrans, vtrans));
	}
}
