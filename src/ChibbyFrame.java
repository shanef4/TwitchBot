import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ChibbyFrame extends OutputStream implements ActionListener{
	private JFrame frame = new JFrame("Bot Chat Window");
	private JPanel p = new JPanel();
	private JTextArea ta = new JTextArea();
	private JButton tb = new JButton("Quit Application");
	private JButton tb2 = new JButton("Edit A User's Points");

	public void init()
	{
		this.frame.setSize(1150, 720);
		this.frame.setVisible(true);
		this.frame.setLayout(new BorderLayout());

		this.p.setLayout(new FlowLayout());

		this.ta.setEditable(false);
		this.ta.setFont(new Font("Arial", 0, 15));
		PrintStream printStream = new PrintStream(new CustomOutputStream(this.ta));

		this.tb.addActionListener(this);
		this.tb2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				@SuppressWarnings("unused")
				SettingsEdit se = new SettingsEdit();
			}
		});

		System.setOut(printStream);
		System.setErr(printStream);

		JScrollPane sp2 = new JScrollPane(this.ta);
		this.frame.add(sp2, "Center");
		this.frame.add(this.tb, "South");
		this.frame.add(this.tb2, "North");

		this.frame.setDefaultCloseOperation(0);
	}

	public void actionPerformed(ActionEvent a)
	{
		int answer = JOptionPane.showConfirmDialog(null, "Are you sure you wish to close the bot?");
		if (answer == 0) {
			if (ChibbotMain.getBot().ps.savePoints())
			{
				ChibbotMain.getBot().disconnect();
				JOptionPane.showMessageDialog(this.frame, 
						"Points are now saved.", 
						"Confirmation", 
						1);
				System.exit(0);
			}
			else
			{
				JOptionPane.showMessageDialog(this.frame, 
						"Points did not save, please try again.", 
						"Error", 
						1);
			}
		}
	}

	public void write(int arg0)
			throws IOException
	{}
}
