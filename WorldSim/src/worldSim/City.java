package worldSim;

import java.awt.Point;
import java.util.*;
import java.util.stream.IntStream;

// represents a city in the world
public class City implements Actor {
    private Point location;
    private String name;
    private ArrayList<Resource> resources;
    private TradeNode trade;

    public City(int x, int y) {
        this(new Point(x, y));
    }

    public City(Point l) {
        location = l;
        name = CityNamer.getName();
        resources = new ArrayList<Resource>();
        resources.add(Resource.randomFood());
        resources.add(Resource.randomNonFood());
        trade = new TradeNode(location);
    }

    public Set<Point> getTradeArea() {
        return trade.getTradeArea();
    }

    public void createTradeNetwork(WorldModel world) {
        trade.createTradeNetwork(world);
    }

    public Point getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public void act(int tick) {
        System.out.println("tick "+tick+": "+getName() + " acts!");
    }

    // static helper class for getting random city names,
    // while avoiding choosing the same name twice
    private final static class CityNamer {
        private static final String[] NAMES = {
            "Damcya", "Alexandria", "Baron", "Mithril",
            "Mysidia", "Mist", "Lindblum", "Fabul",
            "Cleyra", "Troia", "Burmecia", "Dali",
            "Oeilvert", "Esto Gaza", "Ipsen's Castle",
            "Madain Sari", "Conde Petie", "Elfheim",
            "Onrac", "Pravoka", "Lufenia", "Melmond",
            "Cornelia", "Altair", "Gatrea", "Fynn",
            "Paloom", "Perseria", "Poft", "Raqia",
            "Machanon", "Bafsk", "Deist", "Salamand",
            "Amur", "Canaan", "Argus", "Hein", "Sasune",
            "Doga", "Dastar", "Falgabard", "Kazus",
            "Goldor", "Gysahl", "Nepto", "Saronia",
            "Replito", "Tokkul", "Tozus", "Ur"
        };
        private static final String[] POSTFIXES = {
            " _", "", " II", " III", " IV", " V", " VI",
            " VII", " VIII", " IX", " X"
        };
        private static int currentNameIndex = 0;
        private static int currentPostfixIndex = 0;
        private static int[] nameOrder;

        private CityNamer() { }

        private static void shuffleNames() {
            nameOrder = IntStream.range(0, NAMES.length).toArray();
            for (int i = 0; i < NAMES.length; i++) {
                int j = (int)(Math.random() * NAMES.length);
                int temp = nameOrder[i];
                nameOrder[i] = nameOrder[j];
                nameOrder[j] = temp;
            }
        }

        public static String getName() {
            if (currentNameIndex == 0) {
                shuffleNames();
                currentPostfixIndex = (currentPostfixIndex + 1) % POSTFIXES.length;
            }
            String name = NAMES[nameOrder[currentNameIndex]] +
                POSTFIXES[currentPostfixIndex];
            currentNameIndex = (currentNameIndex + 1) % NAMES.length;
            return name;
        }

        public static void reset() {
            currentNameIndex = 0;
            currentPostfixIndex = 0;
        }
    }

    public static void resetCityNames() {
        CityNamer.reset();
    }
}
