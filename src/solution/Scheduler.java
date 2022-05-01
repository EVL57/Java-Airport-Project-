package solution;
import java.time.LocalDate;
import java.util.List;

import baseclasses.Aircraft;
import baseclasses.DoubleBookedException;
import baseclasses.FlightInfo;
import baseclasses.IAircraftDAO;
import baseclasses.ICrewDAO;
import baseclasses.IPassengerNumbersDAO;
import baseclasses.IRouteDAO;
import baseclasses.IScheduler;
import baseclasses.Schedule;


/**
 * The Scheduler class is responsible for deciding which aircraft and crew will be
 * used for each of an airline's flights in a specified period of time, referred to
 * as a "scheduling horizon". A schedule must have an aircraft, two pilots, and 
 * sufficient cabin crew for the aircraft allocated to every flight in the horizon 
 * to be valid.
 */
public class Scheduler implements IScheduler {
	
	
	
	

	/**
	 * Generates a schedule, providing you with ready-loaded DAO objects to get your data from
	 * @param aircraftDAO the DAO for the aircraft to be used when scheduling
	 * @param crewDAO the DAO for the crew to be used when scheduling
	 * @param routeDAO the DAO to use for routes when scheduling
	 * @param passengerNumbersDAO the DAO to use for passenger numbers when scheduling
	 * @param startDate the start of the scheduling horizon
	 * @param endDate the end of the scheduling horizon
	 * @return The generated schedule - which must happen inside 2 minutes
	 */
	@Override
	public Schedule generateSchedule(IAircraftDAO aircraftDAO, ICrewDAO crewDAO, IRouteDAO routeDAO, 
			IPassengerNumbersDAO passengerNumbersDAO, LocalDate startDate, LocalDate endDate) {
		
		Schedule sc = new Schedule(routeDAO, startDate, endDate);
		
		List<FlightInfo> flightInfoObjects = sc.getRemainingAllocations();
		
		for (int i = 0; i < flightInfoObjects.size(); i++) {
			for(int j = 0; j <aircraftDAO.getAllAircraft().size(); j++) {
				
				try {
					sc.allocateAircraftTo(aircraftDAO.getAllAircraft().get(i), flightInfoObjects.get(i));
				} catch (DoubleBookedException e) {
					e.printStackTrace();
				}
			
				
				
		
				
				
			
			}
		}
	
		return sc;
	}

}
