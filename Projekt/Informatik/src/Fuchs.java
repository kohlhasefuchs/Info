import java.awt.Color;
import java.awt.Image;
import java.util.Random;

/**
 * Die Klasse der Füchse. Jeder Fuchs ist ein Tier.
 * 
 * @author Frank Leßke / C. Brand
 */
public class Fuchs extends Tier {
  
  /* 
   * Eigenschaften aller Füchse (statische Felder) 
   ****************************************************/
  
  // das Alter, ab dem sich ein Fuchs fortpflanzen kann
	private static final int REIFE_ALTER = 10;
	  
	  // die maximale Lebendauer von einem Fuchs
	  static int MAX_ALTER = 40;
	  
	  // die Fortpflanzungswahrscheinlichkeit
	  static double PAARUNGS_WAHRSCHEINLICHKEIT = 0.6;
	  
	  // die maximale Anzahl von Nachkommen
	  static int MAX_BRUT = 2;
	  
	  // wieviel Essenspunkte bringt das Verzehren eines Hasens
	  private static int HASEN_FUTTER = 3;
	  
	  // Gesamtzahl der F�chse
	  public static int anzahl = 0;
	  
	  // die generelle Fuchs-Jagdwahrscheinlichkeit 
	  static double JAGD_WAHRSCHEINLICHKEIT = 0.5;
	  
	  // die Jagdwahrscheinlichkeit (eines einzelnen Fuchses)
	  double jagdWahrsch;
	  
	  // gemeinsamer Zufallsgenerator zur Geburtenkontrolle
	  private static final Random random = new Random();
	  
	  // das anzuzeigende Bild
	  private Image fuchsBild;
	  
	  //Je satter der Fuchs, desto niedriger sein Jagdgl�ck um SATT_FAKTOR
	  private double SATT_FAKTOR = 0.096;
	  
	  /**
	   * Suche auf dem Feld feld auf den direkt angrenzenden Nachbarpositionen 
	   * zur Position des Fuchses nach etwas zum Fressen.
	   * F�chse fressen Hasen. Wenn ein Hase gefunden wird, so wird dieser get�tet und verspeist.
	   * Allerdings h�ngt das Jagdgl�ck auch etwas vom Zufall ab.
	   * Als Ergebnis wird die Position des Hasens zur�ckgegeben.
	   */
	  protected Location findeFressen(Feld feld) {
		  int hunger;
		  // TODO: fehlende Implementierung erg�nzen (siehe Aufgabe 3)
		// durchsuche alle Nachbarpositionen
			  for (int i = 0; i < 8; i++) {
				  
				  // pr�fe ob Nachbar noch innerhalb des Feldes liegt
				  if (liegtAufFeld(feld, i)) {
					  
					  // Hole Nachbar-Position
					  Location loc = getNachbarPosition(feld, i);
					  
					  // pr�fe ob darauf ein hase steht
					  if (stehtHase(feld, loc)) {
						  
						  // hole den Hasen und pr�fe, ob dieser essbar ist
						  Hase hase = getHase(feld, loc);
						  double jagdw = Math.random();
						  hunger = getHunger();
						  if (hase.istLebendig()&&jagdw<(JAGD_WAHRSCHEINLICHKEIT-hunger*SATT_FAKTOR)) {
							  fresse(hase);
							  setSatt(getHunger() + HASEN_FUTTER);
							  return loc;
						  }
					  }
				  }			
			  }
		  
		  // falls kein Hase gefunden wird, dann gebe null zur�ck  
		  return null;
	  }

