package solution;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;

import baseclasses.DataLoadingException;
import baseclasses.IPassengerNumbersDAO;

/**
 * The PassengerNumbersDAO is responsible for loading an SQLite database
 * containing forecasts of passenger numbers for flights on dates
 */
public class PassengerNumbersDAO implements IPassengerNumbersDAO {
	
	HashMap<String, Integer> passengerNumbers = new HashMap<String, Integer>();


	/**
	 * Returns the number of passenger number entries in the cache
	 * @return the number of passenger number entries in the cache
	 */
	@Override
	public int getNumberOfEntries() {
		
		int passengerEntries = passengerNumbers.size();
		
		return passengerEntries;
	}

	/**
	 * Returns the predicted number of passengers for a given flight on a given date, or -1 if no data available
	 * @param flightNumber The flight number of the flight to check for
	 * @param date the date of the flight to check for
	 * @return the predicted number of passengers, or -1 if no data available
	 */
	@Override
	public int getPassengerNumbersFor(int flightNumber, LocalDate date) {
		
		String number = String.valueOf(flightNumber);
	
		for (String i : passengerNumbers.keySet()) {
			
	
			int passengerNumber = passengerNumbers.get(i);
			
				if (i.toString().contains(date.toString()) & i.toString().contains(number)) {
					
					
					System.out.println(passengerNumber);
					
					return passengerNumber;
				}	
					}
			return -1;
			
	
	}

	/**
	 * Loads the passenger numbers data from the specified SQLite database into a cache for future calls to getPassengerNumbersFor()
	 * Multiple calls to this method are additive, but flight numbers/dates previously cached will be overwritten
	 * The cache can be reset by calling reset() 
	 * @param p The path of the SQLite database to load data from
	 * @throws DataLoadingException If there is a problem loading from the database
	 */
	@Override
	public void loadPassengerNumbersData(Path p) throws DataLoadingException {
		
		Connection c = null;
		
		String path = p.toString();
	
		try {
			
			c = DriverManager.getConnection("jdbc:sqlite:" + path);
			
			PreparedStatement s = c.prepareStatement("SELECT * FROM PassengerNumbers");
			
			ResultSet rs = s.executeQuery();
			
			while(rs.next()) {
				
				String key = (rs.getString("Date") + " " +  rs.getInt("flightNumber"));
				int loadEstimate = rs.getInt("LoadEstimate");
				
			
				
				passengerNumbers.put(key, loadEstimate);
				
				
			}
		
		}
	
		
		
		catch(SQLException | NullPointerException se) {
			throw new DataLoadingException(se);
		}
		
	}

	/**
	 * Removes all data from the DAO, ready to start again if needed
	 */
	@Override
	public void reset() {

		passengerNumbers.clear();
		

	}

}
