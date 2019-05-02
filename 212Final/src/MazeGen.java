import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;

public class MazeGen {
	
	
	int WIDTH;
	
	int HEIGHT;
	
	int tw;
	
	int th;
	
	List<cell> visited;
	
	cell position;
	
	CellStructure struct;

	public MazeGen() {
		
		WIDTH = 10;
		
		HEIGHT = 10;
		
		tw = 10;
		
		th = 10;
		
		struct = new CellStructure(WIDTH, HEIGHT);
		
		visited = new ArrayList<>();
		
	}

	public static void main(String[] args) {
		//Use PlayFish as a template for the world drawing
		System.out.println("Test");
		MazeGen x = new MazeGen();
		x.generateMaze();
		
	}
	
	public void generateMaze() {
		//Pick starting spot
		Random rand = new Random();
		int startx = rand.nextInt(this.WIDTH);
		int starty = rand.nextInt(this.HEIGHT);
		cell start = struct.find(startx, starty);
		start.visited = true;
		position = start;
		visited.add(start);
		
		JFrame world = new JFrame("MazeGen");
		Canvas canvas = new Canvas();
		canvas.setSize(1000,1000);
		world.add(canvas);
		world.pack();
        world.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        world.setVisible(true);
        
		
		while (true) {
			/*
			 * From position choose random direction
			 * if the cell in that direction is visited try again
			 * if not, remove the wall of the position cell and the wall of the destination cell
			 * mark destination cell as visited
			 */
			int index = rand.nextInt(3);
			String direction = position.walls.get(index);
			position.walls.remove(index);
			// REMOVE THE WALL
			if(direction.equals("up")) {
				position = struct.find(position.getX(), position.getY() - 1);
				position.walls.remove(1);
			} else if(direction.equals("down")) {
				position = struct.find(position.getX(), position.getY() + 1);
				position.walls.remove(0);
			} else if(direction.equals("left")) {
				position = struct.find(position.getX() - 1, position.getY());
				position.walls.remove(3);
			} else if(direction.equals("right")) {
				position = struct.find(position.getX() + 1, position.getY());
				position.walls.remove(2);
			}
			position.visited = true;
			this.visited.add(position);
			
		}
		
	}
	
	public void paint(Graphics g) {
		g.fillOval(100, 100, 200, 200);
	}
	/*
	public void draw(Graphics2D g) {
		// Background of window is dark-dark green.
		Color gray = new Color(128,128,128);
		g.setColor(gray);
		g.fillRect(0, 0, WIDTH*tw, HEIGHT*th);

		// Draw the ocean (not the whole screen).
		g.setColor(gray);
		g.fillRect(0, 0, tw * WIDTH, th * HEIGHT);
		// Draw a grid to better picture how the game works.
		g.setColor(gray.darker().darker());
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				g.drawRect(x * tw, y * th, tw, th);
			}
		}
		

	}
	*/
}
