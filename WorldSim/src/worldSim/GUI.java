package worldSim;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class GUI implements Runnable,ActionListener,ChangeListener,
        TileSelectionReceiver {

    private WorldModel world = null;
    private TileInfoText textInfo = null;

    public void run() {
        world = new WorldModel();

        JFrame f = new JFrame("WorldSim");
        f.setPreferredSize(new Dimension(800, 400));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new FlowLayout());

        /*ImageIcon icon = new ImageIcon("data/map.png", "Map mockup");
        JLabel mapLabel = new JLabel(icon);
        f.add(mapLabel);*/
        WorldMap map = new WorldMap(this, world);
        f.add(map);

        textInfo = new TileInfoText();
        f.add(textInfo);

        JSlider timeline = new JSlider(JSlider.HORIZONTAL, 0, 400, 0);
        timeline.addChangeListener(this);
        timeline.setMajorTickSpacing(100);
        timeline.setPaintTicks(true);
        timeline.setPreferredSize(new Dimension(700, 50));
        f.add(timeline);

        f.pack();
        f.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "wow":
                System.out.println("wow!");
                break;
            default:
                break;
        }
    }

    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
                System.out.println("new value: " + source.getValue());
        }
    }

    // assumes given tile position is within the actual world
    public void setTileSelection(Point tile) {
        textInfo.updateText(tile);
    }
}
