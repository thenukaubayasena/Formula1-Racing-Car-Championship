import java.util.Map;

public interface ChampionshipManager{

	void createNewDriver(String name, String location, String team); //Create driver
	void deleteDriver(String name); //Delete driver
	void changeTeam(String name, String newTeam) ; //Change team
	void displayStatistics(String name); //Display statistics
	void displayDriverTable(); //Display all driver data
	void markRaceCompleted(String date,Map<String, Integer> positions); //Add race completed
	void saveState(); //Save drivers and race data to file
	void restoreState(); //Restore from files
	
}
