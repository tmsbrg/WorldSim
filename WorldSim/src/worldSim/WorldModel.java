package worldSim;

import java.util.ArrayList;

public class WorldModel {
    private int width;
    private int height;
    private boolean[][] terrainMap;
    private ArrayList<City> cities;

    public WorldModel() {
        this(21, 15);
    }

    public WorldModel(int w, int h) {
        width = w;
        height = h;
        terrainMap = new boolean[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                terrainMap[y][x] = Math.random() < 0.6;
            }
        }
        cities = new ArrayList<City>();
        cities.add(new City(w / 2, h / 2));
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
}
