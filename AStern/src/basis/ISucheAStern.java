/**
 * 
 */
package basis;

import java.util.ArrayList;

/**
 * @author Thomas Laarmann
 * @version
 */
public interface ISucheAStern <T> extends Comparable<T> {
	/**
	 * Berechnet den bisher zurückgelegten Weg zwischen
	 * dem Startknoten und dem aktuellen Knoten.
	 * @return
	 */
	public double berechneG(T herkunft);
	
	/**
	 * Berechnet den Restweg zwischen dem aktuellen Knoten
	 * und dem Ziel als Schätzung.
	 * @param ziel
	 * @return
	 */
	public double berechneH(T ziel);
	
	/**
	 * Berechnet den Wert des Knotens. (nur der Vollständig-
	 * keit halber implementiert)
	 */
	public double berechneF();
	
	/** Liefert den Wert von G ohne Neuberechnung */
	public double getG();
	/** Liefert den Wert von H ohne Neuberechnung */
	public double getH();
	/** Liefert den Wert von F ohne Neuberechnung */
	public double getF();
	/** Setzt den Wert von G */
	public void setG( double wert );
	/** Setzt den Wert von H */
	public void setH( double wert );
	/** Setzt den Wert von F */
	public void setF( double wert );
	/** Liefert die ID des Knotens */
	public int getId();
	
	/** Vorgänger des Knotens in der Suche */
	public T getVorgaenger();
	/** Vorgänger des Knotens in der Suche */
	public void setVorgaenger(T knoten);
}
