package worldSim;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.function.Consumer;
import java.util.HashMap;

// main class, contains UI and model components and handles input
public class GUI implements Runnable,ChangeListener, TileSelectionReceiver {

    private WorldModel world;
    private WorldMap map;
    private TileInfoText textInfo;
    private KeyboardHandler keyboard;

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
        keyboard = new KeyboardHandler();
        f.setFocusable(true);
        f.addKeyListener(keyboard);
        keyboard.bindKey(KeyEvent.VK_UP,
                (KeyEvent e) -> map.moveSelectedTile(0, -1));
        keyboard.bindKey(KeyEvent.VK_DOWN,
                (KeyEvent e) -> map.moveSelectedTile(0, 1));
        keyboard.bindKey(KeyEvent.VK_LEFT,
                (KeyEvent e) -> map.moveSelectedTile(-1, 0));
        keyboard.bindKey(KeyEvent.VK_RIGHT,
                (KeyEvent e) -> map.moveSelectedTile(1, 0));
        keyboard.bindKey(KeyEvent.VK_SPACE,
                (KeyEvent e) -> {world.nextTick(); map.updateView();});
        keyboard.bindKey(KeyEvent.VK_R,
                (KeyEvent e) -> {world.regenerateMap(); map.reloadMap();});
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

    private class KeyboardHandler extends KeyAdapter {
        private HashMap<Integer, Consumer<KeyEvent>> keyMap;

        public KeyboardHandler() {
            keyMap = new HashMap<Integer, Consumer<KeyEvent>>();
        }

        public void bindKey(int key, Consumer<KeyEvent> f) {
            keyMap.put(key, f);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            Consumer<KeyEvent> f = keyMap.get(e.getKeyCode());
            if (f != null) {
                f.accept(e);
            }
        }
    }
}
