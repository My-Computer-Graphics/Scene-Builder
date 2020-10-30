package shapes;

public interface Transformation {
	public boolean scale(View v, Point pivotPoint, int scaleFactorX, int scaleFactorY, boolean scaleUp);
	public boolean translate(View v, int xMoveBy, int yMoveBy);
	public boolean rotate(View v, Point pivotPoint, double theta);
}
