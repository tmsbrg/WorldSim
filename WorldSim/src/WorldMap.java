import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class WorldMap extends JPanel {

    private static final long serialVersionUID = 4025499860418720647L;

    private static final int TILE_SIZE = 16;
    private int width = 21;
    private int height = 15;
    private Point selectedTile = null;
    private boolean[][] terrainMap = null;

    public WorldMap() {
        setPreferredSize(new Dimension(width * TILE_SIZE, height * TILE_SIZE));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedTile = new Point(e.getPoint().x / TILE_SIZE,
                        e.getPoint().y / TILE_SIZE);
                repaint();
            }
        });
        terrainMap = new boolean[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                terrainMap[y][x] = Math.random() < 0.6;
            }
        }
    }

    public void paintComponent(Graphics g) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c;
                if (selectedTile != null && x == selectedTile.x
                        && y == selectedTile.y) {
                    c = Color.YELLOW;
                } else if (terrainMap[y][x]) {
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
