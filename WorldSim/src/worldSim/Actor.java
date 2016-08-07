package worldSim;

// interface for things that act while time in world moves forward
public interface Actor {
    // called every tick
    public void act(int tick);
}
