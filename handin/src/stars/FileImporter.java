package stars;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileImporter {
	
	private StarNameDict dict;
	
	public FileImporter(StarNameDict dict) {
		this.dict = dict;
	}
	
	public FileImporter() {
	}
	
	public ArrayList<Star> importData(String filename) throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		ArrayList<Star> stars = new ArrayList<Star>();
		Star star;
		try {
			br.readLine();
			while((line = br.readLine()) != null) {
				 String[] data = line.split(",");
				 if(data[1].length() > 0) {
					 
					 star = new Star(Integer.parseInt(data[0]), data[1], 
							 Double.parseDouble(data[2]), Double.parseDouble(data[3]), Double.parseDouble(data[4]));
					 
					 // add to dictionary
					 if(dict != null) {
						 dict.addStar(data[1], star);
					 }
				 }
				 else {
					 star = new Star(Integer.parseInt(data[0]), 
							 Double.parseDouble(data[2]), Double.parseDouble(data[3]), Double.parseDouble(data[4]));
				 }
				 // add new star to results list
				 stars.add(star);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stars;
	}
}
