import java.util.HashMap;

/*
 * Created on 17.01.2005
 */

/**
 * Die Schnittstelle Lebewesen definiert Gemeinsamkeiten von Tieren und anderen
 * Lebewesen.
 *
 * @author Lesske
 */
interface Lebewesen  {

  public void run(Feld altesFeld, Feld neuesFeld, HashMap neueTiere) ;
  
  public boolean istLebendig();
  
}
