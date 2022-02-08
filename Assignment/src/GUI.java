import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class GUI {

	JFrame f;
	JTable table;
	DefaultTableModel model;
	protected int arrSize;

	// Constructor
	GUI(ArrayList<Formula1Driver> drivers, ArrayList<Races> races) {
		f = new JFrame();

		// Frame Title
		f.setTitle("Formula 1");

		arrSize = races.size();

		String[][] data = new String[drivers.size()][6];

		// Column Names
		String[] columnNames = { "Name", "Location", "Points", "First Positions", "Second Positions",
				"Third Positions" };

		// Data
		for (int i = 0; i < drivers.size(); i++) {

			data[i][0] = drivers.get(i).getName();
			data[i][1] = drivers.get(i).getLocation();
			data[i][2] = Integer.toString(drivers.get(i).getPoints());
			data[i][3] = Integer.toString(drivers.get(i).getFirstPositions());
			data[i][4] = Integer.toString(drivers.get(i).getSecondPositions());
			data[i][5] = Integer.toString(drivers.get(i).getThirdPositions());
		}

		// Initializing the JTable
		model = new DefaultTableModel(data, columnNames);
		table = new JTable(model);
		table.setBounds(30, 40, 200, 300);

		// Sort
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();

		// Sort by first positions
		JButton btn1 = new JButton("Sort by points");
		JButton btn2 = new JButton("Sort by first positions");
		JButton btn3 = new JButton("New race");
		JButton btn4 = new JButton("All races");
		JButton btn5 = new JButton("Probability based race");

		// Sort by points
		btn1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				table.getRowSorter().toggleSortOrder(2); // Sort by points

			}

		});

		btn2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				table.getRowSorter().toggleSortOrder(3); // Sort by position

			}

		});

		// New race
		btn3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Random positions
				ArrayList<Integer> positions = generateRandomPositions(drivers.size());

				// Current date
				LocalDate date = LocalDate.now();

				// Calculate points for race
				for (int i = 0; i < drivers.size(); i++) {
					drivers.get(i).calculatePoints(true, positions.get(i));
				}

				// Display table
				String[] columns = { "Driver", "Position" };
				String[][] raceData = new String[drivers.size()][2];

				Map<String, Integer> posMap = new HashMap<>();

				// Table data
				for (int i = 0; i < drivers.size(); i++) {

					raceData[i][0] = drivers.get(i).getName();
					raceData[i][1] = Integer.toString(positions.get(i));

					// Save in map
					posMap.put(drivers.get(i).getName(), positions.get(i));
					GUI.this.arrSize++;

				}

				// Add object to arrayList
				Races race = new Races(date.toString(), posMap);
				races.add(race);

				btn1.setVisible(false);
				btn2.setVisible(false);
				model = new DefaultTableModel(raceData, columns);
				table.setModel(model);

			}

		});

		// New probability based race
		btn5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Random positions
				double probs[] = { 0.4, 0.3, 0.1, 0.1, 0.02, 0.02, 0.02, 0.02, 0.02 };
				ArrayList<Integer> positions = generateRandomProbability(probs, drivers.size());

				// Current date
				LocalDate date = LocalDate.now();

				// Calculate points for race
				for (int i = 0; i < drivers.size(); i++) {
					drivers.get(i).calculatePoints(true, positions.get(i));
				}

				// Display table
				String[] columns = { "Driver", "Position" };
				String[][] raceData = new String[drivers.size()][2];

				Map<String, Integer> posMap = new HashMap<>();

				// Table data
				for (int i = 0; i < drivers.size(); i++) {

					raceData[i][0] = drivers.get(i).getName();
					raceData[i][1] = Integer.toString(positions.get(i));

					// Save in map
					posMap.put(drivers.get(i).getName(), positions.get(i));
					GUI.this.arrSize++;

				}

				// Add object to arrayList
				Races race = new Races(date.toString(), posMap);
				races.add(race);

				btn1.setVisible(false);
				btn2.setVisible(false);
				model = new DefaultTableModel(raceData, columns);
				table.setModel(model);
			}

		});

		// All races
		btn4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String[] columns = { "Date", "Driver", "Position" };

				int x = 0;
				String[][] data = new String[arrSize][3];

				for (int i = 0; i < races.size(); i++) {
					String date = races.get(i).getDate();

					Map<String, Integer> mp = races.get(i).getPositions();

					for (String driver : mp.keySet()) {
						data[x][0] = date;
						data[x][1] = driver;
						data[x][2] = Integer.toString(mp.get(driver));
						x++;
					}
				}

				// Show in new window
				JTextField filterText = new JTextField();
				filterText.setPreferredSize(new Dimension(200, 20));
				JButton btn5 = new JButton("Filter By Name");
				JFrame f = new JFrame("All races");
				JTable table = new JTable();

				model = new DefaultTableModel(data, columns);
				table = new JTable();
				table.setModel(model);

				table.setBounds(30, 40, 200, 300);
				JScrollPane sp = new JScrollPane(table);
				JPanel panel = new JPanel();

				f.add(sp);
				panel.add(filterText);
				panel.add(btn5);
				f.add(panel, BorderLayout.SOUTH);
				f.setSize(1000, 500);
				f.setVisible(true);

				// To filter by driver name
				TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table.getModel());
				table.setRowSorter(rowSorter);

				// Sort by date
				table.getRowSorter().toggleSortOrder(0);

				btn5.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						String text = filterText.getText();

						if (text.trim().length() == 0) {
							rowSorter.setRowFilter(null);
						} else {
							rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text)); // Filter by name
						}

					}

				});
			}

		});

		// adding it to JScrollPane
		JScrollPane sp = new JScrollPane(table);
		JPanel panel = new JPanel();
		f.add(sp);
		panel.add(btn1);
		panel.add(btn2);
		panel.add(btn3);
		panel.add(btn4);
		panel.add(btn5);

		f.add(panel, BorderLayout.SOUTH);

		f.setSize(1000, 500);
		f.setVisible(true);
		f.setState(Frame.NORMAL);
		f.requestFocus();

	}

	private ArrayList generateRandomPositions(int setSize) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i < setSize + 1; i++) {
			list.add(i);
		}

		Collections.shuffle(list);
		return list;
	}

	private ArrayList generateRandomProbability(double[] probs, int size) {

		ArrayList<Integer> initial = generateRandomPositions(size);

		Object[] starting = initial.toArray();

		// Random position
		Random rand = new Random();
		double p = rand.nextDouble();
		double sum = 0.0;
		int i = 0;
		int someIndex = Arrays.asList(probs).indexOf(null);
		probs = Arrays.copyOfRange(probs, 0, size);

		while (sum < p && i < probs.length) {
			sum += probs[i];
			i++;
		}

		int winner = (int) starting[i - 1];

		// Change first position
		int x = 0, y = 0;
		for (int j = 0; j < initial.size(); j++) {

			if ((int) initial.get(j) == 1) {
				x = j;
			}
			if ((int) initial.get(j) == winner) {
				y = j;
			}
		}

		// Swap position
		initial.set(y, 1);
		initial.set(x, winner);

		initial.remove(y);

		// Shuffle again
		Collections.shuffle(initial);

		// Add first position again
		initial.add(y, 1);

		return initial;
	}
}
