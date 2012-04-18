package Model;

public class SlimmeStrategie implements Strategie {

	/**
	 * Domme strategie.
	 * Als er met een vakje gewonnen kan worden, kiest de strategie dit vakje.
	 * Als er met een vakje voorkomen kan worden dat de tegenstander wint, kiest de strategie dit vakje.
	 * Als er met het draaien van een subbord gewonnen kan worden, draait de strategie dit bord.
	 * Als er met het draaien van een subbord voorkmoen kan worden dat de tegenstander wint, draait de strategie dit bord
	 * Als het middelste vakje vrij is, kiest de strategie dit vakje.
	 * Anders kiest de strategie random vakjes.
	 */
	
	private Mark mark;
	private int players;
	private boolean heeftSubBord = false;
	private int denkTijd;
	
	
	public SlimmeStrategie(int m, int players, int denkTijd) {
		mark = Mark.getMark(m);
		this.players = players;
		this.denkTijd = denkTijd;
	}
	
	
	@Override
	public int[] bepaalZet(Bord b) {
		long startTime = System.currentTimeMillis();
		int[] zet = new int[4];
		int kwal = -1;
		Bord copy = b.deepCopy();
		//gaat alle vakjes van het bord langs
		for (int x = 0;(System.currentTimeMillis()-startTime)<denkTijd && x<Bord.DIM && !copy.heeftWinnaar();x++) {
			for(int y = 0;(System.currentTimeMillis()-startTime)<denkTijd && y<Bord.DIM && !copy.heeftWinnaar();y++) {
				//zet een vakje als er mee gewonnen kan worden
				if (copy.isLeegVakje(x, y) && kwal < 5) {
					copy.setVakje(x, y, mark);
					if (copy.heeftWinnaar()) {
						zet[0] = x;
						zet[1] = y;
						kwal = 5;
					}
					copy.setVakje(x, y, Mark.LEEG);
				}
				//zet een vakje als er mee kan worden voorkomen dat de tegenstander wint
				if(copy.isLeegVakje(x, y) && kwal < 4) {
					
					copy.setVakje(x, y, mark.next(players));
					if(copy.heeftWinnaar()) {
						zet[0] = x;
						zet[1] = y;
						kwal = 4;
					}
					copy.setVakje(x, y, Mark.LEEG);
				}				
			}
		}
		//zet het middelste vakje als deze vrij is
			if (b.isLeegVakje((int)Bord.DIM/2, (int)Bord.DIM/2) && kwal < 3) {
				zet[0] = 4;
				zet[1] = 4;
				kwal = 3;
			}
		//zet anders een willekeurig vakje
		if (kwal < 2) {
			boolean heeftVakje = false;
			while(!heeftVakje) {
				int testx=(int)(Math.random()*(Bord.DIM-1));
				int testy=(int)(Math.random()*(Bord.DIM-1));
				if(b.isLeegVakje(testx, testy)){
					zet[0] = testx;
					zet[1] = testy;
					kwal = 2;
					heeftVakje = true;
				}
			}
		}
		
		copy.setVakje(zet[0], zet[1], mark);
		//ga alle subborden langs
		for (int subBord=0;(System.currentTimeMillis()-startTime)<denkTijd && subBord<Bord.DIM;subBord++) {
			copy.draaiBord(subBord, '+');
			if (copy.heeftWinnaar()) {
				zet[2] = subBord;
				zet[3] = 1;
				copy.draaiBord(subBord, '-');
				heeftSubBord= true;
			}
			copy.draaiBord(subBord, '-');
			if (copy.heeftWinnaar()) {
				zet[2] = subBord;
				zet[3] = -1;
				copy.draaiBord(subBord, '+');
				heeftSubBord = true;
			}
		}
			if(!heeftSubBord) {
				zet[2] = (int)(Math.random()*(Bord.DIM-1));
				zet[3] = ((Math.random()>0.5)?-1:1);
			}
		return zet;
	}

}
