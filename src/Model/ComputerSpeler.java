package Model;



public class ComputerSpeler implements Speler {
	private String naam;
	private Mark mark;
	private Strategie strategie;
	
    /** Construeert een ComputerSpeler object. */
	public ComputerSpeler(String naam, int mark, Strategie strat) {
		this.naam=naam;
		this.mark=Mark.getMark(mark);
		this.strategie=strat;
	}
    /** Construeert een ComputerSpeler object. */
	public ComputerSpeler(String naam, int mark) {
		this.naam=naam;
		this.mark=Mark.getMark(mark);
		this.strategie=new DommeStrategie();
	}
	

	/**
	 * Geeft de Mark van deze speler terug
	 */
	@Override
	public Mark getMark() {return mark;}
	/**
	 * geeft de naam van deze speler terug
	 */
	@Override
	public String getNaam() {return naam;}
	

	/**
	 * geeft de zet die deze speler bepaald heeft terug.
	 * @return {x,y,subBord,(+||-)}
	 */
	@Override
	public int[] bepaalZet(Bord b) {
		return strategie.bepaalZet(b);}

	@Override
	public boolean equals(Object o){
		Speler s=null;
		if(o instanceof Speler)
			s= (Speler)o;
		return this.getNaam().equals(s.getNaam());
	}

}
