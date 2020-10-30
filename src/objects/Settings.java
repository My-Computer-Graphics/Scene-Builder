package objects;

import java.awt.Color;
import java.awt.Graphics;

import shapes.Pixel;
import shapes.View;

public class Settings {

	private int xCenter, yCenter;
	Color settingsColor, boundaryColor;
	shapes.Circle innerCircle, outerCircle;
	SettingsTeeth teeth[];
	
	public Settings(View v, int xCenter, int yCenter, int innerRadius, int outerRadius,
				int teethHeight, int teethBaseWidth, int teethTopWidth) {
		boundaryColor = Color.black;
		settingsColor = new Color(144,164,173);
		
		this.xCenter = xCenter;
		this.yCenter = yCenter;
		
		innerCircle = new shapes.Circle(v, innerRadius, this.xCenter, this.yCenter);
		outerCircle  = new shapes.Circle(v, outerRadius, this.xCenter, this.yCenter);
		
		teeth = new SettingsTeeth[8];
		
		int rx, ry;
		rx= (int)((double)outerRadius*Math.cos(Math.toRadians(45)));
		ry= (int)((double)outerRadius*Math.sin(Math.toRadians(45)));
		
		teeth[0] = new SettingsTeeth(v, xCenter+outerRadius, yCenter, teethHeight, teethBaseWidth, teethTopWidth ,0);
		teeth[1] = new SettingsTeeth(v, xCenter+rx, yCenter+ry, teethHeight, teethBaseWidth, teethTopWidth ,1);
		teeth[2] = new SettingsTeeth(v, xCenter, yCenter+outerRadius, teethHeight, teethBaseWidth, teethTopWidth ,2);
		teeth[3] = new SettingsTeeth(v, xCenter-rx, yCenter+ry, teethHeight, teethBaseWidth, teethTopWidth ,3);
		teeth[4] = new SettingsTeeth(v, xCenter-outerRadius, yCenter, teethHeight, teethBaseWidth, teethTopWidth ,4);
		teeth[5] = new SettingsTeeth(v, xCenter-rx, yCenter-ry, teethHeight, teethBaseWidth, teethTopWidth ,5);
		teeth[6] = new SettingsTeeth(v, xCenter, yCenter-outerRadius, teethHeight, teethBaseWidth, teethTopWidth ,6);
		teeth[7] = new SettingsTeeth(v, xCenter+rx, yCenter-ry, teethHeight, teethBaseWidth, teethTopWidth ,7);
	}
	
	public void draw(View v, Graphics g, Pixel pixels) {
		shapes.Point interiorPoint = new shapes.Point(v, xCenter, yCenter);
		
		g.setColor(boundaryColor);
		this.outerCircle.draw(g, v, pixels);
		this.outerCircle.fill(g, v, pixels, interiorPoint, boundaryColor, settingsColor);
		
		g.setColor(boundaryColor);
		this.innerCircle.draw(g, v, pixels);
		this.innerCircle.fill(g, v, pixels, interiorPoint, boundaryColor, new Color(184, 223, 230));
		
		for(int i=0; i<7; i++){
			teeth[i].draw(g, v, pixels, settingsColor);
		}
		
	}
	
}
