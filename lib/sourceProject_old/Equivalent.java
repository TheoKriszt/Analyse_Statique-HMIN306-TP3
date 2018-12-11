package resolution;

public class Equivalent extends Prop {
	
	protected Prop p1;
	protected Prop p2;
	
	public Equivalent (Prop prop1 , Prop prop2) {
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
		p1.affichage();
		System.out.print( " ⇔ " );
		p2.affichage();
		
		
	}

	@Override
	Prop clausifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	Prop getProp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	Prop skolemizer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	Prop herbrandiser() {
		// TODO Auto-generated method stub
		return null;
	}

}
