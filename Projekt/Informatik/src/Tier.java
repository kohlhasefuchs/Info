<<<<<<< HEAD
import java.awt.Color;
import java.util.HashMap;


/**
 * Die allgemeine Klasse der Tiere.
 * 
 * @author Frank Leßke / C. Brand
 */
public abstract class Tier extends PositionsObjekt implements Lebewesen {
  
  /**
   * Diese Methode bewegt ein Tier innerhalb eines Simulationsschrittes.
   * Dabei altert das Tier auch und es bekommt mehr Hunger.
   * Wenn es etwas zum Essen findet, dann kann es essen.
   * Wenn möglich, dann pflanzen sich die Tier noch um einen zufälligen
   * Faktor fort.
   */
	public void run(Feld altesFeld, Feld neuesFeld, HashMap neueTiere) {
		erhoeheAlter();
		erhoeheHunger();
		if (istLebendig()){
			
		}
		
		// TODO: fehlende Implementierung ergänzen (siehe Aufgabe 2b)
		
	}

  
  /****************************************
   * Ab hier muss der Programmcode nicht mehr ver�ndert werden.
   ********/
  
  /**
   * Instanzvariablen
   */
  
  private int alter;

  private int satt;

  private boolean lebendig = true;
  
  private Location neuePosition;


  /**
   * Konstruktor für Objekte der Klasse Tier
   */
  public Tier() {

  }
  
  /**
   * Erzeuge ein neues Tier und füge es zur Menge neueTiere hinzu.
   */
  private void erzeugeTier(HashMap neueTiere) {
    Tier kind = geburt();
    neueTiere.put(kind, this);
  }
  
  /**
   * Platziere das Tier auf dem Feld auf der bisherigen Position.
   */
  private void bleibeStehen(Feld neuesFeld) {
    neuesFeld.platziere(this, getLocation().getRow(), getLocation().getCol());
  }
  
  /**
   * Suche, ob es in der Nachbarschaft etwas zum Essen gibt.
   * Wenn ja, dann verspeise das und setze intern die Position als 
   * neue Position.
   */
  private void sucheFressen(Feld feld) {
    neuePosition = findeFressen(feld);
  }
  
  /**
   * Wurde bei der letzten Suche nach Fressen etwas gefunden?
   */
  private boolean fressenGefunden() {
    return neuePosition != null;
  }
  
  /**
   * Bewege das Tier auf dem Feld neuesFeld zu der Position, wo das letzte Essen gefunden wurde.
   */
  private void geheZumFressen(Feld feld) {
    setLocation(neuePosition);
    feld.platziere(this, neuePosition.getRow(), neuePosition
        .getCol());
  }
  
  /**
   * Gehe auf dem Feld feld zum nächsten freien Nachbarschaftsfeld
   * von der Position loc. Falls das nicht möglich ist, und auch der
   * eigen Platz im Feld feld schon besetzt ist, so muss das Tier sterben.
   */
  private void geheNaechstesFreiesFeld(Feld feld, Location loc) {
	  neuePosition = feld.freeAdjacentLocation(loc);
	  if (neuePosition != null) {
		  setLocation(neuePosition);
		  feld.platziere(this, neuePosition.getRow(), neuePosition.getCol());
	  } else {
		  // kein Platz mehr auf dieser Welt
		  sterbe();
	  }
  }

  /**
   * Verändere das Alter.
   */
  protected void setAlter(int einAlter) {
    alter = einAlter;
  }

  /**
   * Welches Alter hat das Tier?
   */
  protected int getAlter() {
    return alter;
  }

  /**
   * Verändere das Sättigkeitsgefähl (damit den Hunger)
   */
  protected void setSatt(int n) {
    satt = n;
  }

  /**
   * Wie satt ist das Tier (wenig satt bedeutet großer Hunger)?
   */
  protected int getHunger() {
    return satt;
  }
  
  
  /**
   * Jede Tierart pflanzt sich auf seine eigene Weise fort.
   */
  protected abstract int fortpflanzen();

  /**
   * Jede Tierart hat Ihre eigene Methode zu gebären.
   */
  protected abstract Tier geburt();

  /**
   * Jede Tierart sucht auf ihre eigene Weise nach Essbarem.
   */
  protected abstract Location findeFressen(Feld feld);

  /**
   * Wenn das Tier noch lebt, so erhöht sich der Hunger.
   * Ist er zu hoch so stirbt das Tier.
   */
  protected void erhoeheHunger() {
    if (istLebendig()) {
      satt--;
      if (satt <= 0) {
        sterbe();
      }
    }
  }

