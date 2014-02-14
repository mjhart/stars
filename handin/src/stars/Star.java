package stars;

import kdTree.KDTreeEntry;

public class Star implements KDTreeEntry {
	private double[] coordinates; // need to make immutable
	public final String name;
	public final int id;
	
	public Star(int id, double x, double y, double z) {
		coordinates = new double[3];
		coordinates[0] = x;
		coordinates[1] = y;
		coordinates[2] = z;
		this.id = id;
		name = null;
	}
	
	public Star(int id, String name, double x, double y, double z) {
		coordinates = new double[3];
		coordinates[0] = x;
		coordinates[1] = y;
		coordinates[2] = z;
		this.id = id;
		this.name = name;
	}
	
	@Override
	public double getCoord(int dim) {
		return coordinates[dim];
	}

	@Override
	public double[] getCoords() {
		return coordinates;
	}

	@Override
	public int getDimensions() {
		return 3;
	}
}
