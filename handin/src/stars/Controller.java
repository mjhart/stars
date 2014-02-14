package stars;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import kdTree.Tree;

public class Controller {
	private Tree<Star> tree;
	private StarNameDict dict;
	
	public Controller(String filename) {
		
		// create data structures
		tree = new Tree<Star>(3);
		dict = new StarNameDict();
		
		ArrayList<Star> list = null;
		
		// import file
		try {
			FileImporter imp = new FileImporter(dict);
			list = imp.importData(filename);
		}
		catch(FileNotFoundException e) {
			System.err.println("ERROR: stardata.csv not found");
			System.exit(1);
		}
		
		tree.addAll(list);
		
	}
	
	// delegate queries from CLI to KDTree
	public List<Star> nearestNeighbor(double[] coords, int n) {
		try {
			return tree.nearestNeighbors(coords, n);
		}
		catch(IllegalArgumentException e) {
			System.err.println("ERROR: number of neighbors must be non-negative");
			return null;
		}
	}
	
	public List<Star> nearestNeighbor(String name, int n) {
		double[] coords = dict.getCoords(name);
		try {
			return tree.nearestNeighbors(coords, n);
		}
		catch(IllegalArgumentException e) {
			System.err.println("ERROR: number of neighbors must be non-negative");
			return null;
		}
	}
	
	public List<Star> inRadius(double[] coords, double r) {
		try {
			return tree.withinRadius(coords, r);
		}
		catch(IllegalArgumentException e) {
			System.err.println("ERROR: radius must be non-negative");
			return null;
		}
	}
	
	public List<Star> inRadius(String name, double r) {
		double[] coords = dict.getCoords(name);
		try {
			return tree.withinRadius(coords, r);
		}
		catch(IllegalArgumentException e) {
			System.err.println("ERROR: radius must be non-negative");
			return null;
		}
	}
	
}
