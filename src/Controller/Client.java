package Controller;



import java.io.*;


import net.ClientProtocol;
import net.Error;

import java.util.*;
import java.net.*;
import Model.*;
import View.*;

/**
 * P2 eindopdracht.
 * Client. De client houdt contact met de server om 
 * in de lobby contact te kunnen zoeken met andere
 * spelers en de spelvorderingen te kunnen communiceren
 * tussen de client en de server.
 * @author  Jeroen Monteban & Floris Smit
 */

public class Client extends Thread {
	
	private String name;
    private Speler speler;
    private Spel spel;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private static final char DELIM = ClientProtocol.DELIM;
    private boolean accepted=false;
    private ClientGUI gui;
    private Scanner scanner;
	private String strategie;
	private int denkTijd;
	private int opponents;
	private int marknr;
    
	/**
	Contrueert een Client object.
	Maakt een nieuw Spel en een nieuwe GUI aan en slaat deze op in instantievariabelen.
	*/
    public Client() {
    	this.spel = new Spel();
    	gui = new ClientGUI(this);
	}
    

    
    /**
	Leest invoer van de BufferedReader.
	*/
    public void run() {
    	while(true) {
    		try {
    			if(in!=null){
    			String commandline = in.readLine();
    			while (commandline != null) {
    				readCommand(commandline);
    				commandline = in.readLine();
    			}
    			
    		}
    		}
    		catch(IOException e) {
    			shutdown();
    		}
    	}
    	
    }
    
    /**
   	Start een socketconnectie op.
   	@require port >= 0
   	@require name != null
   	@require strat != null
   	@require players >= 0
   	@ensure Een socketverbinding via de gegeven hostname en port.
   	@param hostname Naam van de host
   	@param port Port waarop de server host
   	@param name Naam van de client

   	*/	
    
    
    public void connect(String hostname, int port, String name) {
		this.name = name;
    	if(socket==null){

    		InetAddress host = null;
    		try {
    			host = InetAddress.getByName(hostname);
   			}
   			catch (UnknownHostException e)  {
   				gui.setMessage("Onbekende host");
   			}
    		
    		if (host != null) {
    			try {
    	    		socket = new Socket(host, port);
    	    		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    	    		this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

    	    		if(!this.isAlive()){
  
        	    		start();
        	    		
    	    		}
    	    		sendJoin();
    			}
    			catch (IOException e) {
    				socket=null;
    				gui.setMessage("Er kon geen verbinding gemaakt worden");
    				gui.setConnected(false);
    			}
    		}
    		else {
    			gui.setMessage("Hostname invalid");
    			gui.setConnected(false);
    		}
    		}
    	else if(socket!=null&&!accepted){
    			sendJoin();
    		}
    		}
    
/**
 * laat de client verbinden met de standaardinstellingen op de GUI
 * Testmethode.
 */
	public void connect(){
		gui.setMessage("Verbinding gemaakt");
    	gui.connect();
    }
	
    
	
    /**
	Geeft het huidige spel van client.
	@return Het Spel-object van deze client
	*/
    
