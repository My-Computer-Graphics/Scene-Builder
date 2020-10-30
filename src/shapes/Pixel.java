package shapes;
import java.awt.*;

public class Pixel {
	int p[][];
	int xmin, ymin;
	
	public Pixel(View v) {
		int x, y;
		
		x = v.getRightX() - v.getLeftX() + 1;
		y = v.getTopY()-v.getBottomY()+1;
		
		xmin = v.getLeftX();
		ymin = v.getBottomY();
		
		p = new int [x][y];
		
	}
	
	public void updateAllPixels(Color c) {
		int t = c.getRGB();
		
		for(int i=0; i< p.length; i++) {
			for(int j=0; j<p[i].length ; j++) {
				p[i][j] = t;
			}
		}
	}
	
	public void setPixel(Color c, int x, int y) {
		if( (x<xmin) || (x>p.length) || (y<ymin) || (y>p[0].length)) {
			System.out.println("Attemp to set an out of screen pixel");
			return;
		}
		
		int t = c.getRGB();
		p[x-xmin][y-ymin] = t;
	}
	
	public int getPixel(int x, int y) {
		if( (x<xmin) || (x>p.length) || (y<ymin) || (y>p[0].length)) {
			System.out.println("Attemp to access an out of screen pixel");
			return 0;
		}
		return p[x-xmin][y-ymin];
	}
}
