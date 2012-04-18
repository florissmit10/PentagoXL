package Model;
/* Generated by Together */

public class Mark {
	
    public static final Mark LEEG = new Mark(' ');
    public static final Mark S1 = new Mark('1');
    public static final Mark S2 = new Mark('2');
    public static final Mark S3 = new Mark('3');
    public static final Mark S4 = new Mark('4');
    
    private char value;
    
    /** Construeert een Mark object. */
    private Mark(char value) {
    	this.value = value;
    }
    /**
     * geeft de mark terug corresponderend met een bepaalde waarde.
     * @param mark
     * @return
     */
    public static Mark getMark(int mark){
    	Mark returnable=LEEG;
    	if(mark==1){
    		returnable=S1;
    	}
    	else if(mark==2){
    		returnable=S2;
    	}else if(mark==3){
    		returnable=S3;
    	}else if(mark==4){
    		returnable=S4;
    	}
    	return returnable;
    }
    
    public static int getMarkNumber(Mark m){
    	
    	
    	
    	return m.value;
    }
    
    public char getValue(){
    	return value;
    }
    /**
     * geeft de mark die aan de beurt is na de mark waarop de methode word aangeroepen
     * @return
     */
 
    public Mark next(int players){
    	Mark returnable=null;
    	
    	if(players == 4) {
    	 	if(value=='1'){
    	 		returnable=S2;
    	 	}
    	 	else if(value=='2'){
    	 		returnable=S3;
    	 	}
    	 	else if(value=='3'){
    	 		returnable=S4;
    	 	}else if(value=='4'){
    	 		returnable=S1;
    	 	}
    	}
    	else if(players == 3) {
    		if(value=='1'){
    			returnable=S2;
    		}
    		else if(value=='2'){
    			returnable=S3;
    		}
    		else if(value=='3'){
    			returnable=S1;
    		}
    	}
    	else if(players == 2) {
    		if(value=='1') {
    			returnable=S2;
    		}
    		else if(value=='2') {
    			returnable=S1;
    		}
    	}
    	return returnable;
    }
}