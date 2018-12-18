package resolution;

public class Not extends Prop {
	
	protected Prop p1;
	
	public Not(Prop p) {
		p1 = p;
	}

	public Prop getProp() {
		return p1;
	}

	
	@Override
	void affichage() {
		System.out.print("¬(");
		p1.affichage();
		System.out.print(")");
		
	}
	
	 Prop clausifier() {
		if (p1 instanceof Not ) {
			Prop n = p1.clausifier();
			return n;		
		}
		else if (p1 instanceof Bottom) {
			Prop t = new Top();
			return t.clausifier();
			
		}
		
		else if (p1 instanceof Top) {
			Prop b = new Bottom();
			return b.clausifier();
		}
		
		else if (p1 instanceof And) {
			Not n1 = new Not(p1.getp1());
			Not n2 = new Not(p1.getp2());
			Or or = new Or(n1,n2);
			return or.clausifier();
			
		}
		
		else if (p1 instanceof Or) {
			Not n1 = new Not(p1.getp1());
			Not n2 = new Not(p1.getp2());
			And a = new And(n1,n2);
			return a.clausifier();
		}
		
		else if (p1 instanceof Predicat) {
			  return this;
			  
		}
		
		else if (p1 instanceof Implique) {
			Not n = new Not(p1.getp2());
			And a = new And(p1.getp1(),n);
			return a.clausifier();
		}
		
		else {
			Not n = new Not(p1.getProp().clausifier());
			return n;
		}
		
		
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


	/**
	 * Skolemise le truc
	 * @return
	 */
	@Override
	Prop skolemizer() {
		Not n = new Not(p1.herbrandiser());
		return n;
	}

	@Override
	Prop herbrandiser() {
		Not n = new Not(p1.skolemizer());
		return n;
	}

	

	


}
