import java.util.ArrayList;

public class RoadImpl implements Road {
	private Place firstPlace;
	private Place secondPlace;
	private String roadName;
	private int length;
	
	private boolean isChosen = false;
	
	private ArrayList<RoadListener> roadListeners;
	
	public RoadImpl(Place from, Place to, String roadName, int length) {
		this.firstPlace = from;
		this.secondPlace = to;
		this.roadName = roadName;
		this.length = length;
		
		this.roadListeners = new ArrayList<RoadListener>();
	}
	
    //Add the RoadListener rl to this place.
    //Note: A road can have multiple listeners
    public void addListener(RoadListener rl) {
    		roadListeners.add(rl);
    }


    //Delete the RoadListener rl from this place.
    public void deleteListener(RoadListener rl) {
    		roadListeners.remove(rl);
    }


    //Return the first place of this road
    //Note: The first place of a road is the place whose name
    //comes earlier in the alphabet.
    public Place firstPlace() {
    		return this.firstPlace;
    }
    

    //Return the second place of this road
    //Note: The second place of a road is the place whose name
    //comes later in the alphabet.
    public Place secondPlace() {
    		return this.secondPlace;
    }
    

    //Return true if this road is chosen as part of the current trip
    public boolean isChosen() {
    		return isChosen;
    }

    //set true if this road is chosen as part of the current trip
    public void setChosen(boolean isChosen) {
    		this.isChosen = isChosen;
    		
    		//chosen flag has been changed,
    		//call roadListeners' roadChanged function
    		for(int i = 0;i < roadListeners.size();i++) {
    			roadListeners.get(i).roadChanged();
    		}
    }

    //Return the name of this road
    public String roadName() {
    		return this.roadName;
    }
    

    //Return the length of this road
    public int length() {
    		return this.length;
    }

    
    //Return a string containing information about this road 
    //in the form (without quotes, of course!):
    //"firstPlace(roadName:length)secondPlace"
    public String toString() {
    		return this.firstPlace.getName() + "(" + this.roadName + ":" + this.length + ")" + this.secondPlace.getName();
    }
}
