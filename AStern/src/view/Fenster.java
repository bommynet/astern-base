/**
 * 
 */
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import logik.Pfad;
import model.Feld;
import model.FeldHex;
import model.FeldQuad;
import model.FeldQuadD;
import model.Knoten;
import model.KnotenHex;
import model.KnotenQuad;
import model.KnotenQuadD;

/**
 * @author Thomas Laarmann
 *
 */
public class Fenster extends JPanel {
	private static final long serialVersionUID = 1L;
//### KONSTANTEN ####################################################
	public final int WIDTH = 8;
	public final int STARTX = 25;
	public final int STARTY = 25;
	public final int MAXX = 41;
	public final int MAXY = 40;
	
//### VARIABLEN #####################################################
	JFrame f;
	
	FeldQuadD feldQ;
	FeldHex feldH;

	KnotenHex pfadH;
	KnotenQuadD pfadQ;
	
	int aktH = -1;

//### KONSTRUKTOR ###################################################
	public Fenster() {
		super( true );
		f = new JFrame("Testframe");
		f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		this.setPreferredSize(new Dimension( 900, 600 ));
		f.add( this );
		
		feldH = new FeldHex( MAXX, MAXY );
		feldQ = new FeldQuadD( MAXX, MAXY );
		
		int xb = (MAXX/2);
		int yb = (MAXY/2);
		
		for(int i=0; i<xb; i++) {
			for(int j=0; j<yb; j++) {
				feldH.getFeld(xb/2+i, yb/2+j).entferneNachbarAlle();
				feldQ.getFeld(xb/2+i, yb/2+j).entferneNachbarAlle();
			}
		}
		
		f.setVisible(true);
		f.pack();
		
		Pfad<KnotenHex> p1 = new Pfad<KnotenHex>();
		pfadH = p1.berechneWeg(feldH.getFeld(0), feldH.getFeld(1415));
		Pfad<KnotenQuadD> p2 = new Pfad<KnotenQuadD>();
		pfadQ = p2.berechneWeg(feldQ.getFeld(0), feldQ.getFeld(MAXX*MAXY-1));
		this.repaint();
		
		this.addMouseMotionListener( new MouseHex(this, feldH) );
	}

//### FUNKTIONEN ####################################################
	public void neuerPfadHex(int ziel, Point m) {
		if( ziel < 0 || ziel >= feldH.getSize() ) return;
		
		int tmp = -1;
		if( !createPoly( ziel ).contains(m) ) {
			for(int i=0; i<feldH.getFeld( ziel ).anzahlNachbarn(); i++) {
				if( feldH.getFeld( ziel ).getNachbar(i) == null ) continue;
				if( createPoly( feldH.getFeld( ziel ).getNachbar(i).getId() ).contains(m) ) {
					tmp = feldH.getFeld( ziel ).getNachbar(i).getId();
					break;
				}
			}
		}
		
		if( aktH == tmp || tmp == -1 ) return;
		aktH = tmp;
		for(int i=0; i<feldH.getSize(); i++) {
			feldH.getFeld(i).setVorgaenger(null);
		}
		Pfad<KnotenHex> p1 = new Pfad<KnotenHex>();
		pfadH = p1.berechneWeg(feldH.getFeld(0), feldH.getFeld(tmp));
		this.repaint();
	}

//### GETTER & SETTER ###############################################

//### OVERRIDES #####################################################
	public void paintComponentQ(Graphics g) {
		for(int y=0; y<MAXY; y++) {
			for(int x=0; x<MAXX; x++) {
				int posX = STARTX + (x*WIDTH) + 500;
				int posY = STARTY + (y*WIDTH);
				
				Knoten v = feldQ.getFeld(x, y);
				if( v.anzahlNachbarnVorhanden() == 0 )
					g.setColor( Color.black );
				else if( v.getVorgaenger() != null )
					g.setColor( Color.red );
				else
					g.setColor( Color.yellow );
				
				g.fillRect(posX, posY, WIDTH, WIDTH);
				
				g.setColor( Color.blue );
				g.drawRect(posX, posY, WIDTH, WIDTH);
			}
		}
		
		if( pfadQ != null ) {
			Knoten lauf = pfadQ;
			while( lauf != null ) {
				int posX = STARTX + (lauf.getX()*WIDTH) + 500;
				int posY = STARTY + (lauf.getY()*WIDTH);
				
				g.setColor( Color.blue );
				g.fillRect(posX, posY, WIDTH, WIDTH);
				
				lauf = lauf.getVorgaenger();
			}
		}
	}
	
	public void paintComponentH(Graphics g) {
		for(int y=0; y<MAXY; y++) {
			for(int x=0; x<MAXX; x++) {
				Knoten v = feldH.getFeld(x, y);
				Polygon poly = null;
				if( v != null ) poly = createPoly( v.getId() );
				
				if( v.anzahlNachbarnVorhanden() == 0 )
					g.setColor( Color.black );
				else if( v.getVorgaenger() != null )
					g.setColor( Color.red );
				else
					g.setColor( Color.yellow );
				
				g.fillPolygon( poly );
				
				g.setColor( Color.blue );
				g.drawPolygon( poly );
			}
		}
		
		if( pfadH != null ) {
			Knoten lauf = pfadH;
			while( lauf != null ) {
				g.setColor( Color.blue );
				g.fillPolygon( createPoly( lauf.getId() ) );
				
				lauf = lauf.getVorgaenger();
			}
		}
	}
		
	@Override
	public void paintComponent(Graphics g) {
		g.setColor( Color.black );
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		paintComponentH( g );
		paintComponentQ( g );
	}
		
		
	private Polygon createPoly(int id) {
		//vorrechnungen
		int breite = WIDTH;
		int hoehe  = (int)(breite * 7 / 8);
		int spalte = id % MAXX;
		int zeile  = (int)((id-spalte)/MAXX);
		boolean ungerade = (( id % MAXX ) % 2 == 1) ? true : false;
		
		//Mittelpunkt bestimmen
		int mX   = STARTX + spalte * (3 * breite / 2);
		int mY   = STARTY + (zeile * 2 * hoehe) + (( ungerade ) ? hoehe : 0);
		
		Polygon poly = new Polygon();
		
		poly.addPoint( mX-(breite), mY ); //(P0) links
		poly.addPoint( mX-(breite/2), mY-hoehe ); //(P1) linksoben
		poly.addPoint( mX+(breite/2), mY-hoehe ); //(P2) rechtsoben
		poly.addPoint( mX+(breite), mY ); //(P3) rechts
		poly.addPoint( mX+(breite/2), mY+hoehe ); //(P4) rechtsunten
		poly.addPoint( mX-(breite/2), mY+hoehe ); //(P5) linksunten
		
		return poly;
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		new Fenster();
	}
}

class MouseHex implements MouseListener, MouseMotionListener {
	private int sX, sY, b;
	private Feld<KnotenHex> source;
	private Fenster panel;
	
	public MouseHex(Fenster panel, Feld<KnotenHex> source) {
		this.sX = panel.STARTX;
		this.sY = panel.STARTY;
		this.b = panel.WIDTH;
		this.panel = panel;
		this.source = source;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent me) {
		int x = (int)((me.getX() - sX) / (3 * b / 2));
		int y = (int)(( ((me.getY() - sY) / (2*(b*7/8))) + ((me.getY() - sY - (b*7/8)) / (2*(b*7/8))) ) / 2);
		
		int id = x + (y * source.getSpalten());
		Point p = new Point(me.getX() , me.getY());
		
		panel.neuerPfadHex( id, p );
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent arg0) {
		mouseClicked( arg0 );
	}
	
}