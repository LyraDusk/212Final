import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class ClickSensor extends Frame implements MouseListener{
	
	MazeGen generator;
	
	MazeGraphic graphic;
	
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
			System.out.println("Correct!");
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
	
	public void mousePressed(MouseEvent e) {}
	
	public void mouseReleased(MouseEvent e) {}
}
