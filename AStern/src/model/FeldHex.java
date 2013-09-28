/**
 * 
 */
package model;

/**
 * @author Thomas Laarmann
 *
 */
public class FeldHex extends Feld<KnotenHex> {

	/**
	 * @param spalten
	 * @param zeilen
	 */
	public FeldHex(int spalten, int zeilen) {
		super(spalten, zeilen);
		//alle Felder initialisieren
		for(int i=0; i<(spalten*zeilen); i++) {
			feld.add( new KnotenHex(i, spalten) );
		}
		//alle Nachbarn verbinden
		for(int i=0; i<(spalten*zeilen); i++) {
			hinzuNachbarAlle( i );
		}
	}

	/* (non-Javadoc)
	 * @see model.FeldT#hinzuNachbarAlle(int)
	 */
	@Override
	public void hinzuNachbarAlle(int id) {
		if(feld.get(id) == null) return;
		
		//aktuelles Feld gehört zu ungerader Spalte
		boolean ungerade = (( id % spalten ) % 2 == 1) ? true : false; 
		
		//alle Felder erstmal mit -1 belegen
		int[] ids = new int[feld.get(0).anzahlNachbarn()];
		for(int i=0; i<feld.get(0).anzahlNachbarn(); i++) ids[i] = -1;
		
		//Position von ID analysieren
		boolean ersteZeile   = ( ((id-(id % spalten))/spalten) == 0 );
		boolean letzteZeile  = ( ((id-(id % spalten))/spalten) == zeilen-1 );
		boolean ersteSpalte  = ( (id % spalten) == 0 );
		boolean letzteSpalte = ( (id % spalten) == spalten-1 );
		
		if( ungerade ) {
			//Feld obenlinks
			ids[KnotenHex.OBENLINKS] = id-1;
			//Feld oben
			if( !ersteZeile ) ids[KnotenHex.OBEN] = id-spalten;
			//Feld obenrechts
			if( !letzteSpalte ) ids[KnotenHex.OBENRECHTS] = id+1;
			//Feld untenlinks
			if( !letzteZeile ) ids[KnotenHex.UNTENLINKS] = id+spalten-1;
			//Feld unten
			if( !letzteZeile ) ids[KnotenHex.UNTEN] = id+spalten;
			//Feld untenrechts
			if( !letzteZeile && !letzteSpalte ) ids[KnotenHex.UNTENRECHTS] = id+spalten+1;
		} else { //gerade
			//Feld obenlinks
			if( !ersteZeile && !ersteSpalte ) ids[KnotenHex.OBENLINKS] = id-spalten-1;
			//Feld oben
			if( !ersteZeile ) ids[KnotenHex.OBEN] = id-spalten;
			//Feld obenrechts
			if( !ersteZeile && !letzteSpalte ) ids[KnotenHex.OBENRECHTS] = id-spalten+1;
			//Feld untenlinks
			if( !ersteSpalte ) ids[KnotenHex.UNTENLINKS] = id-1;
			//Feld unten
			if( !letzteZeile ) ids[KnotenHex.UNTEN] = id+spalten;
			//Feld untenrechts
			if( !letzteSpalte ) ids[KnotenHex.UNTENRECHTS] = id+1;
		}
		
		//und abschließend Nachbarn setzen
		for(int i=0; i<ids.length; i++) {
			if( ids[i] < 0 || ids[i] >= feld.size() ) continue;
			feld.get(id).setNachbar( i, feld.get(ids[i]) );
			feld.get(ids[i]).setNachbar( feld.get(id).gegenrichtungZu(i), feld.get(id) );
		}
	}

	/* (non-Javadoc)
	 * @see model.FeldT#berechneFFolge(int, model.Knoten)
	 */
	@Override
	public void berechneFFolge(int id, KnotenHex ziel) {
		for(int i=0; i<feld.get(id).anzahlNachbarn(); i++) {
			Knoten v = feld.get(id).getNachbar(i);
			if( v == null ) continue;
			v.berechneG( feld.get(id) );
			v.berechneH( ziel );
			v.berechneF();
		}
	}
	
//### OVERRIDES: Object #############################################
	public String toString() {
		String tmp = "";
		
		for(Knoten v : feld) {
			tmp += "Feld(" + v.getId() + ")=[";
			tmp += "LO(" + ((v.getNachbar(KnotenHex.OBENLINKS) == null)   ? "null" : v.getNachbar(KnotenHex.OBENLINKS).getId() )   +"), ";
			tmp += " O(" + ((v.getNachbar(KnotenHex.OBEN) == null)        ? "null" : v.getNachbar(KnotenHex.OBEN).getId() )        +"), ";
			tmp += "RO(" + ((v.getNachbar(KnotenHex.OBENRECHTS) == null)  ? "null" : v.getNachbar(KnotenHex.OBENRECHTS).getId() )  +"), ";
			tmp += "LU(" + ((v.getNachbar(KnotenHex.UNTENLINKS) == null)  ? "null" : v.getNachbar(KnotenHex.UNTENLINKS).getId() )  +"), ";
			tmp += " U(" + ((v.getNachbar(KnotenHex.UNTEN) == null)       ? "null" : v.getNachbar(KnotenHex.UNTEN).getId() )       +"), ";
			tmp += "RU(" + ((v.getNachbar(KnotenHex.UNTENRECHTS) == null) ? "null" : v.getNachbar(KnotenHex.UNTENRECHTS).getId() ) +")";
			tmp += "]\n";
		}
		
		return tmp;
	}
}
