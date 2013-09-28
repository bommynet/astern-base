/**
 * 
 */
package model;

/**
 * @author Thomas Laarmann
 *
 */
public class KnotenQuad extends Knoten {
//### KONSTANTEN ####################################################
	public static final int LINKS  = 0;
	public static final int OBEN   = 1;
	public static final int UNTEN  = 2;
	public static final int RECHTS = 3;

	/**
	 * @param id
	 * @param spalten
	 */
	public KnotenQuad(int id, int spalten) {
		super(id, spalten, 4);
	}
}
