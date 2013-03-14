/**
 * MapPanel, The main panel display the map
 * @author Ren DING a1202524
 * 
 */

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import java.util.*;


class MapPanel extends JPanel implements MapListener{
		private Map map;
		private Set<Place> places;
		private Set<PlaceIcon> placeIcons;
		private Set<Road> roads;
		private Set<RoadIcon> roadIcons;
		
		private MouseAdapter mouseHandler;
		private int prevX;
		private int prevY;
		private int currX;
		private int currY;
		private Rectangle rect;
		
		private MapPanel mapPanel;
		//for NewRoad menuItem
		public boolean willNewRoad;
		public String newRoadName;
		public int newRoadLenght;
		
		/**
		 * add mouse listener and initialise the instance variables
		 */
		public MapPanel() {
			map = new MapImpl();
			places = new HashSet<Place>();
			placeIcons = new HashSet<PlaceIcon>();
			roads = new HashSet<Road>();
			roadIcons = new HashSet<RoadIcon>();
			
			this.prevX = 0;
			this.prevY = 0;
			this.currX = 0;
			this.currY = 0;
			
			this.mouseHandler = new MouseHandler();
			this.addMouseListener(mouseHandler);
			this.addMouseMotionListener(mouseHandler);
			
			this.rect = null;
			
			willNewRoad = false;
			newRoadName = null;
			newRoadLenght = 0;
			
			mapPanel = this;
		}
	
		/**
		 * destructor this class and reset all variables
		 */
		public void clear() {
			map = new MapImpl();
			places = new HashSet<Place>();
			placeIcons = new HashSet<PlaceIcon>();
			roads = new HashSet<Road>();
			roadIcons = new HashSet<RoadIcon>();
			this.mouseHandler = new MouseHandler();
			this.rect = null;
			removeAll();
			
			willNewRoad = false;
			newRoadName = null;
			newRoadLenght = 0;
			
			mapPanel = this;
		}
		
		public void setMap(Map map) {
			this.map = map; 
		}
		
		public Map getMap() {
			return this.map;
		}
		
		public Set<Place> getPlaces() {
			return this.places;
		}
		
		public Set<PlaceIcon> getPlaceIcons() {
			return this.placeIcons;
		}
		
		public Set<Road> getRoads() {
			return this.roads;
		}
		
		public Set<RoadIcon> getRoadIcons() {
			return this.roadIcons;
		}
		
		/**
		 * draw the selecting box
		 */
		public void paintComponent(Graphics g) {
			if(this.rect != null) {
				g.drawRect(this.rect.x, this.rect.y, this.rect.width, this.rect.height);
			}
			
			selectPlaceForNewRoad();
		}

		/**
		 * For NewRoad on the menuItem,
		 * Choose two places to create a new road
		 */
		private void selectPlaceForNewRoad() {
			//for new road menuItem
			if(willNewRoad == true) {
				Set<PlaceIcon> icons = this.getPlaceIcons();
				ArrayList<PlaceIcon> iconList = new ArrayList<PlaceIcon>();
				for(PlaceIcon icon: icons) {
					if(icon.isSelected()) {
						iconList.add(icon);
					}
				}
				
				if(iconList.size() == 2){
					try {
						this.map.newRoad(iconList.get(0).getPlace(),
										 iconList.get(1).getPlace(),
										 this.newRoadName,
										 this.newRoadLenght);
					} catch(IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(this.getParent(),"the name is illegal or create a duplicate road");
						iconList = new ArrayList<PlaceIcon>();
						this.getParent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						willNewRoad = false;
					}
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					willNewRoad = false;
				}
			}
		}
		
	    /**
	     * Called whenever the number of places in the map has changed
	     */
	    public void placesChanged() {
	    		System.out.println("placeChanged");
	    		Set<Place> getPlaces = this.map.getPlaces();
	    		
	    		for(Place p: getPlaces) {
	    			if( this.places.contains(p) ) {
	    				
	    			} else {
	    				PlaceIcon placeIcon = new PlaceIcon(p);
	    				p.addListener(placeIcon);
	    				
	    				//add a placeIcon on the panel
	    				this.add(placeIcon);
	    				
	    				//add a new place with its icon
	    				this.places.add(p);
	    				this.placeIcons.add(placeIcon);
	    			}
	    		}
	    		
	    		for(Place p: this.places) {
	    			if( getPlaces.contains(p) ) {
	    				
	    			} else {
	    				//remove a place
	    				this.places.remove(p);
	    				//find the PlaceIcon which contains p, then remove it from the panel
	    				for(PlaceIcon pi:placeIcons) {
	    					if( pi.getPlace().equals(p) ) {
	    						this.remove(pi);
	    						this.placeIcons.remove(pi);
	    						break;
	    					}
	    				}
	    				break;
	    			}
	    		}
	    		
	    		this.getParent().repaint();
	    }

