package Model;



public class MensSpeler implements Speler {
	private String naam;
	private Mark mark;
	

	  /** Construeert een MensSpeler object. */
	public MensSpeler(String naam, int mark) {
		this.naam=naam;
		this.mark=Mark.getMark(mark);
	}
	
    /** Vraagt de speler een zet te doen en geeft deze door aan de server. */


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
	 * geeft de zet die deze speler bepaald heeft terug. Bij een mensSpeler is dat altijd {-1,-1,-1,0}.
	 * @return {-1,-1,-1,0}
	 */
	@Override
	public int[] bepaalZet(Bord b) {

		return new int[] {-1,-1,-1,0};}


	@Override
	public boolean equals(Object o){
		Speler s=null;
		if(o instanceof Speler)
			s= (Speler)o;
		return this.getNaam().equals(s.getNaam());
	}
	

    
    
}
