import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class PointsSystem {

	public ArrayList<TwitchViewer> viewersT = new ArrayList<TwitchViewer>();
	public ArrayList<TwitchViewer> viewers = new ArrayList<TwitchViewer>();
	public DropboxUpload du = new DropboxUpload();

	public ArrayList<TwitchViewer> getViewersT() {
		return viewersT;
	}

	public void setViewersT(ArrayList<TwitchViewer> viewersT) {
		this.viewersT = viewersT;
	}

	public ArrayList<TwitchViewer> getViewers() {
		return viewers;
	}

	public void setViewers(ArrayList<TwitchViewer> viewers) {
		this.viewers = viewers;
	}

	public PointsSystem(){

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

	//reads points.txt and adds users to list of total viewers with points
	public void getPoints() throws IOException{
		File f = new File("points.txt");
		if(f.exists() && !f.isDirectory()){
			String content = readFile("points.txt");
			String[] u;
			if (content.length() > 0)
			{
				u = content.split("\n");
				for (int i = 0; i < u.length; i++)
				{
					String[] p = u[i].split(" ");
					TwitchViewer tmp = new TwitchViewer(p[0], Integer.parseInt(p[1]), 0, 0);
					this.viewersT.add(tmp);
				}
			}
			delPoints();
		}
	}

	//delete text files upon startup after reading them
	public void delPoints(){
		boolean success = new File("points.txt").delete();
		if (success) {
			System.out.println("Points file successfully read and deleted, don't forget to save before closing the application window.");
		}
		boolean successWon = new File("winners.txt").delete();
		if (successWon) {
			System.out.println("Points file successfully read and deleted, don't forget to save before closing the application window.");
		}
	}

	//adds points to each viewer in chat and reduces the cooldown on their roll
	public void updatePoints(){
		if (this.viewers.size() > 0) {
			for (int i = 0; i < this.viewers.size(); i++)
			{
				((TwitchViewer)this.viewers.get(i)).setPoints(((TwitchViewer)this.viewers.get(i)).getPoints() + ChibbotMain.getPPM());
			}
		}
	}
	//lower cooldown on each user's roll
	public void rollCD(){
		if (this.viewers.size() > 0) {
			for (int i = 0; i < this.viewers.size(); i++)
			{
				if (((TwitchViewer)this.viewers.get(i)).getRoll() > 0) {
					((TwitchViewer)this.viewers.get(i)).setRoll(((TwitchViewer)this.viewers.get(i)).getRoll() - 1);
				}
			}
		}
	}
	//checks the points of a person with the username u
	public int checkPoints(String u){
		for (int i = 0; i < this.viewers.size(); i++) {
			if (u.equalsIgnoreCase(((TwitchViewer)this.viewers.get(i)).getName())) {
				return ((TwitchViewer)this.viewers.get(i)).getPoints();
			}
		}
		return 0;
	}

	public void changePoints(String u, int p){
		boolean c = false;

		for (int i = 0; i < this.viewers.size(); i++) {
			if (u.equalsIgnoreCase(this.viewers.get(i).getName())) {
				this.viewers.get(i).setPoints(p);
				c = true;
				JOptionPane.showMessageDialog(null, "Points have been changed successfully.");
			}
		}
		if(c == false){
			for (int i = 0; i < this.viewersT.size(); i++) {
				if (u.equalsIgnoreCase(this.viewersT.get(i).getName())) {
					this.viewersT.get(i).setPoints(p);
					c = true;
					JOptionPane.showMessageDialog(null, "Points have been changed successfully.");
				}
			}
		}
		if(c == false){
			JOptionPane.showMessageDialog(null, "User does not exist in the system.");
		}
	}

	//saves points and other information
	public boolean savePoints(){
		delPoints();
		ArrayList<TwitchViewer> viewersWon = ChibbotMain.getBot().cc.getViewersWon();;
		ArrayList<TwitchViewer> viewersDiscord = ChibbotMain.getBot().cc.getViewersDiscord();
		for (int i = 0; i < this.viewers.size(); i++) {
			if (this.viewersT.indexOf(this.viewers.get(i)) != -1) {
				this.viewersT.set(this.viewersT.indexOf(this.viewers.get(i)), (TwitchViewer)this.viewers.get(i));
			}
		}
		try
		{
			StringBuilder fileContent = new StringBuilder();
			for (int i = 0; i < this.viewersT.size(); i++) {
				if(this.viewersT.get(i).getPoints() > 5){
					fileContent.append(((TwitchViewer)this.viewersT.get(i)).getName() + " " + ((TwitchViewer)this.viewersT.get(i)).getPoints() + System.getProperty("line.separator"));
				}
			}
			FileWriter fstreamWrite = new FileWriter("points.txt", false);
			BufferedWriter bufferWritter = new BufferedWriter(fstreamWrite);
			bufferWritter.write(fileContent.toString());
			bufferWritter.close();

			du.uploadFile();

			StringBuilder fileContentW = new StringBuilder();
			for (int i = 0; i < viewersWon.size(); i++) {
				fileContentW.append(((TwitchViewer)viewersWon.get(i)).getName() + System.getProperty("line.separator"));
			}

			FileWriter fstreamWriteW = new FileWriter("winners.txt", false);
			BufferedWriter bufferWritterW = new BufferedWriter(fstreamWriteW);
			bufferWritterW.write(fileContentW.toString());
			bufferWritterW.close();

			StringBuilder fileContentD = new StringBuilder();
			for (int i = 0; i < viewersDiscord.size(); i++) {
				fileContentD.append(((TwitchViewer)viewersDiscord.get(i)).getName() + System.getProperty("line.separator"));
			}
			FileWriter fstreamWriteD = new FileWriter("DiscordInvitations.txt", false);
			BufferedWriter bufferWritterD = new BufferedWriter(fstreamWriteD);
			bufferWritterD.write(fileContentD.toString());
			bufferWritterD.close();

			return true;
		}
		catch (Exception e)
		{
			System.err.println("Error: " + e.getMessage());
		}
		return false;
	}
}
