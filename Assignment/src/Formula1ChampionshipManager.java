import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Formula1ChampionshipManager implements ChampionshipManager {

	// Variables
	int numOfDrivers;
	ArrayList<Formula1Driver> drivers = new ArrayList<>();
	ArrayList<String> teams = new ArrayList<>();
	ArrayList<Races> raceData = new ArrayList<>();
	Map<String, Map> races = new HashMap<>();

	// New driver
	@Override
	public void createNewDriver(String name, String location, String team) {
		// Check if a driver is already in the team
		boolean hasPlayer = false;
		for (String t : teams) {
			if (t.equalsIgnoreCase(team)) {
				System.out.println("This team already has a player");
				hasPlayer = true;
				break;
			}
		}

		// Create player otherwise
		if (hasPlayer == false) {
			Formula1Driver fc = new Formula1Driver(name, location, team);
			drivers.add(fc);
			numOfDrivers = drivers.size();
		}
	}

	// Delete player
	@Override
	public void deleteDriver(String name) {

		// Check if player exists
		boolean hasPlayer = false;
		for (Formula1Driver d : drivers) {
			if (d.getName().equalsIgnoreCase(name)) {
				hasPlayer = true;
				teams.remove(d.getTeam()); // Remove team
				drivers.remove(d); // Remove driver
				System.out.println("Player deleted");
				numOfDrivers -= 1;
				break;
			}
		}
		if (hasPlayer == false) {
			System.out.println("Player doesn't exist");
		}

	}

	// Change Team
	@Override
	public void changeTeam(String name, String newTeam) {

		// Check for player
		boolean hasPlayer = false;
		for (Formula1Driver d : drivers) {
			if (d.getName().equalsIgnoreCase(name)) {
				hasPlayer = true;
				d.setTeam(newTeam);
				System.out.println("Team changed");
			}
		}
		if (hasPlayer == false) {
			System.out.println("Player doesn't exist");
		}

	}

	@Override
	public void displayStatistics(String name) {
		for (Formula1Driver d : drivers) {
			if (d.getName().equalsIgnoreCase(name)) {
				System.out.println("Points: " + d.getPoints());
				System.out.println("First positions: " + d.getFirstPositions());
				System.out.println("Second positions: " + d.getSecondPositions());
				System.out.println("Third positions: " + d.getThirdPositions());
			}
		}
	}

	@Override
	public void displayDriverTable() {

		Formula1Driver dv[] = new Formula1Driver[drivers.size()];

		// To array
		for (int i = 0; i < drivers.size(); i++) {
			dv[i] = drivers.get(i);
		}

		// Sort according to descending order
		for (int i = 0; i < drivers.size(); i++) {
			for (int j = i + 1; j < drivers.size(); j++) {
				Formula1Driver tmp;

				if (dv[i].getPoints() < dv[j].getPoints()) {
					tmp = dv[i];
					dv[i] = dv[j];
					dv[j] = tmp;
				}

				// If 2 drivers have same points
				else if (dv[i].getPoints() == dv[j].getPoints()) {
					if (dv[i].getFirstPositions() > dv[j].getFirstPositions()) {
						tmp = dv[j];
						dv[j] = dv[i];
						dv[i] = tmp;
					} else {
						tmp = dv[i];
						dv[i] = dv[j];
						dv[j] = tmp;
					}
				}
			}
		}

		//Display table
		System.out.println("\nName\tTeam\tPoints");
		for (int i = 0; i < drivers.size(); i++) {
			System.out.println(dv[i].getName() + "\t" + dv[i].getTeam() + "\t" + dv[i].getPoints());
		}
		System.out.println();
	}

	//Mark race completed
	@Override
	public void markRaceCompleted(String date, Map<String, Integer> positions) {

		Iterator it = positions.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();

			// Update statistics
			for (Formula1Driver d : drivers) {
				if (d.getName().equals(pair.getKey())) {
					d.calculatePoints(true, (int) pair.getValue()); // Set positions
				}
			}
		}
	}

	//Save to file
	@Override
	public void saveState() {

		// Write driver data to file
		try {
			FileWriter file = new FileWriter("drivers.txt");
			
			//Data
			for (Formula1Driver d : drivers) {
				file.write(d.getName() + "\t" + d.getLocation() + "\t" + d.getTeam() + "\t" + d.getPoints() + "\t"
						+ d.getFirstPositions() + "\t" + d.getSecondPositions() + "\t" + d.getThirdPositions() + "\n");
			}

			file.close();
			System.out.println("Successfully wrote driver data to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		// Write race data
		try {
			FileWriter file = new FileWriter("races.txt");

			int x = 0;
			for (int i = 0; i < raceData.size(); i++) {
				String date = raceData.get(i).getDate();
				Map<String, Integer> mp = raceData.get(i).getPositions();
				
				//Data
				for (String driver : mp.keySet()) {
					file.write(date + "\t" + driver + "\t" + mp.get(driver) + "\n");
					x++;
				}
			}

			file.close();
			System.out.println("Successfully wrote race data to the file.");

		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

	@Override
	public void restoreState() {

		// Read driver data file
		try {
			File file = new File("drivers.txt");
			Scanner scn = new Scanner(file);
			int count = 0;
			
			//Save to arrayList
			while (scn.hasNextLine()) {
				String data = scn.nextLine();
				String info[] = data.split("\t", -1); // split from tab spaces

				Formula1Driver dv = new Formula1Driver(info[0], info[1], info[2]);

				dv.setPoints(Integer.parseInt(info[3]));
				dv.setFirstPositions(Integer.parseInt(info[4]));
				dv.setSecondPositions(Integer.parseInt(info[5]));
				dv.setThirdPositions(Integer.parseInt(info[6]));

				drivers.add(dv);
				count++;
			}
			System.out.println("Data loaded from drivers file");
			scn.close();
		} catch (FileNotFoundException e) {
			System.out.println("No saved state.");
		}

		// Read race data file
		try {
			File file = new File("races.txt");
			Scanner scn = new Scanner(file);
			int count = 0;
			while (scn.hasNextLine()) {
				String data = scn.nextLine();
				String info[] = data.split("\t", -1); // split from tab spaces

				Map<String, Integer> pos = new HashMap<>();

				// Save race details
				pos.put(info[1], Integer.parseInt(info[2]));

				Races rc = new Races(info[0], pos);
				if (rc.getDate() != "")
					raceData.add(rc); // Add object to arrayList
				count++;
			}
			System.out.println("Data loaded from races file\n");
			scn.close();

		} catch (FileNotFoundException e) {
			System.out.println("No saved state.\n");
		}

	}

	public static void main(String[] args) {

		Formula1ChampionshipManager fm = new Formula1ChampionshipManager();

		// Restore state
		fm.restoreState();

		Scanner in = new Scanner(System.in);

		while (true) {
			
			// Actions
			System.out.println("Select the action (Enter number)");
			System.out.println("1.Add new driver");
			System.out.println("2.Delete driver");
			System.out.println("3.Change the team of a driver");
			System.out.println("4.Display driver statistics");
			System.out.println("5.Display drivers table");
			System.out.println("6.Add a race completed");
			System.out.println("7.Go to GUI");
			System.out.println("8.Save & Exit");

			int input = in.nextInt();

			if (input == 1) {
				System.out.println("\n### Add new driver ###");
				// Get driver data
				System.out.println("Enter name:");
				in.nextLine();
				String name = in.nextLine();
				System.out.println("Enter location:");
				String location = in.nextLine();
				System.out.println("Enter team:");
				String team = in.nextLine();

				fm.createNewDriver(name, location, team);
				System.out.println();
			}

			else if (input == 2) {
				System.out.println("\n### Delete driver ###");
				// Get driver data
				System.out.println("Enter driver's name:");
				in.nextLine();
				String name = in.nextLine();

				fm.deleteDriver(name);
				System.out.println();
			}

			else if (input == 3) {
				System.out.println("\n### Change the team ###");
				// Get driver data
				System.out.println("Enter driver's name:");
				in.nextLine();
				String name = in.nextLine();
				System.out.println("Enter new team:");
				String team = in.nextLine();

				fm.changeTeam(name, team);
				System.out.println();

			} else if (input == 4) {
				System.out.println("\n### Display statistics ###");
				// Get driver data
				System.out.println("Enter driver's name:");
				in.nextLine();
				String name = in.nextLine();
				fm.displayStatistics(name);
				System.out.println();

			} else if (input == 5) {
				System.out.println("\n### Display drivers table ###");
				fm.displayDriverTable();

			} else if (input == 6) {
				System.out.println("\n### Add a race completed ###");
				System.out.println("Enter date (yyyy-mm-dd):");
				String date = in.next();

				Map<String, Integer> positions = new HashMap<>();

				// Get position data
				System.out.println("Enter drivers name and position, enter -1 to exit");

				for (int i = 0; i < fm.drivers.size(); i++) {

					System.out.println("Enter name:");
					in.nextLine();
					String name = in.nextLine();
					if (name.equals("-1"))
						break;
					System.out.println("Enter position:");
					int pos = in.nextInt();

					positions.put(name, pos);
				}

				fm.raceData.add(new Races(date, positions));
				fm.markRaceCompleted(date, positions);
				System.out.println();
				
			} else if (input == 7) {
				GUI gui = new GUI(fm.drivers, fm.raceData);
			}

			else if (input == 8) {
				fm.saveState();
				System.out.println("Saved and exit\n");
				break;
			}
		}
	}

}
