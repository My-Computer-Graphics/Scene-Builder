package shapes;
import java.awt.Color;
import java.awt.Graphics;

public class Grid {
	private Color gridColor;
	
	/**
	 * 
	 * @param gridColor - Color object for the grid line color
	 */
	public Grid(Color gridColor){
		this.gridColor = gridColor;
	}
	/**
	 * 
	 * @param g: Graphics object
	 * @param v: View object containing the screen coordinates
	 */
	public void drawGrid(Graphics g, View v){
		g.setColor(gridColor);
		
		int h, w;
		
		//drawing horizontal lines in the upper half
		for(h=v.getyOrigin(); h>=0; h-=v.getyPadding()) {
			g.drawLine(0, h, v.getScreenWidth(), h);
		}
		
		//drawing horizontal lines in the lower half
		
		for(h=v.getyOrigin(); h<=v.getScreenHeight(); h+=v.getyPadding()) {
			g.drawLine(0, h, v.getScreenWidth(), h);
		}
		
		//drawing vertical lines in the left half
		
		for(w=v.getxOrigin(); w>=0; w-=v.getyPadding()){
			g.drawLine(w, 0, w, v.getScreenHeight());
		}

		//drawing vertical lines in the right half
		
		for(w=v.getxOrigin(); w<=v.getScreenWidth(); w+=v.getxPadding()){
			g.drawLine(w, 0, w, v.getScreenHeight());
		}
		
		//Axes
		g.setColor(Color.black);
    	g.drawLine(0, v.getyOrigin(), v.getScreenWidth(), v.getyOrigin());
  
    	g.drawLine(v.getxOrigin(), 0, v.getxOrigin(), v.getScreenHeight());
	}

}
