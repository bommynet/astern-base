/**
 * 
 */
package model;

import java.util.ArrayList;

import basis.ISucheAStern;

/**
 * @author Thomas Laarmann
 *
 */
public abstract class Knoten implements ISucheAStern<Knoten> {
//### KONSTANTEN ####################################################
	protected int MAXNACHBARN;

//### VARIABLEN #####################################################
	protected int id;
	protected ArrayList<Knoten> nachbar = new ArrayList<Knoten>();
	protected int vorhanden = 0;
	private int x, y;
	private double wertG = 0;
	private double wertH = 0;
	private double wertF = 0;
	
	private Knoten vorgaenger = null;

//### KONSTRUKTOR ###################################################
	/**
	 * @param id
	 * @param spalten maximale Anzahl an Spalten im Feld
	 */
	public Knoten(int id, int spalten, int maxNachbarn) {
		//Knoten-ID
		this.id = id;
		//Position im Feld merken
		this.x = id % spalten;
		this.y = (id - x) / spalten;
		//Anzahl der maximalen Nachbarn des Knotens
		this.MAXNACHBARN = maxNachbarn;
		//ein Viereck hat maximal 4 Nachbarn, also erstmal als nuller erstellen.
		for(int i=0; i<MAXNACHBARN; i++)
			this.nachbar.add( null );
	}

//### FUNKTIONEN ####################################################
	/**
	 * Berechnet die Gegenrichtung zum Nachbarindex für die Funktion zum
	 * Entfernen von Nachbarschaftsbeziehungen.
	 */
	public int gegenrichtungZu(int richtung) {
		return ( anzahlNachbarn()-1 ) - richtung;
	}

//### GETTER & SETTER ###############################################
	/**
	 * Gibt die x-Position des Vierecks im Feld aus.
	 * @return
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * Gibt die y-Position des Vierecks im Feld aus.
	 * @return
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * Gibt die ID des Vierecks aus.
	 * @return
	 */
	public int getId() {
		return this.id;
	}

//### OVERRIDES: Knoten<T> ##########################################

	public void entferneNachbar(int index) {
		//wenn die Verbindung schon weg ist, brauch sie nicht gelöscht werden
		if( this.nachbar.get(index) == null ) return;
		//gegenüberliegenden Knoten ermitteln 
		int gegenrichtung = gegenrichtungZu( index );
		//Verbindung von diesem Knoten löschen
		Knoten tmp = this.nachbar.get(index);
		this.nachbar.set(index, null);
		//Verbindung von gegenüberliegenden Knoten löschen
		tmp.entferneNachbar(gegenrichtung);
		//vorhandene Nachbarn zählen
		int val = 0;
		for(int i=0; i<MAXNACHBARN; i++)
			if( this.nachbar.get(i) != null ) val++;
		this.vorhanden = val;
	}

	public void entferneNachbarAlle() {
		for(int i=0; i<MAXNACHBARN; i++)
			entferneNachbar( i );
	}

	public int anzahlNachbarn() {
		return MAXNACHBARN;
	}

	public int anzahlNachbarnVorhanden() {
		return vorhanden;
	}

	public Knoten getNachbar(int index) {
		return nachbar.get(index);
	}

	public void setNachbar(int index, Knoten k) {
		nachbar.set(index, k);
		//vorhandene Nachbarn zählen
		int val = 0;
		for(int i=0; i<MAXNACHBARN; i++)
			if( this.nachbar.get(i) != null ) val++;
		this.vorhanden = val;
	}

//### OVERRIDES: ISucheAStern #######################################
	/* (non-Javadoc)
	 * @see basis.ISucheAStern#berechneG()
	 */
	@Override
	public double berechneG(Knoten herkunft) {
		return herkunft.getG() + 1;
	}

	/* (non-Javadoc)
	 * @see basis.ISucheAStern#berechneH(basis.Knoten)
	 */
	@Override
	public double berechneH(Knoten ziel) {
		//Seiten a und b für den Pythagoras berechnen
		int a = ziel.getX() - this.getX();
		int b = ziel.getY() - this.getY();
		// a² und b² (nur zum Debuggen separat)
		double a2 = a * a;
		double b2 = b * b;
		//Luftlinie zum Ziel ist wurzel(a² + b²)
		double c = Math.sqrt( a2 + b2 );
		//und speichern :)
		return c;
	}

	/* (non-Javadoc)
	 * @see basis.ISucheAStern#berechneF()
	 */
	@Override
	public double berechneF() {
		// F = G() + H()
		return this.wertG + this.wertH;
	}

	/* (non-Javadoc)
	 * @see basis.ISucheAStern#getG()
	 */
	@Override
	public double getG() {
		return this.wertG;
	}

	/* (non-Javadoc)
	 * @see basis.ISucheAStern#getH()
	 */
	@Override
	public double getH() {
		return this.wertH;
	}

	/* (non-Javadoc)
	 * @see basis.ISucheAStern#getF()
	 */
	@Override
	public double getF() {
		return this.wertF;
	}

	/* (non-Javadoc)
	 * @see basis.ISucheAStern#setG(double)
	 */
	@Override
	public void setG(double wert) {
		this.wertG = wert;
	}

	/* (non-Javadoc)
	 * @see basis.ISucheAStern#setH(double)
	 */
	@Override
	public void setH(double wert) {
		this.wertH = wert;
	}

	/* (non-Javadoc)
	 * @see basis.ISucheAStern#setF(double)
	 */
	@Override
	public void setF(double wert) {
		this.wertF = wert;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Knoten other) {
		if( this.getF() > other.getF() )
			return 1;
		else if( this.getF() < other.getF() )
				return -1;
		return 0;
	}

	/* (non-Javadoc)
	 * @see basis.ISucheAStern#getVorgaenger()
	 */
	@Override
	public Knoten getVorgaenger() {
		return vorgaenger;
	}

	/* (non-Javadoc)
	 * @see basis.ISucheAStern#setVorgaenger(java.lang.Object)
	 */
	@Override
	public void setVorgaenger(Knoten knoten) {
		vorgaenger = knoten;
	}
	
	@Override
	public String toString() {
		String tmp = "";
		
		tmp += this.getClass().getCanonicalName() + "\n";
		for(Knoten n : nachbar) {
			tmp += this.id + " => " + ((n == null ) ? "null" : n.getId()) + "\n";
		}
		tmp += "Vorgänger: " + ((vorgaenger == null) ? "null" : vorgaenger.getId()) + "\n";
		
		return tmp;
	}
}
