package Model;
import java.util.Observable;





public class Spel extends Observable {
    private Bord bord;
    private Speler huidig;
    // @supplierCardinality 2..4 
    private Speler[] spelers;
    public static final int MARBLES2P=40;
    public static final int MARBLES=20;
    
    /** Construeert een Spel object. 
     * @param spelers Een array met spelers die meedoen aan dit spel
     * @require spelers.length >=2 && spelers.length <=4
     * */
    public Spel (Speler[] spelers) {
    	bord=new Bord();
    	this.spelers=spelers;
    	huidig=spelers[0];
    }
    /** Construeert een Spel object. */
    public Spel(){
    	bord=new Bord();
    }
    
    /**
     *  Construeert een Spel object.
     * @param namen String[] met de namen van andere spelers
     * @require namen.length >=2 && namen.length <=4
     * @param numberOfPlayers hoeveel spelers er meedoen
     * @require numberOfPlayers<=2&&numberOfPlayers<=4
     */
    public Spel (String[] namen,int numberOfPlayers){
    	bord=new Bord();
    	spelers=new Speler[numberOfPlayers];
    	int i=1;
    	for(String naam:namen){
    		spelers[i-1]=new MensSpeler(naam, i);
    		i++;
    	}
    	huidig=spelers[0];
    }
    /**
     * Overschrijft de spelers die betrokken zijn bij het spel.
     * @require spelers==null
     * @param s 
     * @require s.length >=2 && s.length <=4
     */
    public void addSpelers(Speler[] s){
    	spelers=s;
    	huidig=spelers[0];}
    
    /**
     * geeft de naam van de speler die aan de beurt is.
     * @return huidig.getnaam()
     */
    public String getHuidig() {return huidig.getNaam();}
    
    
    /**
     * geeft de huidige speler
     * @return huidig
     */
    public Speler getHuidigSpeler() {return huidig;}
    
    /**
     * geeft het bord terug.
     * @return bord
     */
    public Bord getBord(){return bord;}
    
    /**
     * Stelt het vakje x,y in op de mark van de huidige speler.
     * @param x X coordinaat op het bord
	 * @require x>=0&&x<DIM
	 * @param y Y coordinaat op het bord
	 * @require y>=0&&x<DIM
     */
    
    public void setVakje(int x, int y){
    	if(!this.gameOver()){
    	bord.setVakje(x, y, huidig.getMark());
    	}
    }
    /**
     * Stelt het vakje x,y in op de mark m.
     * @param x X coordinaat op het bord
     * @param y Y coordinaat op het bord
     * @param m Mark die ingesteld moet worden
	 * @require x>=0&&x<DIM
	 * @require y>=0&&x<DIM
     */

	public void setVakje(int x, int y, Mark s1) {
		if(!this.gameOver()){
		bord.setVakje(x, y, s1);
		}
	}
	
	/**
	 * geeft de zet terug van speler s.
	 * @param s Speler die de zet moet doen.
	 * @return {x,y,SubBord, -1||+1} if s instanceof MensSpeler.{-1,-1,-1,0}
	 */
    
	public int[] getZet(Speler s){
		int[] returnable=new int[]{-1,-1,-1,-1};
		if(!this.gameOver()){
		returnable = s.bepaalZet(bord);
		}
		return returnable;
	}
 /**
  * Plaats de mark van de huidige speler op x,y en draai subbord subBord.
  * @param x X coordinaat op het bord
  * @param y Y coordinaat op het bord
  * @param subBord
  * @param richting
  * @require x>=0&&x<DIM
  * @require y>=0&&x<DIM
  * @require subBord<=0&&subBord>Bord.DIM
  */
    public void doeZet(int x, int y, int subBord, char richting){
    	if(!this.gameOver()){
    	bord.setVakje(x, y, huidig.getMark());
    	if(!this.gameOver()){
    	bord.draaiBord(subBord, richting);
    	this.nextPlayer();
    	}
    	setChanged();
    	notifyObservers();
    	}
    	}
    /**
     * stelt de speler die aan de beurt is in op speler s.
     * @param s
     * @require getSpeler(s)!=null;
     * @ensure huidig=getSpeler(s);
     */
    
    public void setHuidig(String s){
    	if(!this.gameOver()){
    	if(getSpeler(s)!=null)
    		huidig=getSpeler(s);
    	}
    }
    /**
     * Stelt de huidige speler in op de volgende speler die aan de beurt is.
     * @ensure huidig= next player on turn
     */
    public void nextPlayer(){
    	if(!this.gameOver()){
    int index=0;
    int counter=0;
    for(Speler s:spelers){
    	if(s.equals(huidig)){
    		index=counter;
    	}
    	counter++;
    
    }
    
    if(index+1==spelers.length)
    	huidig=spelers[0];
    else{
    	huidig=spelers[index+1];
    }
   
    	}
    }
    /**
     * geeft true terug als het spel een winnaar heeft of als de huidige speler geen Marbles meer heeft.
     * @return
     */
    public boolean gameOver(){
    	return bord.heeftWinnaar()||!hasMarblesLeft();}
    
   /**
    * geeft true terug als de huidige speler meer dan 0 marbles over heeft
    * @return huidig.marksLeft()>0
    */
   
   
    public boolean hasMarblesLeft(){
    	boolean returnable=true;
    	int legeVakjes=bord.getLegeVakjes();
    	if(spelers.length==2){
    		returnable=legeVakjes>((Bord.DIM*Bord.DIM)-(2*MARBLES2P));
    	}else{
    		returnable=legeVakjes>((Bord.DIM*Bord.DIM)-(spelers.length*MARBLES));
    	}
    	return returnable;}
    
    /**
     * Geeft de namen van alle winnaars van het spel terug
     * @return
     */
    
    public String[] getWinnaars(){
    	Mark [] winners=bord.getWinnaars();
    	String[] returnable=new String[winners.length];
    	int i=0;
    	for(Mark m:winners){
    		String name=m.getValue()+"";
    		returnable[i]=spelers[Integer.parseInt(name)-1].getNaam();
    		i++;}
    	
    	return returnable;}
    /**
     * Draait Subbord subBord in de richting richting.
     * @param subBord het te draaien subBord.
     * @param richting Richting waarin het subBord gedraaid moet worden
     * @require subBord>=0&&subBord<9
     * @require c=='+' || c=='-'
     */
    
    public void draaiBord(int subBord, char richting){
    	if(!this.gameOver())
    	bord.draaiBord(subBord, richting);

    }
    /**
     * returned het speler object met naam s. 
     * @param s
     * @return null||Speler s from spelers[].
     */
    public Speler getSpeler(String s){
    	Speler returnable=null;
    	for(Speler sp:spelers){
  		   if(s.equals(sp.getNaam()))
  			   returnable=sp;}
    	return returnable;
    }
    /**
     * reset het Bord van dit spel
     * @ensure for all x,y with isGeldigVakje(x,y)==true. getVakje(x,y)==Mark.LEEG
     */
    
    public void resetBord(){
    	bord.reset();
    	setChanged();
    	notifyObservers();
    }
    /**
     * geeft true terug als het vakje x,y leeg is.
     * @param x X coordinaat op het bord
     * @param y Y coordinaat op het bord
	 * @require x>=0&&x<DIM
	 * @require y>=0&&x<DIM
     * @return bord.isLeegVakje(x, y)
     */
    
	public boolean isLeegVakje(int x, int y) {return bord.isLeegVakje(x, y);}

}
