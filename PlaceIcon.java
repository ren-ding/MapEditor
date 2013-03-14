/**
 * PlaceIcon, display a place with its name
 * 
 * @author Ren DING a1202524
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.*;


public class PlaceIcon extends JComponent implements PlaceListener {
	public static final int PLACE_RADIUS = 40;
	private Place place;
	private boolean isSelected;
	
	private MouseAdapter mouseHandler;
	private int prevX;
	private int prevY;
	private int currX;
	private int currY;
	
	/**
	 * initialise variables and add mouse listener for the PlaceIcon
	 * @param place Place, the place that will be displayed
	 */
	public PlaceIcon(Place place) {
		this.place = place;
		this.isSelected = false;
		this.prevX = 0;
		this.prevY = 0;
		this.currX = 0;
		this.currY = 0;
		
		setBounds(place.getX()-PLACE_RADIUS/2, place.getY()-PLACE_RADIUS/2, PLACE_RADIUS+1, PLACE_RADIUS+1);
		
		this.mouseHandler = new MouseHandler();
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);
	}
	
	/**
	 * set a place icon's bounds and paint it with color and name
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		if(place.isStartPlace() && place.isEndPlace()) {
			g2d.setColor(Color.blue);
		}else if(place.isStartPlace()) {
			g2d.setColor(Color.red);
		} else if(place.isEndPlace()) {
			g2d.setColor(Color.green);
		} else {
			g2d.setColor(Color.black);
		}
		
		setBounds(place.getX()-PLACE_RADIUS/2, place.getY()-PLACE_RADIUS/2, PLACE_RADIUS+1, PLACE_RADIUS+1);			
		
		if(isSelected) {
			g2d.fill(new Ellipse2D.Double(0, 0, PLACE_RADIUS, PLACE_RADIUS));
		} else {
			g2d.draw(new Ellipse2D.Double(0, 0, PLACE_RADIUS, PLACE_RADIUS));
		}
		//System.out.println(this.place.getName() + " " + this.place.getX() + " " + this.place.getY());
		g2d.drawString(this.place.getName(), 0, PLACE_RADIUS/2+1);
		//repaint the frame
		this.getParent().getParent().repaint();
	}
	
	public Place getPlace() {
		return this.place;
	}
	
	public boolean isSelected() {
		return this.isSelected;
	}
	
	public void isSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	/**
	 * Called whenever the visible state of a place has changed
	 */
    public void placeChanged() {
    		System.out.println("placeChanged");
    }
    
    /**
     * a mouse handler to handle with the mouse action on the place
     */
    private class MouseHandler extends MouseAdapter {
    		public void mouseClicked(MouseEvent me) {
			//System.out.println("mouseClicked");
			isSelected = !isSelected;
		}
		
		public void mouseDragged(MouseEvent me)  {
			//System.out.println("mouseDragged");
			currX = me.getX();
			currY = me.getY();
			place.moveBy(currX - prevX, currY - prevY);
		}
		
    		public void mouseEntered(MouseEvent me) {
    			//System.out.println("mouseEntered");
    		}
    		
    		public void mouseExited(MouseEvent me) {
    			//System.out.println("mouseExited");
    		}
    		
    		public void mouseMoved(MouseEvent me) {
    			//System.out.println("mouseMoved");
    		}
    		
    		public void mousePressed(MouseEvent me) {
    			//System.out.println("mousePressed");
    			prevX = me.getX();
    			prevY = me.getY();
    		}
    		
    		public void mouseReleased(MouseEvent me) {
    			//System.out.println("mouseReleased");
    		}
    }
}