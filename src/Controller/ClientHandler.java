package Controller;


import java.io.*;

import net.ClientHandlerProtocol;
import net.Error;
import java.net.*;
import java.util.*;

/**
 * P2 eindopdracht.
 * ClientHandler. Een klasse voor het onderhouden van een 
 * socketverbinding tussen een Client en een Server.
 * @author  Jeroen Monteban & Floris Smit
 */
public class ClientHandler extends Thread {
	
	private Server server;
	private GameRoom gameRoom;
	private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private static final char DELIM =ClientHandlerProtocol.DELIM;
    private String clientName;
    private Scanner scanner;
	private boolean isActive=true;
    
    /** 
     * Construeert een ClientHandler object en start een socketconnectie op. 
     * @param server de server waaraan de handler verbonden is
     * @param socket de socket waarmee een connectie gemaakt wordt
     * @throws IOException gooit een IOException als er problemen zijn met de socketconnectie
     */  
    public ClientHandler(Server server, Socket socket) throws IOException {
    	this.server = server;
    	this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }
    /**
     * Leest invoer van de BufferedReader.
     */
    public void run() {
    	while(isActive){
    	try {
    		String commandline = in.readLine();
    		while (commandline != null) {
    			readCommand(commandline);
    			commandline = in.readLine();
    		}

    	}
    	catch(IOException e) {
    		if(isActive)
    		shutdown();
    	}
    	}
    }
    /**
     * Stuurt een commando naar de Client over de socket
     * @param commandline de String met commando en eventuele parameters die gestuurd moet worden
     */
    public void sendCommand(String commandline) {
    	server.addMessage("to: "+clientName+" "+commandline);
    	try {
    		out.write(commandline);
    		out.flush();
    	}
    	catch (IOException e) {
    		
    	}
    }
    /**
     * Leest de invoer van de methode run().
     * Gebruikt deze om vervolgens een andere methode aan te roepen om het gegeven commando uit te voeren
     * Gebruikt hiervoor een Scanner object. Eventuele parameters die met het commando zijn meegestuurd staan nog in de scanner.
     * @require commandline != null
     * @ensure Commando behorend bij de commandline wordt uitgevoerd
     * @param commandline Invoer gelezen door methode run() met een commando en eventuele parameters.
     */
    
    public void readCommand(String commandline) {
    	server.addMessage("from:"+clientName+": "+commandline+'\n');
    	scanner = new Scanner(commandline);
    	scanner.useDelimiter(Character.toString(DELIM));
    	String command = scanner.next();
    	
    	
    	if (command.equals("join")) {
    		executeJoin();
    	}
    	else if(command.equals("request")) {
    		executeRequest();
    	}
    	else if(command.equals("doMove")) {
    		executedoMove();
    	}    	
    	else if(command.equals("error")) {
    		
    	}
    	else if(command.equals("chat")) {
    		executeChat();
    	}
    	else if(command.equals("players")) {
    		sendError(Error.CHALLENGE_NOT_SUPPORTED.ordinal());
    	}
    	else if(command.equals("challenge")) {
    		sendError(Error.CHALLENGE_NOT_SUPPORTED.ordinal());
    	}
    	else if(command.equals("challengeAccept")) {
    		sendError(Error.CHALLENGE_NOT_SUPPORTED.ordinal());
    	}
    	else if(command.equals("challengeReject")) {
    		sendError(Error.CHALLENGE_NOT_SUPPORTED.ordinal());
    	}
    	else if(command.equals("challengeCancel")) {
    		sendError(Error.CHALLENGE_NOT_SUPPORTED.ordinal());
    	}
    	else {
    		
    			sendError(Error.INVALID_COMMAND.value());
    		
    	
    	}
    	
    	
    }
    /**
     * Meldt de Client aan bij de handler. Laat de server aan alle clients doorgeven dat de client geconnect is.
     */
    public void executeJoin() {
    	if (scanner.hasNext()) {
    		String clientName = scanner.next();
    		this.clientName = clientName;
    		//kijk of de clientname nog vrij is.
    		if(server.nameAvailable(this)){
    			sendAccept();
    	}
   		else {
   			sendError(Error.NAME_TAKEN.ordinal());
   		}
    	}
    	else
    	sendError(Error.INVALID_NAME.ordinal());
    	}
    