  /**
   * Erhöhe das Alter. Falls das maximale Alter erreicht ist, 
   * stirbt das Tier.
   */
  protected void erhoeheAlter() {
    if (istLebendig()) {
      alter++;
      if (zuAlt(alter)) {
        sterbe();
      }
    }
  }

  /**
   * Wann ein Tier zu alt ist, hängt von der Art des Tieres ab.
   */
  protected abstract boolean zuAlt(int einAlter);

  /**
   * Standardfarbe für die Anzeige von Tieren.
   */
  public Color getColor() {
    return Color.darkGray;
  }

  /**
   * Lebt das Tier noch?
   */
  public boolean istLebendig() {
    return lebendig;
  }

  /**
   * Verändere den Lebendig-Status des Tieres
   */
  protected boolean setLebendig(boolean l) {
    return lebendig = l;
  }

  /**
   * Ein Tier stirbt.
   */
  protected void sterbe() {
    lebendig = false;
  }
  
  /**
   * Überprüfe, ob die i-te Nachbarposition des Tieres noch
   * innerhalb der Feldgrenzen liegt.
   */
  protected boolean liegtAufFeld(Feld feld, int i) {
    return feld.nachbarImFeld(this.getLocation(), i);
  }
  
  /**
   * Gebe die Position des i-ten Nachbarn als Objekt vom Typ
   * Location zurück. Hier wird nicht überprüft, ob diese
   * Position noch innerhalb des Feldes liegt.
   */
  protected Location getNachbarPosition(Feld f, int i) {
    int zeile = this.getLocation().getRow();
    int spalte = this.getLocation().getCol();
    switch (i) {
    case 0 : zeile--; break;
    case 1 : zeile--; spalte++; break;
    case 2 : spalte++; break;
    case 3 : spalte++; zeile++; break;
    case 4 : zeile++; break;
    case 5 : zeile++; spalte--; break;
    case 6 : spalte--; break;
    case 7 : spalte--; zeile--; break;
    }
    return new Location(zeile, spalte);
  }

}
=======
import java.awt.Color;
import java.util.HashMap;


/**
 * Die allgemeine Klasse der Tiere.
 * 
 * @author Frank Leßke / C. Brand
 */
public abstract class Tier extends PositionsObjekt implements Lebewesen {
  
  /**
   * Diese Methode bewegt ein Tier innerhalb eines Simulationsschrittes.
   * Dabei altert das Tier auch und es bekommt mehr Hunger.
   * Wenn es etwas zum Essen findet, dann kann es essen.
   * Wenn möglich, dann pflanzen sich die Tier noch um einen zufälligen
   * Faktor fort.
   */
	public void run(Feld altesFeld, Feld neuesFeld, HashMap neueTiere) {
		//2b
		erhoeheAlter();
		erhoeheHunger();
		if (istLebendig()){
				sucheFressen(altesFeld);
				if (fressenGefunden()){
					geheZumFressen(neuesFeld);
				}
				else {
					geheNaechstesFreiesFeld(neuesFeld, getLocation());
				}
			
		}

		// TODO: fehlende Implementierung ergänzen (siehe Aufgabe 2b)
		
	}

  
  /****************************************
   * Ab hier muss der Programmcode nicht mehr ver�ndert werden.
   ********/
  
  /**
   * Instanzvariablen
   */
  
  private int alter;

  private int satt;

  private boolean lebendig = true;
  
  private Location neuePosition;


  /**
   * Konstruktor für Objekte der Klasse Tier
   */
  public Tier() {

  }
  
  /**
   * Erzeuge ein neues Tier und füge es zur Menge neueTiere hinzu.
   */
  private void erzeugeTier(HashMap neueTiere) {
    Tier kind = geburt();
    neueTiere.put(kind, this);
  }
  
  /**
   * Platziere das Tier auf dem Feld auf der bisherigen Position.
   */
  private void bleibeStehen(Feld neuesFeld) {
    neuesFeld.platziere(this, getLocation().getRow(), getLocation().getCol());
  }
  
  /**
   * Suche, ob es in der Nachbarschaft etwas zum Essen gibt.
   * Wenn ja, dann verspeise das und setze intern die Position als 
   * neue Position.
   */
  private void sucheFressen(Feld feld) {
    neuePosition = findeFressen(feld);
  }
  
