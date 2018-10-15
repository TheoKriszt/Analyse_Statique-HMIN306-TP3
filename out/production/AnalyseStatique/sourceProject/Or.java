package resolution;


public class Or extends Prop {

	protected Prop p1;
	protected Prop p2;
	
	public Or (Prop prop1 , Prop prop2) {
		p1 = prop1;
		p2 = prop2;
	}
	
	public Prop getp1() {
		return p1;
	}
	
	public Prop getp2() {
		return p2;
	}
	
	@Override
	void affichage() {
		System.out.print("(");
		p1.affichage();
		System.out.print( " v " );
		p2.affichage();
		System.out.print(")");
	}
	
	public String toString() {
		String o = "("+p1.toString()+"v"+p2.toString()+")";
		return o;
	}

	@Override
	Prop clausifier() {
		
		if (p2 instanceof Top) {
			Top t = new Top();
			return t;		
		}
		
		else if (p1 instanceof Top) {
			Top t = new Top();
			return t;		
		}
		
		else if (p1 instanceof Bottom) {
			return p2.clausifier();
		}
		
		else if (p2 instanceof Bottom) {
			return p1.clausifier();
		}
		
		else if (p1 instanceof And) {
			Or o1 = new Or (p1.getp1(),p2);
			Or o2 = new Or (p1.getp2(),p2);
			
			And a1 = new And (o1,o2);
			return a1;
		}
		
		else if (p2 instanceof And) {
			Or o1 = new Or (p1,p2.getp1());
			Or o2 = new Or (p1,p2.getp2());
			
			And a1 = new And (o1,o2);
			return a1;
		}
		
		else if (p1 instanceof Or) {
			if ((p1.getp1() instanceof Predicat && p1.getp2() instanceof Predicat)
					||(p1.getp1() instanceof Not && p1.getp1().getProp() instanceof Predicat && p1.getp2() instanceof Not && p1.getp2().getProp() instanceof Predicat)
					||(p1.getp1() instanceof Not && p1.getp1().getProp() instanceof Predicat && p1.getp2() instanceof Predicat)
					||(p1.getp1() instanceof Predicat && p1.getp2() instanceof Not && p1.getp2().getProp() instanceof Predicat)) {
				return this;
			}
			else {
		
				Or o1 = new Or(p1.clausifier(),p2.clausifier());
				return o1.clausifier();			
			}
		}
		
		else if (p2 instanceof Or) {
			if ((p2.getp1() instanceof Predicat && p2.getp2() instanceof Predicat)
					||(p2.getp1() instanceof Not && p2.getp1().getProp() instanceof Predicat && p2.getp2() instanceof Not && p2.getp2().getProp() instanceof Predicat)
					||(p2.getp1() instanceof Not && p2.getp1().getProp() instanceof Predicat && p2.getp2() instanceof Predicat)
					||(p2.getp1() instanceof Predicat && p2.getp2() instanceof Not && p2.getp2().getProp() instanceof Predicat)){
				return this;
			}
			else {
				Or o1 = new Or(p1.clausifier(),p2.clausifier());
				return o1.clausifier();
			}
			
		}
		
		else if ((p1 instanceof Predicat && p2 instanceof Predicat) 
				|| (p1 instanceof Not && p1.getProp() instanceof Predicat && p2 instanceof Not && p2.getProp() instanceof Predicat) 
				|| (p1 instanceof Not && p1.getProp() instanceof Predicat && p2 instanceof Predicat)
				|| (p1 instanceof Predicat && p2 instanceof Not && p2.getProp() instanceof Predicat)) {
			
			return this;			
		}
	
		else if (p2 instanceof Not && p2.getProp() instanceof Predicat) {
			Or o1 = new Or(p1,p2.clausifier());
			return o1.clausifier();
		}
		
		else if (p1 instanceof Not && p1.getProp() instanceof Predicat) {
			Or o1 = new Or(p1.clausifier(),p2);
			return o1.clausifier();
		}
		
	
		else if (p1 instanceof Predicat ) {
			Or o1 = new Or(p1,p2.clausifier());
			return o1.clausifier();
		}
		
		else if (p2 instanceof Predicat) {
			Or o1 = new Or(p1.clausifier(),p2);
			return o1.clausifier();
			
		}
			

		else {
			Or o1 = new Or(p1.clausifier(),p2.clausifier());
			return o1.clausifier(); 
		}
			
	}
	
	@Override
	Prop skolemizer() {
		
		Or o = new Or(p1.skolemizer(),p2.skolemizer());
		return o;
	}

	@Override
	Prop herbrandiser() {
		Or o = new Or(p1.herbrandiser(),p2.herbrandiser());
		return o;
	}
	



	@Override
	Prop getProp() {
		// TODO Auto-generated method stub
		return null;
	}

	

	

}
