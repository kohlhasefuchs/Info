import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

/**
 * Ein Simulator für ein ökologisches System mit
 * Füchsen, Hasen und Pflanzen.
 *
 * @author F. Lesske / C. Brand
 * 
 */
public class Simulator extends Thread {

	// Konstanten die die Erzeugung zu Beginn des Spieles beeinflussen
	private static final double FUCHS_ERZEUGUNG = 0.02;

	private static final double HASE_ERZEUGUNG = 0.05;

	private static final double KOHL_ERZEUGUNG = 0.02;

	// vielleicht gibt es auch noch Jäger, die Füchse jagen
	private static final double JAEGER_ERZEUGUNG = 0.005;


	/**
	 * Bevölkere das Feld mit Hasen und Füchsen und generiere einige Pflanzen
	 */
	private void bevoelkere(Feld feld) {
		Random random = new Random();
		feld.clear();
		
		//1b
		double ERZEUGUNG;
		int height = feld.getHeight();
		int width = feld.getWidth();
		System.out.println("Feldgr��e "+height+" + "+width);
		int z�hler=0;
		for (int i=0; i<height; i++) {
			for (int j=0; j<width;j++) {
				ERZEUGUNG = Math.random();
				if (ERZEUGUNG<KOHL_ERZEUGUNG){
					erzeugeKohl(i,j);
					z�hler++;
					System.out.println("Initial-Kohl Nummer "+z�hler+" bei "+i+"  "+j);
				}
				else if (ERZEUGUNG<HASE_ERZEUGUNG){
					erzeugeHase(i,j);
					z�hler++;
				}
			}
		}

		// TODO: fehlende Implementierung ergänzen
		// erzeuge zufällig zuerst Pflanzen, dann später Hasen, dann Füchse
		// siehe Aufgaben 1b (Kohl), 2a (Hasen) und 3 (Füchse)


		// erzeuge zufällige Reihenfolge der Tiere
		Collections.shuffle(lebewesen);
	}

	/**
	 * Erzeuge zufällig neue Pflanzen auf freie Felder mit guter Erde.
	 */
	private void erzeugePflanzen() {

		// TODO: fehlende Implementierung ergänzen (siehe Aufgabe 1a)		
		
		double RANDOM_ERZEUGUNG;
		int height = getHeight();
		int width = getWidth();
		int z�hler=0;
		for (int i=0; i<height; i++) {
			for (int j=0; j<width;j++) {
				boolean empty= isEmpty(i,j);
				boolean good= goodSoil(i,j);
				RANDOM_ERZEUGUNG = Math.random();
				if (empty==true&&good==true&&KOHL_ERZEUGUNG>=RANDOM_ERZEUGUNG){
					erzeugeKohl(i,j);
					z�hler++;
					System.out.println("Kohl Nummer "+z�hler+" bei "+i+"  "+j);
				}
			}
		}
		
		

	}

	/*
	 * **** Ab hier muss am Programmcode nichts geändert werden *****
	 */ 

	/*
	 * Konstanten, Instanzvariablen
	 */ 
	// die Menge der Tiere
	@SuppressWarnings("rawtypes")
	private Vector lebewesen;

	// die Menge der neugeborenen Tiere
	private HashMap<PositionsObjekt, PositionsObjekt> neueTiere;

	// die Menge der Pflanzen
	@SuppressWarnings("rawtypes")
	private Vector pflanzen;

	// das Feld auf dem sich alles abspielt
	private Feld feld;
	// Größe des Feldes
	private int hoehe, breite;

	// ein zweites Feld auf dem der nächste Simulationsschritt aufgebaut wird
	private Feld aktualisiertesFeld;

	// der aktuelle Simulationsschritt
	private int schritt;
	private int anzahlSchritte;

	// Kontrollvariablen
	private boolean gestartet = false;
	private boolean weiter = true;
	private boolean neueGroesse = false;

	// Semaphor zur Synchronisation mit der Anzeige
	private int s = 1;

	// grafische Ansicht der Simulation
	private SimulatorAnzeige anzeige;

	private int waitTime = 300;


	/*
	 * Methoden zur Erzeugung von Pflanzen und Lebewesen
	 */
	@SuppressWarnings("unchecked")
	private void erzeugeKohl(int zeile, int spalte) {
		Kohl kohl = new Kohl(SimulatorAnzeige.plantImage);
		pflanzen.add(kohl);
		kohl.setLocation(zeile, spalte);
		feld.platziere(kohl, zeile, spalte);    
	}

	@SuppressWarnings("unchecked")
	private void erzeugeFuchs(int zeile, int spalte) {
		Fuchs fuchs = new Fuchs(true, SimulatorAnzeige.foxImage);
		lebewesen.add(fuchs);
		fuchs.setLocation(zeile, spalte);
		feld.platziere(fuchs, zeile, spalte);
	}

	@SuppressWarnings("unchecked")
	private void erzeugeHase(int zeile, int spalte) {
		Hase hase = new Hase(true);
		lebewesen.add(hase);
		hase.setLocation(zeile, spalte);
		feld.platziere(hase, zeile, spalte);
	}

