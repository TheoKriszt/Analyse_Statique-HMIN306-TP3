package resolution;

import java.util.ArrayList;

public class AppFonction extends Terme {
	
	String nom;
	
	ArrayList<Terme> args;

	@Override
	void affichage() {
		String predicat = nom+"(";
		for (Terme t : args){
			predicat +="," +t.toString();		
		}
		predicat +=")";
		System.out.println(predicat);
		
	}

	@Override
	public String toString() {
		String predicat = nom+"(";
		for (Terme t : args){
			predicat +="," +t.toString();		
		}
		predicat +=")";
		return predicat;
	}
	

}