	    /**
	     * Called whenever the number of roads in the map has changed
	     */
	    public void roadsChanged() {
    			System.out.println("roadsChanged");
    			Set<Road> getRoads = this.map.getRoads();
    			
    			for(Road r: getRoads) {
	    			if( this.roads.contains(r) ) {
	    				
	    			} else {
	    				RoadIcon roadIcon = new RoadIcon(r);
	    				r.addListener(roadIcon);
	    				
	    				//add a roadIcon on the panel
	    				this.add(roadIcon);
	    				
	    				//add a new road with its icon
	    				this.roads.add(r);
	    				this.roadIcons.add(roadIcon);
	    			}
	    		}
	    		
	    		for(Road r: this.roads) {
	    			if( getRoads.contains(r) ) {
	    				
	    			} else {
	    				//remove a road
	    				this.roads.remove(r);
	    				
	    				//find the roadIcon which contains r, then remove it from the panel
	    				for(RoadIcon ri : roadIcons) {
	    					if( ri.getRoad().equals(r) ) {
	    						this.placeIcons.remove(ri);
	    						this.remove(ri);
	    						break;
	    					}
	    				}
	    				break;
	    			}
	    		}
	    		
	    		this.getParent().repaint();
	    }

	    /**
	     * Called whenever something about the map has changed
	     * (other than places and roads)
	     */
	    public void otherChanged() {
    			System.out.println("otherChanged");
    			this.getParent().repaint();
    		}

	    /**
	     * mouse handler to listen to the mouse action
	     */
	    private class MouseHandler extends MouseAdapter {
	    		private boolean isInMultiPlace;
	    		private ArrayList<PlaceIcon> icons;
	    		
	    		public MouseHandler() {
	    			isInMultiPlace = false;
	    			icons = new ArrayList<PlaceIcon>();
	    		}
	    		
	    		/**
	    		 * drag multi-Place or set selecting box's bounds
	    		 */
		    	public void mouseDragged(MouseEvent me)  {
				System.out.println("mouseDragged");
				
				currX = me.getX();
				currY = me.getY();
				
				if(isInMultiPlace) {
					for(PlaceIcon icon:icons) {
						icon.getPlace().moveBy(currX - prevX, currY - prevY);
					}
					prevX = currX;
					prevY = currY;
					return;
				}
				
				if(rect != null) {
					if(currX - prevX > 0 && currY - prevY > 0) {
						rect.setBounds(prevX, prevY, currX - prevX, currY - prevY);
					} else if(currX - prevX > 0 && currY - prevY < 0) {
						rect.setBounds(prevX, currY, currX - prevX, prevY - currY);
					} else if(currX - prevX < 0 && currY - prevY < 0) {
						rect.setBounds(currX, currY, prevX - currX , prevY - currY);
					} else if(currX - prevX < 0 && currY - prevY > 0) {
						rect.setBounds(currX, prevY, prevX - currX, currY - prevY);
					}
				}
				
				 Component[] components = getComponents();
				 for(Component component:components) {
					 if(component instanceof PlaceIcon) {
						 PlaceIcon icon = (PlaceIcon)component;
						 if( rect.intersects( icon.getBounds() ) ){
							 icon.isSelected(true);
						 } else {
							 icon.isSelected(false);
						 }
					 } 
					 else if(component instanceof RoadIcon) {
						 RoadIcon icon = (RoadIcon)component;
						 if( rect.intersects( icon.getBounds() ) ){
							 icon.isSelected(true);
						 } else {
							 icon.isSelected(false);
						 }
					 }
				 }	
			}
		
		    	/**
		    	 * calculate the mouse press point is in the multi-Places or not
		    	 */
	    		public void mousePressed(MouseEvent me) {
	    			System.out.println("mousePressed");
	    			prevX = me.getX();
	    			prevY = me.getY();
	    			rect = new Rectangle(prevX,prevY,1,1);
	    			
	    			int leftUpX = prevX;
	    			int leftUpY = prevY;
	    			int rightDownX = 0;
	    			int rightDownY = 0;
	    			Set<PlaceIcon> getIcons = mapPanel.getPlaceIcons();
	    			for(PlaceIcon icon: getIcons) {
	    				if(icon.isSelected()) {
						int x = icon.getPlace().getX();
						int y = icon.getPlace().getY();
						leftUpX = Math.min(leftUpX, x);
						leftUpY = Math.min(leftUpY, y);
						rightDownX = Math.max(rightDownX, x);
						rightDownY = Math.max(rightDownY, y);
						icons.add(icon);
	    				}
		    		}
	    			
				if(icons.size() < 2) {
					isInMultiPlace = false;
					icons = new ArrayList<PlaceIcon>();
					return;
				}
				
				if(prevX > leftUpX && prevX < rightDownX 
				   && prevY > leftUpY && prevY < rightDownY) {
					isInMultiPlace = true;
				}
	    		}
    		
	    		/**
	    		 * reset the selecting box and multi-place flag
	    		 */
	    		public void mouseReleased(MouseEvent me) {
	    			System.out.println("mouseReleased");
	    			rect = null;
	    			isInMultiPlace = false;
	    			icons = new ArrayList<PlaceIcon>();
	    		}
	    		
	    		public void mouseClicked(MouseEvent me) {
	    			Component component = mapPanel.getComponentAt(me.getX(), me.getY());
	    			if(component instanceof RoadIcon) {
	    				RoadIcon icon = (RoadIcon)component;
	    				icon.isSelected(!icon.isSelected());
	    			}
	    		}
	    }
}
