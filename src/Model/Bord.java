package Model;

import java.util.ArrayList;



/* 
 * Het bord van het spel.
 * 
 */

public class Bord {
	public  static final int DIM=9;
	public static final int SUBDIM=3;
	public static final int WINNUM=5;
	
    private Mark[][] bord=new Mark[DIM][DIM];

	
	
    /** Construeert een Bord object met 81 velden en Mark.LEEG object-referenties op de velden */
	public Bord() {
		reset();
			
		}
	
    /**
     * Stelt de mark op het bord in.
     * @param x X coordinaat van het vakje
     * @param y Y coordinaat van het vakje.
     * @param m De mark waar het vakje op ingesteld moet worden.
     */
	
    public void setVakje(int x, int y, Mark m){bord[y][x]=m;}
    
    /**
     * geeft de mark terug die op een bepaalde plek op het bord staat.
     * @param x X coordinaat van het vakje.
     * @param y Y coordinaat van het vakje.
     * @return de mark op (x,y)
     */
    
	public Mark getVakje(int x, int y){return bord[y][x];}
	
	/**
	 * geeft true terug als het vakje op x,y leeg is.
	 * @param x X coordinaat van het vakje.
	 * @param y Y coordinaat van het vakje.
	 * @return true if getVakje(x,y)==Mark.LEEG
	 */
	
	public boolean isLeegVakje(int x, int y){return bord[y][x]==Mark.LEEG;}
	
	
	/**
	 *  levert de index van het subbord op waarin het vakje zich bevind.

	 * @param x X coordinaat van het vakje
	 * @param y Y coordinaat van het vakje
	 * @return
	 */
	public int getSubBord(int x, int y){
		int rij=((int)x/SUBDIM);
		int kolom=((int)y/SUBDIM)*SUBDIM;
		return rij+kolom;}
	
	
	/**
	 * geeft een int[] met de coordinaten van het betreffende subBord.
	 * @require subBordIndex>=0	&&	subBordIndex<DIM
	 * @param subBordIndex int tussen de 0 en DIM.
	 * @return int[4]: x1, x2, y1, y2
	 */
	
	public int[] getSubBordVakjes(int subBordIndex){
		int[] returnable=new int[4];
		returnable[0]=(subBordIndex%3)*(SUBDIM);
		returnable[1]=(subBordIndex%3)*(SUBDIM)+(SUBDIM-1);
		returnable[2]=((int)subBordIndex/3)*(SUBDIM);
		returnable[3]=((int)subBordIndex/3)*(SUBDIM)+(SUBDIM-1);
		return returnable;
	}
	/**
	 * Draait  het subBord een kwartslag met de klok mee, of tegen de klok in.
	 * @param subBord Nummer van het subBord
	 * @param c direction De richting van het draaien van het subBord
	 * @require c=='+' || c=='-'
	 * @require subBord>=0&&subBord<DIM
	 */
	
	public void draaiBord(int subBord, char richting){
		//kopieert het te draaien subbord.
		Mark[][] temp=copySubBord(subBord);
		Mark[][] nieuw=new Mark[SUBDIM][SUBDIM];
		int teller=0;
		if(richting=='+'){
			//voor elke rij: kopieer de kolom, keer hem om, en sla hem op in een rij van een nieuwe array.
			for(int i=0;i<SUBDIM;i++){
				nieuw[i]=keerArrayOm(copyKolom(temp, teller, SUBDIM));
				teller++;
			}
			}
		else if(richting=='-'){
			//voor elke rij: kopieer de kolom, sla hem op in een rij van een nieuwe array.
				for(int i=SUBDIM-1;i>=0;i--){
					nieuw[i]=copyKolom(temp, teller, SUBDIM);
					teller++;
				}
				}
		int[] coordinaten=getSubBordVakjes(subBord);
		int subY=0;
		//Plak het gedraaide subBord op de juiste plek op het originele bord.
		for(int y=coordinaten[2];y<=coordinaten[3];y++){
			int subX=0;
			for(int x=coordinaten[0];x<=coordinaten[1];x++){
			bord[y][x]=nieuw[subY][subX];
				subX++;
			}
			subY++;
		}

		}

/**
 * Kopieerd het subBord: i in een nieuwe Mark[][] en geeft deze terug.
 * @param i het subBord wat gekopieerd moet worden
 * @require i>=0&&i<DIM
 * @return
 */
    
