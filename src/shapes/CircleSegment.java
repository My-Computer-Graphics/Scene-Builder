package shapes;

import java.awt.*;

import java.util.*;

import shapes.Point;

public class CircleSegment implements Transformation {
	private int r;
	private shapes.Point center;
	double prevTheta;
	private int ylimit;
	boolean upward, rotatedAlready;
	/**
	 * xBounds(xmin,xmax)
	 * yBounds(ymin,ymax)
	 */
	private shapes.Point xBounds, yBounds, leftLimit, rightLimit;
	private ArrayList<shapes.Point> boundaryPoints;
	private Map<Integer, shapes.Point> topRotatedPoints, bottomRotatedPoints;
	
	/**
	 * Initializes the Circle Segment with center(x,y) and radius "r" and the secant
	 * @param v - Current View Object
	 * @param r - Radius of the circle
	 * @param x - x coordinate of Center
	 * @param y - y coordinate of Center
	 * @param upward - true if the segment lies below the line otherwise false
	 * 
	 * @implNote - Secant must not be parallel to y axis
	 * if you want to do any kind of transformation
	 * must call the compute boundary method or the segment must be drawn at some stage
	 */
	public CircleSegment(View v, int xCenter, int yCenter, int r, int ylimit, boolean upward) {
		center = new shapes.Point(v, xCenter, yCenter);
		this.r = r;
		this.ylimit = ylimit;
		this.upward = upward;
		this.xBounds = new shapes.Point(v);
		this.yBounds = new shapes.Point(v);
		this.leftLimit = new shapes.Point(v);
		this.rightLimit = new shapes.Point(v);
		this.computeBounds(v);
		this.boundaryPoints = new ArrayList<shapes.Point>();
		this.topRotatedPoints = new HashMap<Integer, shapes.Point>();
		this.bottomRotatedPoints = new HashMap<Integer, shapes.Point>();
		this.rotatedAlready = false;
		this.prevTheta = 0.0;
	}
	
	/**
	 * Initializes the Circle Segment same as the reference Segment
	 * @param v - Current View Object
	 * @param referenceSegment - Circle Segment Object
	 */
	public CircleSegment(View v, CircleSegment referenceSegment) {
		center = new shapes.Point(v, referenceSegment.center);
		this.r = referenceSegment.r;
		this.ylimit = referenceSegment.ylimit;
		this.upward = referenceSegment.upward;
		this.computeBounds(v);
		this.prevTheta = 0.0;
		this.rotatedAlready = false;
	}
	
	/**
	 * 
	 * @param v - Current View Object
	 * @return - Returns a clone of the current center
	 */
	public Point getCentre(View v) {
		return center.clone(v);
	}
	
	public Point getLeftLimit(View v) {
		return this.leftLimit.clone(v);
	}
	
	public Point getRightLimit(View v) {
		return this.rightLimit.clone(v);
	}
	public Point getXBounds(View v) {
		return this.xBounds.clone(v);
	}
	public Point getYBounds(View v) {
		return this.yBounds.clone(v);
	}
	public void updateSegment(View v, Point center, Point leftLimit, Point rightLimit, Point xBounds, Point yBounds) {
		this.leftLimit.updatePoint(v, leftLimit);
		this.rightLimit.updatePoint(v, rightLimit);
		this.center.updatePoint(v, center);
		this.xBounds.updatePoint(v, xBounds);
		this.yBounds.updatePoint(v, yBounds);
	}
	
	public Point getInteriorPoint(View v) {
		int x = (this.leftLimit.getx()+this.rightLimit.getx())/2;
		int y = this.leftLimit.gety()+1;
		shapes.Point interiorPoint = new shapes.Point(v, x, y);
		return interiorPoint;
	}
	
