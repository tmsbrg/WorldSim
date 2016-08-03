package worldSim;


public class WorldModel {
    private int width;
    private int height;
    private boolean[][] terrainMap = null;

    WorldModel() {
        this(21, 15);
    }

    WorldModel(int w, int h) {
        width = w;
        height = h;
        terrainMap = new boolean[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                terrainMap[y][x] = Math.random() < 0.6;
            }
        }
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    // assumes x and y are in range
    boolean getTerrain(int x, int y) {
        return terrainMap[y][x];
    }
}
