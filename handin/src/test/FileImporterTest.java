package test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Collection;

import kdTree.KDTree;
import kdTree.Tree;

import org.junit.Test;

import stars.FileImporter;
import stars.Star;
import stars.StarNameDict;

public class FileImporterTest {

	@Test
	public void test() throws FileNotFoundException {
		Tree<Star> tree = new Tree<Star>(3);
		FileImporter fi = new FileImporter();
		fi.importData("stardata.csv");
		//tree.printTree();
	}

}
