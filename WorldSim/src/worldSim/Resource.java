package worldSim;

import java.util.ArrayList;

public enum Resource {
    GRAIN("Grain", true), FISH("Fish", true), PIGS("Pigs", true),
    WOOD("Wood"), STONE("Stone"), IRON("Iron");

    private final String name;
    private final boolean isFood;

    private Resource(String name) {
        this(name, false);
    }

    private Resource(String name, boolean isFood) {
        this.name = name;
        this.isFood = isFood;
    }

    public String getName() {
        return name;
    }

    public boolean isFood() {
        return isFood;
    }

    private static ArrayList<Resource> foodResource;
    private static ArrayList<Resource> nonFoodResource;

    public static Resource RandomFood() {
        if (foodResource == null) {
            foodResource = new ArrayList<Resource>();
            for (Resource r : Resource.values()) {
                if (r.isFood) {
                    foodResource.add(r);
                }
            }
        }
        int index = (int)(Math.random() * foodResource.size());
        return foodResource.get(index);
    }

    public static Resource RandomNonFood() {
        if (nonFoodResource == null) {
            nonFoodResource = new ArrayList<Resource>();
            for (Resource r : Resource.values()) {
                if (!r.isFood) {
                    nonFoodResource.add(r);
                }
            }
        }
        int index = (int)(Math.random() * nonFoodResource.size());
        return nonFoodResource.get(index);
    }
}
