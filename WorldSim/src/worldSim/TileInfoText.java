package worldSim;

import java.awt.*;
import java.util.*;
import javax.swing.*;

// handles the text description of the selected tile in the UI
public class TileInfoText extends JScrollPane {

    private static final long serialVersionUID = -1713085912898401821L;
    private static final String DEFAULT_TEXT = "Click a tile in the map to the"
       + " left to get information about it.";

    private JTextArea infoArea;

    public TileInfoText() {
        infoArea = new JTextArea(DEFAULT_TEXT);
                /*
                "Halverton - Castle\n"+
                "Owned by Kingdom of Goldrock\n"+
                "Controlled by Lord Averick of Halverton\n"+
                "\n"+
                "Population: ~50'000\n"+
                "Garrison: 2'180\n"+
                "\n"+
                "Founded on 5 jan 98 by Kingdom of Dalmycia\n"
        );*/

        infoArea.setEditable(false);
        infoArea.setFont(new Font("Serif", Font.PLAIN, 12));
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);

        setViewportView(infoArea);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setPreferredSize(new Dimension(350, 250));
        setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Selected tile"),
                                BorderFactory.createEmptyBorder(5,5,5,5)),
                getBorder()));
    }

    public void updateText(Point tile, WorldModel world) {
        if (tile == null) {
            infoArea.setText(DEFAULT_TEXT);
            return;
        }
        String s = "";
        for (City c : world.getCities()) {
            if (c.getLocation().equals(tile)) {
                s += c.getName() + " - City\n";
                ArrayList<Resource> resources = c.getResources();
                if (resources.size() > 0) {
                    s += "\nResources:\n";
                    for (Resource r : resources) {
                        s += "    " + r.getName() + "\n";
                    }
                }
            }
        }
        String terrainText = world.getTerrain(tile.x, tile.y) ?
            "Grassland" : "Water";
        s += "\nTerrain: "+terrainText;
        infoArea.setText(s);
    }
}