    /**
     * Meldt de client via sendCommand dat hij succesvol aangemeldt is.
     */
    public void sendAccept() {
		sendCommand("accept"+'\n');
		server.localBroadcast(clientName+DELIM+" has connected.");
	}

    /**
     * Verzoekt de server om een nieuw spel
     * @require scanner.hasNextInt()
     * @ensure de server start een nieuw spel of stuur het command 'wait'
     */
	public void executeRequest() {
    	int players = scanner.nextInt();
    	gameRoom=server.requestGame(clientName, players);
    }
	/**
	 * Stuurt aan de server de zet door die de client wil doen.
	 */
    public void executedoMove() {
    	//kijk of deze client aan de beurt is
    	if(gameRoom.getHuidig().equals(clientName)){
    		//controleert de eerste parameter
    	if (scanner.hasNextInt()) {
    		int x = scanner.nextInt();
    		//controleert de tweede parameter
    		if (scanner.hasNextInt()) {
    			int y = scanner.nextInt();
    			//controleert de derde parameter
    			if (scanner.hasNextInt()) {
    				int field = scanner.nextInt();
    				//controleert de vierde parameter
    				if (scanner.hasNext()) {
    					char direction = scanner.next().charAt(0);
    					if(direction=='+'||direction=='-'){
    						//als het vakje leeg is, doe dan de zet.
    						if(gameRoom.isLeegVakje(x, y))    							
    							gameRoom.doMove(x, y, field, direction);
    						else{
    							sendError(Error.INVALID_MOVE.ordinal());
    						
    						}
    					}
    					else
    						sendError(Error.INVALID_PARAMETER.ordinal());
    					
    						
    				}else 
    					sendError(Error.INVALID_PARAMETER.ordinal());
    					
    				
    			}else 
    				sendError(Error.INVALID_PARAMETER.ordinal());
    				
    		}else 
    			sendError(Error.INVALID_PARAMETER.ordinal());
    			
    	}else		
    		sendError(Error.INVALID_PARAMETER.ordinal());
    		
    	}else
    		sendError(Error.NOT_YOUR_TURN.ordinal());
    	}
		
    
    /**
     * Stuurt een chatbericht door aan de server
     * @require scanner.hasNext()
     * @ensure chatbericht wordt naar alle clients gestuurd
     */
    public void executeChat() { 	
    String	message =scanner.next();
    if(gameRoom!=null)
    gameRoom.broadcast(clientName + DELIM + message + '\n');
    else{

    server.localBroadcast(clientName + DELIM + message + '\n');
    }
    }
   
    /**
     * Stuurt een chatbericht naar de client
     * @param message het chatbericht
     */
    
    public void sendMessage(String message) {
    	try {
    		out.write("chat"+DELIM+message+'\n');
    		out.flush();
    	}
    	catch (IOException e) {
    		shutdown();
    	}
    }
    /**
     * Stuurt via sendCommand een error naar de client
     * @param code de errorcode
     */
    public void sendError(int code){
    
			String error="error" + DELIM + code+DELIM+Error.values()[code].toString() +  '\n';
			sendCommand(error);
    }

    

    /**
     * Geeft de naam van de client die bij deze handler hoort
     * @return naam van de client van deze handler
     */
    public String getNaam() {
    	return clientName;
    }
    /**
     * Sluit de socketconnectie af en geeft hier een melding van.
     * Meld de handler af bij op de juiste plekken.
     */
    public void shutdown() {
    	isActive=false;
		server.removeHandler(this);
		if(gameRoom==null){
			server.removeIdleClient(this);
			server.localBroadcast(clientName+DELIM+" has disconnected");
		}else{
			gameRoom.removeHandler(this);	
			gameRoom.broadcast(clientName+DELIM+" has disconnected");
			gameRoom.closeGameRoom();
		}

		try{
			socket.close();
		}catch(IOException e){
			
		}
		
    }
    /**
     * Stelt de gameRoom van deze clienthandler in op null.
     */
    public void removeGameRoom(){
    	gameRoom=null;
    }
    
}