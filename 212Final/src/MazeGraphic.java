import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/*
 * The graphical engine of the generator
 */
public class MazeGraphic extends JPanel {
	int x = 0;
	int y = 0;
	MazeGen gen;
	int xmax;
	int ymax;
	int multiplier;
	CellStructure struct;
	boolean setup = false;
	
	//Gets a reference to the MazeGen and CellStructure (I didn't want to deal with overwriting the constructor)
	public void getMaze(MazeGen maze) {
		this.gen = maze;
		this.struct = gen.struct;
		this.xmax = MazeGen.WIDTH;
		this.ymax = MazeGen.HEIGHT;
		this.multiplier = MazeGen.MULT;
	}
	
	
	public void paint(Graphics g) {
		super.paint(g);
		
		g.setColor(Color.lightGray);
		g.fillRect(0,0,xmax * multiplier,ymax * multiplier);
		g.setColor(Color.white);
		
		// For every grid square (Every x, and then every y for each x)
		for (x = 0; x < xmax; x++) {
			for (y = 0; y < ymax; y ++) {
				//Sets the color to correspond to cell status (Blue is visited, Cyan is start, Orange is dead end)
				if (this.gen.isVisited(x,y)) {
					g.setColor(Color.blue);
				} else if (gen.isDeadEnd(x, y)) {
					g.setColor(Color.green);
				} else {
					g.setColor(Color.white);
				} 
				if (this.gen.isStart(x, y)) {
					g.setColor(Color.cyan);
				}
				//Fill in the square
				int x_act = x * multiplier + multiplier/4;
				int y_act = y * multiplier + multiplier/4;
				g.fillRect(x_act, y_act, multiplier/2,multiplier/2);
				
				//Fill in the missing walls
				cell currentpos = this.struct.find(x, y);
				if (currentpos.down == false) {
					int ywall = y * multiplier + multiplier * 3/4;
					g.fillRect(x_act, ywall, multiplier/2, multiplier/2);
				} 
				if (currentpos.right == false) {
					int xwall = x * multiplier + multiplier * 3/4;
					g.fillRect(xwall, y_act, multiplier/2, multiplier/2);
				} 
			}
		}
	}
}
