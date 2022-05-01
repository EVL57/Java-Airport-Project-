package solution;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.DuplicateFormatFlagsException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import baseclasses.DataLoadingException;
import baseclasses.IRouteDAO;
import baseclasses.Route;

/**
 * The RouteDAO parses XML files of route information, each route specifying
 * where the airline flies from, to, and on which day of the week
 */
public class RouteDAO implements IRouteDAO {
	
	List<Route> routes = new ArrayList<>();

	/**
	 * Finds all flights that depart on the specified day of the week
	 * @param dayOfWeek A three letter day of the week, e.g. "Tue"
	 * @return A list of all routes that depart on this day
	 */
	@Override
	public List<Route> findRoutesByDayOfWeek(String dayOfWeek) {
		
		List<Route> routesByDay = new ArrayList<>();
		
		try {
			for (int i = 0; i < routes.size(); i++) 
				
				if (routes.get(i).getDayOfWeek().contains(dayOfWeek)) {
		
				routesByDay.add(routes.get(i));
				
			}
	}
		catch (Exception e) {
			return Collections.emptyList();
		
	}
	
	
		return routesByDay;
	
	}

	/**
	 * Finds all of the flights that depart from a specific airport on a specific day of the week
	 * @param airportCode the three letter code of the airport to search for, e.g. "MAN"
	 * @param dayOfWeek the three letter day of the week code to searh for, e.g. "Tue"
	 * @return A list of all routes from that airport on that day
	 */
	@Override
	public List<Route> findRoutesByDepartureAirportAndDay(String airportCode, String dayOfWeek) {
		
	List<Route> routesByDepartingAirportDay= new ArrayList<>();
		
		try {
			for (int i = 0; i < routes.size(); i++) 
				if (routes.get(i).getDepartureAirportCode().contains(airportCode) & routes.get(i).getDayOfWeek().contains(dayOfWeek)) {
				routesByDepartingAirportDay.add(routes.get(i));
				
			}
	}
		catch (Exception e) {
			return Collections.emptyList();
		
	}
	
		
		return routesByDepartingAirportDay;
		

	}

	/**
	 * Finds all of the flights that depart from a specific airport
	 * @param airportCode the three letter code of the airport to search for, e.g. "MAN"
	 * @return A list of all of the routes departing the specified airport
	 */
	@Override
	public List<Route> findRoutesDepartingAirport(String airportCode) {
		
	List<Route> routesByDepartingAirport = new ArrayList<>();
		
		try {
			for (int i = 0; i < routes.size(); i++) 
				
				if (routes.get(i).getDepartureAirportCode().contains(airportCode)) {
		
				routesByDepartingAirport.add(routes.get(i));
				
			}
	}
		catch (Exception e) {
			return Collections.emptyList();
		
	}
	
		
		
		return routesByDepartingAirport;
		
	}

	/**
	 * Finds all of the flights that depart on the specified date
	 * @param date the date to search for
	 * @return A list of all routes that depart on this date
	 */
	@Override
	public List<Route> findRoutesbyDate(LocalDate date) {
		
	List<Route> routesByDate = new ArrayList<>();
			
		try {
				for (int i = 0; i < routes.size(); i++) {
					if(date.getDayOfWeek().toString().startsWith(routes.get(i).getDayOfWeek().toString().toUpperCase())) {	
				routesByDate.add(routes.get(i));
					
					}
			}
		}
		catch (Exception e) {
			
			return Collections.emptyList();
			
		
	}
		
		return routesByDate;
	}

	/**
	 * Returns The full list of all currently loaded routes
	 * @return The full list of all currently loaded routes
	 */
	@Override
	public List<Route> getAllRoutes() {
		
		List<Route> copyOfRoutes = new ArrayList<>();
		copyOfRoutes.addAll(routes);
		
		
		try {
			return copyOfRoutes;
		}
		catch(Exception e) {
			return Collections.emptyList();
		}
		
		
		
	}

	/**
	 * Returns The number of routes currently loaded
	 * @return The number of routes currently loaded
	 */
	@Override
	public int getNumberOfRoutes() {
		
		int numberOfRoutes = routes.size();
		
		
		return numberOfRoutes;
	}

	/**
	 * Loads the route data from the specified file, adding them to the currently loaded routes
	 * Multiple calls to this function, perhaps on different files, would thus be cumulative
	 * @param p A Path pointing to the file from which data could be loaded
	 * @throws DataLoadingException if anything goes wrong. The exception's "cause" indicates the underlying exception
	 */
	@Override
	public void loadRouteData(Path p) throws DataLoadingException {
		
		try {
	
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			String path = p.toString();
			File file = new File(path);
			InputStream inputStream = new FileInputStream(file);
			Document doc = db.parse(inputStream);
			
		
			Element root = doc.getDocumentElement();
			
			NodeList children = root.getChildNodes();
			
			for(int i = 0; i < children.getLength(); i++) {
				
				Route r = new Route();
			
				Node c = children.item(i);
				
				if(c.getNodeName().equals("Route")) {
					
					NodeList grandChildren = c.getChildNodes();
					for(int j = 0; j < grandChildren.getLength(); j++ ) {
					
						Node d = grandChildren.item(j);
						
						if(d.getNodeName().equals("FlightNumber")) {
							
							int flightNumber = Integer.parseInt(d.getChildNodes().item(0).getNodeValue());
							
							r.setFlightNumber(flightNumber);
						
							
							
						}else if(d.getNodeName().equals("DayOfWeek")) {
								
								String dayOfWeek = d.getChildNodes().item(0).getNodeValue();
						
								r.setDayOfWeek(dayOfWeek);
								
								
	
								
							}else if(d.getNodeName().equals("DepartureTime")) {
								
								LocalTime departureTime = LocalTime.parse(d.getChildNodes().item(0).getNodeValue());
								
								r.setDepartureTime(departureTime);
								
								
								
								
							}else if(d.getNodeName().equals("DepartureAirport")) {
								
								String departureAirport = d.getChildNodes().item(0).getNodeValue();
								
								r.setDepartureAirport(departureAirport);
								
								
								
							}else if(d.getNodeName().equals("DepartureAirportIATACode")) {
								
								String departureAirportCode  = d.getChildNodes().item(0).getNodeValue();
								
								r.setDepartureAirportCode(departureAirportCode);
								
								
							}else if(d.getNodeName().equals("ArrivalTime")) {
								
								LocalTime arrivalTime = LocalTime.parse(d.getChildNodes().item(0).getNodeValue());
								
								r.setArrivalTime(arrivalTime);
								
								
							}else if (d.getNodeName().equals("ArrivalAirport")) {
								
								String arrivalAirport = d.getChildNodes().item(0).getNodeValue();
								
								r.setArrivalAirport(arrivalAirport);
								
								
							}else if (d.getNodeName().equals("ArrivalAirportIATACode")) {
								
								String arrivalAirportCode  = d.getChildNodes().item(0).getNodeValue();
								
								r.setArrivalAirportCode(arrivalAirportCode);
								
								
							}else if (d.getNodeName().equals("Duration")) {
								
								Duration duration = Duration.parse(d.getChildNodes().item(0).getNodeValue());
								
								r.setDuration(duration);
								
							
							}
					
						}
					
						routes.add(r);
				
					}
					
				}	
		}
		
		
		catch (ParserConfigurationException | SAXException | IOException  | IllegalArgumentException | DateTimeParseException | NullPointerException  e) {
			throw new DataLoadingException (e);
		}
		
	}

	/**
	 * Unloads all of the crew currently loaded, ready to start again if needed
	 */
	@Override
	public void reset() {
		
		
		routes.clear();

	}

}
