package shapes;

import java.awt.*;

import shapes.Point;

import java.util.*;
import java.util.*;
/**
 * 
 * @author Shivam Biswas
 * 
 * @implNote - in case of tranform and draw
 * define a private method to check for the missing points
 * draw some approximate points between them
 * so that the fill algorithms can be used
 *
 */

public class Ellipse implements Transformation{
	private int rx, ry;
	private shapes.Point center;
	private ArrayList<shapes.Point> boundaryPoints;
	private double prevTheta;
	private Map<Integer, shapes.Point> topRotatedPoints, bottomRotatedPoints;
	
	/**
	 * Initializes an ellipse with center(xCenter, yCenter)
	 * and semi-major/minor axis along x-axis as rx
	 * and semi-minor/major axis along y-axis as ry.
	 * If the ellipse lies out of the screen then it will be initialized
	 * as a zero size point.
	 * @param v - View object
	 * @param xCenter -	x coordinate of the center
	 * @param yCenter - y coordinate of the center
	 * @param rx - semi-major/minor axis along x-axis
	 * @param ry - semi-minor/major axis along y-axis
	 */
	public Ellipse(View v, int xCenter, int yCenter, int rx, int ry) {
		if(this.ellipseInScreen(v, xCenter, yCenter, rx, ry)) {
			this.center = new shapes.Point(v, xCenter, yCenter);
			this.rx = rx;
			this.ry = ry;
			this.prevTheta = 0.0;
		}
		else {
			this.center = new shapes.Point(v);
		}
		this.boundaryPoints = new ArrayList<shapes.Point>();
		this.topRotatedPoints = new HashMap<Integer, shapes.Point>();
		this.bottomRotatedPoints = new HashMap<Integer, shapes.Point>();
		
	}
	/**
	 * 
	 * @param v - View object
	 * @param xCenter -	x coordinate of the center
	 * @param yCenter - y coordinate of the center
	 * @param rx - semi-major/minor axis along x-axis
	 * @param ry - semi-minor/major axis along y-axis
	 * 
	 * @return - true if ellipse in screen else false
	 */
	private boolean ellipseInScreen(View v, int xCenter, int yCenter, int rx, int ry) {
		if(v.pointInScreen(xCenter, yCenter+ry)
				&& v.pointInScreen(xCenter+rx, yCenter)
				  && v.pointInScreen(xCenter, yCenter-ry)
				    && v.pointInScreen(xCenter-rx, yCenter)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if the ellipse falls within the current screen
	 * @param v - Current View Object
	 * @return - Returns true if the ellipse falls within the current View
	 */
	private boolean ellipseInScreen(View v) {
		int xCenter = this.center.getx();
		int yCenter = this.center.gety();
		
		if(v.pointInScreen(xCenter, yCenter+this.ry)
				&& v.pointInScreen(xCenter+this.rx, yCenter)
				  && v.pointInScreen(xCenter, yCenter-this.ry)
				    && v.pointInScreen(xCenter-this.rx, yCenter)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param v - Current View Object
	 * @return - Returns the current centre of the ellipse
	 */
	public Point getCenter(View v) {
		return this.center.clone(v);
	}
	
	/**
	 * Private helper method to draw the reflection of calculated ellipse point
	 * in first quadrant by midpoint ellipse theorem in the rest of the quadrants
	 * @param g - Current Graphics Object
	 * @param v - Current View Object
	 * @param pixels - Current Pixel object
	 * @param x - x coordinate of the approximate point in first quadrant
	 * @param y - x coordinate of the approximate point in first quadrant	
	 */
	private void drawAllReflections(Graphics g, View v, Pixel pixels, int x, int y) {
		int xCenter = this.center.getx();
		int yCenter = this.center.gety();
		
		shapes.Point pt = new shapes.Point(v, xCenter+x,  yCenter+y);
		
		pt.drawPoint(g, v, pixels);
		
		pt.updatePoint(v,  xCenter+x,  yCenter-y);
		pt.drawPoint(g, v, pixels);
		
		pt.updatePoint(v,  xCenter-x,  yCenter-y);
		pt.drawPoint(g, v, pixels);
		
		pt.updatePoint(v,  xCenter-x,  yCenter+y);
		pt.drawPoint(g, v, pixels);
	}
	
	/**
	 * Draws the ellipse on the screen
	 * @param g - Current Graphics object
	 * @param v - Current View Object
	 * @param pixels - Current Pixel Object
	 */
	public void draw(Graphics g, View v, Pixel pixels) {	
		int xi, yi;
		double p1,p2;
		
		int rx2 = rx*rx;
		int ry2 = ry*ry;
		
		p1 = ry2  - rx2*(ry - 0.25);

		
		xi = 0;
		yi = ry;
		
		while(xi*ry2 <= yi*rx2) {
			this.drawAllReflections(g, v, pixels, xi, yi);
			xi++;
			if(p1<0) {
				p1 += (double)(2*ry2*xi + ry2);
			}
			else {
				yi--;
				p1 += (double)(2* (ry2*(xi +1) - rx2*yi));
			}
		}
		
		p2 = ry2*(xi+0.5)*(xi+0.5) + rx2*(yi-1)*(yi-1) - rx2*ry2;
		
		
		while(yi>=0) {
			this.drawAllReflections(g, v, pixels, xi, yi);
			yi--;
			if(p2 >0) {
				p2 += ( rx2 - 2*rx2*yi);
			}
			else{
				xi++;
				p2 += 2*(ry2*xi - rx2*yi) + rx2;
			}
			
		}
	}
	
	/**
	 * Helper fill method
	 * @param g - Current Graphics Object
	 * @param v -  Current View Object
	 * @param pixels - Current Pixel Object
	 * @param interiorPoint - potentially the center of the ellipse
	 * @param boundaryColor - color by which boundary is drawn
	 * @param interiorColor - color to fill
	 */
	private void fill4Points(Graphics g, View v, Pixel px, Point interiorPoint, 
			Color boundaryColor, Color interiorColor) {
		int x, y;
		x = interiorPoint.getx();
		y = interiorPoint.gety();
		
		//end of screen reached
		if(v.pointInScreen(x, y) == false ) {
			return;
		}
		
	
		if( (px.getPixel(x, y) != boundaryColor.getRGB() )
				&& ( px.getPixel(x, y) != interiorColor.getRGB() ) ) {
			interiorPoint.drawPoint(g, v, px);
			
			Point p[] = new Point[4];
			
			p[0] = new Point(v, x-1, y);
			this.fill4Points(g, v, px, p[0], boundaryColor, interiorColor);
			
			
			p[1] = new Point(v, x, y+1);
			this.fill4Points(g, v, px, p[1], boundaryColor, interiorColor);
		
			p[2] = new Point(v, x+1, y);
			this.fill4Points(g, v, px, p[2], boundaryColor, interiorColor);
			
			p[3] = new Point(v, x, y-1);
			this.fill4Points(g, v, px, p[3], boundaryColor, interiorColor);
			
			return;
		}
	}
	
	/**
	 * Fill the ellipse shape with the interior color.
	 * Boundary color mismatch with the boundary color may spill color in the rest of the screen
	 * or throw pixel out of screen error.
	 * @param g - Current Graphics Object
	 * @param v -  Current View Object
	 * @param pixels - Current Pixel Object
	 * @param interiorPoint - potentially the center of the ellipse
	 * @param boundaryColor - color by which boundary is drawn
	 * @param interiorColor - color to fill
	 */
	public void fill(Graphics g, View v, Pixel pixels,
						Point interiorPoint, Color boundaryColor, Color interiorColor) {
		g.setColor(interiorColor);
		
		this.fill4Points(g, v, pixels, interiorPoint, boundaryColor, interiorColor);
	}
	
	/**
	 * Private helper method to draw upper half of the ellipse
	 * @param g - Current Graphics Object
	 * @param v -  Current View Object
	 * @param pixels - Current Pixel Object
	 * @param x - x coordinate of the approximate point in first quadrant
	 * @param y - x coordinate of the approximate point in first quadrant	
	 */
	private void drawUpperHalf(Graphics g, View v, Pixel pixels, int x, int y) {
		int xCenter = this.center.getx();
		int yCenter = this.center.gety();
		shapes.Point pt = new shapes.Point(v, xCenter+x, yCenter+y);
		pt.drawPoint(g, v, pixels);
		
		pt.updatePoint(v, xCenter-x, yCenter+y);
		pt.drawPoint(g, v, pixels);
	}
	
	/**
	 * Private helper method to draw lower half of the ellipse
	 * @param g - Current Graphics Object
	 * @param v -  Current View Object
	 * @param pixels - Current Pixel Object
	 * @param x - x coordinate of the approximate point in first quadrant
	 * @param y - x coordinate of the approximate point in first quadrant	
	 */
	private void drawLowerHalf(Graphics g, View v, Pixel pixels, int x, int y) {
		int xCenter = this.center.getx();
		int yCenter = this.center.gety();
		
		shapes.Point pt = new shapes.Point(v, xCenter+x,  yCenter-y);
		pt.drawPoint(g, v, pixels);
		
		pt.updatePoint(v,  xCenter-x,  yCenter-y);
		pt.drawPoint(g, v, pixels);
	}
	
	/**
	 * Draws only one half of the ellipse.
	 * If flapup = true
	 * @param g - Current Graphics Object
	 * @param v -  Current View Object
	 * @param pixels - Current Pixel Object
	 * @param flapUp - true for upper half-ellipse, false for lower-half ellipse
	 */
	public void drawHalf(Graphics g, View v, Pixel pixels, boolean flapUp) {
		int xi, yi;
		double p1,p2;
		
		int rx2 = rx*rx;
		int ry2 = ry*ry;
		
		p1 = ry2  - rx2*(ry - 0.25);

		xi = 0;
		yi = ry;
		
		while(xi*ry2 <= yi*rx2) {
			if(flapUp) {
				this.drawUpperHalf(g, v, pixels, xi, yi);
			}
			else {
				this.drawLowerHalf(g, v, pixels, xi, yi);
			}
			xi++;
			if(p1<0) {
				p1 += (double)(2*ry2*xi + ry2);
			}
			else {
				yi--;
				p1 += (double)(2* (ry2*(xi +1) - rx2*yi));
			}
		}
		
		p2 = ry2*(xi+0.5)*(xi+0.5) + rx2*(yi-1)*(yi-1) - rx2*ry2;
		
		
		while(yi>=0) {
			if(flapUp) {
				this.drawUpperHalf(g, v, pixels, xi, yi);
			}
			else {
				this.drawLowerHalf(g, v, pixels, xi, yi);
			}
			yi--;
			if(p2 >0) {
				p2 += ( rx2 - 2*rx2*yi);
			}
			else{
				xi++;
				p2 += 2*(ry2*xi - rx2*yi) + rx2;
			}
			
		}
		
		shapes.Line midLine = new shapes.Line(v, this.center.getx()-this.rx, this.center.gety(),
				this.center.getx()+this.rx, this.center.gety());
		midLine.drawLineBresenham(g, v, pixels);
	}
	
	/**
	 * 
	 * @param v - Current View Object
	 * @param xCenter - new x coordinate for the Center
	 * @param yCenter - new y coordinate for the center
	 * @param rx - new semi-major/minor axis along x-axis
	 * @param ry - new semi-major/minor axis along y-axis
	 * @return
	 */
	public boolean updateEllipse(View v, int xCenter, int yCenter, int rx, int ry) {
		if(this.ellipseInScreen(v, xCenter, yCenter, rx, ry)) {
			this.center.updatePoint(v, xCenter, yCenter);
			this.rx = rx;
			this.ry = ry;
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * Add all the reflections the four quadrants for the current computed point
	 * @param v - Current View Object
	 * @param x - Current computed x coordinate
	 * @param y - Current computed y coordinate
	 */
	private void addAllReflections(View v, int x, int y) {
		int xCenter = this.center.getx();
		int yCenter = this.center.gety();
		
		shapes.Point pt = new shapes.Point(v, xCenter+x,  yCenter+y);
		this.boundaryPoints.add(pt.clone(v));
		
		pt.updatePoint(v,  xCenter+x,  yCenter-y);
		this.boundaryPoints.add(pt.clone(v));
		
		pt.updatePoint(v,  xCenter-x,  yCenter-y);
		this.boundaryPoints.add(pt.clone(v));
		
		pt.updatePoint(v,  xCenter-x,  yCenter+y);
		this.boundaryPoints.add(pt.clone(v));
		
	}
	
	/**
	 * Computes and adds all the boundary points in the boundaryPoints ArrayList
	 * @param v - Current View Object
	 * @return - Returns true if the ellipse falls in the screen otherwise false
	 */
	public boolean computeBoundary(View v) {
		
		if(!this.ellipseInScreen(v)) {
			return false;
		}
		
		int xi, yi;
		double p1,p2;
		
		int rx2 = rx*rx;
		int ry2 = ry*ry;
		
		p1 = ry2  - rx2*(ry - 0.25);

		
		xi = 0;
		yi = ry;
		
		while(xi*ry2 <= yi*rx2) {
			this.addAllReflections(v, xi, yi);
			xi++;
			if(p1<0) {
				p1 += (double)(2*ry2*xi + ry2);
			}
			else {
				yi--;
				p1 += (double)(2* (ry2*(xi +1) - rx2*yi));
			}
		}
		
		p2 = ry2*(xi+0.5)*(xi+0.5) + rx2*(yi-1)*(yi-1) - rx2*ry2;
		
		
		while(yi>=0) {
			this.addAllReflections(v, xi, yi);
			yi--;
			if(p2 >0) {
				p2 += ( rx2 - 2*rx2*yi);
			}
			else{
				xi++;
				p2 += 2*(ry2*xi - rx2*yi) + rx2;
			}
			
		}
		return true;
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
		
		
		ArrayList<shapes.Point> topPoints = new ArrayList<shapes.Point>();
		ArrayList<shapes.Point> bottomPoints = new ArrayList<shapes.Point>();
		this.separateBoundaryPoints(v, topPoints, bottomPoints);
		
		int i=0;
		
		for(shapes.Point temp: topPoints) {
			this.topRotatedPoints.put(i, temp);
			i++;
		}
		
		shapes.Point t;
		
		for(i=0; i<this.topRotatedPoints.size(); i++) {
			t = this.topRotatedPoints.get(i);
			t.rotate(v, pivotPoint, theta);
		}
		
		i=0;
		for(shapes.Point temp: bottomPoints) {
			this.bottomRotatedPoints.put(i, temp);
			i++;
		}
		
		for(i=0; i<this.bottomRotatedPoints.size(); i++) {
			t = this.bottomRotatedPoints.get(i);
			t.rotate(v, pivotPoint, theta);
			this.bottomRotatedPoints.put(i, t.clone(v));
		}
		
		fillMissing(v, this.topRotatedPoints);
		fillMissing(v, this.bottomRotatedPoints);
		
		
		return true;
	}
	
	public boolean drawRotatedBoundary(Graphics g, View v, Pixel pixels, Point pivotPoint, double theta) {
		if(this.boundaryPoints.size()!=0){
			this.boundaryPoints.clear();
		}
		if((this.topRotatedPoints.size()!=0) || (this.bottomRotatedPoints.size()!=0)) {
			this.topRotatedPoints.clear();
			this.bottomRotatedPoints.clear();
		}
		
		if(!this.center.rotate(v, pivotPoint, theta)) {
			return false;
		}
		this.computeRotatedBoundary(v, pivotPoint, theta+prevTheta);
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
		this.prevTheta += theta;
		return true;
	}

	
	@Override
	public boolean scale(View v, Point pivotPoint, int scaleFactorX, int scaleFactorY, boolean scaleUp) {
		shapes.Point prevCenter = new shapes.Point(v, this.center);
		int prevrx,prevry;
		prevrx = this.rx;
		prevry = this.ry;
		
		this.center.scale(v, pivotPoint, scaleFactorX, scaleFactorY, scaleUp);
		if(scaleUp) {
			this.rx *= scaleFactorX;
			this.ry *= scaleFactorY;
		}
		else {
			this.rx /= scaleFactorX;
			this.ry /= scaleFactorY;
		}
		
		if(this.ellipseInScreen(v)) {
			return true;
		}
		
		this.center = prevCenter;
		this.rx  = prevrx;
		this.ry  = prevry;
		
		return false;
	}
	@Override
	public boolean translate(View v, int xMoveBy, int yMoveBy) {
		
		if(this.ellipseInScreen(v, this.center.getx()+xMoveBy, this.center.gety()+yMoveBy ,
				this.rx, this.ry) ) {
			this.center.translate(v, xMoveBy, yMoveBy);
			return true;
		}
		return false;
		
	}
	@Override
	public boolean rotate(View v, Point pivotPoint, double theta) {
		shapes.Point prevCenter = new shapes.Point(v, this.center);
		int prevrx,prevry;
		prevrx = this.rx;
		prevry = this.ry;
		
		this.center.rotate(v, pivotPoint, theta);
		
		if(this.ellipseInScreen(v)) {
			return true;
		}
		this.center = prevCenter;
		this.rx  = prevrx;
		this.ry  = prevry;
		
		return false;
	}
}
