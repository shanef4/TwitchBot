//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.LinkedList;
//import java.util.Queue;
//import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import PircBot.*;
//Include the Dropbox SDK.
import com.dropbox.core.*;
import java.io.*;
//import java.util.Locale;

public class Chibbot extends PircBot
{
	private String botName;
	PointsSystem ps = new PointsSystem();
	CostCalc cc = new CostCalc();
	private Timer timer = new Timer();
	//private Timer timerSongs = new Timer();
	private Timer rTimer = new Timer();
	//private BufferedReader br = null;
	//private FileReader reader = null;
	private int secs = ChibbotMain.getSeconds();
	private int costD = ChibbotMain.getCostDiscord();
	//private boolean nowPlaying = false;
	//private boolean allowSongs = false;
	//private boolean songsOpen = false;
	//private int songSkipped = 0;
	//public Queue<String> songQ = new LinkedList<String>();
	//public Queue<Integer> durationList = new LinkedList<Integer>();
	//public Queue<String> songOwners = new LinkedList<String>();
	//private int maxSongs = ChibbotMain.getSongQLimit();
	//private int songCost = ChibbotMain.getSongCost();
	//YouTubeViewer temp;

	public Chibbot(String inBotName) throws DbxException, IOException{

		this.botName = inBotName;
		setName(this.botName);
		try
		{
			ps.getPoints();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		this.timer.schedule(new TimerTask()
		{
			public void run()
			{
				ps.updatePoints();
			}
		}, 0L, this.secs * 1000);
		this.rTimer.schedule(new TimerTask()
		{
			public void run()
			{
				ps.rollCD();
			}
		}, 0L, 60 * 1000);
	}
	
	public PointsSystem getPS(){
		return ps;
	}
	
	//adds a viewer to the list of total viewers and active viewers
	public void addUser(String tmpUser){
		boolean check = false;
		int p = 0;
		for (int c = 0; c < ps.getViewersT().size(); c++) {
			if (((TwitchViewer)ps.getViewersT().get(c)).getName().equalsIgnoreCase(tmpUser))
			{
				check = true;
				p = c;
				c = ps.getViewersT().size();
			}
		}
		if (check)
		{
			ps.getViewers().add((TwitchViewer)ps.getViewersT().get(p));
		}
		else
		{
			TwitchViewer tmp = new TwitchViewer(tmpUser, 0, 0, 0);
			ps.getViewersT().add(tmp);
			ps.getViewers().add(tmp);
		}
	}
	//removes user from the list of active viewers
	public void removeUser(String tmpUser){
		for (int i = 0; i < ps.getViewers().size(); i++) {
			if (((TwitchViewer)ps.getViewers().get(i)).getName().equalsIgnoreCase(tmpUser))
			{
				ps.getViewers().remove(i);
				i = ps.getViewers().size();
			}
		}
	}
	//reads a file with name fileName
	public String readFile(String fileName) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try
		{
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null)
			{
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		}
		finally
		{
			br.close();
		}
	}
	//does something when viewer leaves chat
	public void onPart(Channel channel, User sender){
		for (int i = 0; i < ps.getViewers().size(); i++) {
			if (((TwitchViewer)ps.getViewers().get(i)).getName().equalsIgnoreCase(sender.getNick())) {
				for (int j = 0; j < ps.getViewers().size(); j++) {
					if (((TwitchViewer)ps.getViewersT().get(j)).getName().equalsIgnoreCase(sender.getNick()))
					{
						((TwitchViewer)ps.getViewersT().get(j)).setPoints(((TwitchViewer)ps.getViewers().get(i)).getPoints());
						j = ps.getViewersT().size();
						i = ps.getViewers().size();
					}
				}
			}
		}
		removeUser(sender.getNick());
	}
	//does something when viewer joins chat
	public void onJoin(Channel channel, User sender){
		addUser(sender.getNick());
	}
	
	
	
