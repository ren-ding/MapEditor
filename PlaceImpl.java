import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;
import java.util.ArrayList;

public class PlaceImpl implements Place {
	private Map map;
	
	private String placeName;
	private int xPos;
	private int yPos;
	
	private ArrayList<PlaceListener> placeListeners;
	
	public PlaceImpl(Map map,String placeName, int xPos, int yPos) {
		this.map = map;
		this.placeName = placeName;
		this.xPos = xPos;
		this.yPos = yPos;
		this.placeListeners = new ArrayList<PlaceListener>();
	}

    //Add the PlaceListener pl to this place. 
    //Note: A place can have multiple listeners
    public void addListener(PlaceListener pl) {
    		placeListeners.add(pl);
    }


    //Delete the PlaceListener pl from this place.
    public void deleteListener(PlaceListener pl) {
    		placeListeners.remove(pl);
    }


    //Return a set containing all roads that reach this place
    public Set<Road> toRoads() {
    		Set<Road> roads = new HashSet<Road>();
    		Iterator<Road> iter = this.map.getRoads().iterator();
    		while(iter.hasNext()) {
    			Road tmpRoad = iter.next();
    			if(tmpRoad.firstPlace().equals(this) || tmpRoad.secondPlace().equals(this)) {
    				roads.add(tmpRoad);
    			}
    		}
    		
    		return roads;
    }


    //Return the road from this place to dest, if it exists
    //Returns null, if it does not
    public Road roadTo(Place dest) {
    		Iterator<Road> iter = this.map.getRoads().iterator();
		while(iter.hasNext()) {
			Road tmpRoad = iter.next();
			if( (tmpRoad.firstPlace().equals(this) && tmpRoad.secondPlace().equals(dest)) 
				|| (tmpRoad.firstPlace().equals(dest) && tmpRoad.secondPlace().equals(this))
			){
				return tmpRoad;
			}
		}
    		return null;
    }
    

    //Move the position of this place 
    //by (dx,dy) from its current position
    public void moveBy(int dx, int dy) {
    		this.xPos += dx;
    		this.yPos += dy;
    		
    		//place position has been changed,
    		//call placeListener's placeChanged function
    		for(int i = 0; i < this.placeListeners.size();i++) {
    			this.placeListeners.get(i).placeChanged();
    		}
    }
    

    //Return the name of this place 
    public String getName() {
    		return this.placeName;
    }
    

    //Return the X position of this place
    public int getX() {
    		return this.xPos;
    }
    

    //Return the Y position of this place
    public int getY() {
    		return this.yPos;
    }


    //Return true if this place is the starting place for a trip
    public boolean isStartPlace() {
    		if(this.map.getStartPlace() == null) {
    			return false;
    		} else {
    			return this.map.getStartPlace().equals(this);
    		}
    }


    //Return true if this place is the ending place for a trip
    public boolean isEndPlace() {
    		if(this.map.getEndPlace() == null) {
    			return false;
    		} else {
    			return this.map.getEndPlace().equals(this);
    		}
    }


    //Return a string containing information about this place 
    //in the form (without the quotes, of course!) :
    //"placeName(xPos,yPos)"  
    public String toString() {
    		return this.placeName + "(" + this.xPos + "," + this.yPos + ")";
    }
}
