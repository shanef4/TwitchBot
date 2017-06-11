import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.dropbox.core.DbxException;

import PircBot.*;

public class ChibbotMain
{
	private String botName;
	private String botPassword;
	private String chanName;
	private String pointsperMin;
	private String pointsperSong;
	private String songQLimit;
	private String chanceTotal;
	private String costTotal;
	private String secsTotal;
	private String costDiscord;
	private static int ppm;
	private static int songQ;
	private static int pps;
	private static Chibbot bot;
	private static int chance;
	private static int cost;
	private static int costD;
	private static int seconds;

	public static void main(String[] args) throws DbxException, IOException
	{
		ChibbotMain c = new ChibbotMain();
		c.connect();

		@SuppressWarnings("resource")
		ChibbyFrame f = new ChibbyFrame();
		f.init();
	}

	public void connect() throws DbxException, IOException
	{
		getSettings();

		bot = new Chibbot(getBotName());
		bot.setVerbose(true);
		try
		{
			bot.connect("irc.chat.twitch.tv", 6667, getBotPw());
		}
		catch (NickAlreadyInUseException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (IrcException e)
		{
			e.printStackTrace();
		}
		bot.joinChannel("#" + getChanName());
	}

	public String getBotName()
	{
		return this.botName;
	}

	public static Chibbot getBot()
	{
		return bot;
	}

	public String getBotPw()
	{
		return this.botPassword;
	}

	public String getChanName()
	{
		return this.chanName;
	}

	public static int getPPM()
	{
		return ppm;
	}

	public static int getChance()
	{
		return chance;
	}

	public static int getCost()
	{
		return cost;
	}
	
	public static int getCostDiscord()
	{
		return costD;
	}

	public static int getSeconds()
	{
		return seconds;
	}
	
	public static int getSongCost()
	{
		return pps;
	}
	
	public static int getSongQLimit()
	{
		return songQ;
	}

	private void getSettings()
	{
		String filename = "settings.txt";
		String line = null;
		try
		{
			FileReader fr = new FileReader(filename);

			BufferedReader br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				if (line.indexOf("twitch account name:") != -1)
				{
					this.botName = line.substring("twitch account name:".length(), line.length());
					if (this.botName.substring(0, 1).equals(" ")) {
						this.botName = this.botName.substring(1, this.botName.length());
					}
				}
				else if (line.indexOf("twitch password:") != -1)
				{
					this.botPassword = line.substring("twitch password:".length(), line.length());
					if (this.botPassword.substring(0, 1).equals(" ")) {
						this.botPassword = this.botPassword.substring(1, this.botPassword.length());
					}
				}
				else if (line.indexOf("twitch channel to join:") != -1)
				{
					this.chanName = line.substring("twitch channel to join:".length(), line.length());
					if (this.chanName.substring(0, 1).equals(" ")) {
						this.chanName = this.chanName.substring(1, this.chanName.length());
					}
				}
				else if (line.indexOf("points per minute:") != -1)
				{
					this.pointsperMin = line.substring("points per minute:".length(), line.length());
					if (this.pointsperMin.substring(0, 1).equals(" ")) {
						this.pointsperMin = this.pointsperMin.substring(1, this.pointsperMin.length());
					}
					ppm = Integer.parseInt(this.pointsperMin);
				}
				else if (line.indexOf("chance of winning(1 out of):") != -1)
				{
					this.chanceTotal = line.substring("chance of winning(1 out of):".length(), line.length());
					if (this.chanceTotal.substring(0, 1).equals(" ")) {
						this.chanceTotal = this.chanceTotal.substring(1, this.chanceTotal.length());
					}
					chance = Integer.parseInt(this.chanceTotal);
				}
				else if (line.indexOf("cost per roll:") != -1)
				{
					this.costTotal = line.substring("cost per roll:".length(), line.length());
					if (this.costTotal.substring(0, 1).equals(" ")) {
						this.costTotal = this.costTotal.substring(1, this.costTotal.length());
					}
					cost = Integer.parseInt(this.costTotal);
				}
				else if (line.indexOf("number of seconds for points:") != -1)
				{
					this.secsTotal = line.substring("number of seconds for points:".length(), line.length());
					if (this.secsTotal.substring(0, 1).equals(" ")) {
						this.secsTotal = this.secsTotal.substring(1, this.secsTotal.length());
					}
					seconds = Integer.parseInt(this.secsTotal);
				}
				else if (line.indexOf("points per song:") != -1)
				{
					this.pointsperSong = line.substring("points per song:".length(), line.length());
					if (this.pointsperSong.substring(0, 1).equals(" ")) {
						this.pointsperSong = this.pointsperSong.substring(1, this.pointsperSong.length());
					}
					pps = Integer.parseInt(this.pointsperSong);
				}
				else if (line.indexOf("song queue limit:") != -1)
				{
					this.songQLimit = line.substring("song queue limit:".length(), line.length());
					if (this.songQLimit.substring(0, 1).equals(" ")) {
						this.songQLimit = this.songQLimit.substring(1, this.songQLimit.length());
					}
					songQ = Integer.parseInt(this.songQLimit);
				}
				else if (line.indexOf("cost for discord:") != -1)
				{
					this.costDiscord = line.substring("cost for discord:".length(), line.length());
					if (this.costDiscord.substring(0, 1).equals(" ")) {
						this.costDiscord = this.costDiscord.substring(1, this.costDiscord.length());
					}
					costD = Integer.parseInt(this.costDiscord);
				}
			}
			br.close();
		}
		catch (FileNotFoundException ex)
		{
			System.out.println("File does not exist");
		}
		catch (IOException ex)
		{
			System.out.println("Error reading file");
		}
	}
}
