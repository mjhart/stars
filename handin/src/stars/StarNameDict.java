package stars;

import java.util.Hashtable;

public class StarNameDict {
	private Hashtable<String, Star> stars;
	
	public StarNameDict() {
		stars = new Hashtable<String, Star>();
	}
	
	public void addStar(String name, Star star) {
		stars.put(name, star);
	}
	
	public double[] getCoords(String name) {
		return stars.get(name).getCoords();
	}
}
