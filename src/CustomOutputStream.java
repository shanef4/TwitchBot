import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import javax.swing.JTextArea;

public class CustomOutputStream extends OutputStream
{
	private JTextArea textArea;
	private StringBuilder sb = new StringBuilder();
	public ArrayList<String> v = new ArrayList<String>();
	Timer timer = new Timer();

	public CustomOutputStream() {}

	public CustomOutputStream(JTextArea textArea)
	{
		this.textArea = textArea;
	}
	public void appendText(String t)
	{
		this.textArea.append(t);
		this.textArea.setCaretPosition(this.textArea.getDocument().getLength());
	}

	public void setMyList(ArrayList<String> v)
	{
		this.v = v;
	}

	public ArrayList<String> getMyList()
	{
		return this.v;
	}

	public void write(int b)
			throws IOException
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		if (!String.valueOf((char)b).equals("\n"))
		{
			this.sb.append(String.valueOf((char)b));
		}
		else if (this.sb.indexOf("PRIVMSG") != -1)
		{
			String p = null;
			String[] l = this.sb.toString().split(":");
			String[] c = l[1].split("!");
			if ((l.length == 3) && (l[0].indexOf("PRIVMSG #chibbun") != -1))
			{
				if (l[1].indexOf("https") != -1) {
					p = sdf.format(cal.getTime()) + ": [chubbot]: " + l[1] + l[2];
				} else if (l[1].equalsIgnoreCase("Next stream should be")) {
					p = sdf.format(cal.getTime()) + ": [chubbot]: " + l[1] + l[2];
				}
				appendText(p + "\n");
			}
			else if ((l.length == 4) && (l[0].indexOf("PRIVMSG #chibbun") != -1))
			{
				if (l[2].indexOf("https") != -1) {
					p = sdf.format(cal.getTime()) + ": [chubbot]: " + l[1] + ":" + l[2] + ":" + l[3];
				}
				appendText(p + "\n");
			}
			else
			{
				p = sdf.format(cal.getTime()) + ": [" + c[0] + "]: ";
				appendText(p);
				for (int f = 2; f < l.length; f++) {
					if (f != l.length - 1) {
						appendText(l[f] + ":");
					}else {
						appendText(l[f]);
					}
				}
				appendText("\n");
			}
			this.sb.setLength(0);
		}
		else if (this.sb.indexOf(" JOIN ") != -1)
		{
			String[] l = this.sb.toString().split(":");
			String[] c = l[1].split("!");
			String p = sdf.format(cal.getTime()) + ": " + c[0] + " JOINED, POINTS BEING COUNTED";

			appendText(p + "\n");
			this.sb.setLength(0);
		}
		else if (this.sb.indexOf(" PART ") != -1)
		{
			String[] l = this.sb.toString().split(":");
			String[] c = l[1].split("!");
			String p = sdf.format(cal.getTime()) + ": " + c[0] + " LEFT, POINTS NOT BEING COUNTED";
			appendText(p + "\n");
			this.sb.setLength(0);
		}
		else
		{
			this.sb.setLength(0);
		}
	}
}
