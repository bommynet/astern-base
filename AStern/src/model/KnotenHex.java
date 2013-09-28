/**
 * 
 */
package model;

/**
 * @author Thomas Laarmann
 *
 */
public class KnotenHex extends Knoten {
//### KONSTANTEN ####################################################
	public static final int OBENLINKS   = 0;
	public static final int OBEN        = 1;
	public static final int OBENRECHTS  = 2;
	public static final int UNTENLINKS  = 3;
	public static final int UNTEN       = 4;
	public static final int UNTENRECHTS = 5;

	/**
	 * @param id
	 * @param spalten
	 */
	public KnotenHex(int id, int spalten) {
		super(id, spalten, 6);
	}
}
