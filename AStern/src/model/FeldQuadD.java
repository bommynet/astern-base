/**
 * 
 */
package model;

/**
 * @author Thomas Laarmann
 *
 */
public class FeldQuadD extends Feld<KnotenQuadD> {

	/**
	 * @param spalten
	 * @param zeilen
	 */
	public FeldQuadD(int spalten, int zeilen) {
		super(spalten, zeilen);
		//alle Felder initialisieren
		for(int i=0; i<(spalten*zeilen); i++) {
			feld.add( new KnotenQuadD(i, spalten) );
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
		if( !ersteSpalte )  ids[KnotenQuadD.LINKS]  = id-1;
		//Feld oben
		if( !ersteZeile )   ids[KnotenQuadD.OBEN]   = id-spalten;
		//Feld rechts
		if( !letzteSpalte ) ids[KnotenQuadD.RECHTS] = id+1;
		//Feld unten
		if( !letzteZeile )  ids[KnotenQuadD.UNTEN]  = id+spalten;
		//Feld linksoben
		if( !ersteSpalte && !ersteZeile )  ids[KnotenQuadD.LINKSOBEN]  = id-spalten-1;
		//Feld rechtsoben
		if( !letzteSpalte && !ersteZeile )   ids[KnotenQuadD.RECHTSOBEN]   = id-spalten+1;
		//Feld rechtsunten
		if( !letzteSpalte && !letzteZeile ) ids[KnotenQuadD.RECHTSUNTEN] = id+spalten+1;
		//Feld linksunten
		if( !ersteSpalte && !letzteZeile )  ids[KnotenQuadD.LINKSUNTEN]  = id+spalten-1;
		
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
	public void berechneFFolge(int id, KnotenQuadD ziel) {
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
			tmp += "L(" + ((v.getNachbar(KnotenQuadD.LINKS) == null)  ? "null" : v.getNachbar(KnotenQuadD.LINKS).getId() )  +"), ";
			tmp += "O(" + ((v.getNachbar(KnotenQuadD.OBEN) == null)   ? "null" : v.getNachbar(KnotenQuadD.OBEN).getId() )   +"), ";
			tmp += "R(" + ((v.getNachbar(KnotenQuadD.RECHTS) == null) ? "null" : v.getNachbar(KnotenQuadD.RECHTS).getId() ) +"), ";
			tmp += "U(" + ((v.getNachbar(KnotenQuadD.UNTEN) == null)  ? "null" : v.getNachbar(KnotenQuadD.UNTEN).getId() )  +") ";
			tmp += "]\n";
		}
		
		return tmp;
	}
}