	/**
	 * Calculates xmin, xmax, ymin, ymax of the segment
	 * and updates the xBounds and yBounds
	 * @param v - View Object
	 */
	private void computeBounds(View v) {
		int xRight, xLeft;
		double cosTheta = Math.sqrt( 1 - ( (double)(this.ylimit-this.center.gety())/this.r )*( (double)(this.ylimit-this.center.gety())/this.r ) );
		
		xRight = this.center.getx() + (int)Math.round((double)this.r * cosTheta);
		xLeft = this.center.getx() - (int)Math.round((double)this.r * cosTheta);
		
		int xMax, xMin, yMax, yMin;
		
		if(upward) {
			if(this.ylimit > this.center.gety()) {
				xMax = xRight;
				xMin = xLeft;
				yMax = this.center.gety()+this.r;
				yMin = this.ylimit;
			}
			else {
				xMax = this.center.getx()+this.r;
				xMin = this.center.getx()-this.r;
				yMax = this.center.gety()+this.r;
				yMin = this.ylimit;
			}
		}
		else {
			if(this.ylimit > this.center.gety()) {
				xMax = this.center.getx()+this.r;
				xMin = this.center.getx()-this.r;
				yMax = this.ylimit;
				yMin = this.center.gety() - this.r;
			}
			else {
				xMax = xRight;
				xMin = xLeft;
				yMax = this.ylimit;
				yMin = this.center.gety() - this.r;
			}
		}
		
		this.xBounds.updatePoint(v, xMin, xMax);
		this.yBounds.updatePoint(v, yMin, yMax);
		this.leftLimit.updatePoint(v, xLeft, this.ylimit);
		this.rightLimit.updatePoint(v, xRight, this.ylimit);
	}
	
	/**
	 * Checks if the current circle is within the View
	 * @param v - Current View Object
	 * @return - Returns false if the circle segment is out of the view passed or the boundary points are not computer 
	 */
	private boolean circleSegmentInScreen(View v) {
		if( (this.xBounds.gety() <= v.getRightX()) && (this.xBounds.getx() >= v.getLeftX() )
				&& (this.yBounds.gety() <= v.getTopY()) && (this.yBounds.getx() >= v.getBottomY()) ) {
			return true;
		}
	
		return false;
	}
	
	/**
	 * Add all the reflections of the current computed point in arraylist
	 * @param v - Current View Object
	 * @param x - Current computed x coordinate
	 * @param y - Current computed y coordinate
	 */
	private void addBoundaryPoints(View v, int x, int y) {
		int xCenter = this.center.getx();
		int yCenter = this.center.gety();
		
		if( ( xCenter + x >= this.xBounds.getx() )
				&& ( xCenter + x <= this.xBounds.gety())
				&& (yCenter + y >= this.yBounds.getx())
				&& (yCenter + y <= this.yBounds.gety()) ){
			
			this.boundaryPoints.add(new shapes.Point(v, xCenter + x,  yCenter + y));
		}
		if( (xCenter + x >= this.xBounds.getx()) 
				&& (xCenter + x <= this.xBounds.gety())
				&& ( yCenter - y >= this.yBounds.getx())
				&& ( yCenter - y <= this.yBounds.gety())){
			
			this.boundaryPoints.add(new shapes.Point(v,  xCenter + x, yCenter - y));
			
		}
		
		if( (xCenter - x >= this.xBounds.getx())
				&& (xCenter - x <= this.xBounds.gety())
				&& ( yCenter + y >= this.yBounds.getx())
				&& ( yCenter + y <= this.yBounds.gety())){
			this.boundaryPoints.add(new shapes.Point(v,  xCenter - x,  yCenter + y));
		}
		
		if( (xCenter - x >= this.xBounds.getx()) 
				&& (xCenter - x <= this.xBounds.gety())
				&& ( yCenter - y >= this.yBounds.getx())
				&& ( yCenter - y <= this.yBounds.gety())){
			this.boundaryPoints.add(new shapes.Point(v, xCenter - x, yCenter - y));
		}
		
		if( (xCenter + y >= this.xBounds.getx()) 
				&& (xCenter + y <= this.xBounds.gety())
				&& ( yCenter + x >= this.yBounds.getx())
				&& ( yCenter + x <= this.yBounds.gety())){
			this.boundaryPoints.add(new shapes.Point(v, xCenter + y, yCenter + x));
		}
		
		if( (xCenter - y >= this.xBounds.getx()) 
				&& (xCenter - y <= this.xBounds.gety())
				&& ( yCenter + x >= this.yBounds.getx())
				&& ( yCenter + x <= this.yBounds.gety())){
			this.boundaryPoints.add(new shapes.Point(v, xCenter - y, yCenter + x));
		}			
		if( (xCenter + y >= this.xBounds.getx())
				&& (xCenter + y <= this.xBounds.gety())
				&& ( yCenter - x >= this.yBounds.getx())
				&& ( yCenter - x <= this.yBounds.gety())){
			this.boundaryPoints.add(new shapes.Point(v, xCenter + y, yCenter - x));
		}
		if( (xCenter - y >= this.xBounds.getx())
				&& (xCenter - y <= this.xBounds.gety())
				&& ( yCenter - x >= this.yBounds.getx())
				&& ( yCenter - x <= this.yBounds.gety())){
			this.boundaryPoints.add(new shapes.Point(v, xCenter - y, yCenter - x));
		}

	}
	
