package worldSim;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class GUI implements Runnable,ChangeListener, TileSelectionReceiver {

    private WorldModel world;
    private WorldMap map;
    private TileInfoText textInfo;

    public void run() {
        world = new WorldModel();

        // UI components
        JFrame f = new JFrame("WorldSim");
        f.setPreferredSize(new Dimension(800, 400));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new FlowLayout());

        map = new WorldMap(this, world);
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

        // keyboard input
        // needs any component, map is chosen arbitrarily
        map.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "regenerate");
        map.getActionMap().put("regenerate", new keyHandler());
    }

    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
                System.out.println("new value: " + source.getValue());
        }
    }

    // assumes given tile position is within the actual world, or null
    // if none is selected
    public void setTileSelection(Point tile) {
        textInfo.updateText(tile, world);
    }

    private void regenerateMap() {
        world.regenerateMap();
        map.reloadMap();
    }

    private class keyHandler extends AbstractAction {
        private static final long serialVersionUID = 3894711001576570273L;

        public void actionPerformed(ActionEvent e) {
            regenerateMap();
        }
    }
}
