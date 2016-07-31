import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class GUI implements Runnable,ActionListener,ChangeListener {

	public void run() {
        JFrame f = new JFrame("WorldSim");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new FlowLayout());

        JSlider timeline = new JSlider(JSlider.HORIZONTAL, 0, 400, 0);
        timeline.addChangeListener(this);
        timeline.setMajorTickSpacing(100);
        timeline.setPaintTicks(true);
        f.add(timeline);

        /*
        f.add(new JLabel("Hello, world!"));
        JButton b = new JButton("Press me!");
        b.setActionCommand("wow");
        b.addActionListener(this);
        f.add(b);
        */

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