  /**
   * Wurde bei der letzten Suche nach Fressen etwas gefunden?
   */
  private boolean fressenGefunden() {
    return neuePosition != null;
  }
  
  /**
   * Bewege das Tier auf dem Feld neuesFeld zu der Position, wo das letzte Essen gefunden wurde.
   */
  private void geheZumFressen(Feld feld) {
    setLocation(neuePosition);
    feld.platziere(this, neuePosition.getRow(), neuePosition
        .getCol());
  }
  
  /**
   * Gehe auf dem Feld feld zum nächsten freien Nachbarschaftsfeld
   * von der Position loc. Falls das nicht möglich ist, und auch der
   * eigen Platz im Feld feld schon besetzt ist, so muss das Tier sterben.
   */
  private void geheNaechstesFreiesFeld(Feld feld, Location loc) {
	  neuePosition = feld.freeAdjacentLocation(loc);
	  if (neuePosition != null) {
		  setLocation(neuePosition);
		  feld.platziere(this, neuePosition.getRow(), neuePosition.getCol());
	  } else {
		  // kein Platz mehr auf dieser Welt
		  sterbe();
	  }
  }

  /**
   * Verändere das Alter.
   */
  protected void setAlter(int einAlter) {
    alter = einAlter;
  }

  /**
   * Welches Alter hat das Tier?
   */
  protected int getAlter() {
    return alter;
  }

  /**
   * Verändere das Sättigkeitsgefähl (damit den Hunger)
   */
  protected void setSatt(int n) {
    satt = n;
  }

  /**
   * Wie satt ist das Tier (wenig satt bedeutet großer Hunger)?
   */
  protected int getHunger() {
    return satt;
  }
  
  
  /**
   * Jede Tierart pflanzt sich auf seine eigene Weise fort.
   */
  protected abstract int fortpflanzen();

  /**
   * Jede Tierart hat Ihre eigene Methode zu gebären.
   */
  protected abstract Tier geburt();

  /**
   * Jede Tierart sucht auf ihre eigene Weise nach Essbarem.
   */
  protected abstract Location findeFressen(Feld feld);

  /**
   * Wenn das Tier noch lebt, so erhöht sich der Hunger.
   * Ist er zu hoch so stirbt das Tier.
   */
  protected void erhoeheHunger() {
    if (istLebendig()) {
      satt--;
      if (satt <= 0) {
        sterbe();
      }
    }
  }

  /**
   * Erhöhe das Alter. Falls das maximale Alter erreicht ist, 
   * stirbt das Tier.
   */
  protected void erhoeheAlter() {
    if (istLebendig()) {
      alter++;
      if (zuAlt(alter)) {
        sterbe();
      }
    }
  }

  /**
   * Wann ein Tier zu alt ist, hängt von der Art des Tieres ab.
   */
  protected abstract boolean zuAlt(int einAlter);

  /**
   * Standardfarbe für die Anzeige von Tieren.
   */
  public Color getColor() {
    return Color.darkGray;
  }

  /**
   * Lebt das Tier noch?
   */
  public boolean istLebendig() {
    return lebendig;
  }

  /**
   * Verändere den Lebendig-Status des Tieres
   */
  protected boolean setLebendig(boolean l) {
    return lebendig = l;
  }

  /**
   * Ein Tier stirbt.
   */
  protected void sterbe() {
    lebendig = false;
  }
  
  /**
   * Überprüfe, ob die i-te Nachbarposition des Tieres noch
   * innerhalb der Feldgrenzen liegt.
   */
  protected boolean liegtAufFeld(Feld feld, int i) {
    return feld.nachbarImFeld(this.getLocation(), i);
  }
  
  /**
   * Gebe die Position des i-ten Nachbarn als Objekt vom Typ
   * Location zurück. Hier wird nicht überprüft, ob diese
   * Position noch innerhalb des Feldes liegt.
   */
  protected Location getNachbarPosition(Feld f, int i) {
    int zeile = this.getLocation().getRow();
    int spalte = this.getLocation().getCol();
    switch (i) {
    case 0 : zeile--; break;
    case 1 : zeile--; spalte++; break;
    case 2 : spalte++; break;
    case 3 : spalte++; zeile++; break;
    case 4 : zeile++; break;
    case 5 : zeile++; spalte--; break;
    case 6 : spalte--; break;
    case 7 : spalte--; zeile--; break;
    }
    return new Location(zeile, spalte);
  }

}
>>>>>>> origin/master