    public Spel getSpel() {
    	return spel;
    }
    

    
    //-----------------------------Input---------------------------------------------------------------------
    /**
	Leest de invoer van de methode run().
	Gebruikt deze om vervolgens een andere methode aan te roepen om het gegeven commando uit te voeren
	Gebruikt hiervoor een Scanner object. Eventuele parameters die met het commando zijn meegestuurd staan nog in de scanner.
	@require commandline != null
	@ensure Commando behorend bij de commandline wordt uitgevoerd
	@param commandline Invoer gelezen door methode run() met een commando en eventuele parameters.
	*/	
    public void readCommand(String commandline) {

    	scanner = new Scanner(commandline);
    	scanner.useDelimiter(Character.toString(DELIM));
    	if(scanner.hasNext()){
    	String command = scanner.next();
    	
    	
    	if (command.equals("accept")) {
    		executeAccept();
    	}
    	else if(command.equals("wait")) {
    		executeWait();
    	}
    	else if(command.equals("startGame")) {
    		executeStartGame();
    	}    	
    	else if(command.equals("move")) {
    		executeMove();
    	}
    	else if(command.equals("nextMove")) {
    		executeNextMove();
    	}
    	else if(command.equals("gameOver")) {
    		executeGameOver();
    	}
    	else if(command.equals("error")) {
    		executeError();
    	}
    	else if(command.equals("chat")) {
    		executeChat();
    	}
    	else if(command.equals("players")) {
    		sendError(Error.CHALLENGE_NOT_SUPPORTED.ordinal());
    	}
    	else if(command.equals("challengedBy")) {
    		sendError(Error.CHALLENGE_NOT_SUPPORTED.ordinal());
    	}
    	else if(command.equals("challengeCanceled")) {
    		sendError(Error.CHALLENGE_NOT_SUPPORTED.ordinal());
    		
    	}
    	else if(command.equals("challengeAccepted")) {
    		sendError(Error.CHALLENGE_NOT_SUPPORTED.ordinal());
    		
    	}
    	else if(command.equals("challengeRejected")) {
    		sendError(Error.CHALLENGE_NOT_SUPPORTED.ordinal());
    		
    	}
    	else {
    		gui.setMessage(Error.INVALID_COMMAND.toString());
    	}
    	}
    	    	
    }
    
    /**
	Geeft op de GUI weer dat de client is aangemeld bij de server
	Stuurt vervolgens een request voor een spel met het aantal spelers dat op de GUI is ingesteld.
	@ensure Request voor nieuw spel wordt gestuurd.
	*/	
    
    public void executeAccept() {
    	gui.setMessage("Verbinding gemaakt");
  		gui.setConnected(true);
    	accepted=true;
    	gui.setPlayEnabled(true);

		
	}

    /**
	Stuurt een error naar de server over de socket.
	@require code >= 0
	@param code Codenummer van de error
	*/	
	public void sendError(int code){
        
			String error="error" + DELIM + code+DELIM+Error.values()[code].toString() +  '\n';
			sendCommand(error);
    }
    

    /**
	Geeft op de GUI weer dat er onvoldoende tegenstanders beschikbaar zijn.
	*/
    public void executeWait() {

    	gui.setPlayEnabled(false);
    	gui.setMessage("Er zijn op dit moment onvoldoende tegenstanders beschikbaar.");
    }
    /**
	Voert de doorgegeven zet uit op het Spel object van de Client.
	@require scanner.hasNextInt() && scanner.hasNextInt() && scanner.hasNextInt() && scanner.hasNext()
	@ensure Spel voert de doorgegeven zet uit.
	*/
    public void executeMove() {
    	int x = scanner.nextInt();
    	int y = scanner.nextInt();
    	int field = scanner.nextInt();
    	char direction = scanner.next().charAt(0);
    	spel.doeZet(x, y, field, direction);
    }
    /**
   	Start een nieuw spel op.
   	@require scanner.hasNext()
   	@ensure start een nieuw spel met de namen die zijn meegestuurd
   	*/	   
       public void executeStartGame() {
    	//leest alle namen van de scanner.
       	int i = 0;
       	String[] names = new String[opponents];
       	while (scanner.hasNext()) {
       		String name = scanner.next();
       		names[i] = name;
       		i++;
       		
       	}
       	Speler[] spelers=new Speler[opponents];
       	int counter=0;
       	//maak voor alle spelers een mensSpeler aan. Behalve als het over deze client gaat.
       	for(String s:names){
       		if(!s.equals(name)) {
       		spelers[counter]=new MensSpeler(s, counter+1);
       		this.marknr = counter+1;
       		}
       		//als het over deze client gaat hangt het van de instellingen van de GUI af wat voor
       		//speler er gemaakt word.
   	    		else{
   	    			if(strategie.equals("DommeStrategie")){
   	    				spelers[counter]=new ComputerSpeler(s, counter+1, new DommeStrategie() );
   	    				speler=spelers[counter];
   	    			}
   	    			else if(strategie.equals("MensSpeler")){
   	    				spelers[counter]=new MensSpeler(s, counter+1);
   	    				speler=spelers[counter];
   	    			}
   	    			else if(strategie.equals("SlimmeStrategie")) {
   	    				spelers[counter]=new ComputerSpeler(s, counter+1, new SlimmeStrategie(counter+1, opponents, denkTijd));
   	    				speler=spelers[counter];
   	    			}
   	    			
   	    		}   		
       		counter++;
       		
       	}
       	//Werk het spel en de GUI bij.
       	spel.addSpelers(spelers);
       	gui.setPlayEnabled(false);
        gui.resetBord();
       	gui.setBordEnabled(true);
       	gui.setMessage("Er is een spel begonnen!");
       }
    /**
	Leest uit de scanner wie er aan de beurt is.
	Als de client dit zelf is, vraagt hij aan zijn strategie om een zet.
	Als de client dit niet is, zet hij op de GUI wie er aan de beurt is.
	@require scanner.hasNext()
	@ensure client weet wie er aan de beurt is
	*/	
    
