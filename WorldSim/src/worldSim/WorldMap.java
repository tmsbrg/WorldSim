package worldSim;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class WorldMap extends JPanel {

    private static final long serialVersionUID = 4025499860418720647L;

    private static final int TILE_SIZE = 16;
    private TileSelectionReceiver receiver = null;
    private Point selectedTile = null;
    private WorldModel world;

    public WorldMap(TileSelectionReceiver r, WorldModel w) {
        receiver = r;
        world = w;
        setPreferredSize(new Dimension(world.getWidth() * TILE_SIZE,
                    world.getHeight() * TILE_SIZE));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedTile = new Point(e.getPoint().x / TILE_SIZE,
                        e.getPoint().y / TILE_SIZE);
                repaint();
                receiver.setTileSelection(selectedTile);
            }
        });
    }

    public void paintComponent(Graphics g) {
        for (int y = 0; y < world.getHeight(); y++) {
            for (int x = 0; x < world.getWidth(); x++) {
                Color c;
                if (selectedTile != null && x == selectedTile.x
                        && y == selectedTile.y) {
                    c = Color.YELLOW;
                } else if (world.getTerrain(x, y)) {
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
