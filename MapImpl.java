import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Arrays;
import java.util.ArrayList;

public class MapImpl implements Map {
	private Set<Place> places;
	private Set<Road> roads;
	private Place startPlace;
	private Place endPlace;
	private ArrayList<MapListener> mapListeners;
	
	public MapImpl() {
		this.places = new HashSet<Place>();
		this.roads = new HashSet<Road>();
		this.mapListeners = new ArrayList<MapListener>();
	}
	
    //Add the MapListener ml to this map.
    //Note: A map can have multiple listeners
    public void addListener(MapListener ml) {
    		mapListeners.add(ml);
    }


    //Delete the MapListener ml from this map.
    public void deleteListener(MapListener ml) {
    		mapListeners.remove(ml);
    }


    //Create a new Place and add it to this map
    //Return the new place
    //Throws IllegalArgumentException if:
    //  the name is not valid or is the same as that
    //  of an existing place
    //Note: A valid placeName begins with a letter, and is 
    //followed by optional letters, digits, or underscore characters
    public Place newPlace(String placeName, int xPos, int yPos)
      throws IllegalArgumentException {
    		//check if the placeName is null
    		if(placeName == null) {
    			throw new IllegalArgumentException();
    		}
    		
		//check if the name is valid (by the regular expression)
		if( !placeName.matches("[a-zA-Z][[a-zA-Z]|[0-9]|[_]]*") ) {
			throw new IllegalArgumentException();
		}
		
    		//check if the name has existed or not
    		boolean nameExisting = false;
		Iterator<Place> iter = places.iterator();
		while(iter.hasNext()) {
			if( iter.next().getName().equals(placeName) ) {
				nameExisting = true;
			}
		}
		if(nameExisting == true) {
			throw new IllegalArgumentException();
		}
		
		//create a new place and add it to this map
    		Place p = new PlaceImpl(this,placeName,xPos,yPos);
    		this.places.add(p);
    		
    		//System.out.println("new placed " + mapListeners.size());
    		//a place has been added into the map,
    		//call mapListeners' placesChanged function
    		for(int i = 0;i < this.mapListeners.size();i++) {
    			this.mapListeners.get(i).placesChanged();
    		}
    		
    		return p;
    }


    //Remove a place from the map
    //If the place does not exist, returns without error
    public void deletePlace(Place s) {
    		if(places.contains(s)) {
    			places.remove(s);
    			
    			Set<Road> roads = s.toRoads();
    			for(Road r: roads) {
    				deleteRoad(r);
    			}
    			
    			if(s.isStartPlace()) {
    				this.setStartPlace(null);
    			}
    			
    			if(s.isEndPlace()) {
    				this.setEndPlace(null);
    			}
    			//a place has been removed from the map,
        		//call mapListeners' placesChanged function
        		for(int i = 0;i < this.mapListeners.size();i++) {
        			this.mapListeners.get(i).placesChanged();
        		}
    		}
    		return;
    }


    //Find and return the Place with the given name
    //If no place exists with given name, return NULL
    public Place findPlace(String placeName) {
		Iterator<Place> iter = places.iterator();
		while(iter.hasNext()) {
			Place tmpPlace = iter.next();
			if( tmpPlace.getName().equals(placeName) ) {
				return tmpPlace;
			}
		}
		
    		return null;
    }


    //Return a set containing all the places in this map
    public Set<Place> getPlaces() {
    		return this.places;
    }
    

    //Create a new Road and add it to this map
    //Returns the new road.
    //Throws IllegalArgumentException if:
    //  the firstPlace or secondPlace does not exist or
    //  the roadName is invalid or
    //  the length is negative
    //Note: A valid roadName is either the empty string, or starts
    //with a letter and is followed by optional letters and digits
    public Road newRoad(Place from, Place to, 
      String roadName, int length) 
      throws IllegalArgumentException {
    		//check if the firstPlace or secondPlace exist
    		if( !places.contains(from) || !places.contains(to) ) {
    			throw new IllegalArgumentException();
    		}
    		
    		if(from.roadTo(to) != null) {
    			//duplicate road
    			throw new IllegalArgumentException();
    		}
    		
    		//check if the roadName is null
    		if(roadName == null) {
    			throw new IllegalArgumentException();
    		}
    		
    		//check if the roadName is invalid
    		if( !roadName.matches("[-]|[a-zA-Z][[a-zA-Z]|[0-9]]*") ) {
    			throw new IllegalArgumentException();
    		}
    		
    		//check if the length is negative
    		if(length < 0) {
    			throw new IllegalArgumentException();
    		}
    		
    		//create a new road
    		Road r = null;
    		if(from.getName().compareTo(to.getName()) < 0) {
    			r = new RoadImpl(from,to,roadName,length);
    		} else {
    			r = new RoadImpl(to,from,roadName,length);
    		}
    		
    		if(!roads.contains(r)) {
    			roads.add(r);
    			
    			//a road has been added into the map,
        		//call mapListeners' roadsChanged function
        		for(int i = 0;i < this.mapListeners.size();i++) {
        			this.mapListeners.get(i).roadsChanged();
        		}
    		} 
    		
    		return r;
    }


