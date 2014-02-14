package stars;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CommandLine {
	
	public CommandLine(Controller c) {
		
		// read from System.in
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		String line = null;
		String[] commands;
		while(true) {
			
			// read line
			try {
				line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(line == null || line.length() == 0) {
				return;
			}
			
			commands = line.split(" ");
			
			// coordinates
			if(commands.length == 5) {
				if(commands[0].equals("neighbors")) {
					double[] coords = new double[3];
					int n;
					try{
						n = Integer.parseInt(commands[1]);
						coords[0] = Double.parseDouble(commands[2]);
						coords[1] = Double.parseDouble(commands[3]);
						coords[2] = Double.parseDouble(commands[4]);
					}
					catch(NumberFormatException e) {
						System.err.println("ERROR: Ill-formed command");
						continue;
					}
					List<Star> results = c.nearestNeighbor(coords, n);
					if(results != null) {
						for(Star s : results) {
							System.out.println(s.id);
						}
						System.out.println();
					}
				}

				else if(commands[0].equals("radius")) {
					double r;
					double[] coords = new double[3];
					try {
						r = Double.parseDouble(commands[1]);
						coords[0] = Double.parseDouble(commands[2]);
						coords[1] = Double.parseDouble(commands[3]);
						coords[2] = Double.parseDouble(commands[4]);
					}
					catch(NumberFormatException e) {
						System.err.println("ERROR: Ill-formed command");
						continue;
					}
					List<Star> results = c.inRadius(coords, r);
					if(results != null) {
						for(Star s : results) {
							System.out.println(s.id);
						}
						System.out.println();
					}
				}

				else {
					System.err.println("ERROR: Ill-formed command");
				}
			}
			
			// star name
			else if(commands.length == 3) {
				if(commands[0].equals("neighbors")) {

				}

				else if(commands[0].equals("radius")) {
					System.out.println("R");
				}

				else {
					System.err.println("ERROR: Ill-formed command");
				}
			}
			
			else {
				System.err.println("ERROR: Ill-formed command");
			}
		}
	}
}
