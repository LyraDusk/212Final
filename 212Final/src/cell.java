import java.util.List;

public class cell {
	
	int x;
	
	int y;
	
	List<String> walls;
	
	boolean visited;
	
	public cell(int x, int y) {
		this.x = x;
		this.y = y;
		walls.add("up");
		walls.add("down");
		walls.add("left");
		walls.add("right");
		this.visited = false;
	}
	
	public boolean visited() {
		return this.visited;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getX() {
		return this.x;
	}
}
