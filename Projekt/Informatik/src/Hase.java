import java.awt.Color;
import java.util.Random;


/**
 * Die Klasse der Hasen. Jeder Hase ist ein Tier.
 * 
 * @author Frank Leßke / C. Brand
 */
public class Hase extends Tier {
  
  /* 
   * Eigenschaften aller Hasen (statische Felder) 
   *****************************************************/
 
  // das Alter, ab dem sich ein Hase fortpflanzen kann
	 private static final int REIFE_ALTER = 5;
	  
	  // die maximale Lebendauer eines Hasen
	  static int MAX_ALTER = 50;
	  
	  // die Fortpflanzungswahrscheinlichkeit
	  static double PAARUNGS_WAHRSCHEINLICHKEIT = 0.2;
	  
	  // die maximale Anzahl von Nachkommen
	  static int MAX_BRUT = 5;
	  
	  // gemeinsamer Zufallsgenerator zur Geburtenkontrolle
	  private static final Random random = new Random();
	  
	  // wieviel Essenspunkte bringt das Verzehren eines Kohls
	  private static final int KOHL_FUTTER = 2;
	 
	  // Gesamtzahl der Hasen
	  public static int anzahl = 0;
	 
	  //--FD--
	  //Faktor, um den ein satter Hase sich besser vermehrt ("flei�aufgabe")
	  private static double SATT_FAKTOR = 0.05;
	  
	  //Hunger-Wert, unter dem ein hase zu hungrig ist, sich zu vermehren
	  private static int MAX_HUNGER = 2;

  
  
  /**
   * ueberprueft, ob sich ein Hase fortpflanzen kann. 
   * Gibt als Ergebnis die Anzahl der Kinder zurueck (kleiner gleich MAX_BRUT)
   */
  protected int fortpflanzen() {
    
  int alter = getAlter();
  int hunger = getHunger();
  if(alter>REIFE_ALTER&&hunger>MAX_HUNGER){  //vorlagerung der alters und Hunger-Frage, um Laufzeit zu verbessern
	  double gesamtw = ((hunger*SATT_FAKTOR)+PAARUNGS_WAHRSCHEINLICHKEIT);
	  double Zufall = Math.random();
	  int Brut;
	  // Hungrige hasen-> unwahrscheinlicher, Satte Hasen-> wahrscheinlicher
	  //=> je satter, desto wahrscheinlicher
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
  
  
  
  
  /*******************************
   * Ab hier muss der Programmcode nicht mehr verändert werden.
   *****************/
  
  /**
   * Suche auf dem Feld feld auf den direkt angrenzenden Nachbarpositionen 
   * zur Position des Hasen nach etwas zum Fressen.
   * Hasen fressen Kohl. 
   * Wenn ein Kohl gefunden wird, so wird dieser verspeist.
   * Als Ergebnis wird die Position des Kohls zurückgegeben.
   */
  protected Location findeFressen(Feld feld) {

	  // durchsuche alle Nachbarpositionen
	  for (int i = 0; i < 8; i++) {
		  
		  // prüfe, ob Nachbar noch innerhalb des Feldes liegt
		  if (liegtAufFeld(feld, i)) {
			  
			  // Hole die Nachbar-Location
			  Location loc = getNachbarPosition(feld, i);
			  
			  // prüfe, ob darauf Kohl liegt
			  if (liegtKohl(feld, loc)) {
				  
				  // hole den Kohl und prüfe, ob dieser essbar ist
				  Kohl kohl = getKohl(feld, loc);
				  if (essbar(kohl)) {
					  fresse(kohl);
					  setSatt(getHunger() + KOHL_FUTTER);
					  return loc;
				  }
			  }
		  }			
	  }

	  // falls kein Kohl gefunden wird, dann gebe null zurück
	  return null;
  }


  /**
   * Erzeuge einen neuen Hasen. Ein Hase kann entweder mit dem Alter = 0
   * erzeugt werden (bei der Geburt), oder mit einem zuf�lligen Alter (bei
   * Beginn der Simulation)
   * 
   * @param zufallsAlter Wenn wahr, dann bekommt der Hase ein Zufallsalter
   */
  public Hase(boolean zufallsAlter)
  {
    if (zufallsAlter) {
      setAlter(random.nextInt(MAX_ALTER));
    } else {
      setAlter(0);
    }
    setSatt(7);
    anzahl++;
  }

  /**
   * Die Implementierung einer Hasengeburt.
   */
  protected Tier geburt() {
    return new Hase(false);
  }
  
  /**
   * Wann ist ein Hase zu alt?
   */
  protected boolean zuAlt(int einAlter) {
    return einAlter > MAX_ALTER;
  }
  
  /**
   * Standardfarben für Hasen (wird zur Zeit nicht benutzt)
   */
  public Color getColor() {
    return Color.magenta;
  }
  
  /**
   * Ist ein Kohlkopf essbar?
   */
  private boolean essbar(Kohl kohl) {
    return kohl.waechstNoch();
  }
  
  /**
   * Verspeise den Kohlkopf.
   */
  private void fresse(Kohl kohl) {
    kohl.wirdGefressen();
  }
  
  /**
   * Liegt auf dem Feld f auf Position loc ein Kohlkopf?
   */
  private boolean liegtKohl(Feld f, Location loc) {
    Object objekt = f.getObjektAt(loc.getRow(), loc.getCol());
    return (objekt instanceof Kohl);
  }
  
  /**
   * Zugriff auf den Kohlkopf auf dem Feld f an Position loc,
   * wenn dort auch ein Kohlkopf zu finden ist.
   */
  private Kohl getKohl(Feld f, Location loc) {
    Object objekt = f.getObjektAt(loc.getRow(), loc.getCol());
    if (objekt instanceof Kohl)
      return (Kohl)objekt;
    else {
      System.out.println("Sie sollten erst überprüfen, ob an der Position auch wirklich Kohl liegt");
      System.out.println("Die Simulation wird jetzt beendet.");
      System.exit(0);
    }
    return null;
  }
  
  /**
   * Zu wenig Kohlköpfe gefunden, der Hase muss sterben.
   */
  public void sterbe() {
    super.sterbe();
    Hase.anzahl--;
  }
  
}