	/**
	 * Adds the boundary points to the boundaryPoints ArrayList
	 * @param v - Current View Object
	 */
	private boolean computeBoundary(View v) {
		if(!this.circleSegmentInScreen(v)) {
			System.out.println("Circle Segment out of screen");
			return false;
		}
		
		int xi,yi;
		double p;
		
		xi= 0;
		yi= this.r;
		p=(5.0/4.0)-(double)this.r;
		
		while(xi<= yi) {
			this.addBoundaryPoints(v, xi, yi);
			
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
	 * Draws the current Circle Segment on the applet screen provided it falls within the
	 * bounds set by the current view object.
	 * Updates the pixel array object with rgb pixel values of the colour by which the circle
	 * boundary is drawn.
	 * @param g - Current Graphics Object
	 * @param v - Current View Object
	 * @param pixels - Current Pixel Object
	 * @return - Returns true if the shape can be drawn
	 */
	public boolean draw(Graphics g, View v, Pixel pixels) {
		if(!this.circleSegmentInScreen(v)) {
			System.out.println("Circle Segment out of screen");
			return false;
		}
		if(this.boundaryPoints.size()!=0) {
			this.boundaryPoints.clear();
		}
		this.computeBoundary(v);
		for(Point p: this.boundaryPoints) {
			p.drawPoint(g, v, pixels);
		}
		//shapes.Line l = new shapes.Line(v, this.xBounds.getx(), this.ylimit, this.xBounds.gety(), this.ylimit);
		shapes.Line l = new shapes.Line(v, this.leftLimit.getx(), this.leftLimit.gety(), this.rightLimit.getx(), this.rightLimit.gety());
		l.drawLineBresenham(g, v, pixels);
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
	public boolean updateSecant(View v, int xCenter, int yCenter, int r) {
		shapes.Point oldCenter = new shapes.Point(v, this.center);
		int oldR = this.r;
		this.center.updatePoint(v, xCenter, yCenter);
		this.r = r;
		if(this.circleSegmentInScreen(v)) {
				return true;
		}
		
		this.center.updatePoint(v, oldCenter);
		this.r  = oldR;
		
		return false;
	}
	
	/**
	 * 
	 * @param p1
	 * @param p2
	 * @param top
	 * @return - Returns true if p1 > p2
	 */
	private boolean comparePoints(Point p1, Point p2, boolean top) {
		int x1, y1, x2, y2;
		
		x1 = p1.getx();
		x2 = p2.getx();
		
		if(x1<x2) {
			return false;
		}
		
		y1 = p1.gety();
		y2 = p2.gety();
		
		if(x1 == x2) {
			if(x1<this.center.getx()) {
				if(top) {
					return y1>y2;
				}
				else {
					return y1<y2;
				}
			}
			else {
				if(top) {
					return y1<y2;
				}
				else {
					return y1>y2;
				}
			}
			
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param v - 
	 * @param p - 
	 * @param top - 
	 */
	public void sortPoints(View v, ArrayList<shapes.Point> p, boolean top) {
		int i,j;
		shapes.Point key;
		for(i=0; i<p.size(); i++) {
			key = p.get(i).clone(v);
			j = i-1;
			while(j>=0 && this.comparePoints(p.get(j),key, top)) {
				p.set(j+1, p.get(j).clone(v));
				j--;
			}
			p.set(j+1, key);
		}
	}
	
	/**
	 * Separated the boundary points in two halves and sorts them from left to right
	 * and stores them in the topPoints and bottomPoints array list
	 * @param v - Current View Object
	 * @param topPoints - array object of top Points
	 * @param bottomPoints - array object of bottom Points
	 */
	public void separateBoundaryPoints(View v, ArrayList<shapes.Point> topPoints, ArrayList<shapes.Point> bottomPoints) {
		for(shapes.Point p : this.boundaryPoints) {
			if( p.gety() >= this.center.gety() ) {
				topPoints.add(p.clone(v));
			}
			else {
				bottomPoints.add(p.clone(v));
			}
		}
		this.sortPoints(v, topPoints, true);
		this.sortPoints(v, bottomPoints, false);
		
	}
	
	/**
	 * 
	 * @param v
	 * @param p1
	 * @param p2
	 * @param pt
	 * @param keyIndexStart
	 * @return - Returns the next key value that can be used
	 */
	private int fillDDA(View v, Point p1, Point p2, Map<Integer, shapes.Point> pt, int keyIndexStart) {
		int dx, dy, steps;
		double xi, yi, xinc, yinc;
		
		shapes.Point point = new shapes.Point(v);
		
		
		dx = p2.getx() - p1.getx();
		dy = p2.gety() - p1.gety();
	
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
		
		point.updatePoint(v, p1);
	
		int i;
		for(i=0; i<steps-1; i++) {
			xi += xinc;
			yi += yinc;
			
			point.updatePoint(v, (int)Math.round(xi), (int)Math.round(yi));
			pt.put(keyIndexStart+i, point);
			
		}
		return keyIndexStart+i;
	}
	
	private void fillMissing(View v, Map<Integer, shapes.Point> pt) {
		
		int i;
		shapes.Point p1,p2;
		int x1,x2,y1,y2;
		
		int n = pt.size();
		int nextFillStartIndex = n;
		
		for(i=1; i<n; i++) {
			p1 = pt.get(i-1);
			p2 = pt.get(i);
			
			x1 = p1.getx();
			x2 = p2.getx();
			y1 = p1.gety();
			y2 = p2.gety();
			
			if((Math.abs(x1-x2)>1) || (Math.abs(y2-y1)>1)) {
				nextFillStartIndex = this.fillDDA(v, p1, p2, pt, nextFillStartIndex);
			}
		}
	}
	
	/**
	 * 
	 * @param v
	 * @param pivotPoint
	 * @param theta
	 * @return
	 */
	public boolean computeRotatedBoundary(View v, Point pivotPoint, double theta) {
		if(!this.computeBoundary(v)) {
			return false;
		}
		this.computeBoundary(v);
		
		ArrayList<shapes.Point> topPoints = new ArrayList<shapes.Point>();
		ArrayList<shapes.Point> bottomPoints = new ArrayList<shapes.Point>();
		this.separateBoundaryPoints(v, topPoints, bottomPoints);
		
		int i=0;
		shapes.Point t;
		
		if(topPoints.size()>0) {
			for(shapes.Point temp: topPoints) {
				this.topRotatedPoints.put(i, temp);
				i++;
			}
			
			for(i=0; i<this.topRotatedPoints.size(); i++) {
				t = this.topRotatedPoints.get(i);
				t.rotate(v, pivotPoint, theta);
			}
			
			fillMissing(v, this.topRotatedPoints);
		}
		
		i=0;
		
		if(bottomPoints.size()>0) {
			for(shapes.Point temp: bottomPoints) {
				this.bottomRotatedPoints.put(i, temp);
				i++;
			}
			
			for(i=0; i<this.bottomRotatedPoints.size(); i++) {
				t = this.bottomRotatedPoints.get(i);
				t.rotate(v, pivotPoint, theta);
				this.bottomRotatedPoints.put(i, t.clone(v));
			}
			fillMissing(v, this.bottomRotatedPoints);
		}
		
		return true;
	}

	

	
	public boolean drawRotatedBoundary(Graphics g, View v, Pixel pixels, Point pivotPoint, double theta) {
		if(this.rotatedAlready){
			this.boundaryPoints.clear();
		}
		if(this.rotatedAlready) {
			//this.boundaryPoints.addAll(this.topRotatedPoints.values());
			//this.boundaryPoints.addAll(this.bottomRotatedPoints.values());
			this.topRotatedPoints.clear();
			this.bottomRotatedPoints.clear();
		}
		this.computeRotatedBoundary(v, pivotPoint, prevTheta+theta);
		shapes.Point prevCenter = this.center.clone(v);
		if(!this.center.rotate(v, pivotPoint, theta)) {
			this.center = prevCenter;
			return false;
		}
		
		shapes.Point temp;
		for(int i=0; i<this.bottomRotatedPoints.size(); i++) {
			temp = this.bottomRotatedPoints.get(i);
			if(!temp.drawPoint(g, v, pixels)) {
				return false;
			}
		}
		for(int i=0; i<this.topRotatedPoints.size(); i++) {
			temp = this.topRotatedPoints.get(i);
			if(!temp.drawPoint(g, v, pixels)) {
				return false;
			}
		}
		shapes.Point p1 = new shapes.Point(v, this.leftLimit.getx(), this.leftLimit.gety());
		shapes.Point p2 = new shapes.Point(v, this.rightLimit.getx(), this.rightLimit.gety());
		
		p1.rotate(v, pivotPoint, prevTheta+theta);
		p2.rotate(v, pivotPoint, prevTheta+theta);
		
		shapes.Line l = new shapes.Line(v, p1.getx(), p1.gety(), p2.getx(), p2.gety());
		l.drawLineBresenham(g, v, pixels);
		//this.prevTheta += theta;
		this.rotatedAlready = true;
		return true;
	}

	@Override
	public boolean scale(View v, Point pivotPoint, int scaleFactorX, int scaleFactorY, boolean scaleUp) {
		if(scaleFactorX == scaleFactorY) {
			int prevRadius = this.r;
			int prevYLimit = this.ylimit;
			shapes.Point prevCenter = this.center.clone(v);
			
			this.center.scale(v, pivotPoint, scaleFactorX, scaleFactorY, scaleUp);
			
			int yPivot = pivotPoint.gety();
			
			if(scaleUp) {
				this.r *= scaleFactorX;
				this.ylimit = yPivot + (this.ylimit-yPivot)*scaleFactorY;
			}
			else {
				this.r /= scaleFactorX;
				this.ylimit = yPivot + (this.ylimit-yPivot)/scaleFactorY;
			}
			this.computeBounds(v);
			if(this.circleSegmentInScreen(v)) {
				return true;
			}
			else {
				System.out.println("Scaling draws circle segment out of the screen");
				this.r = prevRadius;
				this.ylimit = prevYLimit;
				this.center = prevCenter.clone(v);
				this.computeBounds(v);
				return false;
			}
		}
		System.out.println("Non Uniform scaling is not allowed in case of circle");
		return false;
	}

	@Override
	public boolean translate(View v, int xMoveBy, int yMoveBy) {
		shapes.Point prevCenter = this.center.clone(v);
		int prevYLimit = this.ylimit;
		
		this.center.translate(v, xMoveBy, yMoveBy);
		this.ylimit += yMoveBy;
		this.leftLimit.translate(v, xMoveBy, yMoveBy);
		this.rightLimit.translate(v, xMoveBy, yMoveBy);
		this.xBounds.translate(v, xMoveBy, xMoveBy);
		this.yBounds.translate(v, yMoveBy, yMoveBy);
		this.computeBounds(v);
		if(this.circleSegmentInScreen(v)) {
			return true;
		}
		this.center = prevCenter.clone(v);
		this.ylimit = prevYLimit;
		this.computeBounds(v);
		return false;
	}

	@Override
	public boolean rotate(View v, Point pivotPoint, double theta) {
		shapes.Point prevCenter = new shapes.Point(v, this.center);
		this.center.rotate(v, pivotPoint, theta);
		if(this.circleSegmentInScreen(v)) {
			return true;
		}
		this.center = prevCenter;
		return false;
	}
}
