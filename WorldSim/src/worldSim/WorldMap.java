package worldSim;

import java.awt.*;
import java.awt.geom.Line2D;
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
    private BufferedImage tileset;

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
            // in comment by Grimfist on 2014-05-17 10:57
            // authors: Lanea Zimmerman (original 16x16),
            //          William Thompson (32x32 repack),
            //          Grimfist (additional edits)
            // license: CC-BY 3.0
            tileset = ImageIO.read(new File("data/tiny32.png"));
        } catch (IOException e) {
            System.err.println("Cannot load \"data/tiny32.png\", exiting");
            System.exit(1);
        }
    }

    public void moveSelectedTile(int x, int y) {
        if (selectedTile != null) {
            selectedTile.x += x + world.getWidth();
            selectedTile.x %= world.getWidth();
            selectedTile.y += y + world.getHeight();
            selectedTile.y %= world.getHeight();
        } else {
            selectedTile = new Point(0, 0);
        }
        updateView();
    }

    public void reloadMap() {
        selectedTile = null;
        updateView();
    }

    public void updateView() {
        repaint();
        receiver.setTileSelection(selectedTile);
    }

    public void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;

        // draw base terrain
        for (int y = 0; y < world.getHeight(); y++) {
            for (int x = 0; x < world.getWidth(); x++) {
                if (world.getTerrain(x, y)) {
                    drawTile(g, Tile.GRASS, x, y);
                } else {
                    drawTile(g, Tile.WATER_BACK, x, y);
                    drawTile(g, Tile.WATER_FRONT, x, y);
                }
            }
        }

        // draw shore tiles
        for (int y = 0; y < world.getHeight(); y++) {
            for (int x = 0; x < world.getWidth(); x++) {
                if (!world.getTerrain(x, y)) {
                    for (Direction d : Direction.NonDiagional()) {
                        Point location = d.move(x, y);
                        if (world.contains(location) && world.getTerrain(location)) {
                            Point shoreMidPoint = Tile.SHORE_MID.getPoint();
                            Point tilePoint = d.moveBack(shoreMidPoint);
                            drawTile(g, tilePoint.x, tilePoint.y, location.x, location.y);
                        }
                    }
                }
            }
        }

        // draw selection background
        if (selectedTile != null) {
            g.setColor(Color.YELLOW);
            g.fillRect(selectedTile.x * TILE_SIZE, selectedTile.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // draw trade network
        for (City c : world.getCities()) {
            g.setColor(Color.LIGHT_GRAY);
            drawTradeRoutes(g, c.trade);
        }

        // draw selected city trade network
        City selectedCity = world.getCity(selectedTile);
        if (selectedCity != null) {
            /* draw trade area
            for (Point p : selectedCity.trade.getTradeArea()) {
                g.setColor(world.getTerrain(p.x, p.y) ? Color.ORANGE : Color.MAGENTA);
                g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE,
                        TILE_SIZE, TILE_SIZE);
            }
            */
            g.setColor(Color.BLACK);
            drawTradeRoutes(g, selectedCity.trade);
        }

        // draw cities
        for (City c : world.getCities()) {
            Point location = c.getLocation();
            drawTile(g, Tile.CITY, location.x, location.y);
        }
    }

    private void drawTradeRoutes(Graphics2D g, TradeNode trade) {
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

    private void drawLine(Graphics2D g, Point p1, Point p2) {
        g.setStroke(new BasicStroke(5));
        g.draw(new Line2D.Float(p1.x * TILE_SIZE + TILE_SIZE / 2,
                   p1.y * TILE_SIZE + TILE_SIZE / 2,
                   p2.x * TILE_SIZE + TILE_SIZE / 2,
                   p2.y * TILE_SIZE + TILE_SIZE / 2));
    }

    private void drawTile(Graphics g, Tile tile, int x, int y) {
        drawTile(g, tile.getX(), tile.getY(), x, y);
    }

    private void drawTile(Graphics g, int tile_x, int tile_y, int x, int y) {
        g.drawImage(tileset,
                x * TILE_SIZE,
                y * TILE_SIZE,
                x * TILE_SIZE + TILE_SIZE,
                y * TILE_SIZE + TILE_SIZE,
                tile_x * TILE_SIZE,
                tile_y * TILE_SIZE,
                tile_x * TILE_SIZE + TILE_SIZE,
                tile_y * TILE_SIZE + TILE_SIZE,
                null);
    }

    private enum Tile {
        GRASS(14, 1), CITY(11, 7), WATER_BACK(13, 7), WATER_FRONT(14, 7),
        SHORE_MID(4, 6); // special tile, not a shore tile itself, but
                         // surrouned by the shore tiles

        private Point p;

        private Tile(int x_index, int y_index) {
            p = new Point(x_index, y_index);
        }

        private int getX() {
            return p.x;
        }

        private int getY() {
            return p.y;
        }

        private Point getPoint() {
            return p;
        }
    }
}
