package resolution;

public class Generator {
	protected static String nom = "f";
	protected static int cpt = 0;
	
	public static String generateFonctionName() {
			cpt++;
	return nom+cpt;
	}

}
