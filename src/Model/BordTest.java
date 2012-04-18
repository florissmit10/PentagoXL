package Model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BordTest {
	private Bord[] borden=new Bord[9];
	 public static void main(String args[]) {
	      org.junit.runner.JUnitCore.main("Model.BordTest");
	    }
	  
	@Before
	public void setUp() throws Exception {
		for(int i=0;i<borden.length;i++){
			borden[i]=new Bord();
		}
	}

	
	@Test
	public void testCopySubBord(){
		int counter=0;
		int[] coord;
		for(Bord b:borden){
			coord=b.getSubBordVakjes(counter);
			b.setVakje(coord[0], coord[2], Mark.S1);
			b.setVakje(coord[0], coord[3], Mark.S1);
			b.setVakje((coord[1]+coord[0])/2, (coord[3]+coord[2])/2, Mark.S1);
			b.setVakje(coord[1], coord[2], Mark.S1);
			b.setVakje(coord[1], coord[3], Mark.S1);

		Mark[][] subBord=b.copySubBord(0);
		assertTrue(subBord[coord[0]][coord[2]]==Mark.S1);
		assertTrue(subBord[coord[0]][coord[3]]==Mark.S1);
		assertTrue(subBord[(coord[1]+coord[0])/2][(coord[3]+coord[2])/2]==Mark.S1);
		assertTrue(subBord[coord[1]][coord[2]]==Mark.S1);
		assertTrue(subBord[coord[1]][coord[3]]==Mark.S1);
		}
	}
	
	@Test 
	public void testCopyColum(){
		int counter=0;
		int[] coord;
		for(Bord b:borden){
			coord=b.getSubBordVakjes(counter);
			b.setVakje(coord[0], coord[2], Mark.S1);
			b.setVakje(coord[0], coord[3], Mark.S2);
			b.setVakje((coord[1]+coord[0])/2, (coord[3]+coord[2])/2, Mark.S3);
			b.setVakje(coord[1], coord[2], Mark.S3);
			b.setVakje(coord[1], coord[3], Mark.S4);
			
			
			Mark[] c1=b.copyKolom(b.copySubBord(counter), coord[2]+0, Bord.SUBDIM);
			assertTrue(c1[0]==Mark.S1);
			assertTrue(c1[1]==Mark.LEEG);
			assertTrue(c1[2]==Mark.S2);
			Mark[] c2=b.copyKolom(b.copySubBord(counter), (coord[2]+1), Bord.SUBDIM);
			assertTrue(c2[0]==Mark.LEEG);
			assertTrue(c2[1]==Mark.S3);
			assertTrue(c2[2]==Mark.LEEG);
			Mark[] c3=b.copyKolom(b.copySubBord(counter), (coord[2]+2), Bord.SUBDIM);
			assertTrue(c3[0]==Mark.S3);
			assertTrue(c3[1]==Mark.LEEG);
			assertTrue(c3[2]==Mark.S4);
			
		}
	
		
		
	}
	
	
	@Test
	public void testDraaiBordKlokMee(){
		int counter=0;
		int[] coord;
		for(Bord b:borden){
			coord=b.getSubBordVakjes(counter);
			b.setVakje(coord[0], coord[2], Mark.S1);
			b.setVakje(coord[0], coord[3], Mark.S2);
			b.setVakje((coord[1]+coord[0])/2, (coord[3]+coord[2])/2, Mark.S3);
			b.setVakje(coord[1], coord[2], Mark.S3);
			b.setVakje(coord[1], coord[3], Mark.S4);
			b.draaiBord(0, '+');			
			assertTrue(b.getVakje(coord[1],coord[2])==Mark.S1);
			assertTrue(b.getVakje(coord[0],coord[2])==Mark.S2);
			assertTrue(b.getVakje((coord[1]+coord[0])/2, (coord[3]+coord[2])/2)==Mark.S3);
			assertTrue(b.getVakje(coord[1],coord[3])==Mark.S3);
			assertTrue(b.getVakje(coord[0],coord[3])==Mark.S4);
			
		}
	}
	
	@Test
	public void testDraaiBordKloktegen(){
		int counter=0;
		int[] coord;
		for(Bord b:borden){
			coord=b.getSubBordVakjes(counter);
			b.setVakje(coord[0], coord[2], Mark.S1);
			b.setVakje(coord[0], coord[3], Mark.S2);
			b.setVakje((coord[1]+coord[0])/2, (coord[3]+coord[2])/2, Mark.S3);
			b.setVakje(coord[1], coord[2], Mark.S3);
			b.setVakje(coord[1], coord[3], Mark.S4);
			b.draaiBord(counter, '-');			
			assertTrue(b.getVakje(coord[0],coord[3])==Mark.S1);
			assertTrue(b.getVakje(coord[1],coord[3])==Mark.S2);
			assertTrue(b.getVakje((coord[1]+coord[0])/2, (coord[3]+coord[2])/2)==Mark.S3);
			assertTrue(b.getVakje(coord[0],coord[2])==Mark.S3);
			assertTrue(b.getVakje(coord[1],coord[2])==Mark.S4);
			
		}
	}
	
	@Test
	public void testHeeftRij(){
		int y=0;
		int i=0;
		for(Bord b:borden){
		
		for(int x=0+i;x<5+i;x++){
			b.setVakje(x, y, Mark.S1);
		}
		assertTrue(b.heeftRij(Mark.S1));
		if(y%2==0){
			i++;
		}
	y++;
		}
	}
	
	@Test
	public void testHeeftKolom(){
		int x=0;
		int i=0;
		for(Bord b:borden){
		
		for(int y=0+i;y<5+i;y++){
			b.setVakje(x, y, Mark.S1);
		}
		assertTrue(b.heeftKolom(Mark.S1));
		if(x%2==0){
			i++;
		}
	x++;
		}
	}
	
	@Test
	public void testHeeftDiagonaal(){
		int beginX=Bord.WINNUM-1;
		for(Bord b:borden){
			int counter=0;
			int x=((beginX<Bord.DIM)?(beginX-(beginX-(Bord.WINNUM-1))):(Bord.DIM-1)-(beginX-(Bord.DIM-1)));
			int y=((beginX<Bord.DIM)?0+(beginX-(Bord.WINNUM-1)):beginX-(Bord.DIM-1));
			while(b.isGeldigHokje(x,y)&&counter<=5){
				b.setVakje(x, y, Mark.S1);
				counter++;
				x--;
				y++;
			}

			
			
			assertTrue(b.heeftDiagonaal(Mark.S1));
			beginX++;
			}
		
		beginX=Bord.DIM-Bord.WINNUM;
		for(Bord b:borden){
			int counter=0;	
			int x=((beginX<Bord.DIM)?(beginX-(beginX-(Bord.WINNUM-1))):(Bord.DIM-1)-(beginX-(Bord.DIM-1)));
			
			int y=((beginX<Bord.DIM)?Bord.DIM-(beginX-(Bord.WINNUM-1)):Bord.DIM-1-(beginX-Bord.DIM-1));
			while(b.isGeldigHokje(x,y)&&counter<=5){
				b.setVakje(x, y, Mark.S1);
				counter++;
				x--;
				y--;
			}

			
			
			assertTrue(b.heeftDiagonaal(Mark.S1));
			beginX++;
			}
	}
	
}
