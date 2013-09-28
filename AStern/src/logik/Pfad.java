/**
 * 
 */
package logik;

import java.util.ArrayList;
import java.util.PriorityQueue;

import model.Knoten;

/**
 * @author Thomas Laarmann
 *
 */
public class Pfad <T extends Knoten> {
//### KONSTANTEN ####################################################

//### VARIABLEN #####################################################
	private ArrayList<T> closed;
	private PriorityQueue<T> open;

//### KONSTRUKTOR ###################################################
	public Pfad() {
		
	}

//### FUNKTIONEN ####################################################
	/* PSEUDO-CODE
	program a-star
	    // Initialisierung der Open List, die Closed List ist noch leer
	    // (die Priorität bzw. der f Wert des Startknotens ist unerheblich)
	    openlist.enqueue(startknoten, 0)
	    // diese Schleife wird durchlaufen bis entweder
	    // - die optimale Lösung gefunden wurde oder
	    // - feststeht, dass keine Lösung existiert
	    repeat
	        // Knoten mit dem geringsten f Wert aus der Open List entfernen
	        currentNode := openlist.removeMin()
	        // Wurde das Ziel gefunden?
	        if currentNode == zielknoten then
	            return PathFound
	        // Der aktuelle Knoten soll durch nachfolgende Funktionen
	        // nicht weiter untersucht werden damit keine Zyklen entstehen
	        closedlist.add(currentNode)
	        // Wenn das Ziel noch nicht gefunden wurde: Nachfolgeknoten
	        // des aktuellen Knotens auf die Open List setzen
	        expandNode(currentNode)
	    until openlist.isEmpty()
	    // die Open List ist leer, es existiert kein Pfad zum Ziel
	    return NoPathFound
	end
	*/
	/**
	 * Berechnet den Weg rückwärts!
	 * @param start
	 * @param ziel
	 * @return
	 */
	public T berechneWeg(T start, T ziel) {
		//open- & closed-Liste initialisieren
		closed = new ArrayList<T>();
		open = new PriorityQueue<T>();
		//Startknoten in open einfügen
		open.add( ziel );
		//und auf geht die Suche
		while( !open.isEmpty() ) {
			//ersten Eintrag aus open lesen
			T aktuell = open.remove();
			//ist der Knoten == ziel?
			//dann geb ihn zurück an den Aufrufer. durch den Vorgänger kann
			//rekursiv der Weg zurück verfolgt werden
			if( aktuell.getId() == start.getId() )
				return aktuell;
			//damit ein Knoten nicht mehrfach analysiert wird, lege ihn in die
			//closed-Liste (Schleifen verhindern)
			closed.add( aktuell );
			//und alle Nachbarn berechnen / zur open-List hinzufügen
			analysiereKnoten( aktuell, start );
		}
		
		//kein Weg gefunden
		return null;
	}
	
	/*PSEUDO-CODE
	// überprüft alle Nachfolgeknoten und fügt sie der Open List hinzu, wenn entweder
	// - der Nachfolgeknoten zum ersten Mal gefunden wird oder
	// - ein besserer Weg zu diesem Knoten gefunden wird
	function expandNode(currentNode)
	    foreach successor of currentNode
	        // wenn der Nachfolgeknoten bereits auf der Closed List ist - tue nichts
	        if closedlist.contains(successor) then
	            continue
	        // g Wert für den neuen Weg berechnen: g Wert des Vorgängers plus
	        // die Kosten der gerade benutzten Kante
	        tentative_g = g(currentNode) + c(currentNode, successor)
	        // wenn der Nachfolgeknoten bereits auf der Open List ist,
	        // aber der neue Weg nicht besser ist als der alte - tue nichts
	        if openlist.contains(successor) and tentative_g >= g(successor) then
	            continue
	        // Vorgängerzeiger setzen und g Wert merken
	        successor.predecessor := currentNode
	        g(successor) = tentative_g
	        // f Wert des Knotens in der Open List aktualisieren
	        // bzw. Knoten mit f Wert in die Open List einfügen
	        f := tentative_g + h(successor)
	        if openlist.contains(successor) then
	            openlist.decreaseKey(successor, f)
	        else
	            openlist.enqueue(successor, f)
	    end
	end
	*/
	public void analysiereKnoten(T knoten, T ziel) {
		for( int i=0; i<knoten.anzahlNachbarn(); i++ ) {
			T nachbar =  (T) knoten.getNachbar(i);
			//null-Nachbarn ignorieren
			if(nachbar == null ) continue;
			//wurde Nachbar schon analysiert, dann ignorieren
			if( closed.contains( nachbar ) ) continue;
			//den Wert G für Nachbar berechnen
			double g = nachbar.berechneG( knoten );
			//ist nachbar schon auf der open-Liste UND ist der aktuelle
			//Wert größer als der alte, dann ignorieren
			if( open.contains(nachbar) )
				if( g >= nachbar.getG() )
					continue;
			//besser/neue, also setzen wir das Ding fest
			nachbar.setG( g );
			nachbar.setVorgaenger( knoten );
			//den Wert H berechnen
			double h = nachbar.berechneH( ziel );
			nachbar.setH( h );
			//abschließend F berechnen
			double f = nachbar.berechneF();
			nachbar.setF( f );
			//und in die open-Liste legen
			if( !open.contains(nachbar) )
				open.add( (T) nachbar );
		}
	}

//### GETTER & SETTER ###############################################

//### OVERRIDES #####################################################

}


class FeldListe <T> {
	private double wertF;
	private T vorgaenger;
	
	public FeldListe(double wertF, T vorgaenger) {
		this.setWertF(wertF);
		this.setVorgaenger(vorgaenger);
	}

	public double getWertF() {
		return wertF;
	}

	public void setWertF(double wertF) {
		this.wertF = wertF;
	}

	public T getVorgaenger() {
		return vorgaenger;
	}

	public void setVorgaenger(T vorgaenger) {
		this.vorgaenger = vorgaenger;
	}
}