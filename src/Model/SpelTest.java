package Model;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SpelTest {
	private Spel s1, s2, s3;
	private String[] n1, n2, n3;
	public static void main(String args[]) {
	      org.junit.runner.JUnitCore.main("Model.SpelTest");
	    }
	
	@Before
	public void setup() throws Exception{
		n1=new String[]{"a","b"};
		n2=new String[]{"a","b","c"};
		n3=new String[]{"a","b","c","d"};
		
		
		s1=new Spel(n1,2);
		s2=new Spel(n2,3);
		s3=new Spel(n3,4);
	}
	
	
	
	
	@Test
	public void testNextPlayer(){
	
			assertTrue(s1.getHuidig().equals(n1[0]));
			assertTrue(s2.getHuidig().equals(n2[0]));
			assertTrue(s3.getHuidig().equals(n3[0]));
			s1.nextPlayer();
			s2.nextPlayer();
			s3.nextPlayer();
			assertTrue(s1.getHuidig().equals(n1[1]));
			assertTrue(s2.getHuidig().equals(n2[1]));
			assertTrue(s3.getHuidig().equals(n3[1]));
			s1.nextPlayer();
			s2.nextPlayer();
			s3.nextPlayer();
			assertTrue(s1.getHuidig().equals(n1[0]));
			assertTrue(s2.getHuidig().equals(n2[2]));
			assertTrue(s3.getHuidig().equals(n3[2]));
			s1.nextPlayer();
			s2.nextPlayer();
			s3.nextPlayer();
			assertTrue(s1.getHuidig().equals(n1[1]));
			assertTrue(s2.getHuidig().equals(n2[0]));
			assertTrue(s3.getHuidig().equals(n3[3]));
			s1.nextPlayer();
			s2.nextPlayer();
			s3.nextPlayer();
			assertTrue(s1.getHuidig().equals(n1[0]));
			assertTrue(s2.getHuidig().equals(n2[1]));
			assertTrue(s3.getHuidig().equals(n3[0]));
	}
}
