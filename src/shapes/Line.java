package shapes;
import java.awt.Graphics;

public class Line implements Transformation{
	private shapes.Point p1, p2;
	private Point point;
	
	/**
	 * Initializes a line with the supplied standard coordinates
	 * @param v - Current View object
	 * @param x1 - Standard x coordinate of first point
	 * @param y1 - Standard y coordinate of first point
	 * @param x2 - Standard x coordinate of second point
	 * @param y2 - Standard y coordinate of second point
	 */
	public Line(View v, int x1, int y1, int x2, int y2){
		this.p1 = new shapes.Point(v, x1, y1);
		this.p2 = new shapes.Point(v, x2, y2);
		point = new Point(v);
	}
	
	/**
	 * Initializes a line with the same coordinates as that of the reference Line
	 * @param v - Current View object
	 * @param referenceLine - A reference Line Object
	 * 
	 */
	public Line(View v, Line referenceLine){
		this.p1 = new shapes.Point(v, referenceLine.p1);
		this.p2 = new shapes.Point(v, referenceLine.p2);
		point = new Point(v);
	}
	
	/**
	 * 
	 * @param v - Current View Object
	 * @return - Return the clone of the first point of the line
	 */
	public Point getPoint1(View v) {
		return p1.clone(v);
	}
	
	/**
	 * 
	 * @param v - Current View Object
	 * @return - Return the clone of the second point of the line
	 */
	public Point getPoint2(View v) {
		return p2.clone(v);
	}
	
