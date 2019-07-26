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
	
	Color DEColor = Color.black;
	Color VColor = Color.cyan;
	Color StartColor = Color.red;
	Color EndColor = Color.orange;
	Color SolColor = Color.green;
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
				
				cell current = struct.find(x, y);
				Color cellColor = null;
				
				if (this.gen.isVisited(x,y)) {
					cellColor = VColor;
				} else if (gen.isDeadEnd(x, y)) {
					cellColor = DEColor;
				} else {
					cellColor = Color.white;
				} 
				if (this.gen.isSolution(x, y)) {
					cellColor = SolColor;
				}
				if (this.gen.isStart(x, y)) {
					cellColor = StartColor;
				}
				if (this.gen.isFinish(x,y)) {
					cellColor = EndColor;
				}
				g.setColor(cellColor);
				//Fill in the square
				int x_act = x * multiplier + multiplier/4;
				int y_act = y * multiplier + multiplier/4;
				g.fillRect(x_act, y_act, multiplier/2,multiplier/2);
				
				
				//Fill in the missing walls
				cell currentpos = this.struct.find(x, y);
				if (currentpos.down == false) {
					cell lower = struct.find(x, y + 1);
					if (lower.deadend && !lower.longestroute) {
						g.setColor(DEColor);
					}
					if(this.gen.isStart(x, y + 1)) {
						g.setColor(StartColor);
					} else if (this.gen.isFinish(x, y + 1)) {
						g.setColor(EndColor);
					}
					
					int ywall = y * multiplier + multiplier * 3/4;
					g.fillRect(x_act, ywall, multiplier/2, multiplier/2);
					g.setColor(cellColor);
				} 
				if (currentpos.right == false) {
					cell right = struct.find(x + 1, y);
					if (right.deadend && !right.onLongest()) {
						g.setColor(DEColor);
					} 
					if(this.gen.isStart(x + 1, y)) {
						g.setColor(StartColor);
					} else if (this.gen.isFinish(x + 1, y)) {
						g.setColor(EndColor);
					}
					
					int xwall = x * multiplier + multiplier * 3/4;
					g.fillRect(xwall, y_act, multiplier/2, multiplier/2);
				} 
			}
		}
	}
}
