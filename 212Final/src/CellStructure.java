import java.util.ArrayList;
import java.util.List;

/*
 * The structure that keeps track of all of the cells in the grid
 */
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
		int i = 0;
		cell found = null;
		boolean finished = false;
		//System.out.println(cellList);
	//	try { 
		//System.out.println(cellList.size());
		int indicator = cellList.size();
		for(cell c: cellList) { //TODO: Throws a Concurrent Mod error, fix?
			//System.out.println(i);
			i ++;
			if (indicator != cellList.size()) {
				System.out.println("Um...");
				System.out.println("CellList size: " + cellList.size() + ", Indicator size: " + indicator);
			} 
			if(c.y == y && c.x == x) {
				found = c;
				finished = true;
			}
		}
		/*} catch(Exception e) {
			System.out.println("Concurrent Mod");
			//System.out.println(i);
			System.out.println(cellList.size());
			
			System.exit(0);
			
		}*/
		if(finished) {
			return found;
		}
		
		// If the cell doesn't exist and is within bounds, make it
		if(x < XMAX && y < YMAX ) {
			cell c = new cell(x, y);
			this.cellList.add(c);
			return c;
		} else {
			throw new IndexOutOfBoundsException("X or Y index out of grid boundary!");
		}
	}
	
	// Adds the cell to its list of cells
	public void register(cell c) {
		this.cellList.add(c);
	}
}
