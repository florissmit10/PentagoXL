package net;

/**
 * <p>
 * Het protocol waarmee de server (client handler) met de clients communiceert.
 * Bij het implementeren moet in ieder geval een invulling gegeven
 * worden aan de standaardcommando's. Als de handler hierdoor niet aan een verzoek
 * van een client kan voldoen, moet de betreffende foutmelding worden doorgegeven
 * met het <tt>error</tt>-commando.
 * </p><p>
 * Elk commando wordt over de socket gestuurd als een string met daarin het 
 * keyword, gevolgd door de argumenten en beëindigd met een newline character.
 * De javadocs bij de commando's leggen de syntax uit bij de <tt>syntax</tt>-tag,
 * en ook eventuele pre- en postcondities. Het
 * keyword en de argumenten worden van elkaar gescheiden door een gereserveerd
 * karakter, opgeslagen in <tt>DELIM</tt>. Bijvoorbeeld:
 * </p><p><tt>
 * "startGame" + DELIM + "Pietje" + DELIM + "Henk" + '\n'
 * </tt></p>
 * 
 * @author Michael Hannema
 */
public interface ClientHandlerProtocol {
	
	/**
	 * Scheidingsteken voor keywords en argumenten
	 */
	public static final char DELIM = ClientProtocol.DELIM;
	
	//standaardcommando's
	/**
	 * Geeft aan de client door dat hij nu in de lobby zit.
	 * @require Een client heeft het <tt>join</tt>-commando gebruikt.
	 * @syntax <code>accept</code>
	 */
	public static final String ACCEPT = "accept";
	
	/**
	 * Geeft aan de client door dat er niet genoeg andere spelers
	 * zijn voor een partij die hij heeft aangevraagd.
	 * @require Een client heeft het <tt>request</tt>-commando gebruikt.
	 * @syntax <code>wait</code>
	 */
	public static final String WAIT = "wait";
	
	/**
	 * Geeft aan de client de spelernamen door van de partij waar hij in zit.
	 * @param name Spelernaam
	 * @require Alle spelers waarvan de namen worden doorgegeven bevinden zich
	 * in de pre-game lobby en zijn dus niet met een partij bezig.
	 * @ensure Het <tt>nextMove</tt>-commando wordt hierna doorgegeven.
	 * @syntax <code>startGame &lt;String:name&gt;(2..4)</code>
	 */
	public static final String START_GAME = "startGame";
	
	/**
	 * Geeft aan de client door dat er een zet is gedaan en specificeert deze.
	 * @param x X-coördinaat van de knikkerpositie, beginnend aan de linkerkant met 0
	 * @param y Y-coördinaat van de knikkerpositie, beginnend aan de bovenkant met 0
	 * @param field Nummer van het (draaibare) deelveld, beginnend linksboven met 0
	 * en oplopend langs de leesrichting
	 * @param direction Draairichting, aangegeven met '+' (met de klok mee) of '-'
	 * (tegen de klok in)
	 * @require <tt>0 <= x,y < 9</tt>
	 * @require <tt>0 <= field < 9</tt>
	 * @require <tt>(direction == '-') || (direction == '+')</tt>
	 * @require Er is nog geen knikker geplaatst op de gewenste positie.
	 * @syntax <code>move &lt;Int:x&gt; &lt;Int:y&gt; &lt;Int:field&gt; &lt;Char:direction&gt;</code>
	 */
	public static final String MOVE = "move";
	
	/**
	 * Geeft aan de client door dat de volgende speler aan de beurt is
	 * waarbij de naam van die speler wordt vermeld.
	 * @param name Naam van de speler die aan de beurt is
	 * @require Er zijn geen spelers die de verbindig hebben verbroken.
	 * @require De speler die aan de beurt is heeft nog knikkers.
	 * @require Het <tt>move</tt>-commando is verzonden maar er heeft nog niemand gewonnen.
	 * @syntax <code>nextMove &lt;String:name&gt;</code>
	 */
	public static final String NEXT_MOVE = "nextMove";
	
	/**
	 * Geeft aan de client door dat het spel over is en wie de winnaars zijn.
	 * @param name Naam van een winnaar
	 * @syntax <code>gameOver &lt;String:name&gt;(0..4)</code>
	 */
	public static final String GAME_OVER = "gameOver";
	
	/**
	 * Geeft een fout door aan de client.
	 * @param code Een foutcode die de fout beschrijft. Gebruik de methode
	 * <tt>value()</tt> op een <tt>Error</tt>-instantie om de code te verkrijgen.
	 * @param description Een beschrijving van de fout. Voor een
	 * standaardbeschrijving kan <tt>toString()</tt> van een <tt>Error</tt>-instantie
	 * gebruikt worden. De server is vrij om te kiezen tussen de standaardbeschrijving
	 * of de meegegeven beschrijving.
	 * @require <tt>0 <= code < Error.values().length</tt>
	 * @require <tt>description != ""</tt>
	 * @require <tt>for 0 <= i < description.length():<br>
	 * &nbsp;&nbsp;name.charAt(i) != DELIM</tt>
	 * @syntax <code>error &lt;Int:code&gt; &lt;String:description&gt;</code>
	 */
	public static final String ERROR = "error";
	
	//chatcommando
	/**
	 * Geeft een chatbericht door aan de client, met de naam van de afzender.
	 * @param name Naam van de afzender
	 * @param message Bericht
	 * @syntax <code>chat &lt;String:name&gt; &lt;String:message&gt;</code>
	 */
	public static final String CHAT = "chat";
	
	//challenge-commando's
	/**
	 * Geeft een lijst van namen van alle andere spelers die niet met een partij
	 * bezig zijn.
	 * @param name Spelernaam
	 * @syntax <code>players &lt;String:name&gt;*</code>
	 */
	public static final String PLAYERS = "players";
	
	/**
	 * Geeft aan de client door dat hij uitgenodigd is door de gegeven speler.
	 * @param name Naam van de uitnodiger
	 * @syntax <code>challengedBy &lt;String:name&gt;</code>
	 */
	public static final String CHALLENGED_BY = "challengedBy";
	
	/**
	 * Geeft aan de client door dat de uitnodiging van de gegeven speler
	 * is geannuleerd.
	 * @param name Naam van de uitnodiger
	 * @syntax <code>challengeCancelled &lt;String:name&gt;</code>
	 */
	public static final String CHALLENGE_CANCELLED = "challengeCanceled";
	
	/**
	 * Geeft aan de client door dat zijn uitnodiging door de gegeven speler
	 * is geaccepteerd.
	 * @param name Naam van de genodigde
	 * @syntax <code>challengeAccepted &lt;String:name&gt;</code>
	 */
	public static final String CHALLENGE_ACCEPTED = "challengeAccepted";
	
	/**
	 * Geeft aan de client door dat zijn uitnodiging door de gegeven speler
	 * is afgewezen.
	 * @param name Naam van de genodigde
	 * @syntax <code>challengeRejected &lt;String:name&gt;</code>
	 */
	public static final String CHALLENGE_REJECTED = "challengeRejected";
}
