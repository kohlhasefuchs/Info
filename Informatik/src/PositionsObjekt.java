import java.awt.Color;
import java.awt.Image;

/*
 * Created on 17.01.2005
 */

/**
 * Die Klasse Positionsobjekte ist eine Oberklasse für alle Elemente,
 * die auf dem Feld positioniert werden können.
 * @author Frank Leßke
 *
 */
public abstract class PositionsObjekt {
  
  private Location position;

  public void setLocation(int zeile, int spalte) {
    position = new Location(zeile,spalte);
  }
  
  public void setLocation(Location pos){
    position = pos;
  }
  
  public Location getLocation() {
    return position;
  }
  
  public Color getColor() {
    return Color.gray;
  }
  
  public Image getImage() {
    return SimulatorAnzeige.StdImage;
  }
}
