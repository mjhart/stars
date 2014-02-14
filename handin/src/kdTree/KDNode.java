package kdTree;

class KDNode<T extends KDTreeEntry> {
	public final int dim;
	private KDNode<T> left;
	private KDNode<T> right;
	public final T entry;
	
	public KDNode(int dim, T entry) {
		if(dim >= 0) {
			this.dim = dim;
		}
		else {
			throw new IllegalArgumentException("dim must be greater than 0");
		}
		if(entry != null) {
			this.entry = entry;
		}
		else {
			throw new IllegalArgumentException("entry must be non-null");
		}
	}
	
	public double[] getCoords() {
		return entry.getCoords();
	}
	
	public double getPlane() {
		return entry.getCoord(dim);
	}

	public KDNode<T> getRight() {
		return right;
	}

	public void setRight(KDNode<T> right) {
		this.right = right;
	}

	public KDNode<T> getLeft() {
		return left;
	}

	public void setLeft(KDNode<T> left) {
		this.left = left;
	}
	
	public boolean hasLeft() {
		return left != null;
	}
	
	public boolean hasRight() {
		return right != null;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Dim: ");
		str.append(dim);
		str.append(" Coords: ");
		for(double d : entry.getCoords()) {
			str.append(d + " ");
		}
		return str.toString();
	}
}
