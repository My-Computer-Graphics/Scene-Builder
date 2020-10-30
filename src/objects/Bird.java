package objects;

import java.awt.*;
import shapes.*;
import shapes.Point;
/**
 * 
 * @author Shivam Biswas
 * Notes for next day
 * There should be a size variable
 * there should be head up, head down boolean 
 * there should be a five variants of wings
 * there should be three variants of tail
 * there should be methods to return the corner coordinates
 * there should be max size method
 */

public class Bird implements Transformation {
	//Colors
	Color boundaryColor, bodyColor, headColor, eyeColor, beakColor, tailColor, wingColor;
	
	private int xCenterBody, yCenterBody, rxBody, ryBody;
	private int xCenterWing, yCenterWing, yLimitWing, rWing, wingType;
	private int xCenterHead, yCenterHead, rhead;
	private int xCenterEye, yCenterEye, reye;
	
	private int x1beak, x2beak, x3beak, y1beak, y2beak, y3beak;
	
	private int x1tail, y1tail, x2tail, y2tail, x3tail, y3tail, x4tail,y4tail;
	
	shapes.Ellipse mainBody;
	shapes.Circle head, eye;
	shapes.CircleSegment wing;
	shapes.Polygon beak, tail;
	
	public Bird(View v,  int xCenter, int yCenter, int birdHeight, int birdWidth, int wingType){
		this.boundaryColor = Color.black;
		this.bodyColor = new Color(253,254,3);
		this.headColor = bodyColor;
		this.eyeColor = new Color(79,128,189);
		this.beakColor = Color.red;
		this.tailColor = new Color(249,191,143);
		this.wingColor = tailColor;
		
		this.initBody(v, xCenter, yCenter, birdWidth/4, birdHeight/4);
		//this.initHead(v, xCenter-5*this.rxBody/4, yCenter+rhead, birdWidth/6);
		this.initHead(v, birdWidth/6);
		this.initWing(v, xCenter, yCenter, this.ryBody, wingType);
		//this.initWing(v, xCenter, yCenter, this.rxBody/2, wingType);
		
		this.initEye(v);
		this.initBeak(v);
		this.initTail(v);
		
	}
	
	/**
	 * Updates the bird object
	 * @param v
	 * @param xCenter
	 * @param yCenter
	 * @param birdHeight
	 * @param birdWidth
	 * @param wingType
	 */
	public void updateBird(View v,  int xCenter, int yCenter, int birdHeight, int birdWidth, int wingType){
		this.initBody(v, xCenter, yCenter, birdWidth/4, birdHeight/4);
		this.initHead(v, birdWidth/6);
		this.initWing(v, xCenter, yCenter, this.ryBody, wingType);
		this.initEye(v);
		this.initBeak(v);
		this.initTail(v);
		
	}
	/**
	 * 
	 * @param v - Current View Object
	 * @return - Return a copy of the current centre of the bird
	 */
	public Point getCenter(View v) {
		shapes.Point pt = new shapes.Point(v, this.mainBody.getCenter(v));
		return pt;
	}
	
	/**
	 * Initializes the main body of the bird
	 * @param v - Current View Object
	 * @param xCenter - x coordinate of the center of bird body
	 * @param yCenter - y coordinate of the center of bird body
	 * @param rx - semi-major/semi-minor axis along x axis of bird main body
	 * @param ry - semi-minor/semi-major axis along x axis of bird main body
	 */
	private void initBody(View v, int xCenter, int yCenter, int rx, int ry) {
		this.xCenterBody = xCenter;
		this.yCenterBody = yCenter;
		this.rxBody = rx;
		this.ryBody = ry;
		this.mainBody = new shapes.Ellipse(v, xCenter, yCenter, rx, ry);
	}
	
	/**
	 * Initializes the head of the bird
	 * @param v - Current View Object
	 * @param xCenter - x coordinate of the center of bird's head
	 * @param yCenter - y coordinate of the center of bird's head
	 * @param r - Radius of the bird's head
	 */
	private void initHead(View v, int r) {
		this.rhead = 5*this.ryBody/6;
		
		this.xCenterHead = this.xCenterBody - this.rxBody - this.rhead/2;
		//this.xCenterHead = this.xCenterBody -5*this.rxBody/4;
		
		this.yCenterHead = this.yCenterBody + this.rhead/2;
		//this.yCenterHead = this.yCenterBody + this.rhead;
		
		this.head = new shapes.Circle(v, this.rhead, this.xCenterHead, this.yCenterHead);
	}
	
