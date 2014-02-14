package kdTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.TreeMap;

public class Tree<T extends KDTreeEntry> {
	private KDNode<T> root;
	private int k;
	
	public Tree(int dimensions) {
		if (dimensions > 0) {
			this.k = dimensions;
		}
		else {
			throw new IllegalArgumentException("Tree must have positive dimension");
		}
	}
	
	//@Override
	public void add(T entry) {
		if(entry.getDimensions() != k) {
			throw new IllegalArgumentException("Entries must have same dimensionality as tree");
		}
		if(root == null) {
			root = new KDNode<T>(0, entry);
		}
		else {
			insert(root, entry, 0);
		}
	}
	
	private void insert(KDNode<T> node, T entry, int depth) {
		int dim = depth%k;
		if(entry.getCoord(dim) < node.getPlane()) {
			if(node.hasLeft()) {
				insert(node.getLeft(), entry, depth+1);
			}
			else {
				node.setLeft(new KDNode<T>(depth+1, entry));
			}
		}
		else {
			if(node.hasRight()) {
				insert(node.getRight(), entry, depth+1);
			}
			else {
				node.setRight(new KDNode<T>(depth+1, entry));
			}
		}
	}

	public void addAll(ArrayList<T> nodeList) {
		if(root == null) {
			root = buildTree(nodeList, 0);
		}
		else {
			for(T e : nodeList) {
				add(e);
			}
		}
	}
	
	private KDNode<T> buildTree(ArrayList<T> nodeList, int depth) {
		if(nodeList.size() == 0) {
			return null;
		}
		
		// base case
		if(nodeList.size() == 1) {
			return new KDNode<T>(depth%k, nodeList.get(0));
		}
		
		// find dimension
		int dim = depth%k;
		
		T mid = median(nodeList, dim);
		
		KDNode<T> node = new KDNode<T>(dim, mid);
		node.setLeft(buildTree(leftList(nodeList, mid, dim), depth+1));
		node.setRight(buildTree(rightList(nodeList, mid, dim), depth+1));
		return node;
	}
	
	private ArrayList<T> leftList(ArrayList<T> nodeList, T mid, int dim) {
		ArrayList<T> newList = new ArrayList<T>();
		for(T e : nodeList) {
			if(e.getCoord(dim) < mid.getCoord(dim)) {
				newList.add(e);
			}
		}
		return newList;
	}
	
	private ArrayList<T> rightList(ArrayList<T> nodeList, T mid, int dim) {
		ArrayList<T> newList = new ArrayList<T>();
		for(T e : nodeList) {
			if(e.getCoord(dim) > mid.getCoord(dim)) {
				newList.add(e);
			}
			else if(e.getCoord(dim) == mid.getCoord(dim) && !e.equals(mid)) {
				newList.add(e);
			}
		}
		return newList;
	}
	
	private T median(ArrayList<T> nodeList, int dim) {
		int from = 0; 
		int to = nodeList.size()-1;
		int m = nodeList.size()/2;

		// if from == to we reached the kth element
		while (from < to) {
			int r = from;
			int w = to;
			T pivot = nodeList.get((r + w) / 2);

			// stop if the reader and writer meets
			while (r < w) {

				if (nodeList.get(r).getCoord(dim) >= pivot.getCoord(dim)) { // put the large values at the end
					Collections.swap(nodeList, w, r);
					w--;
				} else { // the value is smaller than the pivot, skip
					r++;
				}
			}

			// if we stepped up (r++) we need to step one down
			if (nodeList.get(r).getCoord(dim) > pivot.getCoord(dim))
				r--;

			// the r pointer is on the end of the first k elements
			if (m <= r) {
				to = r;
			} else {
				from = r + 1;
			}
		}

		return nodeList.get(m);
	}

	public List<T> nearestNeighbors(double[] coordinates, int n) {
		if(n < 0) {
			throw new IllegalArgumentException("Neighbor count must be non-negative");
		}
		if(coordinates.length != k) {
			throw new IllegalArgumentException("Length of coordinate array must match tree dimension");
		}
		TreeMap<Double, T> tm = new TreeMap<Double, T>();
		
		if(root != null && n != 0) {
			nnHelper(coordinates, root, 0, tm, n);
		}
		return new ArrayList<T>(tm.values());
	}
	
