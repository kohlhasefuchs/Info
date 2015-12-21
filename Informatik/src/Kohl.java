import java.awt.Color;
import java.awt.Image;

/**
 * Die Klasse der Kohl(köpfe). Jeder Kohl ist eine Pflanze.
 * 
 * @author Lesske
 */
public class Kohl extends Pflanze 
{
  //  maximale Lebensdauer für Kohl
  static int MAX_ALTER = 10;
  
  // das anzuzeigende Bild
  private Image kohlBild;
  
  /**
   * Konstruktor
   */
  public Kohl(Image image) {
    kohlBild = image;
  }
  
  /**
   * Welche Farbe hat Kohl?
   */
  public Color getColor() {
    return Color.green;
  }
  
  /**
   * Wann verwelkt Kohl?
   */
  public boolean istWelk() {
    return getAlter() > MAX_ALTER;
  }
  
  /**
   * Das Kohl-Bild
   */
  public Image getImage() {
    return kohlBild;
  }
}
