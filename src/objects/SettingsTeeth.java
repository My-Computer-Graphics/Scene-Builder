package objects;

import shapes.*;
import java.awt.*;

public class SettingsTeeth {
	private int x1, x2, x3, x4;
	private int y1, y2, y3, y4;
	private int height, baseWidth, topWidth;
	private int quadrant;
	private shapes.Polygon teeth;
	
	public SettingsTeeth(View v, int xCenterBase, int yCenterBase, int height, int baseWidth, int topWidth ,int quadrant) {
		
		this.baseWidth = baseWidth;
		this.topWidth = topWidth;
		this.height = height;
		this.quadrant = quadrant;
		
		switch(quadrant){
			case 0:	this.initQuad0(xCenterBase, yCenterBase);
					break;
					
			case 1:	this.initQuad1(xCenterBase, yCenterBase);
					break;
			
			case 2:	this.initQuad2(xCenterBase, yCenterBase);
					break;
			
			case 3:	this.initQuad3(xCenterBase, yCenterBase);
					break;
			
			case 4:	this.initQuad4(xCenterBase, yCenterBase);
					break;
			
			case 5:	this.initQuad5(xCenterBase, yCenterBase);
					break;
			
			case 6:	this.initQuad6(xCenterBase, yCenterBase);
					break;
			
			case 7:	this.initQuad7(xCenterBase, yCenterBase);
					break;
					
			default:this.initQuad0(xCenterBase, yCenterBase);
					break;
		}
		
		int x[] = { x1, x2, x3, x4 };
		int y[] = { y1, y2, y3, y4 };
		
		teeth = new shapes.Polygon(v, x, y, 4);
		
	}
		
	private void initQuad0(int xCenterBase, int yCenterBase) {
		this.x1 = this.x4 = xCenterBase;
		this.x2 = this.x3 = xCenterBase + this.height;	
		
		this.y1 = yCenterBase - this.baseWidth/2;
		this.y4 = yCenterBase + this.baseWidth/2;
		
		this.y2 = yCenterBase - this.topWidth/2;
		this.y3 = yCenterBase + this.topWidth/2;
	}
	
	private void initQuad1(int xCenterBase, int yCenterBase) {
		double cos45 = Math.cos( Math.toRadians(45) );
		double sin45 = Math.sin( Math.toRadians(45) );
		double dxCenterBase = (double)xCenterBase;
		double dyCenterBase = (double)yCenterBase;
		
		
		double t1;
		
		t1 = cos45 * this.baseWidth/2;
		
		this.x1 = (int)(dxCenterBase + t1);
		this.x4 = (int)(dxCenterBase - t1);
		
		t1 = sin45*this.baseWidth/2;
		this.y1 = (int)(dyCenterBase - t1);
		this.y4 = (int)(dyCenterBase + t1);
		
		t1 = this.height*cos45;
		dxCenterBase += t1;
		t1 = this.height*sin45;
		dyCenterBase += t1;
		
		t1 = cos45*this.topWidth/2;
		this.x2 = (int)(dxCenterBase + t1);
		this.x3 = (int)(dxCenterBase - t1);
		
		t1 = sin45*this.topWidth/2;
		this.y2 = (int)(dyCenterBase - t1);
		this.y3 = (int)(dyCenterBase + t1);

	}
	

	private void initQuad2(int xCenterBase, int yCenterBase) {
		this.x1 = xCenterBase + this.baseWidth/2;
		this.x4 = xCenterBase - this.baseWidth/2;
		int temp = (this.baseWidth-this.topWidth)/2;
		
		this.x2 = this.x1-temp;
		this.x3 = this.x4+temp;
		
		this.y1 = this.y4 = yCenterBase;
		this.y2 = this.y3 = yCenterBase + this.height;
	}
	
	private void initQuad3(int xCenterBase, int yCenterBase) {
		double cos45 = Math.cos( Math.toRadians(45) );
		double sin45 = Math.sin( Math.toRadians(45) );
		double dxCenterBase = (double)xCenterBase;
		double dyCenterBase = (double)yCenterBase;
		
		double t1;
		
		t1 = cos45 * this.baseWidth/2;
		
		this.x1 = (int)(dxCenterBase + t1);
		this.x4 = (int)(dxCenterBase - t1);
		
		t1 = sin45*this.baseWidth/2;
		this.y1 = (int)(dyCenterBase + t1);
		this.y4 = (int)(dyCenterBase - t1);
		
		t1 = this.height*cos45;
		dxCenterBase -= t1;
		t1 = this.height*sin45;
		dyCenterBase += t1;
		
		t1 = cos45*this.topWidth/2;
		this.x2 = (int)(dxCenterBase + t1);
		this.x3 = (int)(dxCenterBase - t1);
		
		t1 = sin45*this.topWidth/2;
		this.y2 = (int)(dyCenterBase + t1);
		this.y3 = (int)(dyCenterBase - t1);
	}

