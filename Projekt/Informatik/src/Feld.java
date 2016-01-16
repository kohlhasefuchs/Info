import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

/**
 * Repräsentiert ein rechteckiges Gitter von Feldpositionen.
 * Jede Position kann ein Tier oder eine Pflanze aufnehmen.
 * 
 * @author Frank Leßke
 * @version 14.1.2005
 */

public class Feld {

	// Ein Zufallsgenerator
	private static final Random random = new Random();
	// Die Tiefe und Breite eines Feldes
	private int hoehe, breite;
	// Hier werden die zeichenbaren Objekte gespeichert
	private PositionsObjekt[][] feld;

	private int[][] soil;
	private static int goodSoil = 6;

	private Vector<Integer> numberOfPlants;
	private Vector<Integer> numberOfRabbits;
	private Vector<Integer> numberOfFox;
	private Vector<Integer> numberOfHunter;

	/**
	 * Repräsentiert ein Feld der gegebenen Dimension.
	 * @param hoehe Die Hoehe des Feldes.
	 * @param breite Die Breite des Feldes.
	 */
	public Feld(int hoehe, int breite)
	{
		this.hoehe = hoehe;
		this.breite = breite;
		feld = new PositionsObjekt[hoehe][breite];
		soil = new int[hoehe][breite];
		Random rand = new Random();
		for (int[] r:soil) {
			for (int i = 0; i<r.length; i++) {
				r[i] = rand.nextInt(goodSoil+1);
			}

		}
		numberOfPlants = new Vector<Integer>();
		numberOfRabbits = new Vector<Integer>();
		numberOfFox = new Vector<Integer>();
		numberOfHunter = new Vector<Integer>();
	}

	protected void addPlantNumber(int n) {
		numberOfPlants.add(new Integer(n));
	}

	protected Vector<Integer> getPlantNumbers() {
		return numberOfPlants;
	}

	protected void addRabbitNumber(int n) {
		numberOfRabbits.add(new Integer(n));
	}

	protected Vector<Integer> getRabbitNumbers() {
		return numberOfRabbits;
	}

	protected void addFoxNumber(int n) {
		numberOfFox.add(new Integer(n));
	}

	protected Vector<Integer> getFoxNumbers() {
		return numberOfFox;
	}
	
	protected void addHunterNumber(int n) {
		numberOfHunter.add(new Integer(n));
	}

	protected Vector<Integer> getHunterNumbers() {
		return numberOfHunter;
	}

	protected boolean goodSoil(int i, int j) {
		return soil[i][j] > goodSoil;
	}

	protected void improveSoil(){
		for (int[] row : soil)
			for (int i=0; i < row.length; i++)
				row[i]++;
	}
	/**
	 * Nährbeschaffenheit einer Bodenzelle
	 */
	 protected int getSoil(Location pos) {
		return soil[pos.getRow()][pos.getCol()];
	 }

	 /**
	  * Verändere die Nährbeschaffenheit einer Bodenzelle
	  */
	 protected void setSoil(int v, Location pos) {
		 soil[pos.getRow()][pos.getCol()] = v;
	 }


	 /**
	  * Leere das Feld
	  */
	 public void clear()
	 {
		 for(int row = 0; row < hoehe; row++) {
			 for(int col = 0; col < breite; col++) {
				 feld[row][col] = null;
			 }
		 }
	 }

	 /**
	  * Leere das Feld und setze die Anzahl der Einwohner zurück
	  */
	 public void reset() {
		 clear();
		 numberOfPlants = new Vector<Integer>();
		 numberOfRabbits = new Vector<Integer>();
		 numberOfFox = new Vector<Integer>();
		 numberOfHunter = new Vector<Integer>();
	 }

	 protected boolean istLeer(int zeile, int spalte) {
		 return (feld[zeile][spalte] == null);
	 }

	 /**
	  * Platziere ein Tier auf der angegebenen Position.
	  * Wenn sich dort bereits eines befindet, so geht es verloren??
	  * @param tier Das zu platzierende Tier
	  * @param zeile Die Zeilenkoordinate der Position.
	  * @param spalte Die Spaltenkoordinate der Position
	  */
	 public void platziere(PositionsObjekt objekt, int zeile, int spalte)
	 {
		 feld[zeile][spalte] = objekt;
	 }

	 public void platziere(PositionsObjekt objekt) {
		 Location loc = objekt.getLocation();
		 feld[loc.getRow()][loc.getCol()] = objekt;
	 }

	 /**
	  * @return Die Höhe des Feldes
	  */
	 public int getHeight()
	 {
		 return hoehe;
	 }

	 /**
	  * @return Die Breite des Feldes
	  */
	 public int getWidth()
	 {
		 return breite;
	 }