	/**
	 * Initializes the wing of the bird
	 * If wingType = 1 
	 * If wingType = 2 
	 * If wingType = 3
	 * If wingType = 4  
	 * @param v - Current View Object
	 * @param xCenter - x coordinate of the center of wing circle segment
	 * @param yCenter - y coordinate of the center of wing circle segment
	 * @param r - Radius of the bird wing
	 * @param wingType - 1 or 2 or 3 or 4
	 */
	private void initWing(View v, int xCenter, int yCenter, int r, int wingType) {
		this.yLimitWing = yCenter;
		this.xCenterWing = xCenter;
		this.rWing = r;
		
		if((wingType == 1)|| (wingType==3)) {
			this.yCenterWing = yCenter + (int)Math.round((double)r/3);
		}
		else if((wingType == 2)|| (wingType==4)) {
			this.yCenterWing = yCenter - (int)Math.round((double)r/3);
		}
		
		boolean upward=true;

		if((wingType==3)||(wingType==4)) {
			upward = false;
		}
		
		this.wing = new shapes.CircleSegment(v, xCenterWing, yCenterWing, rWing, yLimitWing, upward);
	}
	

	
	/**
	 * It depends on the initialization of the head of the bird
	 */
	private void initEye(View v) {
		
		this.xCenterEye = this.xCenterHead-2*this.rhead/5;
		this.yCenterEye = this.yCenterHead+2*this.rhead/5;
		this.reye = this.rhead/5;
		
		this.eye = new shapes.Circle(v, this.reye, this.xCenterEye , this.yCenterEye);
	}
	
	private void initBeak(View v) {
		//int beakBase = 3*this.rhead/4;
		int beakBase = (int)Math.round((double)3*this.rhead/4);
		int beakHeight = 3*this.rhead/2;
		
		//this.x1beak =  this.xCenterHead - this.rhead;
		this.x1beak =  (int)(Math.ceil( this.xCenterHead - ((double)4*this.rhead/5)) );
		this.y1beak = this.yCenterHead + beakBase/2;
		
		this.x2beak = this.x1beak;
		this.y2beak = this.yCenterHead - beakBase/2;
		
		this.x3beak = this.x1beak - beakHeight;
		this.y3beak = this.yCenterHead;
		
		int x[] = {this.x1beak, this.x2beak, this.x3beak};
		int y[] = {this.y1beak, this.y2beak, this.y3beak};
		
		this.beak = new shapes.Polygon(v, x,y,3);
	}
	
	private void initTail(View v) {
		int tailHeight = this.ryBody;
		int tailWidth = this.rxBody/2;
		
		this.x1tail = this.xCenterBody + this.rxBody;
		this.y1tail = this.yCenterBody;
		
		this.x2tail = this.x1tail + tailWidth;
		this.y2tail = this.y1tail;
		
		this.x3tail = this.x2tail-tailWidth/6;
		this.y3tail = this.y2tail+tailHeight;
		
		this.x4tail = this.x1tail;
		this.y4tail = this.y1tail+ tailHeight/3;
		
		int x[] = {this.x1tail, this.x2tail, this.x3tail, this.x4tail};
		int y[] = {this.y1tail, this.y2tail, this.y3tail, this.y4tail};
		
		this.tail = new shapes.Polygon(v, x, y, 4);

	}
	
	public void draw(Graphics g, View v, Pixel pixels) {
		shapes.Point interiorPoint;
		
		//Main Body
		g.setColor(boundaryColor);
		this.mainBody.draw(g, v, pixels);
		interiorPoint = this.mainBody.getCenter(v);
		this.mainBody.fill(g, v, pixels, interiorPoint, boundaryColor, bodyColor);
		
		//Tail
		g.setColor(boundaryColor);
		this.tail.draw(g, v, pixels);
		interiorPoint = this.tail.getCentroid(v);
		//interiorPoint.updatePoint(v, this.x1tail+1, this.y1tail+1);
		this.tail.boundryFill(g, v, pixels, interiorPoint, boundaryColor, tailColor, true);
		
		
		
		//Wing or Flap
		g.setColor(wingColor);
		this.wing.draw(g, v, pixels);
		int t = 1;
		if((this.wingType==3)||(this.wingType ==4))	{
			t *= -1;
		}
		interiorPoint = this.wing.getInteriorPoint(v);
		//interiorPoint.updatePoint(v, this.xCenterBody, this.yCenterBody+t);
		this.wing.fill(g, v, pixels, interiorPoint, wingColor, wingColor);
		g.setColor(boundaryColor);
		this.wing.draw(g, v, pixels);
		
		
		
		//Head
		g.setColor(headColor);
		this.head.draw(g, v, pixels);
		interiorPoint = this.head.getCenter(v);
		this.head.fill(g, v, pixels, interiorPoint, headColor, headColor);
		g.setColor(boundaryColor);
		this.head.draw(g, v, pixels);
		
		
		//Eye
		g.setColor(boundaryColor);
		this.eye.draw(g, v, pixels);
		interiorPoint = this.eye.getCenter(v);
		//interiorPoint.updatePoint(v, this.xCenterEye, this.yCenterEye);
		this.eye.fill(g, v, pixels, interiorPoint, boundaryColor, eyeColor);
		
		
		//Beak
		g.setColor(beakColor);
		this.beak.draw(g, v, pixels);
		interiorPoint = this.beak.getCentroid(v);
		//interiorPoint.updatePoint(v, this.x1beak-1, this.yCenterHead);
		this.beak.boundryFill(g, v, pixels, interiorPoint, beakColor, beakColor, true);
		g.setColor(boundaryColor);
		this.beak.draw(g, v, pixels);
		
	}

