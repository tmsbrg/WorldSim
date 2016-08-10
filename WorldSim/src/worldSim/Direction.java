package worldSim;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

public enum Direction {
    NORTHWEST(-1, -1), NORTH(0, -1), NORTHEAST(1, -1),
    WEST(-1, 0),                     EAST(1, 0),
    SOUTHWEST(-1, 1),  SOUTH(0, 1),  SOUTHEAST(1, 1);

    private final Point direction;

    private Direction(int x, int y) {
        direction = new Point(x, y);
    }

    public Point getPoint() {
        return direction;
    }

    public boolean isDiagonal() {
        return Math.abs(direction.x) == Math.abs(direction.y);
    }

    public Point move(int x, int y) {
        return new Point(x + direction.x, y + direction.y);
    }

    public Point move(Point p) {
        return move(p.x, p.y);
    }

    public Point moveBack(int x, int y) {
        return new Point(x - direction.x, y - direction.y);
    }

    public Point moveBack(Point p) {
        return moveBack(p.x, p.y);
    }

    private static Direction[] nonDiagionalDirections;

    public static List<Direction> nonDiagional() {
        if (nonDiagionalDirections == null) {
            nonDiagionalDirections = new Direction[4];
            int i = 0;
            for (Direction d : Direction.values()) {
                if (!d.isDiagonal()) {
                    nonDiagionalDirections[i] = d;
                    i++;
                }
            }
        }
        return Arrays.asList(nonDiagionalDirections);
    }
};
