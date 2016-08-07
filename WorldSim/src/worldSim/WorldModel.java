package worldSim;

import java.awt.Point;
import java.util.*;
import za.co.luma.geom.Vector2DDouble;
import za.co.luma.math.sampling.UniformPoissonDiskSampler;

public class WorldModel {
    private int width;
    private int height;
    private int tick;
    private boolean[][] terrainMap;
    private ArrayList<City> cities;
    private ArrayList<Actor> actors;

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
        terrainMap = new boolean[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                terrainMap[y][x] = Math.random() < 0.6;
            }
        }
        UniformPoissonDiskSampler cityLocationSampler =
            new UniformPoissonDiskSampler(0.0, 0.0,
                    (double)width, (double)height, 3.5);
        List<Vector2DDouble> locations = cityLocationSampler.sample();

        cities = new ArrayList<City>();
        actors = new ArrayList<Actor>();
        for (Vector2DDouble l : locations) {
            Point tile = new Point((int)l.x, (int)l.y);
            if (getTerrain(tile.x, tile.y)) {
                City city = new City(tile);
                cities.add(city);
                actors.add(city);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // assumes x and y are in range
    public boolean getTerrain(int x, int y) {
        return terrainMap[y][x];
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void nextTick() {
        tick++;
        for (Actor actor : actors) {
            actor.act(tick);
        }
    }
}
