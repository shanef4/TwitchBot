public class TwitchViewer
{
	private String vName;
	private int vPoints;
	private int vRoll;
	private int vSongs;

	public TwitchViewer(String name, int points, int validRoll, int validSongs)
	{
		this.vName = name;
		this.vPoints = points;
		this.vRoll = validRoll;
		this.vSongs = validSongs;
	}

	public String getName()
	{
		return this.vName;
	}
	
	public void setName(String name)
	{
		this.vName = name;
	}

	public int getPoints()
	{
		return this.vPoints;
	}

	public void setPoints(int p)
	{
		this.vPoints = p;
	}

	public int getRoll()
	{
		return this.vRoll;
	}

	public void setRoll(int p)
	{
		this.vRoll = p;
	}
	
	public int getSongs()
	{
		return this.vSongs;
	}
	
	public void setSongs(int p)
	{
		this.vSongs = p;
	}

	public void addPoints(int newP)
	{
		this.vPoints += newP;
	}

	public void subPoints(int newP)
	{
		this.vPoints -= newP;
		if (this.vPoints < 0) {
			this.vPoints = 0;
		}
	}
}
