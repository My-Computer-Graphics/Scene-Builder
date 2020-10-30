package shapes;
import java.awt.Graphics;

public class Point implements Transformation{
	private int x, y;
	
	/**
	 * Initializes a point on origin 
	 * @param v - Current View object
	 */
	public Point(View v) {
		this.x = 0;
		this.y = 0;
	}
	
	/**
	 * Initializes a point at the coordinate (x,y)
	 * 
	 * @param v - Current View object
	 * @param x - x coordinate(standard)
	 * @param y - y coordinate(standard) 
	 */
	public Point(View v, int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * 
	 * @param v - Current View object
	 * @param p - Point object
	 */
	public Point(View v, Point p) {
		this.x = p.getx();
		this.y = p.gety();
	}
	
	/**
	 * @return - Returns the x coordinate of the point
	 */
	public int getx() {
		return x;
	}
	
	/**
	 * 
	 * @return - Returns the y coordinate of the point
	 */
	public int gety() {
		return y;
	}
	
	
	/**
	 * Draws the point on the applet screen using the graphics object.
	 * Updates the pixel value with the rgb value at that point
	 * @param g - Current Graphics object
	 * @param v - Current View object
	 * @param p - Current Pixel object
	 * 
	 * @return - Returns true if point is drawn i.e. falls within the bound
	 *  set by the current view object passed.
	 */
	public boolean drawPoint(Graphics g, View v, Pixel pixels) {
		if(v.pointInScreen(x, y)) {
			g.fillRect(v.getAppletX(x), v.getAppletY(y), v.transformWidth(1),v.transformHeight(1));
			pixels.setPixel(g.getColor(), x, y);
			return true;
		}
		else {
			System.out.println("Point out of screen");
			return false;
		}
	}
	
	/**
	 * Creates and returns a clone of the current point object
	 * @param v - Current view Object
	 * @return - Returns a new point object which is a copy of the current point
	 */
	public Point clone(View v) {
		shapes.Point clonePoint = new shapes.Point(v, this);
		return clonePoint;
	}
	
	/**
	 * Updates the point coordinates with the new coordinates
	 * This method must be called when scaling the view up or down
	 * @param v - Current View Object
	 * @param x - new x coordinate
	 * @param y - new y coordinate
	 * 
	 * @return
	 * Returns true if point updated i.e. point in screen.
	 * Returns false if the point couldn't be updated i.e. out of the screen
	 */
	public boolean updatePoint(View v, int x, int y) {
		if(v.pointInScreen(x, y)) {
			this.x = x;
			this.y = y;
			return true;
		}
		
		return false;
	}
	
	/**
	 * Updates the point coordinates with the new coordinates
	 * This method must be called when scaling the view up or down
	 * @param v - Current View Object
	 * @param newPoint - new Point object
	 * 
	 * @return
	 * Returns true if point updated i.e. point in screen.
	 * Returns false if the point couldn't be updated i.e. out of the screen
	 */
	public boolean updatePoint(View v, Point newPoint) {
		if(v.pointInScreen(newPoint.getx(), newPoint.gety())) {
			this.x = newPoint.getx();
			this.y = newPoint.gety();
			return true;
		}
		
		return false;
	}


	@Override
	public boolean scale(View v, Point pivotPoint, int scaleFactorX, int scaleFactorY, boolean scaleUp) {
		int tx, ty, xPivot, yPivot;
		xPivot = pivotPoint.getx();
		yPivot = pivotPoint.gety();
		
		if(scaleUp) {
			tx = xPivot + (this.x-xPivot)*scaleFactorX;
			ty = yPivot + (this.y-yPivot)*scaleFactorY;
		}
		else {
			tx = xPivot + (this.x-xPivot)/scaleFactorX;
			ty = yPivot + (this.y-yPivot)/scaleFactorY;
		}
		
		if(v.pointInScreen(tx, ty)) {
			this.x = tx;
			this.y = ty;
			return true;
		}
		
		return false;
	}

	@Override
	public boolean translate(View v, int xMoveBy, int yMoveBy) {
		if(v.pointInScreen(this.x+xMoveBy,this.y+ yMoveBy)) {
			this.x += xMoveBy;
			this.y += yMoveBy;
			return true;
		}
		
		return false;
	}

	@Override
	public boolean rotate(View v, Point pivotPoint, double theta) {
		double cosTheta, sinTheta;
		cosTheta = Math.cos(Math.toRadians(theta));
		sinTheta = Math.sin(Math.toRadians(theta));
		
		int tx, ty, xPivot, yPivot;
		xPivot = pivotPoint.getx();
		yPivot = pivotPoint.gety();
		
		tx = xPivot + (int) ( ( (double)(this.x -xPivot)*cosTheta ) - ((double)(y-yPivot)*sinTheta ) );
		ty = yPivot + (int)( ( (double)(this.x -xPivot)*sinTheta) + ( (double)(y-yPivot)*cosTheta ) );
		
		if(v.pointInScreen(tx, ty)){
			this.x = tx;
			this.y = ty;
			return true;
		}
		
		return false;
	}

	
}

