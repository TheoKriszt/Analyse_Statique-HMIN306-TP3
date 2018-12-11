package resolution;

import java.util.ArrayList;

public abstract class Prop {
	
	abstract void affichage();
	
	abstract Prop clausifier();

	abstract Prop getProp();
	
	abstract Prop getp1();
		
	abstract Prop getp2();
	
	abstract Prop skolemizer();
	
	abstract Prop herbrandiser();
	
	ArrayList<Prop> ListeClause;
		
	

}
