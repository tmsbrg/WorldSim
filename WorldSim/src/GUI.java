import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GUI implements Runnable,ActionListener {

	public void run() {
        JFrame f = new JFrame("WorldSim");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new FlowLayout());

        f.add(new JLabel("Hello, world!"));
        JButton b = new JButton("Press me!");
        b.setActionCommand("wow");
        b.addActionListener(this);
        f.add(b);

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
}
