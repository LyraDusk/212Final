import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class ClickSensor extends Frame implements MouseListener, MouseMotionListener{
	
	MazeGen generator;
	
	MazeGraphic graphic;
	
	boolean press;
	
	int gwidth = generator.WIDTH * generator.MULT;
	
	int gheight = generator.HEIGHT * generator.MULT;
	
	int mult = generator.MULT;
	
	
	
	ClickSensor() {
		addMouseListener(this);
		
		
		
	}
	
	public void mouseClicked(MouseEvent e) {
		
		int x = e.getX() / MazeGen.MULT;
		int y = e.getY() / MazeGen.MULT - 1;
		if(!MazeGen.inGUI) {
		try {
		cell c = generator.struct.find(x,y);
		c.toggleClick();
		} catch (Exception z) {
			System.out.println("Not a cell!");
		}
		
		if(MazeGen.finished && generator.checkAnswer()) {
				graphic.ClickColor = Color.green;
				generator.solved = true;
				System.out.println("Correct!");
			}
		
		graphic.repaint();
		}
		
	}
	
	public void mousePressed(MouseEvent e) {
		press = true;
		int x = e.getX();
		
		int y = e.getY();
		if(MazeGen.inGUI) {
			
			if(x<gwidth/3 * 2 && x > gwidth/3) {
				if(y < gheight/2 + gheight/10 + gheight/15&& y > gheight/2 - gheight/10 + gheight/15) {
					graphic.YesColor = Color.green.darker();
					generator.sensorOutput = 0;
					
				}
				
				if(y>gheight/3*2 + gheight/15 && y < gheight/3*2 + gheight/5 + gheight/15) {
					graphic.NoColor = Color.red.darker();
					generator.sensorOutput = 1;
				}
			}
			
			graphic.repaint();
		}
				
	}
	
	public void mouseReleased(MouseEvent e) {
		if(MazeGen.inGUI) { 
		int x = e.getX();
		int y = e.getY();
		press = false;
		if(x<gwidth/3 * 2 && x > gwidth/3) {
			if(y < gheight/2 + gheight/10 + gheight/15 && y > gheight/2 - gheight/10 + gheight/15) {
				generator.sensorOutput = 0;
				
			}
			
			if(y>gheight/3*2 + gheight/15 && y < gheight/3*2 + gheight/5 + gheight/15) {
				generator.sensorOutput = 1;
				
			}
			graphic.YesColor = Color.green;
			graphic.NoColor = Color.red;
		}
		}
		graphic.repaint();
	}
		
	
	public void importMazeGen(MazeGen gen) {
		this.generator = gen;
		
	}
	public void importGraphic(MazeGraphic graphic) {
		this.graphic = graphic;
	}

	public void mouseEntered(MouseEvent e) {}
	
	public void mouseExited(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {
		//System.out.println("Mouse dragged!");
		int x = e.getX() / MazeGen.MULT;
		int y = e.getY() / MazeGen.MULT - 1;
		
		if(!MazeGen.inGUI) {
		try {
		cell c = generator.struct.find(x,y);
		c.clicked = true;
		} catch (Exception z) {
			//System.out.println("Not a cell!");
		}
		
		if(MazeGen.finished && generator.checkAnswer()) {
				graphic.ClickColor = Color.green;
				MazeGen.solved = true;
				//System.out.println("Correct!");
			}
		
		graphic.repaint();
		}
	}

	public void mouseMoved(MouseEvent e) {
		
		
	}
	
	
}
