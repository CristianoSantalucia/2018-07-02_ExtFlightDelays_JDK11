package it.polito.tdp.extflightdelays.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.extflightdelays.model.Adiacenza;
import it.polito.tdp.extflightdelays.model.Airline;
import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Flight;

public class ExtFlightDelaysDAO
{

	public List<Airline> loadAllAirlines()
	{
		String sql = "SELECT * from airlines";
		List<Airline> result = new ArrayList<Airline>();

		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next())
			{
				result.add(new Airline(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRLINE")));
			}

			conn.close();
			return result;

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public void loadAllAirports(Map<Integer, Airport> vertici)
	{
		String sql = "SELECT * FROM airports";

		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next())
			{
				Airport airport = new Airport(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRPORT"),
						rs.getString("CITY"), rs.getString("STATE"), rs.getString("COUNTRY"), rs.getDouble("LATITUDE"),
						rs.getDouble("LONGITUDE"), rs.getDouble("TIMEZONE_OFFSET"));
				vertici.putIfAbsent(airport.getId(), airport);
			}

			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Flight> loadAllFlights()
	{
		String sql = "SELECT * FROM flights";
		List<Flight> result = new LinkedList<Flight>();

		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next())
			{
				Flight flight = new Flight(rs.getInt("ID"), rs.getInt("AIRLINE_ID"), rs.getInt("FLIGHT_NUMBER"),
						rs.getString("TAIL_NUMBER"), rs.getInt("ORIGIN_AIRPORT_ID"),
						rs.getInt("DESTINATION_AIRPORT_ID"),
						rs.getTimestamp("SCHEDULED_DEPARTURE_DATE").toLocalDateTime(), rs.getDouble("DEPARTURE_DELAY"),
						rs.getDouble("ELAPSED_TIME"), rs.getInt("DISTANCE"),
						rs.getTimestamp("ARRIVAL_DATE").toLocalDateTime(), rs.getDouble("ARRIVAL_DELAY"));
				result.add(flight);
			}

			conn.close();
			return result;

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Adiacenza> getAdiacenze(Map<Integer, Airport> vertici, int distanzaMin)
	{
		String sql = "SELECT ORIGIN_AIRPORT_ID id1, "
					+ "		 DESTINATION_AIRPORT_ID id2, "
					+ "		 AVG(DISTANCE) media "
					+ "FROM flights f "
					+ "GROUP BY id1, id2 "
					+ "HAVING media > ? ";
		List<Adiacenza> result = new ArrayList<>();

		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, distanzaMin);
			ResultSet rs = st.executeQuery();

			while (rs.next())
			{
				Airport a1 = vertici.get(rs.getInt("id1"));
				Airport a2 = vertici.get(rs.getInt("id2"));
				Double media = rs.getDouble("media");
				if (a1 != null && a2 != null)
				{
					result.add(new Adiacenza(a1, a2, media));
				}
			}
			conn.close();
			return result;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}