	/**
	//add song to list of song requests
	public String addSong(String code, String username){
		int songCount = 0;
		TwitchViewer r = null;
		for (int c = 0; c < this.viewers.size(); c++) {
			if (((TwitchViewer)this.viewers.get(c)).getName().equalsIgnoreCase(username)) {
				r = (TwitchViewer)this.viewers.get(c);
			}
		}
		//access via new for-loop
		for(Object object : songOwners) {
			String element = (String) object;
			if(element.equalsIgnoreCase(username)){
				songCount++;
			}
		}
		if(songCount == 2){
			return "Sorry, you seem to already have 2 songs waiting in queue, please wait!";
		}
		if (r.getPoints() >= this.songCost){
			r.setPoints(r.getPoints() - this.songCost);
			String duration = null;
			String durationMin = null;
			String durationSec = null;
			int totalSecs = 0;
			URL oracle = null;
			try {
				oracle = new URL("https://www.googleapis.com/youtube/v3/videos?id=" + code + "&key=AIzaSyDYwPzLevXauI-kTSVXTLroLyHEONuF9Rw&part=snippet,contentDetails");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			URLConnection yc = null;
			try {
				yc = oracle.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(
						yc.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String inputLine;
			try {
				while ((inputLine = in.readLine()) != null){
					if (inputLine.indexOf("\"duration\": ") != -1)
					{
						duration = inputLine.substring("\"duration\": ".length() + 5, inputLine.length());
						durationMin = duration.substring(duration.indexOf("PT") + 2, duration.indexOf("M"));
						durationSec = duration.substring(duration.indexOf("M") + 1, duration.indexOf("S"));
						totalSecs = (Integer.parseInt(durationMin) * 60) + Integer.parseInt(durationSec);
					}
				}
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			songQ.add(code);
			durationList.add(totalSecs);
			songOwners.add(username);
			if(allowSongs == true && nowPlaying == false){
				songRequestsOn();
			}

			return "Song added to queue!";
		}
		else{
			return "Sorry, you need more points to request a song! Minimum is " + songCost + ".";
		}
	}
	**/
	
