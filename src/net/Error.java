package net;

/**
 * <p>
 * Enumeratie van foutmeldingen die de server naar de client kan sturen
 * via de methode <tt>error(Error)</tt>. De foutcode kan verkregen worden
 * met <tt>value()</tt>. De code kan weer omgezet
 * worden naar een <tt>Error</tt>-object door het als index te gebruiken
 * in de <tt>Error</tt>-array verkregen met <tt>Error.values()</tt>.
 * Bijvoorbeeld:
 * </p><p><tt>
 * Error err = Error.values()[code];<br>
 * if (err.equals(Error.NOT_YOUR_TURN)) {<br>
 * &nbsp;&nbsp;//doe iets met het Error-object<br>
 * }
 * </tt></p><p>
 * Een standaard beschrijving van de fout kan worden verkregen met
 * <tt>toString()</tt>.
 * </p><p>
 * Let op: enums worden automatisch aangemaakt en kunnen daarna behandeld
 * worden als constantes, zoals <tt>Error.DEFAULT</tt>. Het heeft geen zin
 * om de constructor aan te roepen.
 * 
 * @author Michael Hannema
 */
public enum Error {
	NAME_TAKEN				("Naam is bezet"),
	UNKNOWN_PLAYER			("Speler niet bekend"),
	PLAYER_BUSY				("Speler is bezig met een partij"),
	INVALID_NAME			("Ongeldige naam"),
	INVALID_COMMAND			("Ongeldig commando"),
	INVALID_PARAMETER		("Ongeldige parameter"),
	INVALID_MOVE			("Ongeldige zet"),
	NOT_YOUR_TURN			("Een andere speler is nu aan de beurt"),
	GAME_OVER				("Je kan geen zet meer doen"),
	SERVER_FULL				("Server is vol"),
	CHALLENGE_NOT_SUPPORTED	("De challenge-functie wordt niet ondersteund"),
	CHAT_NOT_SUPPORTED		("De chatfunctie wordt niet ondersteund"),
	DEFAULT					("Error");
	
	/**
	 * Beschrijving van de foutmelding
	 */
	private final String description;
	
	/**
	 * Interne constructor voor het initialiseren van de enum-objecten.
	 * @param description Beschrijving van de foutmelding
	 */
	private Error(String description) {
		this.description = description;
	}
	
	/**
	 * Geeft de error code van de foutmelding.
	 * @return Een integer-waarde die uniek is voor elk type <tt>Error</tt>
	 */
	public int value() {
		return ordinal();
	}
	
	/**
	 * Geeft een beschrijving van de foutmelding
	 * @return De beschrijving van de foutmelding
	 */
	public String toString() {
		return description;
	}
}