	/**
	 * Konstruktor für die Klasse Simulator
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Simulator(int hoehe, int breite) {
		lebewesen = new Vector();
		neueTiere = new HashMap();
		pflanzen = new Vector();
		this.hoehe = hoehe;
		this.breite = breite;
		feld = new Feld(hoehe, breite);
		aktualisiertesFeld = new Feld(hoehe, breite);
	}

	protected void setAnzeige(SimulatorAnzeige view) {
		anzeige = view;
	}

	public void simuliere(int schritte) {
		reset();
		for (int i = 1; i <= schritte && weiter; i++) {
			simulationsSchritt();
		}
	}

	protected void setSchrittZahl(int k) {
		anzahlSchritte = k;
	}

	public void simuliere() {
		weiter = true;
		if (!gestartet) {
			System.out.println("Simulation startet...");
			this.start();
		} else {
			v();
		}
	}

	/**
	 * Ablauf einer Simulation im eigenen Thread
	 */
	public void run() {
		gestartet = true;
		while (true) {
			p();
			simuliere(anzahlSchritte);
		}
	}

	/*
	 * Methoden zur Synchronisation mit der GUI
	 */
	private synchronized void p() {
		if (s <= 0) 
			try {
				Thread.currentThread().wait();
			} catch (Exception e) {}
		s--;
	}

	private synchronized void v() {
		if (s== 0) {
			s++;
			this.notify();
		}
	}

	public void halte() {
		weiter = false;
	}

	/**
	 * Durchführung eines Simulationsschrittes
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	private void simulationsSchritt() {
		schritt++;
		neueTiere.clear();

		Iterator it = lebewesen.iterator();
		Vector<Lebewesen> toDelete = new Vector<Lebewesen>();
		while (it.hasNext()) {
			Lebewesen next = (Lebewesen) it.next();
			if (next.istLebendig()) {
				next.run(feld, aktualisiertesFeld, neueTiere);
				if (!next.istLebendig()) {
					toDelete.add(next);
				}
			}
			else {
				//it.remove();
				toDelete.add(next);
			}
		}

		// nur Kinder die Platz haben k�nnen �berleben
		Iterator<PositionsObjekt> iter = neueTiere.keySet().iterator();
		while (iter.hasNext()) {
			PositionsObjekt next = (PositionsObjekt)iter.next();
			PositionsObjekt parent = neueTiere.get(next);
			Location loc = parent.getLocation();
			Location pos = aktualisiertesFeld.freeAdjacentLocation(loc);
			if (pos != null && pos != loc) {
				next.setLocation(pos);
				aktualisiertesFeld.platziere(next);
			} else {
			//	iter.remove();
			//	((Tier)next).sterbe();
			//	toDelete.add((Lebewesen)next);
			}
		}


		lebewesen.addAll(neueTiere.keySet());

		for (Lebewesen tier:toDelete){
			lebewesen.remove(tier);
		}

		//lebewesen.addAll(neueTiere);
		//Fuchs.anzahl = Fuchs.anzahl - toteFuechse;
		//Hase.anzahl = Hase.anzahl - toteHasen;

		feld.addRabbitNumber(Hase.anzahl);
		aktualisiertesFeld.addRabbitNumber(Hase.anzahl);

		feld.addFoxNumber(Fuchs.anzahl);
		aktualisiertesFeld.addFoxNumber(Fuchs.anzahl);


		improveSoil();
		it = pflanzen.iterator();
		while (it.hasNext()) {
			Pflanze next = (Pflanze) it.next();
			if (next.waechstNoch())
				next.bluehe(feld, aktualisiertesFeld);
			else {
				it.remove();
			}
		}
		erzeugePflanzen();

		aktualisiertesFeld.addPlantNumber(pflanzen.size());
		feld.addPlantNumber(pflanzen.size());

		// Swap the field and updatedField at the end of the step.
		Feld temp = feld;
		feld = aktualisiertesFeld;
		aktualisiertesFeld = temp;
		aktualisiertesFeld.clear();
		try {
			Thread.currentThread().sleep(waitTime);
		} catch (Exception e) {
		}


		// display the new field on screen
		anzeige.showStatus(
				schritt,
				feld,
				"Füchse: " + Fuchs.anzahl + " Hasen: " + Hase.anzahl);

	}

	protected void setWaitTime(int v) {
		waitTime = v;
	}

	private void improveSoil() {
		feld.improveSoil();
	}

	/**
	 * Erzeuge eine neue Ausgangssituation
	 */
	public void reset() {
		lebewesen.clear();
		pflanzen.clear();
		neueTiere.clear();
		Fuchs.anzahl = 0;
		Hase.anzahl = 0;
		if (neueGroesse) {
			feld = new Feld(hoehe, breite);
			aktualisiertesFeld = new Feld(hoehe, breite);
			neueGroesse=false;
		}
		else {
			feld.reset();
			aktualisiertesFeld.reset();
		}
		bevoelkere(feld);
		schritt = 0;
		anzeige.showStatus(schritt, feld, "Füchse: 0, Hasen: 0");
	}



	protected void changeDimension(int br, int h) {
		hoehe = h;
		breite = br;
		neueGroesse = true;
	}

	protected int getHeight() {
		return feld.getHeight();
	}

	protected int getWidth() {
		return feld.getWidth();
	}

	private boolean isEmpty(int zeile, int spalte) {
		return feld.istLeer(zeile,spalte);
	}

	private boolean goodSoil(int zeile, int spalte) {
		return feld.goodSoil(zeile,spalte);
	}

	/**************************************
	 *  Die main-Methode
	 * 
	 **********************************/

	public static void main(String[] arg) {
		// erzeuge Simulator
		Simulator simulator = new Simulator(60, 80);
		// erzeuge Anzeige
		SimulatorAnzeige anzeige = new SimulatorAnzeige(60, 80, simulator);
		anzeige.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		// verbinde Simulator mit Anzeige
		simulator.setAnzeige(anzeige);
	}

}
