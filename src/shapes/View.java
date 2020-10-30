package shapes;
import java.awt.Color;

public class View {
	private int screenHeight, screenWidth;
	private int xPadding, yPadding;
	private int oX, oY;
	private int xZoomScale, yZoomScale;
	private int xmin, xmax, ymin, ymax;
	
	/**
	 * Stores the primary display details only
	 * @param sh - screenHeight
	 * @param sw - screenWidth
	 * @param xp - xPadding
	 * @param yp - yPadding
	 * @param ox - xOrigin
	 * @param oy - yOrigin
	 * @param xZ - xZoomScale
	 * @param yZ - yZoomScale
	 */
	public View(int screenHeight, int screenWidth, int xPadding, int yPadding) {
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.xPadding = xPadding;
		this.yPadding = yPadding;
		this.oX = screenWidth/2;
		this.oY = screenHeight/2;
		
		this.xZoomScale = xPadding;
		this.yZoomScale = yPadding;
				
		xmin = -( this.screenWidth/(2*this.xPadding) );
		xmax = ( this.screenWidth/(2*this.xPadding) );
		
		ymin = -( this.screenHeight/(2*this.yPadding) );
		ymax = ( this.screenHeight/(2*this.yPadding) );
	}
	
	/**
	 * 
	 * @return - Returns the screen height.
	 */
	public int getScreenHeight() {
		return this.screenHeight;
	}
	
	/**
	 * 
	 * @return - Returns the screen width
	 */
	public int getScreenWidth() {
		return this.screenWidth;
	}
	
	/**
	 * 
	 * @return - 
	 * Returns the xPadding i.e 
	 * the distance between the x coordinate of centers of two consecutive points on screen  
	 */
	public int getxPadding() {
		return this.xPadding;
	}
	
	/**
	 * 
	 * @return -
	 * Returns the xPadding i.e 
	 * the distance between the x coordinate of centers of two consecutive points on screen
	 */
	public int getyPadding() {
		return this.yPadding;
	}
	
	/**
	 * 
	 * @return - Returns the x coordinate of the origin
	 */
	public int getxOrigin() {
		return this.oX;
	}
	
	/**
	 * 
	 * @return - Returns the y coordinate of the origin
	 */
	public int getyOrigin() {
		return this.oY;
	}
	
	/**
	 * must call in case of zoom in
	 * 
	 * @return - Returns true if scaling was done
	 */
	public boolean scaleup() {
		if((xPadding <= oX) && (yPadding <= oY) ){
			xPadding += xZoomScale;
			yPadding += yZoomScale;
			
			return true;
		}
		return false;
	}
	/**
	 * Must call in case of zoomOut
	 * @returns - Returns true if the scaling was done
	 */
	public boolean scaledown() {
		if((xPadding > xZoomScale) && (yPadding > yZoomScale)) {
			xPadding -= xZoomScale;
			yPadding -= yZoomScale;
			return true;
		}
		
		return false;
	}

	/**
	 * 
	 * @param x - x coordinate of the standard point
	 * @return - Returns the applet x coordinate for the passed standard coordinate
	 */
    public int getAppletX(int x){
    	//return oX + (x*xPadding) - (xPadding/4);
    	return oX + (x*xPadding) - (xPadding/2);
    }
    
    /**
     * 
     * @param y - x coordinate of the standard point
     * @return - Returns the applet x coordinate for the passed standard coordinate
     */
	public int getAppletY(int y){
		//return oY - y*yPadding - (yPadding/4);
		return oY - y*yPadding - (yPadding/2);
	}
	
	/**
	 * 
	 * @param h - Standard Height
	 * @return - Returns the transformed applet height as per the grid size
	 */
	public int transformHeight(int h) {
		//int yUnit = yPadding/2;
		int yUnit = yPadding;
		return h*yUnit;
	}
	
	/**
	 * 
	 * @param w - Standard Width
	 * @return - Returns the transformed applet height as per the grid size
	 */
	public int transformWidth(int w) {
		//int xUnit = xPadding/2;
		int xUnit = xPadding;
		return w*xUnit;
	}
	

	/**
	 * 
	 * @return - Returns the leftmost standard x coordinate that can be drawn as per the current view
	 */
	public int getLeftX() {
		return xmin;
	}
	
	/**
	 * 
	 * @return - Returns the rightmost standard x coordinate that can be drawn as per the current view
	 */
	public int getRightX() {
		return xmax;
	}
	
	/**
	 * 
	 * @return - Returns the lowest standard y coordinate that can be drawn as per the current view
	 */
	public int getBottomY() {
		return ymin;
	}
	
	/**
	 * 
	 * @return - Returns the highest standard y coordinate that can be drawn as per the current view
	 */
	public int getTopY() {
		return ymax;
	}
	
	/**
	 * 
	 * @param x - Standard x coordinate
	 * @param y - Standard y coordinate
	 * @return - Returns true if the point falls in the current view
	 */
	public boolean pointInScreen(int x, int y) {
		if ( (x<xmin) || (x>xmax) || (y>ymax) || (y<ymin)) {
			return false;
		}
		return true;
	}

	/**
	 * Prints all the extreme points of the screen
	 */
	public void printExtremes() {
		System.out.println("Top left: ( "+ this.getLeftX()+ ", "+this.getTopY()+")");
		System.out.println("Bottom left: ( "+ this.getLeftX()+ ", "+this.getBottomY()+")");
		System.out.println("Top right: ( "+ this.getRightX()+ ", "+this.getTopY()+")");
		System.out.println("Bottom right: ( "+ this.getRightX()+ ", "+this.getBottomY()+")");
	}

}