	/**
	//turns on song requests
	public void songRequestsOn(){
		if(songQ.size() > 0){
			timerSongs.schedule(new TimerTask()
			{
				public void run()
				{
					if(songSkipped == 0){
						if(songQ.size() > 0){
							songOwners.remove();
							temp.loadSong((String)songQ.remove());
							nowPlaying = true;
						}
						else{
							nowPlaying = false;
							temp.killFrame();
						}
					}
					else if(songSkipped == 1){
						songOwners.remove();
						temp.loadSong((String)songQ.remove());
						songSkipped += 1;
					}
					else if(songSkipped == 3){
						songOwners.remove();
						temp.loadSong((String)songQ.remove());
						songSkipped += 1;
					}
					else {
						songSkipped = 0;
					}
				}
			}, 0L, (int)durationList.remove() * 1000);
		}
	}
	//get next scheduled stream
	public String getNextStream()
	{
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(7);
		int hours = calendar.get(11);
		String d = null;
		switch (dayOfWeek)
		{
		case 1: 
			if (hours < 19) {
				d = "Sunday 7PM GMT!";
			} else if (hours >= 19) {
				d = "Monday 7PM GMT!";
			}
		case 2: 
			if (hours < 19) {
				d = "Monday 7PM GMT!";
			} else if (hours >= 19) {
				d = "Wednesday 7PM GMT!";
			}
			break;
		case 3: 
			d = "Wednesday 7PM GMT!";
			break;
		case 4: 
			if (hours < 19) {
				d = "Wednesday 7PM GMT!";
			} else if (hours >= 19) {
				d = "Thursday 7PM GMT!";
			}
			break;
		case 5: 
			if (hours < 19) {
				d = "Thursday 7PM GMT!";
			} else if (hours >= 19) {
				d = "Friday 7PM GMT!";
			}
			break;
		case 6: 
			if (hours < 19) {
				d = "Friday 7PM GMT!";
			} else {
				d = "Sunday 7PM GMT!";
			}
			break;
		case 7: 
			d = "Sunday 7PM GMT!";
			break;
		}
		d = "It Broke.";

		return d;
	}
	**/
	@Override
	public void onMessage(Channel channel, User sender, String message)
	{
		if (message.equalsIgnoreCase("!points")) {
			if(ps.checkPoints(sender.getNick()) == 0){
				addUser(sender.getNick());
			}
			sendMessage(channel, sender.getNick() + " has " + ps.checkPoints(sender.getNick()) + " points!");
		} /**else if ((message.equalsIgnoreCase("!stream")) || (message.equalsIgnoreCase("!schedule"))) {
			//sendMessage(channel, "Next stream should be: " + getNextStream());
		} else if (message.equalsIgnoreCase("!songson")) {
			if ((sender.getNick().equalsIgnoreCase("shaneylol"))) {
				allowSongs = true;
				if(songsOpen == false){
					temp = new YouTubeViewer();
					songsOpen = true;
				}
				sendMessage(channel, "Song requests now on!");
			}
		} else if (message.equalsIgnoreCase("!songskip")) {
			if ((sender.getNick().equalsIgnoreCase("shaneylol"))) {
				if(songQ.size() > 0){
					songSkipped += 1;
					songRequestsOn();
				}
				else{
					nowPlaying = false;
					temp.killFrame();
				}
				sendMessage(channel, "Song skipped!");
			}
		} else if (message.equalsIgnoreCase("!songsoff")) {
			if ((sender.getNick().equalsIgnoreCase("shaneylol"))) {
				temp.killFrame();
				allowSongs = false;
				sendMessage(channel, "Song requests now off!");
			}
		} else if (message.equalsIgnoreCase("!nextsong")) {
			songSkipped += 1;
			songRequestsOn();
		} else if (message.indexOf("!songrequest ") != -1) {
			if(allowSongs == true){
				if(songQ.size() < maxSongs){
					int i = message.indexOf("wwww.youtube.com");
					String c = message.substring(i + 8, i + 19);
					sendMessage(channel, sender.getNick() + ": " + addSong(c, sender.getNick()));
				}
				else{
					sendMessage(channel, "Already " + maxSongs + " songs in queue, please wait for more room!");
				}
			}
		}**/ else if (message.equalsIgnoreCase("!prizes")) {
			sendMessage(channel, "If you have more than 100 points currently in your bank you can type !roll for a giveaway!");
		} else if (message.equalsIgnoreCase("!social")) {
			sendMessage(channel, "Twitter: https://www.twitter.com/chibbbun, Instagram: https://instagram.com/chibbbun");
		} else if (message.equalsIgnoreCase("!donate")) {
			sendMessage(channel, "You can donate at https://www.gamingforgood.net/s/chibbun, every amount no matter what size is appreciated and helps me out a ton!");
		} else if (message.equalsIgnoreCase("!save"))
		{
			if ((sender.getNick().equalsIgnoreCase("chibbun"))) {
				if (ps.savePoints()) {
					sendMessage(channel, "Points were saved, you can now close the application.");
				} else {
					sendMessage(channel, "Points didn't save, shane sucks.");
				}
			}
		}
		else if (message.equalsIgnoreCase("!roll")) {
			if(ps.checkPoints(sender.getNick()) == 0){
				addUser(sender.getNick());
			}
			sendMessage(channel,sender.getNick() + ": " + cc.viewerRoll(sender));
		}
		else if (message.equalsIgnoreCase("!extendbuy")) {
			if(ps.checkPoints(sender.getNick()) == 0){
				addUser(sender.getNick());
			}
			sendMessage(channel,cc.extendStream(sender.getNick()));
		}
		else if (message.equalsIgnoreCase("!extend")) {
			sendMessage(channel, "You can now add an extra 30 minutes of time to this stream using your points! Just use the command !extendbuy and it will cost you 800 points."
					+ " You can only do this once per stream and it is limited to an hour per stream of time bought by everyone.");
		}
		else if (message.equalsIgnoreCase("!extratime")) {
			if ((sender.getNick().equalsIgnoreCase("chibbun"))) {
				sendMessage(channel, cc.getExtraTime());
			}
		}
		else if (message.equalsIgnoreCase("!discordinvite")) {
			if(ps.checkPoints(sender.getNick()) == 0){
				addUser(sender.getNick());
			}
			sendMessage(channel,sender.getNick() + ": " + cc.discordInvite(sender.getNick()));
		}
		else if (message.equalsIgnoreCase("!discord")) {
			sendMessage(channel,sender.getNick() + ": If you have "+ costD +" or more points in your account then you may purchase a "
					+ "discord invitation, just use the command !discordinvite. Subs can automatically join our discord by connecting their Twitch account to Discord!");
		}
		//else if (message.equalsIgnoreCase("!playlist") || message.equalsIgnoreCase("!spotify")) {
			//sendMessage(channel,sender.getNick() + ": I listen to music on Spotify, you can find my profile here https://open.spotify.com/user/trollfacekappa");
		//}
	}

	protected void onConnect()
	{
		sendRawLine("CAP REQ :twitch.tv/membership");
		sendRawLine("CAP REQ :twitch.tv/tags");
	}
}