	/**
	 * Draws a line between the points using the DDA algorithm
	 * @param g - Graphics object
	 * @param v - Current view object
	 * @param p - Current pixel object
	 * @return - Returns true if line is successfully drawn otherwise false
	 */
	public boolean drawLineDDA(Graphics g, View v, Pixel pixels) {
		int dx, dy, steps;
		double xi, yi, xinc, yinc;
		
		dx = this.p2.getx() - this.p1.getx();
		dy = this.p2.gety() - this.p1.gety();
	
		int dxAbs = Math.abs(dx);
		int dyAbs = Math.abs(dy); 
		
		if( dxAbs > dyAbs ) {
			steps = dxAbs;
		}
		else {
			steps = dyAbs;
		}
		
		xinc = (double)dx / (double)steps;
		yinc = (double)dy / (double)steps;
		
		xi= (double)p1.getx();
		yi= (double)p1.gety();
		
		this.point.updatePoint(v, this.p1);
		if(!this.point.drawPoint(g,v, pixels)) {
			return false;
		}
		
		for(int i=0; i<steps; i++) {
			xi += xinc;
			yi += yinc;
			
			point.updatePoint(v, (int)Math.floor(xi), (int)Math.floor(yi));
			if(!this.point.drawPoint(g,v, pixels)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Supporting method for the Bresenham line drawing algorithm
	 * @param g - Current Graphics object
	 * @param v - Current View object 
	 * @param p - Current Pixel object
	 * @param x1 - left x
	 * @param y1 - left y
	 * @param x2 - right x
	 * @param y2 - right y
	 * @return - Returns true if line is successfully drawn otherwise false
	 */
	private boolean plotLineLow(Graphics g, View v, Pixel pixels, int x1, int y1, int x2, int y2) {
	    int dx = x2 - x1;
	    int dy = y2 - y1;
	    int yinc = 1;
	    int pi;
	    int pxi, pyi;
	    
	    int i1, i2;
	    
	    if(dy < 0) {
	    	yinc = -1;
	    	dy *= -1;
	    }
	    
	    i1 = 2*dx;
	    i2 = 2*dy;
	    
	    pi = 2*dy - dx;
	    pyi = y1;

	    for(pxi = x1; pxi<= x2; pxi++) {
	    	point.updatePoint(v, pxi, pyi);
	    	if(!this.point.drawPoint(g,v, pixels)) {
				return false;
			}
	    	if(pi > 0) {
	    		pyi += yinc;
	    		pi -= i1; 
	    	}
	    	pi += i2;
	    }
	    return true;
	}
	
	/**
	 * Supporting method for the Bresenham line drawing algorithm
	 * @param g - Current Graphics object
	 * @param v - Current View object 
	 * @param p - Current Pixel object
	 * @param x1 - left x
	 * @param y1 - left y
	 * @param x2 - right x
	 * @param y2 - right y
	 * @return - Returns true if line is successfully drawn otherwise false
	 */
	private boolean plotLineHigh(Graphics g, View v, Pixel pixels, int x1, int y1, int x2, int y2) {
	    int dx = x2 - x1;
	    int dy = y2 - y1;
	    int xinc = 1;
	    int pi;
	    int pxi, pyi;
	    
	    int i1, i2;
	    
	    if(dx < 0) {
	    	xinc = -1;
	    	dx *= -1;
	    }
	    
	    i1 = 2*dx;
	    i2 = 2*dy;
	    
	    pi = 2*dx - dy;
	    pxi = x1;

	    for(pyi = y1; pyi<= y2; pyi++) {
	    	point.updatePoint(v, pxi, pyi);
	    	if(!this.point.drawPoint(g,v, pixels)) {
				return false;
			}
	    	
	    	if(pi > 0) {
	    		pxi += xinc;
	    		pi -= i2; 
	    	}
	    	pi += i1;
	    }
	    return true;
	}

	/**
	 * Draw line using the standard Bresenham Algorithm
	 * @param g - Current Graphics object
	 * @param v - Current View object 
	 * @param p - Current Pixel object
	 * @return - Returns true if line is successfully drawn otherwise false
	 */
	public boolean drawLineBresenham(Graphics g, View v, Pixel pixels) {
		int dy,dx;
		
		dx = this.p2.getx() - this.p1.getx();
		dy = this.p2.gety() - this.p1.gety();
	
		if( Math.abs(dy) < Math.abs(dx)) {
			if(p1.getx()>this.p2.getx()) {
				return this.plotLineLow(g, v, pixels, p2.getx(), p2.gety(), p1.getx(), p1.gety());
			}
			else {
				return this.plotLineLow(g, v, pixels, p1.getx(), p1.gety(), p2.getx(), p2.gety());
			}
		}
		else {
			if(this.p1.gety()>this.p2.gety()) {
				return this.plotLineHigh(g, v, pixels, p2.getx(), p2.gety(), p1.getx(), p1.gety());
			}
			else {
				return this.plotLineHigh(g, v, pixels, p1.getx(), p1.gety(), p2.getx(), p2.gety());
			}
		}
	}

	/**
	 * Draw line using the standard Bresenham Midpoint Algorithm
	 * @param g - Current Graphics object
	 * @param v - Current View object 
	 * @param p - Current Pixel object
	 * @return - Returns true if line is successfully drawn otherwise false
	 */
	public boolean drawMidpointLine(Graphics g, View v, Pixel pixels) {
		double m, pi;
		int pxi, pyi;
		int yinc;
		
		m = (double)(this.p2.gety() - this.p1.gety()) / (double)(this.p2.getx()-this.p1.getx());
		
		if(m<0) {
			yinc = -1;
			m *= -1;
		}
		else {
			yinc = 1;
		}
		
		if(this.p1.getx() > this.p2.getx()) {
			shapes.Point temp = new shapes.Point(v, this.p1);
			this.p1.updatePoint(v, p2);
			this.p2.updatePoint(v, temp);
		}
		
		
		pxi = this.p1.getx();
		pyi = this.p1.gety();
		
		if(m<1) {
			pi = 1 - m;
			
			for(pxi = this.p1.getx(); pxi <= this.p2.getx(); pxi++) {
				this.point.updatePoint(v, pxi, pyi);
				if(!this.point.drawPoint(g,v, pixels)) {
					return false;
				}
				if(pi>=0) {
					pi -= m;
				}
				else {
					pyi += yinc;
					pi += (1-m);
				}
			}
		}
		else {
			pi = 1 - m/2;
			
			if(this.p1.gety() <= this.p2.gety()) {
				for(pyi = this.p1.gety(); pyi<=this.p2.gety(); pyi+=yinc) {
					this.point.updatePoint(v, pxi, pyi);
					if(!this.point.drawPoint(g,v, pixels)) {
						return false;
					}
					
					if(pi>=0) {
						pi += (1-m);
					}
					else {
						pxi ++;
						pi += 1;
					}
				}
			}
			
			else {
				for(pyi = this.p1.gety(); pyi>=this.p2.gety(); pyi+=yinc) {
					this.point.updatePoint(v, pxi, pyi);
					if(!this.point.drawPoint(g,v, pixels)) {
						return false;
					}
					
					if(pi>=0) {
						pi -= m;
					}
					else {
						pxi ++;
						pi += (1-m);
					}
				}
			}
			
		}
		this.point.updatePoint(v, this.p2);
		if(!this.point.drawPoint(g,v, pixels)) {
			return false;
		}
		return true;
	}

	/**
	 * Updates the current line with the new coordinates supplied
	 * @param v - Current View Object
	 * @param x1 - New x coordinate of Point 1
	 * @param y1 - New y coordinate of Point 1
	 * @param x2 - New x coordinate of Point 2
	 * @param y2 - New x coordinate of Point 2
	 * @return - Returns true if the line is successfully updated
	 */
	public boolean updateLine(View v, int x1, int y1, int x2, int y2) {
		if(v.pointInScreen(x1, y1) && v.pointInScreen(x2, y2)) {
			this.p1.updatePoint(v, x1, y1);
			this.p2.updatePoint(v, x2, y2);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Updates the current line with the new points supplied
	 * @param v - Current View Object
	 * @param p1 - New Point 1 Object
	 * @param p2 - New Point 2 Object
	 * @return - Returns true if the line is successfully updated
	 */
	public boolean updateLine(View v, Point p1, Point p2) {
		if(v.pointInScreen(p1.getx(), p1.gety()) && v.pointInScreen(p2.getx(), p2.gety())) {
			this.p1.updatePoint(v, p1);
			this.p2.updatePoint(v, p2);
			this.point.updatePoint(v, 0, 0);
			return true;
		}
		
		return false;
	}
	

	@Override
	public boolean scale(View v, Point pivotPoint,int scaleFactorX, int scaleFactorY, boolean scaleUp) {
		shapes.Point temp1 = new shapes.Point(v, this.p1);
		shapes.Point temp2 = new shapes.Point(v, this.p2);
		
		if(temp1.scale(v, pivotPoint, scaleFactorX, scaleFactorY, scaleUp) 
				&& temp2.scale(v, pivotPoint, scaleFactorX, scaleFactorY, scaleUp)) {
			this.p1.updatePoint(v, temp1);
			this.p2.updatePoint(v, temp2);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean translate(View v, int xMoveBy, int yMoveBy) {
		shapes.Point temp1 = new shapes.Point(v, this.p1);
		shapes.Point temp2 = new shapes.Point(v, this.p2);
		
		if(temp1.translate(v, xMoveBy, yMoveBy) && temp2.translate(v, xMoveBy, yMoveBy)) {
			this.p1.updatePoint(v, temp1);
			this.p2.updatePoint(v, temp2);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean rotate(View v, Point pivotPoint, double theta) {
		shapes.Point temp1 = new shapes.Point(v, this.p1);
		shapes.Point temp2 = new shapes.Point(v, this.p2);
		
		if(temp1.rotate(v, pivotPoint, theta) && temp2.rotate(v, pivotPoint, theta)) {
			this.p1.updatePoint(v, temp1);
			this.p2.updatePoint(v, temp2);
			return true;
		}
		return false;
	}
}
