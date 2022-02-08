
public abstract class Driver {

	private String name;
	private String location;
	private String team;
	
	//Constructor
	public Driver(String name, String location, String team) {
		this.name = name;
		this.location = location;
		this.team = team;
	}
	public Driver() {}
	
	//Calculate points
	public abstract void calculatePoints(boolean isFinished, int position);
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

}


