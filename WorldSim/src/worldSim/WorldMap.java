package worldSim;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.*;


public class WorldMap extends JPanel {

    private static final long serialVersionUID = 4025499860418720647L;

    private static final int TILE_SIZE = 16;
    private TileSelectionReceiver receiver;
    private WorldModel world;
    private BufferedImage cityImg;

    private Point selectedTile = null;

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
        try {
            // from http://opengameart.org/content/tiny-16-basic
            // author: Sharm, license: CC-BY 3.0
            cityImg = ImageIO.read(new File("data/city.png"));
        } catch (IOException e) {
            System.err.println("Cannot load \"data/city.png\", exiting");
            System.exit(1);
        }
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
        for (City c : world.getCities()) {
            Point location = c.getLocation();
            g.drawImage(cityImg, location.x * TILE_SIZE, location.y * TILE_SIZE,
                    null);
        }
    }
}