    //Remove a road r from the map
    //If the road does not exist, returns without error
    public void deleteRoad(Road r) {
	    	if(roads.contains(r)) {
			roads.remove(r);
				
    			//a road has been removed from the map,
        		//call mapListeners' roadsChanged function
        		for(int i = 0;i < this.mapListeners.size();i++) {
        			this.mapListeners.get(i).roadsChanged();
        		}
		}
		return;
    }


    //Return a set containing all the roads in this map
    public Set<Road> getRoads() {
    		return roads;
    }
    

    //Set the place p as the starting place
    //If p==null, unsets the starting place
    //Throws IllegalArgumentException if the place p is not in the map
    public void setStartPlace(Place p)
      throws IllegalArgumentException {
    		if(p == null) {
    			this.startPlace = null;
    			return;
    		}
    		
    		if(!places.contains(p)) {
    			throw new IllegalArgumentException();
    		}
    		
    		this.startPlace = p;
    		
		//startPlace has been set,
    		//call mapListeners' otherChanged function
    		for(int i = 0;i < this.mapListeners.size();i++) {
    			this.mapListeners.get(i).otherChanged();
    		}
    }


    //Return the starting place of this map
    public Place getStartPlace() {
    		return this.startPlace;
    }


    //Set the place p as the ending place
    //If p==null, unsets the ending place
    //Throws IllegalArgumentException if the place p is not in the map
    public void setEndPlace(Place p)
      throws IllegalArgumentException {
		if(p == null) {
			this.endPlace = null;
			return;
		}
		
		if(!places.contains(p)) {
			throw new IllegalArgumentException();
		}
		
		this.endPlace = p;
		
		//endPlace has been set,
		//call mapListeners' otherChanged function
		for(int i = 0;i < this.mapListeners.size();i++) {
			this.mapListeners.get(i).otherChanged();
		}
    }


    //Return the ending place of this map
    public Place getEndPlace() {
    		return this.endPlace;
    }