    public void executeNextMove() {
    	String playeronturn = scanner.next();
    	spel.setHuidig(playeronturn);
    	//als de huidige speler deze client is.
    	if (playeronturn.equals( name)) {
    		//als de client een computerspeler is moet de AI een zet doen
    		if(speler instanceof ComputerSpeler){
    			int[] zet=spel.getZet(speler);
    			char dir=(zet[3]>0?'+':'-');
    			doMove(zet[0],zet[1],zet[2],dir);
    		}
    		else
    		gui.setHintEnabled(true);
    		gui.setMessage("Je bent aan de beurt! Druk op Hint voor een tip!");
    	}
    	else {
    		gui.setHintEnabled(false);
    		gui.setMessage(playeronturn + " is aan de beurt.");
    	}
    }    
    /**
   	Leest de errorcode en zet deze om naar een beschrijving. Print deze op de GUI.
   	@require scanner.hasNextInt() && scanner.hasNext()
   	@ensure Print erroromschrijving op GUI.
   	*/
    public void executeError() {
    	int errorcode = scanner.nextInt();
    	String description = scanner.next();
    	
    	if(errorcode==0||errorcode==3){
    		accepted=false;
    		description="Kies een andere naam, druk  op connect om verder te gaan.";
    	}
    	gui.setMessage(description);
    }
    /**
     * Methode die chatberichten naar de GUI stuurt.
     */
    public void executeChat() {
    	String speaker = scanner.next();
    	String message = scanner.next();
    	gui.addChatMessage("> " + speaker + ": " + message + '\n');
    }
    
    
    
  //-----------------------------Output---------------------------------------------------------------------
   
    /**
   	Beëindigt het spel een geeft op de GUI weer of er een winnaar is (en zo ja, wie deze winnaar(s) is/zijn)	
   	*/	
    public void executeGameOver() {
//Maak een String[] met namen van winnaars.
    	int winnerCount=0;
    	String[] temp=new String[4];
    	while(scanner.hasNext()){
    		temp[winnerCount]=scanner.next();
    		winnerCount++;
    	}
    	String[] winners=new String[winnerCount];
    	for(int counter=0;counter<winnerCount;counter++){
    		winners[counter]=temp[counter];
    	}
    	//aan de hand van de uitslag worden er andere dingen op de GUI geprint.
    	switch(winnerCount){
    	case 0:{
    		gui.setMessage("Remise, er is geen winnaar.");
    		break;
    	}
    	case 1:{
    		if (winners[0].equals(name)) {
    			gui.setMessage("Gefeliciteerd, je hebt gewonnen!");
    		}
    		else {
    			gui.setMessage(winners[0] + " heeft gewonnen.");
    		}
    		break;
    	}
    	default:{
    		String message="";
    		for(String s:winners){
    			message=message+" "+s;
    		}
    		message=message+" hebben gewonnen.";
    		gui.setMessage(message);
    		break;
    	}
    	}
    	//werk de GUI bij.
    	gui.setBordEnabled(false);
    	gui.setPlayEnabled(true);
     }

    
    
  
    	/**
    	Stuurt een commando naar de Server over de socket.
    	@param commandline de String met commando en eventuele parameters die gestuurd moet worden
    	*/	   
        public void sendCommand(String commandline) {
        	try {
        		out.write(commandline);
        		out.flush();
        	}
        	catch (IOException e) {
        		exitSocket();
        	}
        }
    