	private void nnHelper(double[] coords, KDNode<T> node, int depth, TreeMap<Double, T> tm, int n) {
		int dim = depth%k;
		// throw error if dimensions dont match
		if(dim != node.dim) {
			System.err.println("Something went wrong");
		}
		
		if(coords[dim] < node.getPlane()) {
			if(node.hasLeft()) {
				nnHelper(coords, node.getLeft(), depth+1, tm, n);
			}
			// if plane less than max thing in pq recur here
			if(tm.size() < n || Math.pow(node.getPlane()-coords[dim], 2) < tm.lastKey()) {
				if(node.hasRight()) {
					nnHelper(coords, node.getRight(), depth+1, tm, n);
				}
			}
		}
		else {
			if(node.hasRight()) {
				nnHelper(coords, node.getRight(), depth+1, tm, n);
			}
			
			// if plane less than max thing in pq recur here
			if(tm.size() < n || Math.pow(node.getPlane()-coords[dim], 2) < tm.lastKey()) {
				if(node.hasLeft()) {
					nnHelper(coords, node.getLeft(), depth+1, tm, n);
				}
			}
		}
		T entry = node.entry;

		// attempt to add
		double d = distance(coords, entry.getCoords());
		
		if(tm.size() < n) {
			tm.put(d, entry);
		}
		else if(d < tm.lastKey()) {
			tm.pollLastEntry();
			tm.put(d, entry);
		}
	}
	
	//@Override
	public List<T> withinRadius(double[] coordinates, double radius) {
		if(radius < 0) {
			throw new IllegalArgumentException("Radius must be non-negative");
		}
		if(coordinates.length != k) {
			throw new IllegalArgumentException("Length of coordinate array must match tree dimension");
		}
		
		// create priority queue for results
		TreeMap<Double, T> tm = new TreeMap<Double, T>();
		radius = Math.pow(radius, 2);
		if(root != null) {
			radiusHelper(coordinates, radius, root, 0, tm);
		}
		
		//build result list
		List<T> results = new LinkedList<T>(tm.values());
		return results;
	}
	
	private void radiusHelper(double[] coords, double r, KDNode<T> node, int depth, TreeMap<Double, T> tm) {
		int dim = depth%k;
		
		// throw error if dimensions dont match
		if(dim != node.dim) {
			System.err.println("Something went wrong");
		}
		
		
		if(coords[dim] < node.getPlane()) {
			if(node.hasLeft()) {
				radiusHelper(coords, r, node.getLeft(), depth+1, tm);
			}
			if(Math.pow((node.getPlane()-coords[dim]), 2) <=r) {
				if(node.hasRight()) {
					radiusHelper(coords, r, node.getRight(), depth+1, tm);
				}
			}
			
		}
		else {
			if(node.hasRight()) {
				radiusHelper(coords, r, node.getRight(), depth+1, tm);
			}
			if(Math.pow((node.getPlane()-coords[dim]), 2) <=r) {
				if(node.hasLeft()) {
					radiusHelper(coords, r, node.getLeft(), depth+1, tm);
				}
			}
		}
		
		// find distance between points and current node
		double d = 0;
		for(int i=0; i<k; i++) {
			d += (Math.pow((coords[i]-node.getCoords()[i]), 2));
		}
		
		// add if within r
		if(d <= r) {
			tm.put(d, node.entry);
		}
	}
	
	public void printTree() {
		
		if(root == null) {
			System.out.println("Empty tree");
			return;
		}
		Queue<KDNode<T>> q = new LinkedList<KDNode<T>>();
		Queue<KDNode<T>> next = new LinkedList<KDNode<T>>();
		next.offer(root);
		while(!next.isEmpty()) {
			q = next;
			next = new LinkedList<KDNode<T>>();
			System.out.print("[");
			while(!q.isEmpty()) {
				KDNode<T> node = q.poll();
				System.out.print(node+ ", ");
				if(node.hasLeft()) {
					next.offer(node.getLeft());
				}
				if(node.hasRight()) {
					next.offer(node.getRight());
				}
			}
			System.out.print("]");
			System.out.println();
		}
	}
	
	private double distance(double[] coords1,double[] coords2) {
		double d = 0;
		for(int i=0; i<k; i++) {
			d += (Math.pow((coords1[i]-coords2[i]), 2));
		}
		return d;
	}
}