	  /**
	   * �berpr�ft, ob sich ein Fuchs fortpflanzen kann. 
	   * Gibt als Ergebnis die Anzahl der Kinder zur�ck (kleiner gleich MAX_BRUT)
	   */
	  protected int fortpflanzen() {
	    
		  // TODO: fehlende Implementierung erg�nzen (siehe Aufgabe 3)
		  int alter = getAlter();
		  int hunger = getHunger();
		  if(alter>REIFE_ALTER){  //vorlagerung der Alters-Frage, um Laufzeit zu verbessern
			  double gesamtw = (PAARUNGS_WAHRSCHEINLICHKEIT+hunger*0.015);
			  double Zufall = Math.random();
			  int Brut;
			  	if (Zufall<gesamtw){
			  		Zufall = Math.random(); //neuer Zufallswert, um Anzahl der Kinder festzustellen
			  		Brut = (int)Math.round(Zufall*MAX_BRUT);
			  		return Brut;
			  	}
			  	else{
				  	return 0;	  
				}
		  } 
		  else{
			  	return 0;	  
		  }
	  }
	 
	  public void fresse(Hase hase){
		  hase.wirdGet�tet();
	  }


  /**
   * Überprüft, ob sich ein Fuchs fortpflanzen kann. 
   * Gibt als Ergebnis die Anzahl der Kinder zurück (kleiner gleich MAX_BRUT)
   */


  
  /*******************************
   * Ab hier muss der Programmcode nicht mehr verändert werden.
   *****************/
  
  
  /**
   * Erzeuge einen neuen Fuchs. Ein Fuchs kann entweder mit dem Alter = 0
   * erzeugt werden (bei der Geburt), oder mit einem zufälligen Alter (bei
   * Beginn der Simulation)
   * 
   * @param zufallsAlter
   *            Wenn wahr, dann bekommt der Fuchs ein Zufallsalter
   */
  public Fuchs(boolean zufallsAlter)
  {
    if (zufallsAlter) {
      setAlter(random.nextInt(MAX_ALTER));
    } else {
      setAlter(0);
    }
    jagdWahrsch = JAGD_WAHRSCHEINLICHKEIT;
    anzahl++;
    setSatt(8);
  }
  
  /**
   * Erzeuge einen Fuchs, mit zugeordnetem Bild
   */
  public Fuchs(boolean zufallsAlter, Image bild)
  {
    if (zufallsAlter) {
      setAlter(random.nextInt(MAX_ALTER));
    } else {
      setAlter(0);
    }
    anzahl++;
    fuchsBild = bild;
    setSatt(5);
  }
  
  /**
   * Wann ist der Fuchs zu alt?
   */
  protected boolean zuAlt(int einAlter) {
    return einAlter > MAX_ALTER || getHunger() <= 0;
  }
  
  /**
   * Die Fuchs-Farbe.
   */
  public Color getColor() {
    return Color.red;
  }
  
  /**
   * Ein Fuchs wird geboren.
   */
  protected Tier geburt() {
    return new Fuchs(false, fuchsBild);
  }
  
  /**
   * Steht auf dem Feld f auf Position loc ein Hase?
   */
  private boolean stehtHase(Feld feld, Location loc) {
    Object objekt = feld.getObjektAt(loc.getRow(), loc.getCol());
    return (objekt instanceof Hase);
  } 
  
  /**
   * Zugriff auf den Hasen auf dem Feld f an Position loc,
   * wenn dort auch ein Hase zu finden ist.
   */
  private Hase getHase(Feld feld, Location loc) {
    Object objekt = feld.getObjektAt(loc.getRow(), loc.getCol());
    if (objekt instanceof Hase)
      return (Hase)objekt;
    else {
      System.out.println("Sie sollten erst überprüfen, ob an der Position auch wirklich ein Hase steht");
      System.out.println("Die Simulation wird jetzt beendet.");
      System.exit(0);
    }
    return null;
  }
  
  /**
   * Welches Bild wird für den Fuchs verwendet?
   */
  public Image getImage() {
    return fuchsBild;
  }
  
  /**
   * Zu alt, oder zu wenige Hasen gegessen, der Fuchs muss sterben.
   */
  public void sterbe() {
    super.sterbe();
    Fuchs.anzahl--;
  }
}
