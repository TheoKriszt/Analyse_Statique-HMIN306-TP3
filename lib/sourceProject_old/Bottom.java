package resolution;

public class Bottom extends Prop {
	
	
public Bottom () {
		
	}

	@Override
	void affichage() {
		System.out.print( " ⊥ " );
		
	}

	@Override
	Prop clausifier() {
		System.out.print( " ⊥ " );
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
		return this;
	}

	@Override
	Prop herbrandiser() {
		return this;
	}

}
