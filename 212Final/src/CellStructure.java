import java.util.ArrayList;
import java.util.List;

public class CellStructure {
	
	/*
	 * Number of X rows in the grid
	 */
	int XMAX;
	
	/*
	 * Number of Y rows in the grid
	 */
	int YMAX;
	
	/*
	 * The cell at 0,0
	 */
	cell root;
	
	/*
	 * List of all cells in the grid
	 */
	List<cell> cellList = new ArrayList<>();
	
	public CellStructure(int x, int y){
		this.root = new cell(0,0);
		XMAX = x;
		YMAX = y;
		this.cellList.add(root);
	}
	
	// returns the specified cell, or creates it and registers it if it doesn't exist
	public cell find(int x, int y) {
		for(cell c: cellList) {
			if(c.getY() == y && c.getX() == x) {
				return c;
			}
		}
		if(x < XMAX && y < YMAX ) {
			cell c = new cell(x, y);
			this.cellList.add(c);
			return c;
		} else {
			throw new IndexOutOfBoundsException("X or Y index out of grid boundary!");
		}
	}
	
	public void register(cell c) {
		this.cellList.add(c);
	}
}