    /**
	Geeft via sendCommand aan de server door dat er een zet gedaan moet worden.
	@require x >= 0
	@require y >= 0
	@require field >= 0
	@require direction == '+' || direction == '-'
	@param x x-coördinaat van de zet
	@param y y-coördinaat van de zet
	@param field nummer van het subbord dat gedraait moet worden
	@param direction richting waarin het subbord gedraait moet worden
	@ensure de zet wordt aan de server doorgegeven
	*/	
    public void doMove(int x, int y, int field, char direction) {
    	String move = "doMove" + DELIM + x + DELIM + y + DELIM + field + DELIM + direction + '\n';
    	sendCommand(move);
    	gui.setHintEnabled(false);
    }
    /**
	Meldt zich via sendCommand aan bij de server.
	*/	
    public void sendJoin() {
    	sendCommand("join" + DELIM + name + '\n');
    }
    /**
   	Geeft via sendCommand een chatbericht aan de server door.
   	@require message != null
   	@param message het bericht dat aan de server gestuurd wordt
   	@ensure het chatbericht wordt aan de server doorgegegeven
   	*/
    public void chat(String message) {
    	sendCommand("chat" + DELIM + message + '\n');
    }

   /**
    * Zorgt er voor dat alle GUI instellignen opgeslagen worden en roept de methode
    * SendRequest aan.
    * @param strat Naam van de strategie waarmee er gespeeld wordt
   	* @param players Aantal spelers waarmee de client wil spelen
    * @param denkTijd tijd in ms. zo lang mag de AI nadenken over een zet
    * @require strat != null
   	* @require players >= 0
    * 
    */
    public void request(String strat, int players, int denkTijd){
 	   	
    this.strategie=strat;
    this.denkTijd=denkTijd;
    this.opponents=players;
    sendRequest(players);
    }
	
    
    /**
	Verzoekt de server om een spel met een bepaald aantal spelers.
	@require opponents > 0
	@param opponents het aantal tegenstanders waartegen gespeeld wil worden.
	*/
    public void sendRequest(int opponents) {
    	gui.resetBord();
    	spel.resetBord();
    	sendCommand("request" + DELIM + opponents + '\n');
    }
    
    /**
     * stuurt een request met de instellingen van de GUI.
     * TestMethode.
     */
    	public void request(){
    		gui.request();
    	}
    	
    
    
    
    
    //-----------------------------Remaining---------------------------------------------------------------------

    /**
     * Berekend welke zet een DommeStrategie zou doen.
     * @return int[] met de zet
     */
     public int[] getHint() {
     	Strategie domme = new SlimmeStrategie(marknr, opponents, denkTijd);
     	return domme.bepaalZet(spel.getBord());    	
     }
     /**
 	Sluit de socketverbinding af en geeft een melding hiervan op de GUI.
 	*/	
     public void exitSocket() {
     	try {	
     		//Sluit de verbinding
     		socket.close();
     		socket=null;
     		in=null;
     		out=null;
     		//past de GUI aan 
     		gui.setBordEnabled(false);
     		gui.setHintEnabled(false);
     		gui.setPlayEnabled(false);
     		gui.setMessage("De connectie is verbroken");
     		gui.setConnected(false);
     		
     	}
     	catch(IOException e) {
     		//past de GUI aan 
     		gui.setConnected(false);
     		
     	}
     	}
     /**
      * probeert een laatste chatbericht te sturen.
      */
    public void shutdown() {
    	
    	try{
    	chat(" has left");
    	}catch(Exception e){}
 
    }
    /**
     * kijkt of de client aan de beur tis.
     * @return name.equals(spel.getHuidig());
     */
    public boolean ownerOnTurn(){
    	return name.equals(spel.getHuidig());
    }
	  /**
	   * geeft de naam van deze client.
	   * @return
	   */
    public String getClientName() {
    	return name;
    }
    /**
     * start een nieuwe client.
     * @param args
     */
    public static void main(String[] args) {
    	new Client();
    	
    }
}
