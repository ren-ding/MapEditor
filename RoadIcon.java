/**
 * RoadIcon, display a road with name and length
 * 
 * @author Ren DING a1202524
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;


public class RoadIcon extends JComponent implements RoadListener, MouseListener {
	private final int wordLength = 20;
	private Road road;
	private int boundsX;
	private int boundsY;
	private int weight;
	private int height;
	private boolean isSelected;
	
	/**
	 * initialise variables
	 * @param road Road, the road that will be displayed
	 */
	public RoadIcon(Road road) {
		this.road = road;
		this.isSelected = false;
		
		calculateBounds();
		setBounds(this.boundsX, this.boundsY, this.weight, this.height);
	}
	
	public Road getRoad() {
		return this.road;
	}
	
	public boolean isSelected() {
		return this.isSelected;
	}
	
	public void isSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	/**
	 * calculate the road's bounds
	 */
	private void calculateBounds() {
		this.boundsX = Math.min(road.firstPlace().getX(), road.secondPlace().getX());
		this.boundsY = Math.min(road.firstPlace().getY(), road.secondPlace().getY());
		this.weight = Math.abs(road.secondPlace().getX() - road.firstPlace().getX());
		this.height = Math.abs(road.secondPlace().getY() - road.firstPlace().getY());
		this.weight = Math.max(this.weight,PlaceIcon.PLACE_RADIUS/2) + 10;
		this.height = Math.max(this.height, PlaceIcon.PLACE_RADIUS/2)+ 10;
		
	}
	
	/**
	 * set a road icon's bounds and paint it with color, name and length
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		if(this.isSelected){
			g2d.setColor(Color.blue);
		} else {
			g2d.setColor(Color.black);
		}
		
		calculateBounds();
		setBounds(this.boundsX, this.boundsY, this.weight, this.height);
		
		g2d.drawLine(road.firstPlace().getX() - boundsX, road.firstPlace().getY() - boundsY,
					road.secondPlace().getX() - boundsX, road.secondPlace().getY() - boundsY);
		
		if(road.roadName().equals("-")) {
			g2d.drawString(""+ " " + road.length(),
						  (road.firstPlace().getX() + road.secondPlace().getX()) /2 - boundsX,
						  (road.firstPlace().getY() + road.secondPlace().getY()) /2 - boundsY+wordLength );
		} else {
			g2d.drawString(road.roadName() + " " + road.length(),
					  (road.firstPlace().getX() + road.secondPlace().getX()) /2 - boundsX,
					  (road.firstPlace().getY() + road.secondPlace().getY()) /2 - boundsY+wordLength );
		}
		//repaint the frame
		this.getParent().getParent().repaint();
	}
	
	public void roadChanged() {
		System.out.println("roadChanged");
	}

	public void mouseClicked(MouseEvent me) {
		isSelected = !isSelected;
	}

	public void mouseEntered(MouseEvent me) {
		
	}

	public void mouseExited(MouseEvent me) {
		
	}

	public void mousePressed(MouseEvent me) {
		
	}

	public void mouseReleased(MouseEvent me) {
		
	}
}