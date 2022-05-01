package solution;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import baseclasses.CabinCrew;
import baseclasses.Crew;
import baseclasses.DataLoadingException;
import baseclasses.ICrewDAO;
import baseclasses.Pilot;

/**
 * The CrewDAO is responsible for loading data from JSON-based crew files 
 * It contains various methods to help the scheduler find the right pilots and cabin crew
 */
public class CrewDAO implements ICrewDAO {
	
	List<CabinCrew> cabinCrew = new ArrayList<>();
	List<Pilot> pilot = new ArrayList<>();
	List<Crew> crew = new ArrayList<>();

	/**
	 * Loads the crew data from the specified file, adding them to the currently loaded crew
	 * Multiple calls to this function, perhaps on different files, would thus be cumulative
	 * @param p A Path pointing to the file from which data could be loaded
	 * @throws DataLoadingException if anything goes wrong. The exception's "cause" indicates the underlying exception
	 */
	@Override
	public void loadCrewData(Path p) throws DataLoadingException {
		
		try {
			
			BufferedReader br = Files.newBufferedReader(p);
			String json = ""; String line = "";
			while ((line = br.readLine ()) != null) { json = json + line; }
			
			
			JSONObject root = new JSONObject(json);
			JSONArray pilots = root.getJSONArray("pilots");
			JSONArray crewCabin = root.getJSONArray("cabincrew");
			
			
			for (int i = 0; i < pilots.length(); i++ ) {
				
				
				JSONArray typeRatings = pilots.getJSONObject(i).getJSONArray("type_ratings");
				String foreName = pilots.getJSONObject(i).getString("forename");
				String surName = pilots.getJSONObject(i).getString("surname");
				String homeBase = pilots.getJSONObject(i).getString("home_airport");
				String rank = pilots.getJSONObject(i).getString("rank");
				
			
				Pilot pl = new Pilot();
				
				pl.setForename(foreName);
				pl.setSurname(surName);
				pl.setRank(Pilot.Rank.valueOf(rank));
				pl.setHomeBase(homeBase);
				
				
				for(int j = 0; j < typeRatings.length(); j++ ) {
					
				String typeRating = typeRatings.getString(j);
			
			
					pl.setQualifiedFor(typeRating);
					
				}
				
				
				pilot.add(pl);
				crew.add(pl);
				
				
			
				}
			
			for (int i = 0; i < crewCabin.length(); i++) {
				
				
				
				String foreName = crewCabin.getJSONObject(i).getString("forename");
				String surName = crewCabin.getJSONObject(i).getString("surname");
				String homeBase = crewCabin.getJSONObject(i).getString("home_airport");
				JSONArray typeRatings = crewCabin.getJSONObject(i).getJSONArray("type_ratings");
				
				
				CabinCrew cabinC = new CabinCrew();
				
				
				cabinC.setForename(foreName);
				cabinC.setSurname(surName);
				cabinC.setHomeBase(homeBase);
				
				
			
				for(int j = 0; j < typeRatings.length(); j++ ) {
					
					String typeRating = typeRatings.getString(j);
					
						cabinC.setQualifiedFor(typeRating);
						
						
					}
				
				cabinCrew.add(cabinC);
				crew.add(cabinC);
			
					
	}
			
			
		}
		
		catch(IOException | NullPointerException  ioe){
			throw new DataLoadingException(ioe);
		}
		
		catch(JSONException e) {
			throw new DataLoadingException(e);
		}
		
		
	}
	
	/**
	 * Returns a list of all the cabin crew based at the airport with the specified airport code
	 * @param airportCode the three-letter airport code of the airport to check for
	 * @return a list of all the cabin crew based at the airport with the specified airport code
	 */
	@Override
	public List<CabinCrew> findCabinCrewByHomeBase(String airportCode) {
	
		List<CabinCrew> cHomeBaseList = new ArrayList<>();
		
			try {
				for (int i = 0; i < cabinCrew.size(); i++)
		
					if (cabinCrew.get(i).getHomeBase().contains(airportCode))  {
					cHomeBaseList.add(cabinCrew.get(i));
					
				}
		}
			catch (Exception e) {
				return Collections.emptyList();
			
		}
		
		
		return cHomeBaseList;
	}

	/**
	 * Returns a list of all the cabin crew based at a specific airport AND qualified to fly a specific aircraft type
	 * @param typeCode the type of plane to find cabin crew for
	 * @param airportCode the three-letter airport code of the airport to check for
	 * @return a list of all the cabin crew based at a specific airport AND qualified to fly a specific aircraft type
	 */
	@Override
	public List<CabinCrew> findCabinCrewByHomeBaseAndTypeRating(String typeCode, String airportCode) {
		
		List<CabinCrew> cabinHomeAndTypeList = new ArrayList<>();
		
		try {
			for (int i = 0; i < cabinCrew.size(); i++)
				if (cabinCrew.get(i).getHomeBase().contains(airportCode) & (cabinCrew.get(i).getTypeRatings().contains(typeCode))) {
					cabinHomeAndTypeList.add(cabinCrew.get(i));
				}
		}
		catch (Exception e) {
			return Collections.emptyList();
		}
		
		
		return cabinHomeAndTypeList;
	}

