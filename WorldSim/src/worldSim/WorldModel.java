package worldSim;

import java.awt.Point;
import java.util.*;
import za.co.luma.geom.Vector2DDouble;
import za.co.luma.math.sampling.UniformPoissonDiskSampler;

// contains and handles world data and simulates time moving forward with ticks
public class WorldModel {
    private static final double CITY_MIN_DISTANCE = 3.5;

    private int width;
    private int height;
    private int tick;
    private boolean[][] terrainMap;
    private HashMap<Point, City> cities;
    private ArrayList<Actor> actors;
    private ArrayList<Trader> traders;

    private ArrayList<Actor> actorAddQueue;
    private ArrayList<Actor> actorRemoveQueue;

    public WorldModel() {
        this(21, 15);
    }

    public WorldModel(int w, int h) {
        width = w;
        height = h;
        regenerateMap();
    }

    public void regenerateMap() {
        tick = 0;
        City.resetCityNames();

        // generate terrain
        terrainMap = new boolean[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                terrainMap[y][x] = Math.random() < 0.6;
            }
        }

        // find city locations
        UniformPoissonDiskSampler cityLocationSampler =
            new UniformPoissonDiskSampler(0.0, 0.0,
                    (double)width, (double)height, CITY_MIN_DISTANCE);
        List<Vector2DDouble> locations = cityLocationSampler.sample();

        // add cities
        cities = new HashMap<Point, City>();
        actors = new ArrayList<Actor>();
        actorAddQueue = new ArrayList<Actor>();
        actorRemoveQueue = new ArrayList<Actor>();
        traders = new ArrayList<Trader>();
        for (Vector2DDouble l : locations) {
            Point tile = new Point((int)l.x, (int)l.y);
            if (getTerrain(tile.x, tile.y)) {
                City city = new City(tile, this);
                cities.put(tile, city);
                addActor(city);
            }
        }

        // create trade routes
        for (City c : getCities()) {
            c.trade.createTradeNetwork();
        }

        finishActorAddRemove();
        System.out.println("== World Regenerated ==");
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean contains(Point p) {
        return contains(p.x, p.y);
    }

    public boolean contains(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public boolean getTerrain(Point p) {
        return getTerrain(p.x, p.y);
    }

    // assumes x and y are in range
    public boolean getTerrain(int x, int y) {
        return terrainMap[y][x];
    }

    public City getCity(Point p) {
        return cities.get(p);
    }

    public Collection<City> getCities() {
        return cities.values();
    }

    public void addActor(Actor a) {
        actorAddQueue.add(a);
    }

    public void removeActor(Actor a) {
        actorRemoveQueue.add(a);
    }

    public void addTrader(Trader t) {
        addActor(t);
        traders.add(t);
    }

    public void removeTrader(Trader t) {
        removeActor(t);
        traders.remove(t);
    }

    public Collection<Trader> getTraders() {
        return traders;
    }

    public void nextTick() {
        tick++;
        System.out.println("Tick "+tick);
        for (Actor actor : actors) {
            actor.act(tick);
        }
        finishActorAddRemove();
    }

    private void finishActorAddRemove() {
        for (Actor a : actorAddQueue) {
            actors.add(a);
        }
        actorAddQueue.clear();
        for (Actor a : actorRemoveQueue) {
            actors.remove(a);
        }
        actorRemoveQueue.clear();
    }
}
