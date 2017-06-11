import java.util.ArrayList;
import java.util.Random;

import PircBot.User;

public class CostCalc {

	public ArrayList<TwitchViewer> viewersWon = new ArrayList<TwitchViewer>();
	public ArrayList<TwitchViewer> viewersDiscord = new ArrayList<TwitchViewer>();
	public ArrayList<TwitchViewer> viewersExtend = new ArrayList<TwitchViewer>();
	private int max = ChibbotMain.getChance();
	private int cost = ChibbotMain.getCost();
	private int costD = ChibbotMain.getCostDiscord();

	public ArrayList<TwitchViewer> getViewersWon() {
		return viewersWon;
	}

	public void setViewersWon(ArrayList<TwitchViewer> viewersWon) {
		this.viewersWon = viewersWon;
	}

	public ArrayList<TwitchViewer> getViewersDiscord() {
		return viewersDiscord;
	}

	public void setViewersDiscord(ArrayList<TwitchViewer> viewersDiscord) {
		this.viewersDiscord = viewersDiscord;
	}

	public ArrayList<TwitchViewer> getViewersExtend() {
		return viewersExtend;
	}

	public void setViewersExtend(ArrayList<TwitchViewer> viewersExtend) {
		this.viewersExtend = viewersExtend;
	}

	public CostCalc(){

	}

	//rolls a random number
	public String viewerRoll(User u){
		ArrayList<TwitchViewer> viewers = ChibbotMain.getBot().ps.getViewers();
		TwitchViewer r = null;
		for (int c = 0; c < viewers.size(); c++) {
			if (((TwitchViewer)viewers.get(c)).getName().equalsIgnoreCase(u.getNick())) {
				r = (TwitchViewer)viewers.get(c);
			}
		}
		if (r.getPoints() >= this.cost)
		{
			if (r.getRoll() == 0)
			{
				r.setRoll(10);
				r.setPoints(r.getPoints() - this.cost);

				Random random = new Random();
				int r1 = random.nextInt(this.max - 1 + 1) + 1;

				int r2 = random.nextInt(this.max - 1 + 1) + 1;
				if(u.getSubscriber() == 1){
					r1 = r1 / 2;
					r2 = r2 / 2;
				}
				if (r1 == r2)
				{
					this.viewersWon.add(r);
					return u.getNick() + ": Your number was: " + r2 + ". YOU WON, CONGRATULATIONS!!!!!!!!!!!! Chibbun will contact you after stream to discuss your prize. :)";
				}
				return "Your number was: " + r2 + ". You lost, better luck next time!";
			}
			return 
					"Sorry you have already rolled in the last 10 minutes! Please wait " + r.getRoll() + " minutes and try again.";
		}
		return 
				"Sorry " + u.getNick() + " but you don't have enough points for this! " + "You need at least " + this.cost + "!";
	}

	//adds viewer to discord invite list and removes their points
	public String discordInvite(String username){
		ArrayList<TwitchViewer> viewers = ChibbotMain.getBot().ps.getViewers();
		TwitchViewer r = null;
		for (int c = 0; c < viewers.size(); c++) {
			if (((TwitchViewer)viewers.get(c)).getName().equalsIgnoreCase(username)) {
				r = (TwitchViewer)viewers.get(c);
			}
		}

		if (r.getPoints() >= costD)
		{
			r.setPoints(r.getPoints() - costD);
			this.viewersDiscord.add(r);
			return "You have been added to the list " + username + "! Please keep in mind if you are already in the "
			+ "Discord server you will not receive an invitation link.";

		}
		return 
				"Sorry " + username + " but you don't have enough points for this! You need at least " + costD + "!";

	}
	//extends the stream by 30 minutes and removes points
	public String extendStream(String username){
		ArrayList<TwitchViewer> viewers = ChibbotMain.getBot().ps.getViewers();
		boolean check = false;
		TwitchViewer r = null;
		for (int c = 0; c < viewers.size(); c++) {
			if (((TwitchViewer)viewers.get(c)).getName().equalsIgnoreCase(username)) {
				r = (TwitchViewer)viewers.get(c);
			}
		}
		if(viewersExtend.size() < 2){
			for(int i = 0; i < viewersExtend.size(); i++){
				if(viewersExtend.get(i).getName().equalsIgnoreCase(username)){
					check = true;
				}
			}
			if(check == false){
				if (r.getPoints() >= 800){
					r.setPoints(r.getPoints() - 800);
					this.viewersExtend.add(r);
					return "You have added an extra 30 minutes to the stream " + username + ", thanks for the support!";

				}
				else{
					return 
							"Sorry " + username + " but you don't have enough points for this! You need at least 800!";
				}
			}
			else{
				return 
						"Sorry " + username + " you have already bought a half an hour of extra time for this stream!";
			}
		}
		else{
			return 
					"Sorry " + username + " but there has already been an hour of extra time bought for this stream! Get here early next time and you can use the extension right away. :)";
		}
	}
	
	//get extra time added to stream
		public String getExtraTime(){
			if(viewersExtend.size() == 0){
				return "There has been no extra time purchased for this stream.";
			}
			else if(viewersExtend.size() == 1){
				return viewersExtend.get(0).getName() + " has purchased extra stream time. Chibbun must stream for 30 more minutes than scheduled!";
			}
			else if(viewersExtend.size() == 2){
				return viewersExtend.get(0).getName() + " and " + viewersExtend.get(1).getName() + " have purchased extra stream time. Chibbun must stream for one more hour than scheduled!";
			}
			else{
				return "Command broke, rip.";
			}
		}
}
