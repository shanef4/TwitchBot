import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class SettingsEdit extends JFrame {

	private JTextField tf1 = new JTextField("Enter Username");
	private JButton tb1 = new JButton("Confirm");
	private JTextField tf2 = new JTextField("Enter New Points");

	SettingsEdit(){
		setVisible(true);
		setSize(240,160);

		setLayout(new FlowLayout());
		add(tf1);
		add(tf2);
		add(tb1);
		tb1.requestFocusInWindow();

		tf1.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				tf1.setText("");
			}

			public void focusLost(FocusEvent e) {
				// nothing
			}
		});
		tf2.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				tf2.setText("");
			}

			public void focusLost(FocusEvent e) {
				// nothing
			}
		});

		tb1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int answer = JOptionPane.showConfirmDialog(null, "Are you sure you wish to change " + tf1.getText() + "'s points to " + tf2.getText() + "?");
				if (answer == 0) {
					PointsSystem change = ChibbotMain.getBot().getPS();
					change.changePoints(tf1.getText(), Integer.parseInt(tf2.getText()));
				}
			}
		});

	}

}
