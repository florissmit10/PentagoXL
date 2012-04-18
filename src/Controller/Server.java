package Controller;

import java.io.*;
import java.util.*;
import java.net.*;
import View.ServerGUI;


/**
 * P2 eindopdracht.
 * Server. De server zoekt genoeg clients om een game te starten.
 * Als er genoeg spelers zijn gevonden begint server een spel en
 * houdt de clients op de hoogte van de vorderingen in het spel.
 * @author  Jeroen Monteban & Floris Smit
 */

public class Server extends Thread{
	

	private int port;
	private ArrayList<ClientHandler> idlePlayers = new ArrayList<ClientHandler>();;
	private ArrayList<ClientHandler> allPlayers = new ArrayList<ClientHandler>();;
	private ServerGUI gui;
    private ArrayList<GameRoom> gameRooms = new ArrayList<GameRoom>();;
    
   
    /** Construeert een Server object. */
    public Server(int port, ServerGUI gui) {
    	this.port = port;
    	this.gui=gui;
    }
    /**
     * Start een serversocket op en laat deze openstaan voor clients om verbinding te maken
     * Maakt een nieuwe clienthandler behorend bij de client zodra er een client verbinding maakt.
     */
    public void run() {
    	ServerSocket ssocket = null;
    	try {
    		ssocket = new ServerSocket(port);
    		while (true) {
    			Socket sock = ssocket.accept();
    			ClientHandler handler = new ClientHandler(this, sock);
    			addIdleClient(handler);
    			addHandler(handler);
    			handler.start();

    		}
    	}
    	catch (IOException e) {
    		
    	}
    }
    /**
     * Stuurt een chatbericht naar alle clienthandlers die niet met een spel bezig zijn.
     * @param message het chatbericht dat gestuurd wordt
     */
    public void localBroadcast(String message){
    	synchronized(idlePlayers){
    		addMessage("to idlePlayers: "+message+'\n');
          	for(ClientHandler cl:idlePlayers){
    		cl.sendMessage(message);
    	}
    	}
    }
    /**
     * Stuurt een chatbericht naar alle clienthandlers.
     * @param message het chatbericht dat gestuurd wordt
     */
    public void broadcast(String message) {
    	synchronized(allPlayers){
    	addMessage("to allPlayers: "+message+'\n');
    	for(ClientHandler cl:allPlayers){
    		cl.sendMessage(message);
    	}
    	}
    }
   
    
    /**
     * Voegt een ClientHandler aan de collectie van ClientHandlers toe.
     * @param handler ClientHandler die wordt toegevoegd
     */
    public void addHandler(ClientHandler handler) {
    	synchronized(allPlayers){
    	allPlayers.add(handler);
    	}
    }
    /**
     * Verwijderd een ClientHandler uit de lijst van handlers.
     * @param han de ClientHandler die verwijderd moet worden.
     * @require han != null
     * @ensure !handler.contains(han)
     */
    public void removeHandler(ClientHandler han){
    	synchronized(allPlayers){
    	allPlayers.remove(han);
    	}
    }
   
    

    /**
     * Zoekt aan de hand van de naam van de client de ClientHandler die bij een client hoort.
     * @param name de naam van de client verbonden aan de gezochten ClientHandler
     * @return de ClientHandler die bij de opgegeven name hoort
     */
    public ClientHandler getHandler(String name) {
		ClientHandler handler = null;
		for (ClientHandler h : allPlayers) {
			if (h.getNaam().equals(name))
				handler = h;
		}
		
		return handler;
	}

    /**
     * Controleert of de naam die een ClientHandler heeft beschikbaar is.
     * @param client de clienthandler waarvan de naam gecontroleert moet worden
     * @return true als de naam beschikbaar is, false als de naam al bezet is
     */
    public boolean nameAvailable(ClientHandler client){
    	boolean returnable=true;
    	for(ClientHandler handler:allPlayers){
    		if(!(client==handler)){
    			if(client.getNaam().equals(handler.getNaam()))
    				returnable=false;
    		}
    	}
    	return returnable;
    }
    
    public void removeGameRoom(GameRoom gr){
    	gameRooms.remove(gr);
    	}
    /**
     * Geeft een gameroom terug met de juiste aantal spelers. 
     * Als er nog geen is word er een nieuwe aangemaakt.
     * @param number aantal spelers dat de gameroom moet hebben
     * @return returnable!=null
     */
  	public GameRoom getGameRoom(int number){
  		GameRoom returnable=null;
  		//kijkt of er een gameroom is met een bepaald aantal spelers en of er ruimte is in deze gameroom.
  		for(GameRoom gr:gameRooms){
  			if(returnable==null&&gr.isCorrectGameRoom(number)&&gr.hasRoom())
  				returnable=gr;
  		}
  		//Maakt een nieuwe gameroom aan als er nog geen is.
  		if(returnable==null){
  			returnable=new GameRoom(number, this);
  			gameRooms.add(returnable);
  		}
  		return returnable;
  	}
    /**
     * voegt een client toe aan de lijst met clients die niet bezig zijn met een spel.
     * @param clientName naam van de client
     */
  	public void addIdleClient(ClientHandler client){
  		synchronized(idlePlayers){
  		idlePlayers.add(client);
  		}
  	}
  	/**
  	 * verwijderd een client van de lijst met clients die niet bezig zijn met een spel.
  	 * @param clientName naam van de client.
  	 */
  	public void removeIdleClient(ClientHandler client){
  		synchronized(idlePlayers){
  		idlePlayers.remove(client);
  		}
  	}
   /**
    * print een bericht op de server output.
    * @param s het bericht
    */
    public void addMessage(String s){gui.addMessage(s);}


    /**
	 * Wijst clientName een gameroom toe die overeenkomt met het aantal spelers en geeft de gameroom terug.
	 * roept de requestGame van gameroom aan en verwijderd de client uit de idleplayers lijst.
     * @param name naam van de speler die het spel verzoekt
     * @param players het aantal spelers waarmee de speler wil spelen
     * @return de gameroom waar de client in is gekomen.
     */
    
	public GameRoom requestGame(String clientName, int players) {
		GameRoom gr=getGameRoom(players);
		gr.addHandler(getHandler(clientName));
		gr.requestGame(clientName);
		removeIdleClient(getHandler(clientName));
		
		return gr;
		
	}
	

}
