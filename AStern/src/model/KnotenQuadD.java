/**
 * 
 */
package model;

/**
 * @author Thomas Laarmann
 *
 */
public class KnotenQuadD extends Knoten {
//### KONSTANTEN ####################################################
	public static final int LINKS       = 0;
	public static final int LINKSOBEN   = 1;
	public static final int OBEN        = 2;
	public static final int RECHTSOBEN  = 3;
	public static final int LINKSUNTEN  = 4;
	public static final int UNTEN       = 5;
	public static final int RECHTSUNTEN = 6;
	public static final int RECHTS      = 7;

	/**
	 * @param id
	 * @param spalten
	 */
	public KnotenQuadD(int id, int spalten) {
		super(id, spalten, 8);
	}
}
