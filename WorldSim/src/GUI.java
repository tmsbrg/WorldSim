import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class GUI implements Runnable,ActionListener,ChangeListener {

    public void run() {
        JFrame f = new JFrame("WorldSim");

        f.setPreferredSize(new Dimension(800, 400));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new FlowLayout());
        
        /*ImageIcon icon = new ImageIcon("data/map.png", "Map mockup");
        JLabel mapLabel = new JLabel(icon);
        f.add(mapLabel);*/
        WorldMap map = new WorldMap();
        f.add(map);

        JTextArea infoArea = new JTextArea(
                "Halverton - Castle\n"+
                "Owned by Kingdom of Goldrock\n"+
                "Controlled by Lord Averick of Halverton\n"+
                "\n"+
                "Population: ~50'000\n"+
                "Garrison: 2'180\n"+
                "\n"+
                "Founded on 5 jan 98 by Kingdom of Dalmycia\n"
        );
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Serif", Font.PLAIN, 12));
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        JScrollPane areaScrollPane = new JScrollPane(infoArea);
        areaScrollPane.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(new Dimension(350, 250));
        areaScrollPane.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Selected tile"),
                                BorderFactory.createEmptyBorder(5,5,5,5)),
                areaScrollPane.getBorder()));
        f.add(areaScrollPane);
        
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
}
