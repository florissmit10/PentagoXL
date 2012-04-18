package net;

/**
 * <p>
 * Het protocol waarmee de client met de server (client handler) communiceert.
 * Bij het implementeren moet in ieder geval een invulling gegeven
 * worden aan de standaardcommando's. Als de client hierdoor niet aan een verzoek
 * van een server kan voldoen, moet de betreffende foutmelding worden doorgegeven
 * met het <tt>error</tt>-commando.
 * </p><p>
 * Elk commando wordt over de socket gestuurd als een string met daarin het 
 * keyword, gevolgd door de argumenten en beëindigd met een newline character.
 * De javadocs bij de commando's leggen de syntax uit bij de <tt>syntax</tt>-tag,
 * en ook eventuele pre- en postcondities. Het
 * keyword en de argumenten worden van elkaar gescheiden door een gereserveerd
 * karakter, opgeslagen in <tt>DELIM</tt>. Bijvoorbeeld:
 * </p><p><tt>
 * "doMove" + DELIM + 1 + DELIM + 2 + DELIM + 3 + DELIM + '+' + '\n'
 * </tt></p><p>
 * Vanzelfsprekend mag het scheidingsteken niet in een spelernaam
 * worden opgenomen. Spaties zijn echter wel toegestaan.
 * </p>
 * 
 * @author Michael Hannema
 */
public interface ClientProtocol {
	
	/**
	 * Scheidingsteken voor keywords en argumenten
	 */
	public static final char DELIM = '\u0000';
	
	//standaardcommando's
	/**
	 * Meldt deze client aan bij de server en geeft de spelernaam door 
	 * aan de server. Voor eenmalig gebruik na het verbinden met de server.
	 * @param name Spelernaam
	 * @require <tt>name != ""</tt>
	 * @syntax <code>join &lt;String:name&gt;</code>
	 */
	public static final String JOIN = "join";
	
	/**
	 * Verzoekt de server om een partij met een gegeven aantal spelers.
	 * @param players Gewenste aantal spelers
	 * @require <tt>2 <= players <= 4</tt>
	 * @syntax <code>request &lt;Int:players&gt;</code>
	 */
	public static final String REQUEST = "request";
	
	/**
	 * Verzoekt de server om een zet te doen. Een zet bestaat uit
	 * het plaatsen van een knikker en het draaien van een deelveld.
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
	 * @syntax <code>doMove &lt;Int:x&gt; &lt;Int:y&gt; &lt;Int:field&gt; &lt;Char:direction&gt;</code>
	 */
	public static final String DO_MOVE = "doMove";
	
	/**
	 * Geeft een fout door aan de server. Bedoeld om aan te geven dat de
	 * chat- of challenge-functie niet beschikbaar is of om te debuggen.
	 * @param code Een foutcode die de fout beschrijft. Gebruik de methode
	 * <tt>value()</tt> op een <tt>Error</tt>-instantie om de code te verkrijgen.
	 * @param description Een beschrijving van de fout. Voor een
	 * standaardbeschrijving kan <tt>toString()</tt> van een <tt>Error</tt>-instantie
	 * gebruikt worden. De server is vrij om te kiezen tussen de standaardbeschrijving
	 * of de meegegeven beschrijving.
	 * @require <tt>(code == Error.CHALLENGE_NOT_SUPPORTED.value()) ||<br>
	 * (code == Error.CHAT_NOT_SUPPORTED.value()) ||<br>
	 * (code == Error.DEFAULT.value())</tt>
	 * @require <tt>description != ""</tt>
	 * @require <tt>for 0 <= i < description.length():<br>
	 * &nbsp;&nbsp;name.charAt(i) != DELIM</tt>
	 * @syntax <code>error &lt;Int:code&gt; &lt;String:description&gt;</code>
	 */
	public static final String ERROR = "error";
	
	//chatcommando
	/**
	 * Geeft een chatbericht door aan de server.
	 * @param message Bericht
	 * @syntax <code>chat &lt;String:message&gt;</code>
	 */
	public static final String CHAT = "chat";
	
	//challenge-commando's
	/**
	 * Verzoekt de server om een lijst van spelers die niet bezig zijn met een partij.
	 * @require Deze client bevindt zich in de lobby.
	 * @syntax <code>players</code>
	 */
	public static final String PLAYERS = "players";
	
	/**
	 * Verzoekt de server om één of meerdere spelers uit te nodigen voor een potje.
	 * @param names Namen van de gewenste tegenstanders
	 * @require <tt>name != ""</tt>
	 * @require De gegeven namen zijn bekend bij de server.
	 * @require De client heeft geen andere uitnodigingen open staan.
	 * @syntax <code>challenge &lt;String:name&gt;(1..3)</code>
	 */
	public static final String CHALLENGE = "challenge";
	
	/**
	 * Accepteert een uitnodiging.
	 * @param name Naam van de uitnodiger
	 * @require <tt>naam</tt> heeft deze client uitgenodigd.
	 * @ensure Eventuele andere openstaande uitnodigingen worden afgewezen
	 * met het <tt>challengeReject</tt>-commando.
	 * @syntax <code>challengeAccept &lt;String:name&gt;</code>
	 */
	public static final String CHALLENGE_ACCEPT = "challengeAccept";
	
	/**
	 * Wijst een uitnodiging af.
	 * @param name Naam van de uitnodiger
	 * @require <tt>naam</tt> heeft deze client uitgenodigd.
	 * @syntax <code>challengeReject &lt;String:name&gt;</code>
	 */
	public static final String CHALLENGE_REJECT = "challengeReject";
	
	/**
	 * Annuleert de uitnodiging die deze client heeft verzonden.
	 * @require Deze client heeft een uitnodiging verzonden die nog open staat.
	 * @syntax <code>challengeCancel</code>
	 */
	public static final String CHALLENGE_CANCEL = "challengeCancel";
}
