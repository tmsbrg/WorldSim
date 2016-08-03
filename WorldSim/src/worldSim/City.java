package worldSim;

import java.awt.Point;

public class City {
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
    private Point location;
    private String name;

    public City(int x, int y) {
        this(new Point(x, y));
    }

    public City(Point l) {
        location = l;
        int index = (int)(Math.random() * NAMES.length);
        name = NAMES[index];
    }

    public Point getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
