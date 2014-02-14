package kdTree;

public class PQEntry implements Comparable<PQEntry> {
	public final KDTreeEntry entry;
	public final double key;
	
	public PQEntry(KDTreeEntry node, double key) {
		this.entry = node;
		this.key = key;
	}

	@Override
	public int compareTo(PQEntry arg) {
		if(arg == this) {
			return 0;
		}
		double c = this.key - arg.key;
		if(c > 0) {
			return 1;
		}
		if(c < 0) {
			return -1;
		}
		else return 0;
	}
}