	 /**
	  * Gib das Tier an der angegebenen Postion zurück, falls sich dort eines
	  * befindet.
	  * @param row Die gewünschte Zeile
	  * @param col Die gewünschte Spalte
	  * @return Das Tier welches sin an der Position befindet, oder null
	  * 	       falls dort kein Tier ist.
	  */
	 public Tier getAnimalAt(int row, int col)
	 {
		 if( row < hoehe && col < breite && feld[row][col] instanceof Tier)
			 return (Tier)feld[row][col];
		 else return null;
	 }

	 public PositionsObjekt getObjektAt(int row, int col) 
	 {
		 return feld[row][col];
	 }

	 /**
	  * Erzeuge eine zufällige Position, welche benachbart zu der
	  * gegebenen Position ist, oder die gleiche Position ist.
	  * Die erzeugte Position befindet sich innerhalb der gültigen
	  * Grenzen des Feldes.
	  * @param location Die Position, von der aus eine benachbarte Position erzeugt wird.
	  * @return Eine gültige Position innerhalb des Gitters. Kann auch die gleich Position sein,
	  * 		   wie sie im Parameter übergeben wurde.
	  */
	 public Location randomAdjacentLocation(Location location)
	 {
		 int row = location.getRow();
		 int col = location.getCol();
		 // Erzeuge einen offset von -1, 0, oder +1 sowohl für die aktuelle Zeile als auch für die Spalte
		 int nextRow = row + random.nextInt(3) - 1;
		 int nextCol = col + random.nextInt(3) - 1;
		 // Überprüfe ob die neue Position ausserhalb der Grenzen liegt, oder besetzt ist
		 if(nextRow < 0 || nextRow >= hoehe || nextCol < 0 || nextCol >= breite) {
			 return location;
		 }
		 else if(nextRow != row || nextCol != col) {
			 return new Location(nextRow, nextCol);
		 }
		 else {
			 return location;
		 }
	 }


	 /**
	  * Versuche eine freie Position zu finden, die benachbart zur
	  * gegebenen Position ist. Wenn es keine solche gibt, dann gib die
	  * aktuelle Position zurück - falls sie frei ist. Ist sie nicht frei, so
	  * ist das Ergebnis null. 
	  * Die erzeugte Position liegt innerhalb der vorgegebenen Grenzen des Feldes.
	  * @param location Die Ausgangsposition von der aus eine benachbarte Position erzeugt wird.
	  * @return Eine gültige Position innerhalb des Gitters. Das kann auch die im Parameter
	  *         übergebene Position sein. Wenn all umgebenden Positions voll sind, so ist
	  *         das Ergebnis null.
	  */
	 public Location freeAdjacentLocation(Location location)
	 {
		 Iterator adjacent = adjacentLocations(location);
		 while(adjacent.hasNext()) {
			 Location next = (Location)adjacent.next();
			 if(feld[next.getRow()][next.getCol()] == null || feld[next.getRow()][next.getCol()] instanceof Pflanze) {
				 return next;
			 }
		 }
		 // check whether current location is free
		 if(feld[location.getRow()][location.getCol()] == null) {
			 return location;
		 } 
		 else {
			 return null;
		 }
	 }


	 public Location nextLocation(Location location) {
		 return (Location)adjacentLocations(location).next();
	 }

	 /**
	  * Erzeuge einen Iterator über einen eine gemischte Liste von benachbarten
	  * Psoitionen. Die Liste wird nicht die Position selbst enthalten
	  * Alle Positionen werden innerhalb des Gitters liegen.
	  * @param location Die Position von der aus Nachbarschaften erzeugt werden
	  * @return Ein Iterator über Positionen die zur gegebenen Position benachbart sind.
	  */
	 public Iterator adjacentLocations(Location location)
	 {
		 int row = location.getRow();
		 int col = location.getCol();
		 LinkedList locations = new LinkedList();
		 for(int roffset = -1; roffset <= 1; roffset++) {
			 int nextRow = row + roffset;
			 if(nextRow >= 0 && nextRow < hoehe) {
				 for(int coffset = -1; coffset <= 1; coffset++) {
					 int nextCol = col + coffset;
					 // Ungültige Positionen und die Orginalposition werden ausgeschlossen.
					 if(nextCol >= 0 && nextCol < breite && (roffset != 0 || coffset != 0)) {
						 locations.add(new Location(nextRow, nextCol));
					 }
				 }
			 }
		 }
		 Collections.shuffle(locations,random);
		 return locations.iterator();
	 }

	 protected boolean nachbarImFeld(Location loc, int i) {
		 boolean result = true;
		 if (i == 0 || i == 1 || i == 7) {
			 result = result && (loc.getRow() > 0);
		 }
		 if (i == 1 || i == 2 || i == 3) {
			 result = result && (loc.getCol() < (breite-1));
		 }
		 if (i == 3 || i == 4 || i == 5) {
			 result = result && (loc.getRow() < (hoehe-1));
		 }
		 if (i == 5 || i == 6 || i == 7) {
			 result = result && (loc.getCol() > 0);
		 }
		 return result;
	 }

}