    public Mark[][] copySubBord(int i){
    	Mark[][] subBord=new Mark[SUBDIM][SUBDIM];
    	int[] coordinaten=getSubBordVakjes(i);
		int subY=0;

    	for(int y=coordinaten[2];y<=coordinaten[3];y++){
    		int subX=0;
			for(int x=coordinaten[0];x<=coordinaten[1];x++){
			subBord[subY][subX]=this.bord[y][x];
			subX++;
			}
			subY++;
		}
    	
    return subBord;}
    
    /**
     * kopieerd het hele bord en geeft deze terug.
     * @return
     */
    
    public Bord deepCopy(){
    	Bord returnable=new Bord();
    	for(int y=0;y<DIM;y++){
    		for(int x=0;x<DIM;x++){
    			Mark m=bord[y][x];
    			returnable.setVakje(x, y, m);
    		}
    	}
    return returnable;}
    
    /**
     * Kopieerd een kolom van een Mark[][] origineel, en geeft deze terug.
     * @param origineel de Mark[][] waar een kolom uit gekopieerd moet worden.
     * @param kolom te kopieren kolom
     * @require kolom>=0&&kolom<DIM
     * @param dim de lengte van origineel[]
     * @return
     */

    public Mark[] copyKolom(Mark[][] origineel, int kolom, int dim){
    	Mark[] returnable=new Mark[dim];
    	int teller=0;
    	for(int rij=0;rij<dim;rij++){
    		returnable[teller]=origineel[rij][kolom];
    		teller++;
    	}
    	return returnable;}
    /**
     * Geeft een array terug die is omgekeerd. De eerste waarde is nu de laatste, etc.
     * @param m de array die omgekeerd moet worden.
     * @return
     */
    
    public Mark[] keerArrayOm(Mark[] m){
    	Mark[] returnable=new Mark[m.length];
    	int teller=m.length-1;
    	for(Mark mark:m){
    		returnable[teller]=mark;
    		teller--;}
    	return returnable;}
    /**
     * Stelt alle vakjes van bord[][] in op Mark.LEEG
     */
    
    public void reset(){
    	for(int y=0;y<DIM;y++){
			for(int x=0;x<DIM;x++){
				bord[x][y]=Mark.LEEG;
			}
    	}

    	
    }

    
 /**
  * kijkt of het bord winnaars heeft, en geeft een array met Marks terug die gewonnen hebben.
  * @return 
  */
    
    public Mark[] getWinnaars(){
    	ArrayList<Mark> returnable=new ArrayList<Mark>();
    	if(isWinnaar(Mark.S1)){
    	returnable.add(Mark.S1);
    	} else if(isWinnaar(Mark.S2)){
    		returnable.add(Mark.S2);
    	}else if(isWinnaar(Mark.S3)){
    		returnable.add(Mark.S3);;
    	}else if(isWinnaar(Mark.S4)){
    		returnable.add(Mark.S4);
    	}
    	Mark[] temp=new Mark[returnable.size()];
    	int counter=0;
    	for(Mark m:returnable){
    		temp[counter]=m;
    		counter++;
    		
    	}
    	return temp;
    }

    /**
     * kijkt of een van de spelers gewonnen heeft.
     * @return true if
     */
	public boolean heeftWinnaar(){
		return isWinnaar(Mark.S1)||isWinnaar(Mark.S2)||isWinnaar(Mark.S3)||isWinnaar(Mark.S4);}
	
	/**
	 * geeft true terug als een bepaalde speler gewonnen heeft.
	 * @param m
	 * @return
	 */
	public boolean isWinnaar(Mark m){
		return heeftRij(m)||heeftKolom(m)||heeftDiagonaal(m);}
	
