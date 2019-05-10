import java.util.ArrayList;
import java.util.List;

public class cell {
	
	int x;
	
	int y;
	
	boolean up;
	
	boolean down;
	
	boolean left;
	
	boolean right;
	
	boolean visited;
	
	public cell(int x, int y) {
		this.x = x;
		this.y = y;
		this.visited = false;
		up = true;
		down = true;
		left = true;
		right = true;

	}
	
	public boolean visited() {
		return this.visited;
	}
	
	public int getY() {
		return this.y;
	}
	
	public boolean isWithinBounds(CellStructure struct) {
		if (x > struct.XMAX || x < 0 || y > struct.YMAX || y < 0) {
			return false;
		} else { 
			return true; 
		}
	}
	
	public int getX() {
		return this.x;
	}
}
