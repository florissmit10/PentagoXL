package Model;

public class DommeStrategie implements Strategie {

	public DommeStrategie(){
		
	}
	/**
	 * geeft een random zet terug.
	 * @return {x,y,subBord,(+||-)}
	 */
	@Override
	public int[] bepaalZet(Bord b) {
		int x=(int)(Math.random()*(Bord.DIM-1));
		int y=(int)(Math.random()*(Bord.DIM-1));
		while(!b.isLeegVakje(x, y)){
			x=(int)(Math.random()*(Bord.DIM-1));
			y=(int)(Math.random()*(Bord.DIM-1));
		}
			
		
		
		return new int[]{x,
						y,
						(int)(Math.random()*(Bord.DIM-1)), 
						((Math.random()>0.5)?-1:1)};

	}
	
	
}

