package stars;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("ERROR: Usage: stars <filename>");
			System.exit(1);
		}
		Controller c = new Controller(args[0]);
		new CommandLine(c);
	}

}
