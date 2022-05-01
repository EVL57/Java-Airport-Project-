package solution;

import java.nio.file.Paths;
import java.time.LocalDate;

import baseclasses.DataLoadingException;
import baseclasses.IAircraftDAO;
import baseclasses.ICrewDAO;
import baseclasses.IRouteDAO;
import baseclasses.IScheduler;
import baseclasses.IPassengerNumbersDAO;

/**
 * This class allows you to run the code in your classes yourself, for testing and development
 */
public class Main {
	
	public static void main(String[] args) throws DataLoadingException {	
		IAircraftDAO aircraft = new AircraftDAO();
		ICrewDAO  crew = new CrewDAO();
		IRouteDAO route = new RouteDAO();
		IPassengerNumbersDAO passengers = new PassengerNumbersDAO();
		IScheduler scheduler = new Scheduler();
		
				
		try {
			//Tells your Aircraft DAO to load this particular data file
			aircraft.loadAircraftData(Paths.get("./data/mini_aircraft.csv"));
			crew.loadCrewData(Paths.get("./data/mini_crew.json"));
			route.loadRouteData(Paths.get("./data/mini_routes.xml"));
			passengers.loadPassengerNumbersData(Paths.get("./data/mini_passengers.db"));
	
			
		}
		catch (NullPointerException e)
		{
			throw new DataLoadingException(e);
		}
		catch (DataLoadingException  dle) {
			System.err.println("Error loading aircraft data");
			dle.printStackTrace();
		}
		
	}
	
}