	@Override
	public boolean scale(View v, Point pivotPoint, int scaleFactorX, int scaleFactorY, boolean scaleUp) {
		
		if(!this.mainBody.scale(v, pivotPoint, scaleFactorX, scaleFactorY, scaleUp)) {
			return false;
		}
		
		if(!this.head.scale(v, pivotPoint, scaleFactorX, scaleFactorY, scaleUp)) {
			return false;
		}
		
		if(!this.eye.scale(v, pivotPoint, scaleFactorX, scaleFactorY, scaleUp)) {
			return false;
		}
		
		if(!this.wing.scale(v, pivotPoint, scaleFactorX, scaleFactorY, scaleUp)) {
			return false;
		}
		
		if(!this.beak.scale(v, pivotPoint, scaleFactorX, scaleFactorY, scaleUp)){
			return false;
		}
		
		if(this.tail.scale(v, pivotPoint, scaleFactorX, scaleFactorY, scaleUp)) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean translate(View v, int xMoveBy, int yMoveBy) {
		// TODO Auto-generated method stub
		
		//this.mainBody.updateEllipse(v, xCenterBody+xMoveBy, yCenterBody+yMoveBy, rxBody, ryBody);
		if(!this.mainBody.translate(v, xMoveBy, yMoveBy)) {
			return false;
		}
		
		if(!this.wing.translate(v, xMoveBy, yMoveBy)) {
			return false;
		}
		
		if(!this.head.translate(v, xMoveBy, yMoveBy)) {
			return false;
		}
	
		if(!this.eye.translate(v, xMoveBy, yMoveBy)) {
			return false;
		}
		
		if(!this.beak.translate(v, xMoveBy, yMoveBy)) {
			return false;
		}
		
		if(!this.tail.translate(v, xMoveBy, yMoveBy)) {
			return false;
		}
		
		return true;
	}

	public boolean rotateAndDraw(Graphics g, View v, Pixel pixels,
			Point pivotPoint, double theta) {
		
		shapes.Point pt;
		
		if(!this.head.rotate(v, pivotPoint, theta)) {
			return false;
		}
		if(!this.eye.rotate(v, pivotPoint, theta)) {
			return false;
		}
		if(!this.beak.rotate(v, pivotPoint, theta)) {
			return false;
		}
		if(!this.tail.rotate(v, pivotPoint, theta)) {
			return false;
		}
		
		
		//Main Body
		g.setColor(this.boundaryColor);
		if(!this.mainBody.drawRotatedBoundary(g, v, pixels, pivotPoint, theta)) {
			return false;
		}
		pt = mainBody.getCenter(v);
		this.mainBody.fill(g, v, pixels, pt, this.boundaryColor, this.bodyColor);
		
		//Wing or Flap
		shapes.Point c, l, r, xb,yb;
		g.setColor(this.wingColor);
		c = this.wing.getCentre(v);
		l = this.wing.getLeftLimit(v);
		r = this.wing.getRightLimit(v);
		xb = this.wing.getXBounds(v);
		yb = this.wing.getYBounds(v);
		if(!this.wing.drawRotatedBoundary(g, v, pixels, pivotPoint, theta)) {
			return false;
		}
		pt = this.wing.getInteriorPoint(v);
		
		this.wing.fill(g, v, pixels, pt, this.wingColor, this.wingColor);
		g.setColor(this.boundaryColor);
		this.wing.updateSegment(v, c, l, r, xb, yb);
		if(!this.wing.drawRotatedBoundary(g, v, pixels, pivotPoint, theta)) {
			return false;
		}
		
		
		//Tail
		g.setColor(boundaryColor);
		this.tail.draw(g, v, pixels);
		pt = this.tail.getCentroid(v);
		this.tail.boundryFill(g, v, pixels, pt, boundaryColor, tailColor, true);
				
		//Head
		g.setColor(headColor);
		this.head.draw(g, v, pixels);
		pt = this.head.getCenter(v);
		this.head.fill(g, v, pixels, pt, headColor, headColor);
		g.setColor(boundaryColor);
		this.head.draw(g, v, pixels);
				
		//Eye
		g.setColor(boundaryColor);
		this.eye.draw(g, v, pixels);
		pt = this.eye.getCenter(v);
		this.eye.fill(g, v, pixels, pt, boundaryColor, eyeColor);
				
		//Beak
		g.setColor(beakColor);
		this.beak.draw(g, v, pixels);
		pt = this.beak.getCentroid(v);
		this.beak.boundryFill(g, v, pixels, pt, beakColor, beakColor, true);
		g.setColor(boundaryColor);
		this.beak.draw(g, v, pixels);

				
		return true;
	}
	
	/**
	 * @param v - Current View Object
	 * @param pivotPoint - The point around which it needs to be rotated
	 * @param theta - The angle by which the bird needs to be rotated
	 * 
	 * @return - Always returns false
	 */
	@Override
	public boolean rotate(View v, Point pivotPoint, double theta) {
		
		
		return false;
	}
	
	

}
