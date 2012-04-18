package Controller;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import net.ClientProtocol;

import Model.Spel;

public class GameRoom implements Observer {
	private int maxPlayers;
	private ArrayList<ClientHandler> handlers = new ArrayList<ClientHandler>();;
	private Spel spel;
	private Server server;
	private static final char DELIM = ClientProtocol.DELIM;

	public GameRoom(int players, Server ser) {
		maxPlayers = players;
		server = ser;
	}

	// Methoden die te maken hebben met het spel

    /**
     * Voert de volgende actie van het spel uit.
     * Als er nog geen winnaar is wordt 'nextMove' aangeroepen, anders 'gameOver'.
     */
	
	public void playGame() {
		if (!spel.gameOver()) {

			nextMove();

		} else {
			gameOver(spel.getWinnaars());
			closeGameRoom();
		}

	}
	/**
     * Kijkt of het doorgegeven vakje leeg is.
     * @param x x-coördinaat van het vakje
     * @param y y-coördinaat van het vakje
     * @return true als het vakjes leeg is, false als het vakje niet leeg is
     */
	public boolean isLeegVakje(int x, int y) {
		return spel.isLeegVakje(x, y);
	}
	/**
     * Stuurt via sendCommand van ClientHandler naar alle clients dat de volgende speler aan de beurt is
     */
	public void nextMove() {
		for (ClientHandler client : handlers) {
			client.sendCommand("nextMove" + DELIM + spel.getHuidig() + '\n');

		}
	}
	/**
     * Voert een zet uit en stuurt deze naar alle clients in het spel.
     * @require x >= 0
     * @require y >= 0
     * @require field >= 0
     * @require direction == '+' || direction == '-'
     * @param x x-coördinaat van de zet
     * @param y y-coördinaat van de zet
     * @param field nummer van het subbord dat gedraait moet worden
     * @param direction de richting waarin het subbord gedraait moet worden
     * @ensure de zet wordt doorgegeven aan alle clients die aan het spel deelnemen
     */
	public void doMove(int x, int y, int field, char direction) {
		synchronized (spel) {
			for (ClientHandler handler : handlers) {

				handler.sendCommand("move" + DELIM + x + DELIM + y + DELIM
						+ field + DELIM + direction + '\n');
			}
			spel.doeZet(x, y, field, direction);

		}
	}
	/**
	 * Geeft de naam van de speler die op dat moment aan de beurt is
	 * @return naam van de speler die aan de beurt is
	 */
	public String getHuidig() {
		return spel.getHuidig();
	}
	 /**
     * Laat alle clients weten dat het spel voorbij is en wie de (eventuele) winnaars zijn.
     * @param winners Een String Array met de namen van de winnaars
     */
	public void gameOver(String[] winners) {
		String command = "gameOver";
		for (String s : winners) {
			if (s.length() > 0)
				command = command + DELIM + s;
		}
		command = command + '\n';
		for (ClientHandler handler : handlers) {
			handler.sendCommand(command);
		}
	}
	/**
	 * Als er iets in het Spel object is veranderd, wordt playGame() aangeroepen
	 * @param arg0 het object waarin een verandering heeft plaatsgevonden (observable)
	 * @param arg1 een object wat als eventuele extra informatie wordt meegegeven
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof Spel) {

			playGame();
		}

	}

	// Methoden die te maken hebben met het regelen van de communicatie en
	// organisatie
/**
 * Stuurt een bericht naar alle clients in deze gameroom.
 * @param message
 */
	public void broadcast(String message) {
		synchronized(handlers){
		server.addMessage(message + '\n');
		for (ClientHandler handler : handlers) {
			handler.sendMessage(message);
		}
		}
	}

    /**
     * Kijkt of er genoeg handlers verbonden zijn om een nieuw spel te starten.
     * Zo ja, dan start de server een nieuw spel en stuurt naar alle clients dat er een nieuw spel is begonnen
     * Zo nee, dan stuurt de server het commando 'wait' naar de client.
     * @param name naam van de speler die het spel verzoekt
     */
	public void requestGame(String name) {
		//als de gameroom vol is.
		if (handlers.size() == maxPlayers) {
			//Het commando startGame word gemaakt met alle spelernamen erachter.
			String commandline = "startGame";
			String[] namen = new String[maxPlayers];
			for (int i = 0; i < maxPlayers; i++) {
				commandline = commandline + DELIM + handlers.get(i).getNaam();
				namen[i] = handlers.get(i).getNaam();
			}
			commandline = commandline + '\n';
			for (int i = 0; i < maxPlayers; i++)
				handlers.get(i).sendCommand(commandline);
			//Er word een spel gemaakt.
			spel = new Spel(namen, maxPlayers);
			spel.addObserver(this);
			playGame();
			//als de gameroom nog niet vol is word het wait commando gestuurd.
		} else {
			server.getHandler(name).sendCommand("wait" + '\n');
		}
	}

	 /**
     * Verwijderd een ClientHandler uit de lijst van handlers.
     * @param han de ClientHandler die verwijderd moet worden.
     * @require han != null
     * @ensure !handler.contains(han)
     */
	public void removeHandler(ClientHandler han) {
		synchronized(handlers){
		handlers.remove(han);
		}
	}

	/**
	 * Voegt een ClientHandler aan de collectie van ClientHandlers toe.
	 * 
	 * @param handler
	 *            ClientHandler die wordt toegevoegd
	 */
	public void addHandler(ClientHandler handler) {
		synchronized(handlers){
		handlers.add(handler);
		}
	}
	/**
	 * Kijkt of huidige naam van de client al in gebruik is.
	 * @param client
	 * @return
	 */
	public boolean nameAvailable(ClientHandler client) {
		boolean returnable = true;
		for (ClientHandler handler : handlers) {
			if (!(client == handler)) {
				if (client.getNaam().equals(handler.getNaam()))
					returnable = false;
			}
		}
		return returnable;
	}
/**
 * geeft terug of deze gameRoom nog ruimte heeft voor meer spelers.
 * @return
 */
	public boolean hasRoom(){
		return maxPlayers>handlers.size();
	}
	/**
	 * Kijkt of deze gameroom maxplayers als het maximale aantal spelers heeft
	 * @param maxplayers
	 * @return
	 */
	public boolean isCorrectGameRoom(int maxplayers) {
		return maxPlayers == maxplayers;
	}
	/**
	 * Sluit deze gameroom af.
	 */
	public void closeGameRoom(){
		String[] end=new String[0];
		gameOver(end);
		
		for(ClientHandler cl:handlers){
			server.addIdleClient(cl);
		}
		handlers.clear();
		server.removeGameRoom(this);
	
	}
}
