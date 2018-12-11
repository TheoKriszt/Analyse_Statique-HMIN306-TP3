package resolution;

public class Exists extends Prop {

	protected Prop p1;
	String var;
	
	public Exists( String v , Prop p) {
		p1 = p;
		var = v;
	}
	
	@Override
	void affichage() {
		System.out.println("∃"+var+"("+p1+")");
		
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	Prop herbrandiser() {
		return p1.herbrandiser();
	}

}
