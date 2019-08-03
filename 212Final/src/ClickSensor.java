import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class ClickSensor extends Frame implements MouseListener, MouseMotionListener{
	
	MazeGen generator;
	
	MazeGraphic graphic;
	
	boolean press;
	
	
	
	ClickSensor() {
		addMouseListener(this);
		
		
		
	}
	
	public void mouseClicked(MouseEvent e) {
		
		int x = e.getX() / MazeGen.MULT;
		int y = e.getY() / MazeGen.MULT - 1;
		
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
	
	public void mousePressed(MouseEvent e) {
		press = true;
		
	}
	
	public void mouseReleased(MouseEvent e) {
		press = false;
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

	public void mouseMoved(MouseEvent e) {
		
		
	}
	
	
}
