/**
 * 
 */
package model;

/**
 * @author Thomas Laarmann
 *
 */
public class FeldQuad extends Feld<KnotenQuad> {

	/**
	 * @param spalten
	 * @param zeilen
	 */
	public FeldQuad(int spalten, int zeilen) {
		super(spalten, zeilen);
		//alle Felder initialisieren
		for(int i=0; i<(spalten*zeilen); i++) {
			feld.add( new KnotenQuad(i, spalten) );
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
		
		//alle Felder erstmal mit -1 belegen
		int[] ids = new int[feld.get(0).anzahlNachbarn()];
		for(int i=0; i<feld.get(0).anzahlNachbarn(); i++) ids[i] = -1;
		
		//Position von ID analysieren
		boolean ersteZeile   = ( ((id-(id % spalten))/spalten) == 0 );
		boolean letzteZeile  = ( ((id-(id % spalten))/spalten) == zeilen-1 );
		boolean ersteSpalte  = ( (id % spalten) == 0 );
		boolean letzteSpalte = ( (id % spalten) == spalten-1 );

		//Feld links
		if( !ersteSpalte )  ids[KnotenQuad.LINKS]  = id-1;
		//Feld oben
		if( !ersteZeile )   ids[KnotenQuad.OBEN]   = id-spalten;
		//Feld rechts
		if( !letzteSpalte ) ids[KnotenQuad.RECHTS] = id+1;
		//Feld unten
		if( !letzteZeile )  ids[KnotenQuad.UNTEN]  = id+spalten;
		
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
	public void berechneFFolge(int id, KnotenQuad ziel) {
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
			tmp += "L(" + ((v.getNachbar(KnotenQuad.LINKS) == null)  ? "null" : v.getNachbar(KnotenQuad.LINKS).getId() )  +"), ";
			tmp += "O(" + ((v.getNachbar(KnotenQuad.OBEN) == null)   ? "null" : v.getNachbar(KnotenQuad.OBEN).getId() )   +"), ";
			tmp += "R(" + ((v.getNachbar(KnotenQuad.RECHTS) == null) ? "null" : v.getNachbar(KnotenQuad.RECHTS).getId() ) +"), ";
			tmp += "U(" + ((v.getNachbar(KnotenQuad.UNTEN) == null)  ? "null" : v.getNachbar(KnotenQuad.UNTEN).getId() )  +") ";
			tmp += "]\n";
		}
		
		return tmp;
	}
}