    //Causes the map to compute the shortest trip between the
    //"start" and "end" places
    //For each road on the shortest route, sets the "isChosen" property
    //to "true".
    //Returns the total distance of the trip.
    //Returns -1, if there is no route from start to end
    public int getTripDistance() {
    		if(this.startPlace == null || this.endPlace == null) {
    			return -1;
    		}
    		
    		int distance = 0;
    		
    		int[] dist = new int[places.size()];
    		Arrays.fill(dist, Integer.MAX_VALUE);
    		
    		int[] previous = new int[places.size()];
    		Arrays.fill(previous, -1);
    		
    		//in order to get index of each Place, create a ArrayList
    		//copy all Places to this ArrayList
    		ArrayList<Place> placesIndexList = new ArrayList<Place>();    		
    		Iterator<Place> iterPlace = this.places.iterator();
    		while(iterPlace.hasNext()) {
    			placesIndexList.add(iterPlace.next());
    		}
    		
    		//distance from startPlace to startPlace
    		int startPlaceIndex = placesIndexList.indexOf(this.startPlace);
    		dist[startPlaceIndex] = 0;
    		
    		ArrayList<Place> placesArrayList = new ArrayList<Place>();
    		for(int i = 0;i < placesIndexList.size();i++) {
    			placesArrayList.add(placesIndexList.get(i));
    		}
    		
    		//for(int i = 0;i < placesArrayList.size();i++) {
    		//	System.out.println(i + ":" +placesArrayList.get(i).getName());
    		//}
    		
    		//boolean array to mark which one has been deleted
    		boolean[] isDeleted = new boolean[placesArrayList.size()];
    		Arrays.fill(isDeleted, false);
    		
    		while(!placesArrayList.isEmpty()) {
    		//	for(int i:dist) {
    		//		System.out.print(i + " ");
    		//	}
    		//	System.out.println();
    			
    			//pick Place in the ArrayList with smallest dist[]
    			int smallDistIndex = 0;
    			for(int i  = 0;i < isDeleted.length;i++) {
    				if(!isDeleted[i]) {
    					smallDistIndex = i;
    					break;
    				} 
    			}
    			
    			for(int i = 0;i < placesArrayList.size();i++) {
    				int currentIndex = placesIndexList.indexOf( placesArrayList.get(i) );
    				if(dist[smallDistIndex] > dist[currentIndex]) {
    					smallDistIndex = currentIndex;
    				}
    			}
    			
    			// all remaining nodes are inaccessible
    			if(dist[smallDistIndex] == Integer.MAX_VALUE) {
    				break;
    			}
    			    			
    			//remove chosenPlace from the placesArrayList
    			Place chosenPlace = placesIndexList.get(smallDistIndex);
    			placesArrayList.remove( chosenPlace );
    			isDeleted[smallDistIndex] = true;
    			
    			//find the chosenPlace's adjacent place
    			Set<Road> adjacentRoads = chosenPlace.toRoads();
    			Iterator<Road> iterRoad = adjacentRoads.iterator();
    			while(iterRoad.hasNext()) {
    				Road road = iterRoad.next();
    				Place otherPlace = null;
    				if(road.firstPlace().equals(chosenPlace)) {
    					otherPlace = road.secondPlace();
    				} else {
    					otherPlace = road.firstPlace();
    				}
    				
    				//get otherPlace's index
    				int otherPlaceIndex = placesIndexList.indexOf(otherPlace);
    				
    				//calculate the new distance
    				int alternative = dist[smallDistIndex] + road.length();
    				if(alternative < dist[otherPlaceIndex]) {
    					dist[otherPlaceIndex] = alternative;
    					previous[otherPlaceIndex] = smallDistIndex;
    				}
    			}
    		}
    		
    		distance = dist[placesIndexList.indexOf(endPlace)];
    		
    		if(distance == Integer.MAX_VALUE) {
    			return -1;
    		}
    		
    		//setChosen, set the shortest path
    		int secondPlaceIndex = placesIndexList.indexOf( this.endPlace );
    		Place secondPlace = this.endPlace;
    		
    		while(previous[secondPlaceIndex] != -1) {
    			Place firstPlace = placesIndexList.get(previous[secondPlaceIndex]);
    			Road chosenRoad = secondPlace.roadTo( firstPlace );
    			((RoadImpl)chosenRoad).setChosen(true);
    			
    			secondPlace = firstPlace;
    			secondPlaceIndex = previous[secondPlaceIndex];
    		}
    		
    		//for(Road r:roads){
    		//	System.out.println(r.roadName() +":" + r.isChosen());
    		//}
    		
    		return distance;
    }


    //Return a string describing this map
    //Returns a string that contains (in this order):
    //for each place in the map, a line (terminated by \n)
    //  PLACE followed the toString result for that place
    //for each road in the map, a line (terminated by \n)
    //  ROAD followed the toString result for that road
    //if a starting place has been defined, a line containing
    //  START followed the name of the starting-place (terminated by \n)
    //if an ending place has been defined, a line containing
    //  END followed the name of the ending-place (terminated by \n)
    public String toString() {
    		String tmpString = new String();
    		
    		//PLACE followed the toString result for that place
    		Iterator<Place> iterPlace = places.iterator();
    		while(iterPlace.hasNext()) {
    			tmpString += "PLACE " + iterPlace.next().toString() +"\n";
    		}
    		
    		//ROAD followed the toString result for that road
    		Iterator<Road> iterRoad = roads.iterator();
    		while(iterRoad.hasNext()) {
    			tmpString += "ROAD " + iterRoad.next().toString() +"\n";
    		}
    		
    		//START followed the name of the starting-place
    		if(this.startPlace != null) {
    			tmpString += "START " + this.startPlace.getName() + "\n";
    		}
    		
    		//END followed the name of the ending-place
    		if(this.endPlace != null) {
    			tmpString += "END " + this.endPlace.getName() + "\n";
    		}
    		
    		return tmpString;
    }
}
