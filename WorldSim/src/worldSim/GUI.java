package worldSim;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

// main class, contains UI and model components and handles input
public class GUI implements Runnable,ChangeListener, TileSelectionReceiver {

    private WorldModel world;
    private WorldMap map;
    private TileInfoText textInfo;

    public void run() {
        world = new WorldModel();

        // UI components
        JFrame f = new JFrame("WorldSim");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new FlowLayout());

        map = new WorldMap(this, world);
        f.add(map);

        textInfo = new TileInfoText();
        textInfo.setPreferredSize(new Dimension(
                    textInfo.getPreferredSize().width,
                    map.getPreferredSize().height));
        f.add(textInfo);

        JSlider timeline = new JSlider(JSlider.HORIZONTAL, 0, 400, 0);
        timeline.addChangeListener(this);
        timeline.setMajorTickSpacing(100);
        timeline.setPaintTicks(true);
        timeline.setPreferredSize(new Dimension(700, 50));
        f.add(timeline);

        Dimension mapSize = map.getPreferredSize();
        Dimension textSize = textInfo.getPreferredSize();
        f.setSize(new Dimension(mapSize.width + textSize.width + 20,
                        Math.max(mapSize.height, textSize.height) +
                        timeline.getPreferredSize().height + 40));
        f.setVisible(true);

        // keyboard input
        // needs any component, map is chosen arbitrarily
        map.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "regenerate");
        map.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "moveleft");
        map.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "moveright");
        map.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "moveup");
        map.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "movedown");
        map.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "space");
        map.getActionMap().put("moveleft", new MoveAction(-1, 0));
        map.getActionMap().put("moveright", new MoveAction(1, 0));
        map.getActionMap().put("moveup", new MoveAction(0, -1));
        map.getActionMap().put("movedown", new MoveAction(0, 1));
        map.getActionMap().put("space", new NextTickAction());
        map.getActionMap().put("regenerate", new RegenerateAction());
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

    private class MoveAction extends AbstractAction {
        private static final long serialVersionUID = -7868311981765821389L;

        private int x;
        private int y;

        public MoveAction(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            map.moveSelectedTile(x, y);
        }
    }

    private class NextTickAction extends AbstractAction {

        private static final long serialVersionUID = 8884774874021699376L;

        @Override
        public void actionPerformed(ActionEvent e) {
            world.nextTick();
        }
    }

    private class RegenerateAction extends AbstractAction {
        private static final long serialVersionUID = 3894711001576570273L;

        @Override
        public void actionPerformed(ActionEvent e) {
            regenerateMap();
        }
    }
}
