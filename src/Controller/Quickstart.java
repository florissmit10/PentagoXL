package Controller;


import View.*;

public class Quickstart {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int players=0;
		try{
			players=Integer.parseInt(args[0]);
			if(players<2||players>4)
				throw new NumberFormatException();
		}catch(NumberFormatException e){
			System.out.println(args[0]+" is geen getal tussen de 2 en 4");
			System.exit(0);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("voeg bij het starten een getal tussen de 2 en vier toe als argument");
			System.exit(0);
		}
		ServerGUI gui=new ServerGUI();
		gui.startListening();
			for(int counter=1;counter<=players;counter++){
			Client c=new Client();
			c.connect();
			}


	}

}
