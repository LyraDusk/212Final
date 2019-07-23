
/*
 * A single cell, with its x, y, and walls
 */
public class cell {
	
	//Logical x
	int x;
	
	//Logical y
	int y;
	
	
	//The walls, whether it has each wall
	boolean up;
	
	boolean down;
	
	boolean left;
	
	boolean right;
	
	//Whether the cell has been visited
	boolean visited;
	
	boolean deadend;
	
	public cell(int x, int y) {
		this.x = x;
		this.y = y;
		this.visited = false;
		up = true;
		down = true;
		left = true;
		right = true;
		deadend = false;

	}
	
	//Whether the cell is a dead end
	public boolean deadEnd() {
		return this.deadend;
	}
	
	//Whether the cell has been visited
	public boolean visited() {
		return this.visited;
	}
	
	//Returns the logical y value
	public int getY() {
		return y;
	}
	
	//Checks if the cell is within the structure bounds
	public boolean isWithinBounds(CellStructure struct) {
		if (x > struct.XMAX || x < 0 || y > struct.YMAX || y < 0) {
			return false;
		} else { 
			return true; 
		}
	}
	
	//returns the logical x value
	public int getX() {
		return x;
	}
}