	/**
	 * Returns a list of all the cabin crew currently loaded who are qualified to fly the specified type of plane
	 * @param typeCode the type of plane to find cabin crew for
	 * @return a list of all the cabin crew currently loaded who are qualified to fly the specified type of plane
	 */
	@Override
	public List<CabinCrew> findCabinCrewByTypeRating(String typeCode) {
		
		List<CabinCrew> cabinTypeRatingList = new ArrayList<>();
		
		try {
			for (int i = 0; i < cabinCrew.size(); i++) 
				
				if (cabinCrew.get(i).getTypeRatings().contains(typeCode)) {
		
				cabinTypeRatingList.add(cabinCrew.get(i));
				
			}
	}
		catch (Exception e) {
			return Collections.emptyList();
		
	}
	
	
		return cabinTypeRatingList;

		
	}

	/**
	 * Returns a list of all the pilots based at the airport with the specified airport code
	 * @param airportCode the three-letter airport code of the airport to check for
	 * @return a list of all the pilots based at the airport with the specified airport code
	 */
	@Override
	public List<Pilot> findPilotsByHomeBase(String airportCode) {
		
		
	List<Pilot> pHomeBaseList = new ArrayList<>();
		
		try {
			for (int i = 0; i < pilot.size(); i++)
				if (pilot.get(i).getHomeBase().contains(airportCode))  {
					pHomeBaseList.add(pilot.get(i));
				}
		}
		catch (Exception e) {
			return Collections.emptyList();
			
		}
		
		
		return pHomeBaseList;
		
		
	}

	/**
	 * Returns a list of all the pilots based at a specific airport AND qualified to fly a specific aircraft type
	 * @param typeCode the type of plane to find pilots for
	 * @param airportCode the three-letter airport code of the airport to check for
	 * @return a list of all the pilots based at a specific airport AND qualified to fly a specific aircraft type
	 */
	@Override
	public List<Pilot> findPilotsByHomeBaseAndTypeRating(String typeCode, String airportCode) {
		
	List<Pilot> pilotHomeAndTypeList = new ArrayList<>();
		
		try {
			for (int i = 0; i < pilot.size(); i++)
				if (pilot.get(i).getHomeBase().contains(airportCode) & (pilot.get(i).getTypeRatings().contains(typeCode))) {
					pilotHomeAndTypeList.add(pilot.get(i));
				}
		}
		catch (Exception e) {
			return Collections.emptyList();
		}
		
		
		
		return pilotHomeAndTypeList;	
		
	}

	/**
	 * Returns a list of all the pilots currently loaded who are qualified to fly the specified type of plane
	 * @param typeCode the type of plane to find pilots for
	 * @return a list of all the pilots currently loaded who are qualified to fly the specified type of plane
	 */
	@Override
	public List<Pilot> findPilotsByTypeRating(String typeCode) {
		
	List<Pilot> pilotTypeRatingList = new ArrayList<>();
		
		try {
			for (int i = 0; i < pilot.size(); i++) 
				
				if (pilot.get(i).getTypeRatings().contains(typeCode)) {
		
				pilotTypeRatingList.add(pilot.get(i));
				
			}
	}
		catch (Exception e) {
			return Collections.emptyList();
		
	}
	
		
		return pilotTypeRatingList;
		
	}

	/**
	 * Returns a list of all the cabin crew currently loaded
	 * @return a list of all the cabin crew currently loaded
	 */
	@Override
	public List<CabinCrew> getAllCabinCrew() {
		
		List<CabinCrew> copyOfCabinCrew = new ArrayList<>();
		copyOfCabinCrew.addAll(cabinCrew);
		
		try {
			//System.out.println(cabinCrew);
			return copyOfCabinCrew;
		}
		catch(Exception e) {
			return Collections.emptyList();
		}
	}
	/**
	 * Returns a list of all the crew, regardless of type
	 * @return a list of all the crew, regardless of type
	 */
	@Override
	public List<Crew> getAllCrew() {
		
		List<Crew> copyOfCrew = new ArrayList<>();
		copyOfCrew.addAll(crew);
		
		try {
			//System.out.println(crew);
			return copyOfCrew;
		}
		catch(Exception e) {
			return Collections.emptyList();
		}
	}
	/**
	 * Returns a list of all the pilots currently loaded
	 * @return a list of all the pilots currently loaded
	 */
	@Override
	public List<Pilot> getAllPilots() {
		
		List<Pilot> copyOfPilot = new ArrayList<>();
		copyOfPilot.addAll(pilot);
		
		try {
			//System.out.println(pilot);
			return copyOfPilot;
		}
		catch(Exception e) {
			return Collections.emptyList();
		}
	
	}

	@Override
	public int getNumberOfCabinCrew() {
		
		int numberOfCabinCrew = cabinCrew.size();
		return numberOfCabinCrew;
	}

	/**
	 * Returns the number of pilots currently loaded
	 * @return the number of pilots currently loaded
	 */
	@Override
	public int getNumberOfPilots() {
		
		int numberOfPilots = pilot.size();
		
		return numberOfPilots;
	}

	/**
	 * Unloads all of the crew currently loaded, ready to start again if needed
	 */
	@Override
	public void reset() {
		
		crew.clear();
		cabinCrew.clear();
		pilot.clear();
		
		// TODO Auto-generated method stub

	}

}
