
/**
 * Repräsentiert eine Position in einem rechteckigen Gitter
 * 
 * @author Frank Leßke
 */
public class Location
{
    // Position in der Zeile und Spalte
    private int zeile;
    private int spalte;

    /**
     * Erzeuge eine neue Position.
     * @param zeile Die Zeile.
     * @param spalte Die Spalte.
     */
    public Location(int zeile, int spalte)
    {
        this.zeile = zeile;
        this.spalte = spalte;
    }
    
    /**
     * Implementiert die Gleichheitsrelation.
     */
    public boolean equals(Object obj)
    {
        if(obj instanceof Location) {
            Location other = (Location) obj;
            return zeile == other.getRow() && spalte == other.getCol();
        }
        else {
            return false;
        }
    }
    
    /**
     * Erzeugt eine textuelle Darstellung der Position
     * @return Ein Text bestehend aus: Zeile,Spalte
     */
    public String toString()
    {
        return zeile + "," + spalte;
    }
    
    /**
     * Zur Erzeugung eines Hashwertes werden die ersten 16 Bit für den
     * Zeilenwert verwendet und der Rest für den Spaltenwert.
     * Wenn das Gitter nicht zu groß ist, sollte dies ein eindeutigen
     * Hashwert für jedes (zeile,spalte)-Paar ergeben.
     */
    public int hashCode()
    {
        return (zeile << 16) + spalte;
    }
    
    /**
     * @return Aktuelle Zeile
     */
    public int getRow()
    {
        return zeile;
    }
    
    /**
     * @return Aktuelle Spalte.
     */
    public int getCol()
    {
        return spalte;
    }
}