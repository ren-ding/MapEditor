import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Set;
import java.util.Iterator;

public class MapReaderWriter implements MapIo {
	public MapReaderWriter() {

	}
	
    //This class handles reading and writing map representations as 
    //described in the practical specification

    //Read the description of a map from the 
    //Reader r, and transfers it to Map, m.
    public void read (Reader r, Map m) 
      throws IOException, MapFormatException {
    		//check if the Reader or Map is null or not
    		if(m == null || r == null) {
    			throw new IOException();
    		}
    	
    		//use a buffered reader to transfer the file to the Map object
    		BufferedReader bufReader = new BufferedReader(r);
    		
    		String line = null;
    		while( (line = bufReader.readLine()) != null) {
    			String[] splitLine = line.split(" ");
    			int length = splitLine.length;
    			
    			if(length > 0) {
    				if(splitLine[0].equals("place")) {
    					if(length != 4) {
    						throw new MapFormatException(37,"length doesnot matched");
    					}
    					
    					//read places from the file
    					if(splitLine[2].matches("[0-9]+") && splitLine[3].matches("[0-9]+") ) {
    						try {
		    					m.newPlace(splitLine[1],
		    							   Integer.parseInt(splitLine[2]),
		    							   Integer.parseInt(splitLine[3]));
    						} catch(IllegalArgumentException iae) {
    							throw new MapFormatException(47,"IllegalArgumentException");
    						}
    					} else {
    						throw new MapFormatException(50,"NumberFormatException");
    					}
    				} else if(splitLine[0].equals("road")) {
    					if(length != 5) {
    						throw new MapFormatException(54,"length doesnot matched");
    					}
    					
    					//read roads from the file
    					if(splitLine[4].matches("[0-9]+") ) {
    						try {
		    					m.newRoad(m.findPlace(splitLine[1]),
		    							  m.findPlace(splitLine[2]),
		    							  splitLine[3],
		    							  Integer.parseInt(splitLine[4]));
    						} catch(IllegalArgumentException iae) {
    							throw new MapFormatException(65,"IllegalArgumentException");
    						}
    					} else {
    						throw new MapFormatException(68,"NumberFormatException");
    					}
    				} else if(splitLine[0].equals("start")) {
    					if(length != 2) {
    						throw new MapFormatException(72,"length doesnot matched");
    					}
    					
    					//read startPlace from the file
    					try {
    						m.setStartPlace(m.findPlace(splitLine[1]));
    					} catch(IllegalArgumentException iae) {
    						throw new MapFormatException(79,"IllegalArgumentException");
					}
    				} else if(splitLine[0].equals("end")) {
    					if(length != 2) {
    						throw new MapFormatException(83,"length doesnot matched");
    					}
    					
    					//read endPlace from the file
    					try {
    						m.setEndPlace(m.findPlace(splitLine[1]));
    					} catch(IllegalArgumentException iae) {
    						throw new MapFormatException(90,"IllegalArgumentException");
					}
    				} 
    			}
    		}
    }
    
    
    //Write a representation of the Map, m, to the Writer w.
    public void write(Writer w, Map m)
      throws IOException {
    		//check if the Writer or Map is null or not
    		if(w == null || m == null) {
    			throw new IOException();
    		}
    	
    		//use a buffered writer to transfer the Map object to the file
    		BufferedWriter bufWriter = new BufferedWriter(w);
    		
    		//write places to the file
    		Set<Place> places = m.getPlaces();
    		Iterator<Place> iterPlaces = places.iterator();
    		while(iterPlaces.hasNext()) {
    			Place place = iterPlaces.next();
    			bufWriter.write("place " 
    							+ place.getName() + " " 
    							+ place.getX() + " " 
    							+ place.getY());
    			bufWriter.newLine();
    		}
    		bufWriter.flush();
    			
    		//write roads to the file
    		Set<Road> roads = m.getRoads();
    		Iterator<Road> iterRoads = roads.iterator();
    		while(iterRoads.hasNext()) {
    			Road road = iterRoads.next();
    			bufWriter.write("road "
    							+ road.firstPlace().getName() + " "
    							+ road.secondPlace().getName() + " "
    							+ road.roadName() + " "
    							+ road.length());
    			bufWriter.newLine();
    		}
    		bufWriter.flush();
    			
    		//write startPlace to the file
	    	if(m.getStartPlace() != null) {
	    		bufWriter.write("start " + m.getStartPlace().getName());
	    		bufWriter.newLine();
	    	}
	    	
	    	//write endPlace to the file
	    	if(m.getEndPlace() != null) {
	    		bufWriter.write("end " + m.getEndPlace().getName());
	    		bufWriter.newLine();
	    	}
	    	bufWriter.flush();
    }
}
