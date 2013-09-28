/**
 * 
 */
package model;

import java.util.ArrayList;

/**
 * @author Thomas Laarmann
 *
 */
public abstract class Feld <T extends Knoten> {
//### KONSTANTEN ####################################################

//### VARIABLEN #####################################################
	protected ArrayList<T> feld = new ArrayList<T>();
	protected int spalten, zeilen;

//### KONSTRUKTOR ###################################################
	public Feld(int spalten, int zeilen) {
		this.spalten = spalten;
		this.zeilen = zeilen;
	}

//### FUNKTIONEN ####################################################
	public abstract void hinzuNachbarAlle(int id);

//### GETTER & SETTER ###############################################
	public T getFeld(int id) {
		return feld.get(id);
	}
	
	public T getFeld(int spalte, int zeile) {
		return feld.get(spalte + (zeile * spalten));
	}
	
	public int getSpalten() {
		return spalten;
	}
	
	public int getZeilen() {
		return feld.size() / spalten;
	}
	
	public int getSize() {
		return feld.size();
	}
	
	public abstract void berechneFFolge(int id, T ziel);
}
