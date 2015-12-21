
/**
 * Die Klasse der Pflanzen. Eine Unterklasse davon ist z.B. der Kohl.
 * Jede Pflanze hat eine Position
 * 
 * @author Lesske
 */
public abstract class Pflanze extends PositionsObjekt {
  
  // ist die Pflanze noch vorhanden?
  private boolean gefressen = false;

  // das Alter der Pflanze
  private int alter = 0;

  /*
   * wächst die Pflanze noch?
   */
  public boolean waechstNoch() {
    return !istWelk() && !gefressen;
  }

  /*
   * Der Vorgang des Aufgefressenwerdens
   */
  public void wirdGefressen() {
    gefressen = true;
  }

  /*
   * Jede Pflanzenart muss selbst entscheiden wann sie welk ist.
   */
  protected abstract boolean istWelk();

  /*
   * Eine Pflanze altert.
   */
  public void erhoeheAlter() {
    alter++;
  }

  /*
   * Das Alter der Pflanze
   */
  public int getAlter() {
    return alter;
  }

  /*
   * Das Blühen einer Pflanze. Wenn eine Pfalnze noch blüht, dann wird sie in
   * einem Simulationsschritt vom alten Feld auf das neue Feld übernommen.
   * Das Blühen einer Pflanze beeinträchtigt die Qualität der Erde,
   */
  public void bluehe(Feld altesFeld, Feld neuesFeld) {
    erhoeheAlter();
    if (waechstNoch()) {
      Location pos = getLocation();
      neuesFeld.setSoil(altesFeld.getSoil(pos)-1,pos);
      neuesFeld.platziere(this, pos.getRow(), pos.getCol());
    }
  }

}
