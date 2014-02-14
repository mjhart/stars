package test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import kdTree.Tree;

import org.junit.Test;

import stars.FileImporter;
import stars.Star;

public class TreeTest {

	@Test
	public void testAdd() {
		Tree<Star> tree = new Tree<Star>(3);
		tree.add(new Star(1, 0, 0, 0));
		tree.add(new Star(2, 1, 1, 1));
		tree.add(new Star(3, 5, 7, 9));
	}
	
	@Test
	public void testSimpleneighbor() {
		Tree<Star> tree = new Tree<Star>(3);
		Star s1 = new Star(0, 0, 0, 0);
		Star s2 = new Star(1, 1, 1, 1);
		Star s3 = new Star(2, 0, 0, 2);
		Star s4 = new Star(3, 5, 5, 5);
		tree.add(s1);
		tree.add(s2);
		tree.add(s3);
		tree.add(s4);
		double[] coords = {0, 0, 0};
		List<Star> results = tree.nearestNeighbors(coords, 1);
		assertTrue(results.get(0) == s1);
		assertFalse(results.contains(s2));
		assertFalse(results.contains(s3));
		assertFalse(results.contains(s4));
		results = tree.nearestNeighbors(coords, 2);
		assertTrue(results.get(0) == s1);
		assertTrue(results.get(1) == s2);
		assertFalse(results.contains(s3));
		assertFalse(results.contains(s4));
	}
	
	@Test
	public void testSimpleRadius() {
		Tree<Star> tree = new Tree<Star>(3);
		Star s1 = new Star(0, 0, 0, 0);
		Star s2 = new Star(1, 1, 1, 1);
		Star s3 = new Star(2, 0, 0, 2);
		Star s4 = new Star(3, 5, 5, 5);
		tree.add(s1);
		tree.add(s2);
		tree.add(s3);
		tree.add(s4);
		double[] coords = {0, 0, 0};
		List<Star> results = tree.withinRadius(coords, 2);
		assertTrue(results.get(0) == s1);
		assertTrue(results.get(1) == s2);
		assertTrue(results.get(2) == s3);
		assertFalse(results.contains(s4));
	}
	
	@Test
	public void testDataRadius() throws FileNotFoundException {
		Tree<Star> tree = new Tree<Star>(3);
		FileImporter fi = new FileImporter();
		ArrayList<Star> list = fi.importData("stardata.csv");
		tree.addAll(list);
		double[] coords = {0, 0, 0};
		List<Star> results = tree.withinRadius(coords, 10);
		List<Star> testResults = withinRadius(list, coords, 10);
		
		// check lengths of result lists are the same
		assertTrue(results.size() == testResults.size());
		
		// check items are in same order
		for(int i=0; i<results.size(); i++) {
			Star s1 = results.get(i);
			Star s2 = testResults.get(i);
			for(int j=0; j<3; j++) {
				assertTrue(s1.getCoord(j) == s2.getCoord(j));
			}
		}
	}
	
	@Test
	public void testDataRadius2() throws FileNotFoundException {
		Tree<Star> tree = new Tree<Star>(3);
		FileImporter fi = new FileImporter();
		ArrayList<Star> list = fi.importData("stardata.csv");
		tree.addAll(list);
		double[] coords = {-38, 204, 120};
		List<Star> results = tree.withinRadius(coords, 10);
		List<Star> testResults = withinRadius(list, coords, 10);
		
		// check lengths of result lists are the same
		assertTrue(results.size() == testResults.size());
		
		// check items are in same order
		for(int i=0; i<results.size(); i++) {
			Star s1 = results.get(i);
			Star s2 = testResults.get(i);
			for(int j=0; j<3; j++) {
				assertTrue(s1.getCoord(j) == s2.getCoord(j));
			}
		}
	}
	
	private List<Star> withinRadius(List<Star> list, double[] coords, double radius) {
		TreeMap<Double, Star> tm = new TreeMap<Double, Star>();
		for(Star e : list) {
			double d = 0;
			for(int i=0; i<coords.length; i++) {
				d += (Math.pow((coords[i]-e.getCoord(i)), 2));
			}
			if(d < Math.pow(radius, 2)) {
				tm.put(d, e);
			}
		}
		return new ArrayList<Star>(tm.values());
	}

}
