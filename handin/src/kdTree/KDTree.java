package kdTree;

import java.util.ArrayList;
import java.util.List;

public interface KDTree<T extends KDTreeEntry> {
	
	public void add(T entry);
	
	/* Requires an ArrayList for performance reasons
	 * Sorting and inserting require random access
	 */
	public void addAll(ArrayList<T> nodeList);
	
	public List<T> nearestNeighbors(double[] coordinates, int n);
	
	public List<T> withinRadius(double[] coordinates, double radius);
}
