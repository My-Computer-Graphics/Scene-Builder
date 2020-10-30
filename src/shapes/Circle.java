package shapes;

import java.awt.*;


public class Circle implements Transformation {
	private int r;
	private shapes.Point center;
	
	/**
	 * Initializes the circle with center(0,0) and radius = 0
	 * @param v - Current View Object
	 */
	public Circle(View v) {
		center = new shapes.Point(v);
		this.r = 0;
	}
	
	/**
	 * Initializes the circle with center(0,0) and radius "r"
	 * @param v - Current View Object
	 * @param r - Radius of the circle
	 */
	public Circle(View v, int r) {
		this.r = r;
		center = new shapes.Point(v);
	}
	
	
	/**
	 * Initializes the circle with center(x,y) and radius "r"
	 * @param v - Current View Object
	 * @param r - Radius of the circle
	 * @param x - x coordinate of Center
	 * @param y - y coordinate of Center
	 */
	public Circle(View v, int r, int x, int y) {
		center = new shapes.Point(v, x, y);
		this.r = r;

	}
	
	/**
	 * Checks if the current circle is within the View
	 * @param v - Current View Object
	 * @return - Returns true if the circle is in screen
	 */
	private boolean circleInScreen(View v) {
		if(v.pointInScreen(this.center.getx()+this.r, this.center.gety())
				&& v.pointInScreen(this.center.getx() - this.r, this.center.gety())
				&& v.pointInScreen(this.center.getx(), this.center.gety()+this.r)
				&& v.pointInScreen(this.center.getx(), this.center.gety()-this.r)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param v - Current View Object
	 * @return - Returns a clone of the center
	 */
	public Point getCenter(View v) {
		return this.center.clone(v);
	}
	
	/**
	 * Draws the reflection of the point(x,y) in all the 8 octants
	 * @param g - Current Graphics Object
	 * @param v - Current View Object
	 * @param pixels - Current Pixel Object
	 * @param x - x coordinate of the point which needs to be reflected in all the the 8 octants
	 * @param y - y coordinate of the point which needs to be reflected in all the the 8 octants
	 */
	private void drawInAllOctants(Graphics g, View v, Pixel pixels, int x, int y) {
		int xCenter = center.getx();
		int yCenter = center.gety();
		
		Point pt = new Point(v, xCenter + x,  yCenter + y);
		pt.drawPoint(g, v, pixels);
		
		pt.updatePoint(v,  xCenter + x, yCenter - y);
		pt.drawPoint(g, v, pixels);
		
		pt.updatePoint(v,  xCenter - x,  yCenter + y);
		pt.drawPoint(g, v, pixels);
		
		pt.updatePoint(v, xCenter - x, yCenter - y);
		pt.drawPoint(g, v, pixels);
		
		pt.updatePoint(v, xCenter + y, yCenter + x);
		pt.drawPoint(g, v, pixels);
		
		pt.updatePoint(v, xCenter - y, yCenter + x);
		pt.drawPoint(g, v, pixels);
		
		pt.updatePoint(v, xCenter + y, yCenter - x);
		pt.drawPoint(g, v, pixels);
		
		pt.updatePoint(v, xCenter - y, yCenter - x);
		pt.drawPoint(g, v, pixels);
		
	}

	public boolean drawDotted(Graphics g, View v, Pixel pixels) {
		if(!this.circleInScreen(v)) {
			System.out.println("Circle out of screen");
			return false;
		}
		
		int xi,yi;
		double p;
		
		xi= 0;
		yi= this.r;
		p=(5.0/4.0)-(double)this.r;
		
		boolean skipPoint=false;
		
		int count = 0;
		while(xi<= yi) {
			
			count++;
			
			if((count%3)==0) {
				skipPoint = true; 
			}
			else {
				skipPoint = false;
			}
			
			if(!skipPoint) {
				this.drawInAllOctants(g, v, pixels, xi, yi);
			}
						
			xi++;
			
			if(p<0) {
				p += (double)( 2*xi + 3 );
			}
			else {
				yi--;
				p += (double)( 2*(xi-yi) + 1);
			}
		}
		
		return true;
	}
	
	/**
	 * Draws the current circle object on the applet screen provided it falls within the
	 * bounds set by the current view object.
	 * Updates the pixel array object with rgb pixel values of the colour by which the circle
	 * boundary is drawn.
	 * @param g - Current Graphics Object
	 * @param v - Current View Object
	 * @param pixels - Current Pixel Object
	 * @return - Returns true if the shape can be drawn
	 */
	public boolean draw(Graphics g, View v, Pixel pixels) {
		if(!this.circleInScreen(v)) {
			System.out.println("Circle out of screen");
			return false;
		}
		
		int xi,yi;
		double p;
		
		xi= 0;
		yi= this.r;
		p=(5.0/4.0)-(double)this.r;
		
		while(xi<= yi) {
			this.drawInAllOctants(g, v, pixels, xi, yi);
			
			xi++;
			
			if(p<0) {
				p += (double)( 2*xi + 3 );
			}
			else {
				yi--;
				p += (double)( 2*(xi-yi) + 1);
			}
		}
		return true;
	}

	/**
	 * Helper method
	 * Fills the current circle in the screen using the four points boundary fill algorithm
	 * @param g - Current Graphics Object
	 * @param v - Current View Object
	 * @param pixels - Current Pixel Object
	 * @param interiorPoint - A point object within the circle boundary
	 * @param boundaryColor - Applet color object used to draw the circle boundary 
	 * @param interiorColor - Applet color object to fill the interior
	 */
	private void fill4Points(Graphics g, View v, Pixel pixels, Point interiorPoint, 
			Color boundaryColor, Color interiorColor) {
		int x, y;
		x = interiorPoint.getx();
		y = interiorPoint.gety();
		
		//end of screen reached
		if(v.pointInScreen(x, y) == false ) {
			return;
		}
		
	
		if( (pixels.getPixel(x, y) != boundaryColor.getRGB() )
				&& ( pixels.getPixel(x, y) != interiorColor.getRGB() ) ) {
			interiorPoint.drawPoint(g, v, pixels);
			
			Point p[] = new Point[4];
			
			p[0] = new Point(v, x-1, y);
			this.fill4Points(g, v, pixels, p[0], boundaryColor, interiorColor);
			
			
			p[1] = new Point(v, x, y+1);
			this.fill4Points(g, v, pixels, p[1], boundaryColor, interiorColor);
		
			p[2] = new Point(v, x+1, y);
			this.fill4Points(g, v, pixels, p[2], boundaryColor, interiorColor);
			
			p[3] = new Point(v, x, y-1);
			this.fill4Points(g, v, pixels, p[3], boundaryColor, interiorColor);
			
			return;
		}
		
	}
	
	/**
	 * Fills the current circle in the screen using the four points boundary fill algorithm
	 * @param g - Current Graphics Object
	 * @param v - Current View Object
	 * @param pixels - Current Pixel Object
	 * @param interiorPoint - A point object within the circle boundary
	 * @param boundaryColor - Applet color object used to draw the circle boundary 
	 * @param interiorColor - Applet color object to fill the interior
	 */
	public void fill(Graphics g, View v, Pixel pixels, Point interiorPoint, 
			Color boundaryColor, Color interiorColor) {
		g.setColor(interiorColor);
		
		this.fill4Points(g, v, pixels, interiorPoint, boundaryColor, interiorColor);
	}

	/**
	 * Updates the circle with the new center coordinates provided. 
	 * @param v - Current View Object
	 * @param xCenter - x coordinate for the new center
	 * @param yCenter - y coordinate for the new center
	 * @param r - New radius
	 * @return - Returns true if the circle is updated.
	 */
	public boolean updateCircle(View v, int xCenter, int yCenter, int r) {
		shapes.Point oldCenter = new shapes.Point(v, this.center);
		int oldR = this.r;
		this.center.updatePoint(v, xCenter, yCenter);
		this.r = r;
		if(this.circleInScreen(v)) {
				return true;
		}
		
		this.center.updatePoint(v, oldCenter);
		this.r  = oldR;
		
		return false;
	}

	@Override
	public boolean scale(View v, Point pivotPoint, int scaleFactorX, int scaleFactorY, boolean scaleUp) {
		if(scaleFactorX == scaleFactorY) {
			int tempRadius = this.r;
			
			shapes.Point prevCenter = this.center.clone(v);
			
			if(!this.center.scale(v, pivotPoint, scaleFactorX, scaleFactorY, scaleUp)) {
				return false;
			}
			
			if(scaleUp) {
				this.r *= scaleFactorX;
			}
			else {
				this.r /= scaleFactorX;
			}
			if(this.circleInScreen(v)) {
				return true;
			}
			else {
				System.out.println("Scaling draws circle out of the screen");
				this.r = tempRadius;
				this.center = prevCenter.clone(v);
				return false;
			}
		}
		System.out.println("Non Uniform scaling is not allowed in case of circle");
		return false;
	}

	@Override
	public boolean translate(View v, int xMoveBy, int yMoveBy) {
		shapes.Point temp = new shapes.Point(v, this.center);
		if(!this.center.translate(v, xMoveBy, yMoveBy)) {
			return false;
		}
		
		if(this.circleInScreen(v)) {
			return true;
		}
		
		this.center = temp;
		return false;
	}

	@Override
	public boolean rotate(View v, Point pivotPoint, double theta) {
		shapes.Point prevCenter = new shapes.Point(v, this.center);
		this.center.rotate(v, pivotPoint, theta);
		if(this.circleInScreen(v)) {
			return true;
		}
		this.center = prevCenter;
		return false;
	}
}
