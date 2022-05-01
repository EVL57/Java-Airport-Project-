package solution;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import baseclasses.Aircraft;
import baseclasses.DataLoadingException;
import baseclasses.IAircraftDAO;


/**
 * The AircraftDAO class is responsible for loading aircraft data from CSV files
 * and contains methods to help the system find aircraft when scheduling
 */
public class AircraftDAO implements IAircraftDAO {
	
	//The data structure we'll use to store the aircraft we've loaded
	List<Aircraft> aircraft = new ArrayList<>();
	
	/**
	 * Loads the aircraft data from the specified file, adding them to the currently loaded aircraft
	 * Multiple calls to this function, perhaps on different files, would thus be cumulative
	 * @param p A Path pointing to the file from which data could be loaded
	 * @throws DataLoadingException if anything goes wrong. The exception's "cause" indicates the underlying exception
     *
	 * Initially, this contains some starter code to help you get started in reading the CSV file...
	 */
	@Override
	public void loadAircraftData(Path p) throws DataLoadingException {	
		try {
			//open the file
			BufferedReader reader = Files.newBufferedReader(p);
			
			//read the file line by line
			String line = "";
			
			
			//skip the first line of the file - headers
			reader.readLine();
			
			
			while( (line = reader.readLine()) != null) {
				//each line has fields separated by commas, split into an array of fields
				String[] fields = line.split(",");
				
				//put some of the fields into variables: check which fields are where atop the CSV file itself
				String tailcode = fields[0];
				String type = fields[2];
				String model = fields[1];
				String manufacturer = fields[3].toUpperCase();
				String startingPosition = fields[4];
				int seats = Integer.parseInt(fields[5]);
				int cabinCrewRequired = Integer.parseInt(fields[6]);
				
				
				//create an Aircraft object, and set (some of) its properties
				Aircraft a = new Aircraft();
				a.setTailCode(tailcode);
				a.setTypeCode(type);
				a.setSeats(seats);
				a.setCabinCrewRequired(cabinCrewRequired);
				a.setManufacturer(Aircraft.Manufacturer.valueOf(manufacturer));
				a.setModel(model);
				a.setStartingPosition(startingPosition);
				
				
				
				
				//add the aircraft to our list
				aircraft.add(a);
				
				System.out.println("Aircraft: " + tailcode +  " of Model " + model + " is a " + type + " of " + manufacturer + " starting " + startingPosition + " with " + seats + " seats "  + cabinCrewRequired + " cabin crew required.");
			}
		}
		
		catch( IOException | ArrayIndexOutOfBoundsException  | NullPointerException | IllegalArgumentException ioe) {
			
			throw new  DataLoadingException(ioe);
			
		}
		

			
	
	}
	
	/**
	 * Returns a list of all the loaded Aircraft with at least the specified number of seats
	 * @param seats the number of seats required
	 * @return a List of all the loaded aircraft with at least this many seats
	 */
	@Override
	public List<Aircraft> findAircraftBySeats(int seats) {
		
		List<Aircraft> seatList = new ArrayList<>();
		try {
			
			for (int i = 0; i < aircraft.size(); i++) {
				if (aircraft.get(i).getSeats() >= (seats)) {
					seatList.add(aircraft.get(i));
					
				}
			}
			
		}
		
		catch (Exception e) {
			return Collections.emptyList();
		}
		return seatList;
	}
				

	/**
	 * Returns a list of all the loaded Aircraft that start at the specified airport code
	 * @param startingPosition the three letter airport code of the airport at which the desired aircraft start
	 * @return a List of all the loaded aircraft that start at the specified airport
	 */
	@Override
	public List<Aircraft> findAircraftByStartingPosition(String startingPosition) {
		
		List<Aircraft> positionList = new ArrayList<>();
		
		try {
			for (int i = 0; i < aircraft.size(); i++)
				if (aircraft.get(i).getStartingPosition().contains(startingPosition))  {
					positionList.add(aircraft.get(i));
				}
		}
		catch (Exception e) {
			return Collections.emptyList();
			
		}
		
		//System.out.println(positionList);
		return positionList;
	}

	/**
	 * Returns the individual Aircraft with the specified tail code.
	 * @param tailCode the tail code for which to search
	 * @return the aircraft with that tail code, or null if not found
	 */
	@Override
	public Aircraft findAircraftByTailCode(String tailCode) {
		
		Aircraft tailCodeAircraft = null;
	
		try {
			for (int i = 0; i < aircraft.size(); i++)
				if (aircraft.get(i).getTailCode().contains(tailCode))  {
					 tailCodeAircraft = aircraft.get(i);
					
				}
		}
		catch (Exception e) {
			return null;
			
		}
		return  tailCodeAircraft;
		
	}

	/**
	 * Returns a List of all the loaded Aircraft with the specified type code
	 * @param typeCode the type code of the aircraft you wish to find
	 * @return a List of all the loaded Aircraft with the specified type code
	 */
	@Override
	public List<Aircraft> findAircraftByType(String typeCode) {
		

		List<Aircraft> typeCodeList = new ArrayList<>();
		
		try {
			for (int i = 0; i < aircraft.size(); i++)
				if (aircraft.get(i).getTypeCode().contains(typeCode))  {
					typeCodeList.add(aircraft.get(i));
				}
		}
		catch (Exception e) {
			return Collections.emptyList();
			
		}
		System.out.println(typeCodeList);
		return typeCodeList;
	
	}

	/**
	 * Returns a List of all the currently loaded aircraft
	 * @return a List of all the currently loaded aircraft
	 */
	@Override
	public List<Aircraft> getAllAircraft() {
		
		List<Aircraft> copyOfAircraft = new ArrayList<>();
		copyOfAircraft.addAll(aircraft);
		
		
			try {
				return copyOfAircraft;
			}
			
			catch (Exception e) {
				return Collections.emptyList();
			}
	}

	/**
	 * Returns the number of aircraft currently loaded 
	 * @return the number of aircraft currently loaded
	 */
	@Override
	public int getNumberOfAircraft() {
		
		
		int numberOfAircraft = aircraft.size(); // get size of array, store it in variable
	//System.out.println(numberOfAircraft);
		
		return numberOfAircraft; // return size of array
	}

	/**
	 * Unloads all of the aircraft currently loaded, ready to start again if needed
	 */
	@Override
	public void reset() {
		
		aircraft.clear();


	}

}
