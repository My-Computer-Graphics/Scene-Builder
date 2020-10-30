package shapes;
import java.awt.*;

public class Polygon implements Transformation{
	shapes.Point[] point;
	int n;
	//to draw the interiors

	/**
	 * Default constructor
	 */
	public Polygon() {
		point = null;
		n = 0;
		
	}
	/**
	 * Polygon with continuous points
	 * @param x: array of x coordinates
	 * @param y: array of y coordinates
	 * @param n: number of points
	 */
	public Polygon(View v, int x[], int y[], int n) {
		if((x.length != y.length) || (x.length!=n) || (y.length != n)) {
			System.out.println("Invalid parameters passed");
			return;
		}
		
		this.n = n;
		
		point = new shapes.Point[this.n];
		for(int i=0; i<n; i++) {
			point[i] = new shapes.Point(v, x[i], y[i]);
		}
	}
	/**
	 * @param v - Current View Object
	 * @return - Returns the Point(xmin, xmax)
	 */
	private Point getXMinMax(View v) {
		if(n <= 0) {
			return null;
		}
		
		int xmax = this.point[0].getx();
		int xmin = this.point[0].getx();
		
		int t;
		for(int i=1; i< this.n; i++) {
			t = this.point[i].getx(); 
			if(t > xmax) {
				xmax = t;
			}
			else if(t<xmin) {
				xmin = t;
			}
		}
		shapes.Point minMax = new shapes.Point(v, xmin, xmax); 
		
		return minMax;
	}
	
	/**
	 * 
	 * @param v - Current View Object
	 * @return - Return the point(ymin,ymax)
	 * 
	 */
	private Point getYMinMax(View v) {
		if(n <= 0) {
			return null;
		}
		
		int ymax = this.point[0].gety();
		int ymin = this.point[0].gety();
		
		int t;
		for(int i=1; i< this.n; i++) {
			t = this.point[i].gety(); 
			if(t > ymax) {
				ymax = t;
			}
			else if(t<ymin) {
				ymin = t;
			}
		}
		shapes.Point minMax = new shapes.Point(v, ymin, ymax); 
		
		return minMax;
	}
	/**
	 * 
	 * @param v - Current View Object
	 * @return - Returns the Centroid point(xg,yg)
	 */
	public Point getCentroid(View v) {
		int xg = 0, yg = 0;
		if(n<=0) {
			return null;
		}

		for(int i=0; i<n; i++) {
			
			xg += this.point[i].getx();
			yg += this.point[i].gety();
		}
		
		xg/=n;
		yg/=n;
		
		shapes.Point centroid = new shapes.Point(v, xg, yg);
		return centroid;
	}
	
