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
	
	// list holding the cells on the longest route (for non-random finish solving)
	List<cell> longestRoute;
	
	// current position
	cell position;
	
	// starting cell
	cell start;
	
	// the cell structure that keeps track of the cells
	CellStructure struct;
	
	// whether or not the generator has finished
	static boolean finished = false;
	
	
	/*
	 * GENERATION CONTROLS
	 */
	
	// Whether to randomly determine the finishing point, rather than
	// using the farthest cell from start as the finishing point
	static boolean randFinish = false;
	
	//True: Finishing point is always on an edge
	//False: Finishing point could be edge or central
	//The ending point is always a dead end with three walls.
	//Only relevant if randFinish is true. 
	static boolean edgeFinish = true;
	
	
	//Shows the maze solution. ONLY WORKS FOR RANDFINISH FALSE.
	static boolean showSolution = false;
	
	//Turns on and off line smoothing
	static boolean lineSmoothing = true;
	
	//Smoothing level
	//0-20, recommended 5-10. 0 is smoothing off, 20 draws spirals. 
	static int smoothLevel = 7;
	
	// Whether or not to add a game aspect: allow clicking cells to win
	static boolean game = true;
	
	// Initializing the maze generator
	public MazeGen() {
		
		WIDTH = 10;
		
		HEIGHT = 10;
		
		MULT = 40;
		
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
		if(game) {
		ClickSensor sensor = new ClickSensor();
		frame.addMouseListener(sensor);
		sensor.importMazeGen(gen);
		sensor.importGraphic(graphic);
		}
		//generate the maze
		gen.generateMaze(graphic);
		  
		
	}
	
	public void generateMaze(MazeGraphic graphic) {
		//Initialize the grid
		for(int x = 0; x < WIDTH; x++) {
			System.out.println("Initializing grid row " + (x + 1) + "/" + WIDTH);
			for(int y = 0; y < HEIGHT; y ++) {
				cell c = new cell(x, y);
				struct.register(c);
			}
		}
		
		//Pick starting spot
		Random rand = new Random();
		int startx = rand.nextInt(WIDTH);
		int starty = rand.nextInt(HEIGHT);;
		//OVERRIDE
		//starty = 0;
		//startx = 0;
		start = struct.find(startx, starty);
		start.visited = true;
		start.dist = 0;
		
		//set that starting spot to the current position
		position = start;
		start.start = true;
		visited.add(start);
        
		int distanceCounter = 0;
		int maxDist = 0;
		cell furthestCell = null;
		String lastPos = "up";
		
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
					distanceCounter --;
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
				if(lineSmoothing && rand.nextInt(20) <= smoothLevel && !checked.contains(lastPos) ) {
					direction = lastPos;
				}
				
				//Get the cell in the chosen direction
				try {
					if(direction.equals("up")) {
						newcell = struct.find(position.getX(), position.getY() - 1);
						lastPos = "up";
					} else if(direction.equals("down")) {
						newcell = struct.find(position.getX(), position.getY() + 1);
						lastPos = "down";
					} else if(direction.equals("left")) {
						newcell = struct.find(position.getX() - 1, position.getY());
						lastPos = "left";
					} else/* if(direction.equals("right"))*/ {
						newcell = struct.find(position.getX() + 1, position.getY());
						lastPos = "right";
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
					distanceCounter ++;
					// Update max distance counters
					if(distanceCounter > maxDist) {
						maxDist = distanceCounter;
						furthestCell = newcell;
						longestRoute = new ArrayList<>(visited);
					}
					newcell.dist = distanceCounter;
					//System.out.println(distanceCounter);
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
				
				if(randFinish) {
				boolean finishFound = false;
				while (finishFound == false) {
					if(!edgeFinish) {
					int endY = rand.nextInt(HEIGHT);
					int endX = rand.nextInt(WIDTH);
					cell last = struct.find(endX, endY);
					int walls = 0;
					if(last.up) {walls ++;}
					if(last.down) {walls ++;}
					if(last.right) {walls ++;}
					if(last.left) {walls ++;}
				
					if(walls == 3 && last != start) {
						last.finish = true;
						break;
					} 
					}
					
					if(edgeFinish) {
						cell last;
						int endY = rand.nextInt(HEIGHT);
						int endX = rand.nextInt(WIDTH);
						int edge = rand.nextInt(4);
						if(edge == 1) {
							last = struct.find(endX, 0);
						} else if(edge == 2) {
							last = struct.find(WIDTH - 1, endY);
						} else if(edge == 3) {
							last = struct.find(endX, HEIGHT - 1);
						} else {
							last = struct.find(0, endY);
						}
						
						int walls = 0;
						if(last.up) {walls ++;}
						if(last.down) {walls ++;}
						if(last.right) {walls ++;}
						if(last.left) {walls ++;}
					
						if(walls == 3 && last != start) {
							last.finish = true;
							break;
						} 
						
					}
					
				}
				}
				
				if(!randFinish) {
					furthestCell.finish = true;
					//if(showSolution) {
					for(cell c: longestRoute) {
						c.longestroute = true;
					}
					//}
					break;
				}
				
				graphic.repaint();
				//break;
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
		//System.out.println("Max Distance from Start: " + maxDist);
		
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
	
	public int distFromStart(int x, int y) {
		cell c = struct.find(x, y);
		return c.distance();
	}
	
	public boolean isFinish(int x, int y) {
		cell c = struct.find(x, y);
		return c.isFinish();
	}
	
	public boolean isSolution(int x, int y) {
		cell c = struct.find(x, y);
		return c.onLongest();
	}
	
	public boolean isClicked(int x, int y) {
		cell c = struct.find(x, y);
		return c.getClicked();
	}
	
	public boolean isCorrect(int x, int y) {
		cell c = struct.find(x, y);
		return c.clickCorrect();
	}
	
	public boolean checkAnswer() {
		cell c = null;
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT; y++) {
				c = struct.find(x, y);
				if(!c.clickCorrect()) {
					return false;
				}
			}
		}
		return true;
	}

}
