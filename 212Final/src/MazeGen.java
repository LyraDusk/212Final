import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;



public class MazeGen {
	
	// Logical width, aka number of cells across
	int WIDTH;
	
	// Logical height, number of cells down
	int HEIGHT;
	
	// list containing up, down, left, and right for random sampling
	List<String> wallIndices = new ArrayList<>();
	
	// list of visited cells
	List<cell> visited;
	
	// list of dead end cells
	List<cell> deadEnd;
	
	// current position
	cell position;
	
	// the cell structure that keeps track of the cells
	CellStructure struct;
	
	// whether or not the generator has finished
	static boolean finished = false;

	
	// Initializing the maze generator
	public MazeGen() {
		
		WIDTH = 10;
		
		HEIGHT = 10;
		
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
		frame.setSize(400,420);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		//generate the maze
		gen.generateMaze(graphic);
		  
		
	}
	
	public void generateMaze(MazeGraphic graphic) {
		//Pick starting spot
		Random rand = new Random();
		int startx = rand.nextInt(this.WIDTH);
		int starty = rand.nextInt(this.HEIGHT);
		cell start = struct.find(startx, starty);
		start.visited = true;
		
		//set that starting spot to the current position
		position = start;
		visited.add(start);
        
		
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
					visited.remove(visited.size() - 1);
					checked.removeAll(checked);
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
					} else if(direction.equals("right")) {
						newcell = struct.find(position.getX() + 1, position.getY());
					}
				} catch (IndexOutOfBoundsException e) {
					newCellFound = false;
					checked.add(direction);
					System.out.println("Bad direction!");
					continue;
					
				}
				//If the new cell is not visited or a dead end
				if (!isVisited(newcell.getX(), newcell.getY()) && !isDeadEnd(newcell.getX(), 
						newcell.getY()) && newcell.isWithinBounds(struct)) {
					newCellFound = true;
					checked.removeAll(checked);
					System.out.println("New cell found!");
					
				} else {
					newCellFound = false;
					checked.add(direction);
					continue;
				}
			
			}
			if (finished == true) {
				break;
			}
			position = newcell;
			position.visited = true;
			visited.add(position);
			graphic.repaint();
			try {Thread.sleep(100);} catch (Exception e) {}
			
		}
	
		System.out.println("Finished!");
	}
	
	public boolean isVisited(int x, int y) {
		boolean found = false;
		for(cell c: visited) {
			if(c.getY() == y && c.getX() == x) {
				found = true;
			}
		}
		return found;
	}
	
	public boolean isDeadEnd(int x, int y) {
		boolean found = false;
		for(cell c: deadEnd) {
			if(c.getY() == y && c.getX() == x) {
				found = true;
			}
		}
		return found;
	}

}
