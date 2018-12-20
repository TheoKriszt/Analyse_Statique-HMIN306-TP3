package resolution;

public class Generator {
	protected static String nom = "f";
	protected static int cpt = 0;
	private Object lol = null;
	private Object lol2 = null;
	private Object lol3 = null;
	private Object lol4 = null;
	private Object lol5 = null;
	private Object lol6 = null;
	private Object lol7 = null;

	public static String generateFonctionName() {
			cpt++;
	return nom+cpt;
	}

}
