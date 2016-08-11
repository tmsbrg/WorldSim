package worldSim;

import java.awt.Point;

public class Trader implements Actor {
    private final Point from;
    private final TradeNode to;
    /*private final Resource trading;
    private final Resource tradingFor;*/
    private final int totalDistance;
    private int step;
    private int nextTileStep;
    private Point location;
    private Point nextLocation;

    Trader(Point from, TradeNode to/*, Resource trading, Resource tradingFor*/) {
        this.from = from;
        this.to = to;
        //this.trading = trading;
        //this.tradingFor = tradingFor;
        totalDistance = to.getDistance(from);
        location = from;
        step = 0;
        nextTileStep = 0;
        findNextLocation();
    }

    public Point getOrigin() {
        return from;
    }

    public Point getLocation() {
        return location;
    }

    public Point getNextLocation() {
        return nextLocation;
    }

    void findNextLocation() {
        nextLocation = to.getNext(location);
        if (nextLocation == null) {
            to.receiveTrader(this);
        } else {
            nextTileStep += to.getMoveCost(location, nextLocation);
        }
    }

    @Override
    public void act(int tick) {
        step++;
        if (step >= nextTileStep) {
            location = nextLocation;
            findNextLocation();
        }
    }
}
