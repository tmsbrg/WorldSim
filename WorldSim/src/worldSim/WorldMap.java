package worldSim;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.*;

// handles the graphical world map and its mouse input
public class WorldMap extends JPanel {

    private static final long serialVersionUID = 4025499860418720647L;

    private static final int TILE_SIZE = 32;
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
                updateView();
            }
        });
        try {
            // from http://opengameart.org/content/tiny-16-basic
            // in comment by William Thompson on 2014-05-16 10:03
            // authors: Lanea Zimmerman (original 16x16),
            //          William Thompson (32x32 repack)
            // license: CC-BY 3.0
            cityImg = ImageIO.read(new File("data/city.png"));
        } catch (IOException e) {
            System.err.println("Cannot load \"data/city.png\", exiting");
            System.exit(1);
        }
    }

    public void moveSelectedTile(int x, int y) {
        if (selectedTile != null) {
            selectedTile.x += x + world.getWidth();
            selectedTile.x %= world.getWidth();
            selectedTile.y += y + world.getHeight();
            selectedTile.y %= world.getHeight();
            updateView();
        }
    }

    public void reloadMap() {
        selectedTile = null;
        updateView();
    }

    public void updateView() {
        repaint();
        receiver.setTileSelection(selectedTile);
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
            g.setColor(Color.LIGHT_GRAY);
            drawTradeRoutes(g, c.trade);
        }
        City selectedCity = world.getCity(selectedTile);
        if (selectedCity != null) {
            /* draw trade area
            for (Point p : selectedCity.trade.getTradeArea()) {
                g.setColor(world.getTerrain(p.x, p.y) ? Color.ORANGE : Color.MAGENTA);
                g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE,
                        TILE_SIZE, TILE_SIZE);
            }
            */
            g.setColor(Color.MAGENTA);
            drawTradeRoutes(g, selectedCity.trade);
        }
        for (City c : world.getCities()) {
            Point location = c.getLocation();
            g.drawImage(cityImg, location.x * TILE_SIZE, location.y * TILE_SIZE, TILE_SIZE, TILE_SIZE,
                    null);
        }
    }

    private void drawTradeRoutes(Graphics g, TradeNode trade) {
        for (Point tradeRoute : trade.getTradeRoutes()) {
            Point current = tradeRoute;
            while (true) {
                Point previous = trade.previousPoint(current);
                if (previous == null) {
                    break;
                }
                drawLine(g, current, previous);
                current = previous;
            }
        }
    }

    private void drawLine(Graphics g, Point p1, Point p2) {
        g.drawLine(p1.x * TILE_SIZE + TILE_SIZE / 2,
                   p1.y * TILE_SIZE + TILE_SIZE / 2,
                   p2.x * TILE_SIZE + TILE_SIZE / 2,
                   p2.y * TILE_SIZE + TILE_SIZE / 2);
    }
}
