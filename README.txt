PentagoXL
Eindopdracht Programmeren 2 2012
Jeroen Monteban en Floris Smit

Toelichting op vereiste programmaonderdelen:
1) README
/README.TXT dit document
2)Java code
/src/
3)Javadoc
/doc/index.html
4)Bytecode
/bin/
5)TestKlassen
Alle testKlassen zitten in de package van de klasse die getest word.




Aanwijzingen voor het snel starten van een server en 2 tot 4 clients.
	voer de klasse: Controller/Quickstart+arg[0] uit
		arg[0]: (getal tussen de 2 en 4) aantal spelers.
		default word er geen request gestuurd.
	de clients worden automatisch verbonden met de server.
	start daarna voor elke client een spel


Aanwijzingen voor het starten van een Server:
	Voer de klasse: View.ServeGUI uit
	vul een gewenste port in
	druk op Host

Aanwijzingen voor het starten van een client:
	Voer de klasse: Controller/Client uit
	voer de gebruikte port in.
	druk op connect.

Aanwijzingen voor het starten van een spel.
	selecteer het gewenste aantal spelers.
	selecteer de gewenste AI of een mensSpeler
	druk op Play