package worldSim;

import java.awt.Point;
import java.util.*;

public class TradeNode {
    private static final int MAX_TRADE_DISTANCE = 18;
    private static final int LAND_MOVE_COST = 3;
    private static final int WATER_MOVE_COST = 2;
    // SHIP_BOARD_COST should be larger than both LAND and WATER move costs
    private static final int SHIP_BOARD_COST = 6;

    private Point location;

    // pathfinding data returned from dijkstra's algorithm
    private HashMap<Point, Integer> distances;
    private HashMap<Point, Point> previousPoints;

    TradeNode(Point l) {
        location = l;
        distances = new HashMap<Point, Integer>();
        previousPoints = new HashMap<Point, Point>();
    }

    public Set<Point> getTradeArea() {
        return previousPoints.keySet();
    }

    // uses pathfinding based on Dijkstra's algorithm with a maximum distance
    public void createTradeNetwork(WorldModel world) {
        // get rectangle to pathfind in, avoid pathfinding the whole world
        int minX = location.x - maxPossibleTradeDistance();
        minX = (minX < 0) ? 0 : minX;
        int minY = location.y - maxPossibleTradeDistance();
        minY = (minY < 0) ? 0 : minY;
        int maxX = location.x + maxPossibleTradeDistance() + 1;
        maxX = (maxX > world.getWidth()) ? world.getWidth() : maxX;
        int maxY = location.y + maxPossibleTradeDistance() + 1;
        maxY = (maxY > world.getHeight()) ? world.getHeight() : maxY;

        Collection<Point> queue =
            new HashSet<Point>((maxX - minX) * (maxY - minY));

        for (int y = minY; y < maxY; y++) {
            for (int x = minX; x < maxX; x++) {
                Point p = new Point(x, y);
                distances.put(p, Integer.MAX_VALUE);
                queue.add(p);
            }
        }

        distances.put(null, Integer.MAX_VALUE);
        distances.put(location, 0);

        while (queue.size() > 0) {
            Point p = getMinDistPoint(queue);
            if (distances.get(p) > MAX_TRADE_DISTANCE) {
                break;
            }
            queue.remove(p);

            // TODO: check for cities

            for (Direction d : Direction.values()) {
                Point neighbour = movePoint(p, d);
                if (neighbour.x < minX || neighbour.y < minY ||
                        neighbour.x >= maxX || neighbour.y >= maxY) {
                    continue;
                }
                int dist = distances.get(p) + distance(p, d, world);
                if (dist <= MAX_TRADE_DISTANCE &&
                        dist < distances.get(neighbour)) {
                    distances.put(neighbour, dist);
                    previousPoints.put(neighbour, p);
                }
            }
        }
    }

    // returns maximum possible trade distance in tiles based on constants
    private int maxPossibleTradeDistance() {
        @SuppressWarnings("unused")
        int fastestMoveCost = (WATER_MOVE_COST < LAND_MOVE_COST) ?
            WATER_MOVE_COST : LAND_MOVE_COST;
        return MAX_TRADE_DISTANCE / fastestMoveCost;
    }

    private Point getMinDistPoint(Collection<Point> queue) {
        Point r = null;
        for (Point p : queue) {
            if (distances.get(p) < distances.get(r)) {
                r = p;
            }
        }
        return r;
    }

    private Point movePoint(Point p, Direction d) {
        return new Point(p.x + d.getPoint().x, p.y + d.getPoint().y);
    }

    // returns distance between point p and next point in direction d
    // assumes movePoint(p, d) returns a value within the world bounds
    private int distance(Point p, Direction d, WorldModel world) {
        Point p2 = movePoint(p, d);

        if (world.getTerrain(p) && world.getTerrain(p2)) {
            // both tiles are land
            // HACK: cannot go diagonal on land
            return (d.isDiagonal()) ? MAX_TRADE_DISTANCE : LAND_MOVE_COST;
        } else if (!world.getTerrain(p) && !world.getTerrain(p2) ||
                world.getCity(p) != null || world.getCity(p2) != null) {
            // both tiles are water, or 1 is water and other is city
            return WATER_MOVE_COST;
        } else {
            // one tile is water and other is non-city land
            return SHIP_BOARD_COST;
        }
    }

    private enum Direction {
        NORTHWEST(-1, -1), NORTH(0, -1), NORTHEAST(1, -1),
        WEST(-1, 0),                     EAST(1, 0),
        SOUTHWEST(-1, 1),  SOUTH(0, 1),  SOUTHEAST(1, 1);

        private final Point p;

        private Direction(int x, int y) {
            p = new Point(x, y);
        }

        public Point getPoint() {
            return p;
        }

        public boolean isDiagonal() {
            return Math.abs(p.x) == Math.abs(p.y);
        }
    };
}
