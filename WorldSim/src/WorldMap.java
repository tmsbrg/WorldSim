import java.awt.*;


public class WorldMap extends Component {

	private static final long serialVersionUID = 4025499860418720647L;
	
	private static final int TILE_SIZE = 16;
	private int width = 21;
	private int height = 15;
	
	public WorldMap()
	{
        setPreferredSize(new Dimension(width * TILE_SIZE, height * TILE_SIZE));
	}

	public void paint(Graphics g)
	{
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color c;
				if (Math.random() < 0.6) {
					c = Color.GREEN;
				} else {
					c = Color.BLUE;
				}
				g.setColor(c);
				g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
			}
		}
	}
}