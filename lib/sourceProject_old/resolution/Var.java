package resolution;

public class Var extends Terme{
	
	 String var;
	
	public Var(String v) {
		var = v;
	}

	@Override
	void affichage() {
		System.out.print(var);
		
	}

	@Override
	public String toString() {
		return var;
	}
	
	/*

	@Override
	public Prop clausifier() {
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
 */
}

