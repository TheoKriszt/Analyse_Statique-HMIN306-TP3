package resolution;

import java.util.ArrayList;

public class Predicat extends Prop {
	
	String nom;
	
	ArrayList<Terme> args;
	
	public Predicat(String n , ArrayList<Terme> a) {
		nom = n;
		args = a;
	}

	@Override
	void affichage() {
		String predicat = nom+"(";
		for (Terme t : args){
			predicat +=t.toString()+",";		
		}
		predicat = predicat.substring(0, predicat.length()-1);
		predicat +=")";
		System.out.print(predicat);		
	}

	@Override
	Prop clausifier() {
		return this;
	}

	@Override
	Prop getProp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	Prop getp1() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	Prop getp2() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	Prop skolemizer() {
		return this;
	}

	@Override
	Prop herbrandiser() {
		return this;
	}
	
	
	
	

}