	private void initQuad4(int xCenterBase, int yCenterBase) {
		this.x1 = this.x4 = xCenterBase;
		this.x2 = this.x3 = xCenterBase - this.height;	
		
		this.y1 = yCenterBase - this.baseWidth/2;
		this.y4 = yCenterBase + this.baseWidth/2;
		
		this.y2 = yCenterBase - this.topWidth/2;
		this.y3 = yCenterBase + this.topWidth/2;
	}

	private void initQuad5(int xCenterBase, int yCenterBase) {
		double cos45 = Math.cos( Math.toRadians(45) );
		double sin45 = Math.sin( Math.toRadians(45) );
		double dxCenterBase = (double)xCenterBase;
		double dyCenterBase = (double)yCenterBase;
		
		double t1;
		
		t1 = cos45 * this.baseWidth/2;
		
		this.x1 = (int)(dxCenterBase + t1);
		this.x4 = (int)(dxCenterBase - t1);
		
		t1 = sin45*this.baseWidth/2;
		this.y1 = (int)(dyCenterBase - t1);
		this.y4 = (int)(dyCenterBase + t1);
		
		t1 = this.height*cos45;
		dxCenterBase -= t1;
		t1 = this.height*sin45;
		dyCenterBase -= t1;
		
		t1 = cos45*this.topWidth/2;
		this.x2 = (int)(dxCenterBase + t1);
		this.x3 = (int)(dxCenterBase - t1);
		
		t1 = sin45*this.topWidth/2;
		this.y2 = (int)(dyCenterBase - t1);
		this.y3 = (int)(dyCenterBase + t1);	
	}

	private void initQuad6(int xCenterBase, int yCenterBase) {
		this.x1 = xCenterBase + this.baseWidth/2;
		this.x4 = xCenterBase - this.baseWidth/2;
		int temp = (this.baseWidth-this.topWidth)/2;
		
		this.x2 = this.x1-temp;
		this.x3 = this.x4+temp;
		
		this.y1 = this.y4 = yCenterBase;
		this.y2 = this.y3 = yCenterBase - this.height;
	}
	
	private void initQuad7(int xCenterBase, int yCenterBase) {
		double cos45 = Math.cos( Math.toRadians(45) );
		double sin45 = Math.sin( Math.toRadians(45) );
		double dxCenterBase = (double)xCenterBase;
		double dyCenterBase = (double)yCenterBase;
		
		double t1;
		
		t1 = cos45 * this.baseWidth/2;
		
		this.x1 = (int)(dxCenterBase + t1);
		this.x4 = (int)(dxCenterBase - t1);
		
		t1 = sin45*this.baseWidth/2;
		this.y1 = (int)(dyCenterBase + t1);
		this.y4 = (int)(dyCenterBase - t1);
		
		t1 = this.height*cos45;
		dxCenterBase += t1;
		t1 = this.height*sin45;
		dyCenterBase -= t1;
		
		t1 = cos45*this.topWidth/2;
		this.x2 = (int)(dxCenterBase + t1);
		this.x3 = (int)(dxCenterBase - t1);
		
		t1 = sin45*this.topWidth/2;
		this.y2 = (int)(dyCenterBase + t1);
		this.y3 = (int)(dyCenterBase - t1);
	}
	
	public void draw(Graphics g, View v, Pixel pixels, Color fillColor) {
		shapes.Point interiorPoint = new shapes.Point(v, 0, 0);
	
		switch(this.quadrant) {
			case 0:	interiorPoint.updatePoint(v, this.x1+1, (this.y1+this.y4)/2);
					break;
					
			case 1:	interiorPoint.updatePoint(v, (this.x1+this.x3)/2, (this.y1+this.y3)/2);
					break;
					
			case 2:	interiorPoint.updatePoint(v, (this.x1+this.x4)/2, this.y1+1);
					break;
					
			case 3:	interiorPoint.updatePoint(v, this.x1-1, this.y1);
					break;
					
			case 4:	interiorPoint.updatePoint(v, this.x1-1, (this.y1+this.y4)/2);
					break;
					
			case 5:	interiorPoint.updatePoint(v, this.x4, this.y4-1);
					break;
					
			case 6:	interiorPoint.updatePoint(v, (this.x1+this.x4)/2, this.y1-1);
					break;
					
			case 7:	interiorPoint.updatePoint(v, (this.x1+this.x3)/2, (this.y1+this.y3)/2);
					break;
					
			default: interiorPoint.updatePoint(v, this.x1+1, (this.y1+this.y4)/2);
					break;
		}
		
		
		g.setColor(fillColor);
		teeth.draw(g, v, pixels);
		teeth.boundryFill(g, v, pixels, interiorPoint, fillColor, fillColor, true);
		
		shapes.Line bl = new shapes.Line(v, this.x1, this.y1, this.x2, this.y2);
		g.setColor(Color.black);
		bl.drawLineBresenham(g, v, pixels);
		
		bl.updateLine(v, x2, y2, x3, y3);
		bl.drawLineBresenham(g, v, pixels);
		
		bl.updateLine(v, x3, y3, x4, y4);
		bl.drawLineBresenham(g, v, pixels);
	}
}
