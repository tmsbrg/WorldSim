package worldSim;

// interface for something that has to update the UI when a different tile is selected
public interface TileSelectionReceiver {
    void setTileSelection(java.awt.Point tile);
}