	/**
	 * geeft true terug als een bepaalde speler gewonnen heeft met een rij.
	 * @param m
	 * @return
	 */
	public boolean heeftRij(Mark m){

		boolean mustContinue=true;
		boolean returnable=false;
		//doorloopt alle rijen van het bord. Als er 5 keer achter elkaar dezelfde mark staat is 
		//counter==5 en kan de methode stoppen.
		for(int y=0;y<DIM&&mustContinue;y++){
			int counter=0;
			for(int x=0;x<DIM&&mustContinue;x++){
				
				if(getVakje(x, y)==m){
					counter++;
					if(counter>=5){
						mustContinue=false;
						returnable=true;
					}}
				else{
					counter=0;
				}
				
			}
		}
		return returnable;
	}
	
	/**
	 * geeft true terug als een bepaalde speler gewonnen heeft met een kolom.
	 * @param m
	 * @return
	 */
	public boolean heeftKolom(Mark m){
		boolean mustContinue=true;
		boolean returnable=false;
		//doorloopt alle kolomen van het bord. Als er 5 keer achter elkaar dezelfde mark staat is 
		//counter==5 en kan de methode stoppen.
		for(int x=0;x<DIM&&mustContinue;x++){
			int counter=0;
			for(int y=0;y<DIM&&mustContinue;y++){
				if(getVakje(x, y)==m){
					counter++;
					if(counter>=5){
						mustContinue=false;
						returnable=true;
					}}
				else{
					counter=0;
				}
			}
		}

		return returnable;
	}
	/**
	 * geeft true terug als een bepaalde speler gewonnen heeft met een diagonaal.
	 * @param m
	 * @return
	 */
	
	public boolean heeftDiagonaal(Mark m){
		boolean returnable=false;
		boolean mustContinue=true;
		//doorloopt alle diagonalen van het bord. Als er 5 keer achter elkaar dezelfde mark staat is 
		//counter==5 en kan de methode stoppen.
		//alle diagonalen van rechtsboven naar linkssonder.
		for(int beginX=WINNUM-1;beginX<2*DIM-(WINNUM-1)&&mustContinue;beginX++){
			int counter=0;
			int x=((beginX<DIM)?beginX:DIM-1);
			int y=((beginX<DIM)?0:beginX-(DIM-1));
			while(isGeldigHokje(x,y)&&mustContinue){
				if(getVakje(x, y)==m){

					counter++;
					if(counter==5){
						returnable=true;
						mustContinue=false;
					}}
				else{
					counter=0;
				}	
				x--;
				y++;
			}	
		}
		//doorloopt alle diagonalen van het bord. Als er 5 keer achter elkaar dezelfde mark staat is 
		//counter==5 en kan de methode stoppen.
		//alle diagonalen van linksboven naar rechtsonder.
			for(int beginX=DIM-WINNUM;beginX<2*DIM-(WINNUM-1)&&mustContinue;beginX++){
				int counter=0;
				int x=((beginX<DIM)?beginX:DIM-1);
				int y=((beginX<DIM)?DIM:DIM-1-(beginX-DIM-1));
				while(isGeldigHokje(x,y)&&mustContinue){
					if(getVakje(x, y)==m){
						counter++;
						if(counter==5){
							returnable=true;
							mustContinue=false;
						}}
					else{
						counter=0;

					}	
					x--;
					y--;
				}	
		}
		
		
		return returnable;
		
	}
	/**
	 * geeft true terug als x,y een geldig hokje is.
	 * @param x X coordinaat op het bord
	 * @param y Y coordinaat op het bord
	 * @require x>=0&&x<DIM
	 * @require y>=0&&x<DIM
	 * @return
	 */
	public boolean isGeldigHokje(int x, int y){
		return x<DIM&&y<DIM&&x>=0&&y>=0;}
	/**
	 * geeft het aantal lege vakjes terug
	 * @return
	 */
	public int getLegeVakjes(){
		int returnable=0;
		for(Mark[] m:bord){
			for(Mark mk:m){
				if(mk==Mark.LEEG)
					returnable++;
			}
		}
		
		
		return returnable;
	}
	
	}