	/**
	 * 
	 * @param v - Current View Object
	 * @return - Returns true if ellipse in screen
	 */
	private boolean polygonInScreen(View v) {
		if(n<=0) {
			return false;
		}
		
		shapes.Point xminmax, yminmax;
		
		xminmax = this.getXMinMax(v);
		yminmax = this.getYMinMax(v);
		
		if(v.pointInScreen(xminmax.getx(), yminmax.getx()) 
				&& v.pointInScreen(xminmax.gety(), yminmax.gety())) {
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * Draws the current polygon on screen
	 * @param g: Current Graphics object
	 * @param v: Current View object
	 * @param pixels - Current Pixels object
	 */
	public void draw(Graphics g, View v, Pixel pixels) {
		int i;
		Line l = new Line(v, 0,0,0,0);
		for(i=0; i<n-1; i++) {
			if(!l.updateLine(v, this.point[i], this.point[i+1])) {
				System.out.println("Line not updated in draw Polygon");
			}
			l.drawLineBresenham(g, v, pixels);
			//Line l
		}
		if(!l.updateLine(v, this.point[i], this.point[0])) {
			System.out.println("Line not updated in draw Polygon");
		}
		l.drawLineBresenham(g, v, pixels);
	}
	
	/**
	 * Helper method for 8-points boundary fill
	 * @param g : Current Graphics Object
	 * @param v : Current View Object
	 * @param p : Current Pixels array
	 * @param interiorPoint : interior seed Point object
	 * @param boundaryColor : boundary Color object
	 * @param interiorColor : interior Color object
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
	 * Helper method for 8-points boundary fill
	 * @param g : Current Graphics Object
	 * @param v : Current View Object
	 * @param p : Current Pixels array
	 * @param interiorPoint : interior seed Point object
	 * @param boundaryColor : boundary Color object
	 * @param interiorColor : interior Color object
	 */
	private void fill8Points(Graphics g, View v, Pixel pixels, Point interiorPoint, 
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
			
			Point p[] = new Point[8];
			
			p[0] = new Point(v, x-1, y);
			this.fill8Points(g, v, pixels, p[0], boundaryColor, interiorColor);
			
			p[1] = new Point(v, x-1, y+1);
			this.fill8Points(g, v, pixels, p[1], boundaryColor, interiorColor);
			
			p[2] = new Point(v, x, y+1);
			this.fill8Points(g, v, pixels, p[2], boundaryColor, interiorColor);
			
			p[3] = new Point(v, x+1, y+1);
			this.fill8Points(g, v, pixels, p[3], boundaryColor, interiorColor);

			p[4] = new Point(v, x+1, y);
			this.fill8Points(g, v, pixels, p[4], boundaryColor, interiorColor);
			
			p[5] = new Point(v, x+1, y-1);
			this.fill8Points(g, v, pixels, p[5], boundaryColor, interiorColor);
			
			p[6] = new Point(v, x, y-1);
			this.fill8Points(g, v, pixels, p[6], boundaryColor, interiorColor);
			
			p[7] = new Point(v, x-1, y-1);
			this.fill8Points(g, v, pixels, p[7], boundaryColor, interiorColor);
			
			return;
		}
		
	}
	
	/**
	 * 
	 * @param g : Graphics Object
	 * @param v : View Object
	 * @param p : Pixels array
	 * @param interiorPoint : interior seed Point object
	 * @param boundaryColor : boundary Color object
	 * @param interiorColor : interior Color object
	 * @param choice : choice=false for 8-points fill, choice=true for 4-points fill
	 */
	public void boundryFill(Graphics g, View v, Pixel p, Point interiorPoint, 
		Color boundaryColor, Color interiorColor, boolean fourPointsFill) {
		g.setColor(interiorColor);
		if(fourPointsFill) {
			this.fill4Points(g, v, p, interiorPoint, boundaryColor, interiorColor);
		}
		else {
			this.fill8Points(g, v, p, interiorPoint, boundaryColor, interiorColor);
		}
	}
	
	/**
	 * Updates the polygon
	 * @param x - x coordinates array
	 * @param y - y coordinates array
	 * @param n - number of vertices
	 * @return - Returns true if polygon updated i.e. polygon in screen
	 */
	public boolean updatePolygon(View v, int x[], int y[], int n) {
		if(n!=x.length || n!=y.length) {
			System.out.println("Can't update polygon.\nSize mismatch of x and y array.");
			return false;
		}
		
		shapes.Point temp[];
		temp = this.point.clone();
		
		this.point = new shapes.Point[n];
		for(int i=0; i<n; i++) {
			point[i] = new shapes.Point(v, x[i], y[i]);
		}
		
		if(this.polygonInScreen(v)) {
			return true;
		}
		
		this.point = temp.clone();
		return false;
	}
	/**
	 * Updates the polygon
	 * @param v - Current View Object
	 * @param p - Current Pixel Object
	 * @return - Returns true if polygon updated i.e. polygon in screen
	 */
	public boolean updatePolygon(View v, Point p[]) {
		if((p.length==0)||(p==null)) {
			System.out.println("Null points array passed to polygon.");
			return false;
		}
		shapes.Point temp[];
		temp = this.point.clone();
		
		this.point = p.clone();
		
		if(this.polygonInScreen(v)) {
			return true;
		}
		
		this.point = temp.clone();
		return false;	
	}
	
	@Override
	public boolean scale(View v, Point pivotPoint, int scaleFactorX, int scaleFactorY, boolean scaleUp) {
		if(n>0) {
			shapes.Point temp[];
			temp = this.point.clone();
			
			for(int i=0; i<n; i++) {
				this.point[i].scale(v, pivotPoint, scaleFactorX, scaleFactorY, scaleUp);
			}
			
			if(this.polygonInScreen(v)) {
				return true;
			}
			
			this.point = temp.clone();
		}
		return false;
	}
	@Override
	public boolean translate(View v, int xMoveBy, int yMoveBy) {
		if(n>0) {
			shapes.Point temp[];
			temp = this.point.clone();
			
			for(int i=0; i<n; i++) {
				this.point[i].translate(v, xMoveBy, yMoveBy);
			}
			
			if(this.polygonInScreen(v)) {
				return true;
			}
			
			this.point = temp.clone();
		}
		return false;
	}
	@Override
	public boolean rotate(View v, Point pivotPoint, double theta) {
		if(n>0) {
			shapes.Point temp[];
			temp = this.point.clone();
			
			for(int i=0; i<n; i++) {
				this.point[i].rotate(v, pivotPoint, theta);
			}
			
			if(this.polygonInScreen(v)) {
				return true;
			}
			
			this.point = temp.clone();
		}
		return false;
	}
	
}
