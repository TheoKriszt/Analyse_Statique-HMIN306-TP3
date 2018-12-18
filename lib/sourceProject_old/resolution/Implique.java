package resolution;

public class Implique extends Prop {
	
	protected Prop p1;
	protected Prop p2;
	
	public Implique (Prop prop1 , Prop prop2) {
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
	@Override
	void affichage() {
		p1.affichage();
		System.out.print( " ⇒ " );
		p2.affichage();
		
	}
	
	public String toString() {
		String o = "("+p1.toString()+" ⇒ "+p2.toString()+")";
		return o;
	}

	@Override
	Prop clausifier() {
		Not n = new Not(p1);
		Or o = new Or(n,p2);
		return o.clausifier();
		
	}

	@Override
	Prop getProp() {
		return this;
	}

	@Override
	Prop skolemizer() {
		Implique i = new Implique(p1.herbrandiser(),p2.herbrandiser());
		return i;
	}

	@Override
	Prop herbrandiser() {
		Implique i = new Implique(p1.skolemizer(),p2.skolemizer());
		return i;
	}

}
