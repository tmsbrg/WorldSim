package worldSim;

import java.awt.*;
import javax.swing.*;

public class TileInfoText extends JScrollPane {

    private static final long serialVersionUID = -1713085912898401821L;

    private JTextArea infoArea;

    public TileInfoText() {
        infoArea = new JTextArea(
                "Click a tile in the map to the left to get information"
                + " about it.");
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

    public void updateText(Point tile) {
        infoArea.setText("Selected tile at "+tile.x+", "+tile.y);
    }
}
