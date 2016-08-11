package worldSim;

import java.awt.Point;
import java.util.*;

public class TradeNode {
    private static final int MAX_TRADE_DISTANCE = 18;
    private static final int LAND_MOVE_COST = 3;
    private static final int WATER_MOVE_COST = 2;
    private static final int CITY_MOVE_COST = 2;
    private static final int SHIP_BOARD_COST = 6;

    private Point location;
    private WorldModel world;

    // pathfinding data returned from dijkstra's algorithm
    // maps tiles to their distance from this trade node
    private HashMap<Point, Integer> distances;
    // maps tiles to the next tile needed to get to this trade node with the
    // fastest route
    private HashMap<Point, Point> nextPoints;
     // other trade nodes within reachable in our trade network
    private ArrayList<Point> tradeRoutes;

    TradeNode(Point l, WorldModel w) {
        location = l;
        world = w;
        distances = new HashMap<Point, Integer>();
        nextPoints = new HashMap<Point, Point>();
        tradeRoutes = new ArrayList<Point>();
    }

    public Set<Point> getTradeArea() {
        return nextPoints.keySet();
    }

    public Collection<Point> getTradeRoutes() {
        return tradeRoutes;
    }

    // p should be within trade area, otherwise returns null or
    // Integer.MAX_VALUE, depending on input
    public int getDistance(Point p) {
        return distances.get(p);
    }

    public Point getNext(Point p) {
        return nextPoints.get(p);
    }

    public void createTrader() {
        if (tradeRoutes.size() == 0) {
            return;
        }
        Point route = tradeRoutes.get((int)(Math.random() * tradeRoutes.size()));
        Trader t = new Trader(location, world.getCity(route).trade);
        System.out.println(world.getCity(location).getName() +
                " created trader traveling to " +
                world.getCity(route).getName());
        world.addTrader(t);
    }

    public void receiveTrader(Trader t) {
        System.out.println(world.getCity(location).getName() +
                " receives trader from " +
                world.getCity(t.getOrigin()).getName());
        world.removeTrader(t);
    }

    // uses pathfinding based on Dijkstra's algorithm with a maximum distance
    public void createTradeNetwork() {
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

            if (world.getCity(p) != null && !p.equals(location)) {
                tradeRoutes.add(p);
            }

            for (Direction d : Direction.values()) {
                Point neighbour = d.move(p);
                if (neighbour.x < minX || neighbour.y < minY ||
                        neighbour.x >= maxX || neighbour.y >= maxY ||
                        // cannot go diagonal on land
                        world.getTerrain(p) && world.getTerrain(neighbour) &&
                        d.isDiagonal()) {
                    continue;
                }
                int dist = distances.get(p) + getMoveCost(p, neighbour);
                if (dist <= MAX_TRADE_DISTANCE &&
                        dist < distances.get(neighbour)) {
                    distances.put(neighbour, dist);
                    nextPoints.put(neighbour, p);
                }
            }
        }

        Collections.sort(tradeRoutes, (Point p1, Point p2) -> distances.get(p1) - distances.get(p2));
    }

    // returns maximum possible trade distance in tiles based on constants
    private int maxPossibleTradeDistance() {
        @SuppressWarnings("unused")
        int fastestMoveCost = (WATER_MOVE_COST < LAND_MOVE_COST) ?
            WATER_MOVE_COST : LAND_MOVE_COST;
        fastestMoveCost = (fastestMoveCost < SHIP_BOARD_COST) ?
            fastestMoveCost : SHIP_BOARD_COST;
        fastestMoveCost = (fastestMoveCost < CITY_MOVE_COST) ?
            fastestMoveCost : CITY_MOVE_COST;
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

    // returns move cost between two adjacent points
    // assumes both points are within world and next to each other
    public int getMoveCost(Point p1, Point p2) {
        if (world.getCity(p1) != null || world.getCity(p2) != null) {
            // either tile is a city
            return CITY_MOVE_COST;
        } else if (world.getTerrain(p1) && world.getTerrain(p2)) {
            // both tiles are land
            return LAND_MOVE_COST;
        } else if (!world.getTerrain(p1) && !world.getTerrain(p2)) {
            // both tiles are water
            return WATER_MOVE_COST;
        } else {
            // one tile is water and other is land
            return SHIP_BOARD_COST;
        }
    }
}
