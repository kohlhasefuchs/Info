import java.awt.Color;
import java.awt.Image;
import java.util.Random;

/**
 * Die Klasse der Jaeger. Jeder Jaeger ist (angenommen) ein Tier.
 * 
 * @author Frank Leßke / C. Brand
 */
public class Jaeger extends Tier {
  
  /* 
   * Eigenschaften aller Jaeger (statische Felder) 
   ****************************************************/
  
  // das Alter, ab dem ein Jaeger in der Ausbildung anfaengt, zu erschie�en
	  private static final int JAGD_ALTER = 10;
	  
	  // die maximale Lebendauer von einem Jaeger
	  static int MAX_ALTER = 110;
	  
	  // wieviel ueberlebenspunkte (nicht nur Futter, es waeren eher Felle...) erhaelt der Jaeger fuer Hasen, bzw. Fuechse
	  private static int TIER_LEBEN = 4;
	  
	  // Gesamtzahl der Jaeger
	  public static int anzahl = 0;
	  
	  // die generelle Jaeger-Jagdwahrscheinlichkeit 
	  static double JAGD_WAHRSCHEINLICHKEIT = 0.05;
	  
	  //Wahrscheinlichkeit, dass ein Jaeger einen anderen, neuen Jaeger ausbildet
	  static double AZUBI_WAHRSCHEINLICHKEIT = 0.005;

	  //random fuer erzeugung
	  private static final Random random = new Random();
	  
	  // die Jagdwahrscheinlichkeit (eines einzelnen Jaegers)
	  double jagdWahrsch;
	  
	  // das anzuzeigende Bild
	  private Image jagdBild;
	  
	  //Je aelter der Jaeger, desto erfahrener ist er und desto hoeher seine Jagd-Wahrscheinlichkeit
	  private double ALTER_FAKTOR = 0.0035;
	  
	  /**
	   * Suche auf dem Feld feld auf den direkt angrenzenden Nachbarpositionen 
	   * zur Position des Fuchses nach etwas zum Fressen.
	   * Fuechse fressen Hasen. Wenn ein Hase gefunden wird, so wird dieser getoetet und verspeist.
	   * Allerdings haengt das Jagdglueck auch etwas vom Zufall ab.
	   * Als Ergebnis wird die Position des Hasens zurueckgegeben.
	   */
	  protected Location findeFressen(Feld feld) { //beibehalten des Methoden-Namens aufgrund der run-Methode
		  int alter;
		// durchsuche alle Nachbarpositionen
			  for (int i = 0; i < 8; i++) {
				  // pruefe ob Nachbar noch innerhalb des Feldes liegt
				  if (liegtAufFeld(feld, i)) {
					  
					  // Hole Nachbar-Position
					  Location loc = getNachbarPosition(feld, i);
					  
					  // pruefe ob darauf ein hase steht
					  if (stehtTier(feld, loc)) {
						  
						  // hole das Tier und pruefe
						  Tier tier = getTier(feld, loc);
						  double jagdw = Math.random();
						  alter = getAlter();
						  if (tier.istLebendig()&&alter>JAGD_ALTER&&jagdw<(JAGD_WAHRSCHEINLICHKEIT+alter*ALTER_FAKTOR)) {
							  jage(tier);
							  setSatt(getHunger() + TIER_LEBEN);
							  return loc;
						  }
					  }
				  }			
			  }
		  
		  // falls kein Tier gefunden wird, dann gebe null zurueck  
		  return null;
	  }

	  //Jaeger pflanzen sich nicht fort, sie kommen einfach!
	  protected int fortpflanzen() {
		  int alter = getAlter();
		  if(alter>JAGD_ALTER){  //Nur jaeger, die auch schon jagen koennen andere Jaeger ausbilden
			  double gesamtw = (AZUBI_WAHRSCHEINLICHKEIT+alter*ALTER_FAKTOR);
			  double Zufall = Math.random();
			  int Brut;
			  	if (Zufall<gesamtw){
			  		Zufall = Math.random(); //neuer Zufallswert, um Anzahl der Azubis festzustellen
			  		Brut = (int)Math.round(Zufall);
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
	 
	  public void jage(Tier tier){ 
		  tier.wirdGetoetet();
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
  public Jaeger(boolean zufallsAlter)
  {
    if (zufallsAlter) {
      setAlter(random.nextInt(MAX_ALTER));
    } else {
      setAlter(0);
    }
    jagdWahrsch = JAGD_WAHRSCHEINLICHKEIT;
    anzahl++;
    setSatt(25);
  }
  
  /**
   * Erzeuge einen Fuchs, mit zugeordnetem Bild
   */
  public Jaeger(boolean zufallsAlter, Image bild)
  {
    if (zufallsAlter) {
      setAlter(random.nextInt(MAX_ALTER));
    } else {
      setAlter(0);
    }
    anzahl++;
    jagdBild = bild;
    setSatt(20);
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
    return new Jaeger(false, jagdBild);
  }
  
  /**
   * Steht auf dem Feld f auf Position loc ein Tier?
   */
  private boolean stehtTier(Feld feld, Location loc) {
    Object objekt = feld.getObjektAt(loc.getRow(), loc.getCol());
    if (objekt instanceof Hase){
    	return (objekt instanceof Hase);
    }
    else if (objekt instanceof Fuchs){
    	return (objekt instanceof Fuchs);
    }
    else return false;
  } 
  
  /**
   * Zugriff auf den Hasen auf dem Feld f an Position loc,
   * wenn dort auch ein Hase zu finden ist.
   */
  private Tier getTier(Feld feld, Location loc) {
    Object objekt = feld.getObjektAt(loc.getRow(), loc.getCol());
    if (objekt instanceof Hase){
      return (Hase)objekt;}
    else if(objekt instanceof Fuchs){
    	return (Fuchs)objekt;}
    else{
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
    return jagdBild;
  }
  
  /**
   * Zu alt, oder zu wenige Hasen gegessen, der Fuchs muss sterben.
   */
  public void sterbe() {
    super.sterbe();
    Jaeger.anzahl--;
  }
}
