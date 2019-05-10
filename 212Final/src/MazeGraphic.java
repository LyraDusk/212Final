import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MazeGraphic extends JPanel {
	int x = 0;
	int y = 0;
	MazeGen gen;
	CellStructure struct;
	boolean setup = false;
	
	public void getMaze(MazeGen maze) {
		this.gen = maze;
		this.struct = gen.struct;
	}
	
	
	public void paint(Graphics g) {
		super.paint(g);
		
		if (setup == false) {
			g.setColor(Color.gray);
			g.fillRect(0,0,400,400);
			setup = true;
		}
		g.setColor(Color.white);
		for (x = 0; x < 10; x++) {
			for (y = 0; y < 10; y ++) {
				if (this.gen.isVisited(x,y)) {
					g.setColor(Color.blue);
				} else if (gen.isDeadEnd(x, y)) {
					g.setColor(Color.orange);
				} else {
					g.setColor(Color.white);
				} 
				if (this.gen.isStart(x, y)) {
					g.setColor(Color.cyan);
				}
				//Fill in the square
				int x_act = x * 40 + 10;
				int y_act = y * 40 + 10;
				g.fillRect(x_act, y_act, 20,20);
				
				//Fill in the missing walls
				cell currentpos = this.struct.find(x, y);
				if (currentpos.down == false) {
					int ywall = y * 40 + 30;
					g.fillRect(x_act, ywall, 20, 20);
				} 
				if (currentpos.right == false) {
					int xwall = x * 40 + 30;
					g.fillRect(xwall, y_act, 20, 20);
				} 
				
			}
		}
	}
		
		
	
	
	
}
