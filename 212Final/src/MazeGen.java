import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;

/*
 * Generates a maze on a ten by ten logical grid
 */
public class MazeGen {
	
	// Logical width, aka number of cells across
	// Recommended Minimum 5
	static int WIDTH;
	
	// Logical height, number of cells down
	// Recommended Minimum 5
	static int HEIGHT;
	
	// Graphical Grid Multiplier (MUST BE DIVISBLE BY 2)
	// Recommended Minimum 16 for good performance, can go smaller for large logical grids
	static int MULT;
	
	// Speed of Generator (Sleep time in ms)
	static int WAIT;
	
	// list containing up, down, left, and right for random sampling
	List<String> wallIndices = new ArrayList<>();
	
	// list of visited cells
	List<cell> visited;
	
	// list of dead end cells
	List<cell> deadEnd;
	
	// current position
	cell position;
	
	// starting cell
	cell start;
	
	// the cell structure that keeps track of the cells
	CellStructure struct;
	
	// whether or not the generator has finished
	static boolean finished = false;

	
	// Initializing the maze generator
	public MazeGen() {
		
		WIDTH = 3000;
		
		HEIGHT = 3000;
		
		MULT = 2;
		
		WAIT = 0;
		
		struct = new CellStructure(WIDTH, HEIGHT);
		
		visited = new ArrayList<>();
		
		deadEnd = new ArrayList<>();
		
		finished = false;
		
		//setup the wallindices list
		wallIndices.add("up");
		wallIndices.add("down");
		wallIndices.add("left");
		wallIndices.add("right");
		
	}

	public static void main(String[] args) {
		//initialize the graphics stuff
		JFrame frame = new JFrame("MazeGen");
		MazeGen gen = new MazeGen();
		MazeGraphic graphic = new MazeGraphic();
		graphic.getMaze(gen);
		frame.add(graphic);
		frame.setVisible(true);
		frame.setSize(WIDTH * MULT + 20, HEIGHT * MULT + 40);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		//generate the maze
		gen.generateMaze(graphic);
		  
		
	}
	
	public void generateMaze(MazeGraphic graphic) {
		//Initialize the grid
		for(int x = 0; x < WIDTH; x++) {
			System.out.println("Initializing grid row " + (x + 1));
			for(int y = 0; y < HEIGHT; y ++) {
				cell c = new cell(x, y);
				struct.register(c);
			}
		}
		
		//Pick starting spot
		Random rand = new Random();
		int startx = rand.nextInt(WIDTH);
		int starty = rand.nextInt(HEIGHT);
		start = struct.find(startx, starty);
		start.visited = true;
		
		//set that starting spot to the current position
		position = start;
		visited.add(start);
        
		mainloop: 
		while (true) {
			/*
			 * From position choose random direction
			 * if the cell in that direction is visited try again
			 * if not, remove the wall of the position cell and the wall of the destination cell
			 * mark destination cell as visited
			 */
			
			//setting up variables
			boolean newCellFound = false;
			int wallIndex = -1;
			cell newcell = null;
			
			//A set of all the directions tried this iteration
			Set<String> checked = new HashSet<>();
			
			//While we haven't found a valid new cell, keep checking adjacent cells
			while (newCellFound == false) {
				wallIndex = rand.nextInt(4);
				
				//If we've checked all directions
				if (checked.size() == 4) {	
					//move this cell to the deadEnd list from the visited list
					deadEnd.add(position);
					position.deadend = true;
					visited.remove(visited.size() - 1);
					position.visited = false;
					checked.removeAll(checked);
					if (visited.size() != 0) {
						position = visited.get(visited.size() - 1);
						try {Thread.sleep(WAIT);} catch (Exception e) {}
						graphic.repaint();
						continue mainloop;
					}
					//if this was the last item in visited, we're done
					if (visited.size() == 0) {
						finished = true;
						break;
					}
					//set position to the last of the visited list
					position = visited.get(visited.size() - 1);
				}
				String direction = wallIndices.get(wallIndex);
				
				//Get the cell in the chosen direction
				try {
					if(direction.equals("up")) {
						newcell = struct.find(position.getX(), position.getY() - 1);
					} else if(direction.equals("down")) {
						newcell = struct.find(position.getX(), position.getY() + 1);
					} else if(direction.equals("left")) {
						newcell = struct.find(position.getX() - 1, position.getY());
					} else/* if(direction.equals("right"))*/ {
						newcell = struct.find(position.getX() + 1, position.getY());
					}
				} catch (IndexOutOfBoundsException e) {
					newCellFound = false;
					checked.add(direction);
					//System.out.println("Bad direction!");
					continue;
					
				}
				//If the new cell is not visited or a dead end
				if (!isVisited(newcell.getX(), newcell.getY()) && !isDeadEnd(newcell.getX(), 
						newcell.getY()) && newcell.isWithinBounds(struct)) {
					newCellFound = true;
					checked.removeAll(checked);
					//System.out.println("New cell found!");
					// Remove the correct walls
					if (direction == "up") {
						newcell.down = false;
						position.up = false;
					} else if (direction == "down") {
						newcell.up = false;
						position.down = false;
					} else if (direction == "left") {
						newcell.right = false;
						position.left = false;
					} else if (direction == "right") {
						newcell.left = false;
						position.right = false;
					}
					
				} else {
					//Keep looking
					newCellFound = false;
					checked.add(direction);
					continue;
				}
			
			}
			// Are we done?
			if (finished == true) {
				graphic.repaint();
				break;
			}
			//Move position to the new cell, mark as visited
			position = newcell;
			position.visited = true;
			visited.add(position);
			try {Thread.sleep(WAIT);} catch (Exception e) {}
			graphic.repaint();
			
			
		}
		// Done!
		System.out.println("Finished!");
	}
		
	
	//Checks if the given x and y correspond to a visited cell
	public boolean isVisited(int x, int y) { 
		cell c = struct.find(x, y);
		return c.visited;
	}
	
	//Checks if the given x and y correspond to the starter cell
	public boolean isStart(int x, int y) {
		if(start.getY() == y && start.getX() == x) {
			return true;
		} else {
			return false;
		}
	}
	
	//Checks if the given x and y correspond to a cell in the dead end list
	public boolean isDeadEnd(int x, int y) {
		cell c = struct.find(x, y);
		return c.deadEnd();
	}
	

}
