package it.polito.tdp.extflightdelays.model;

/*
 * classe Model preimpostata questo documento è soggetto ai relativi diritti di
 * ©Copyright giugno 2021
 */

import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.*;

import com.zaxxer.hikari.pool.HikariPool;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model
{
	private ExtFlightDelaysDAO dao;
	private Map<Integer, Airport> vertici;
	private Graph<Airport, DefaultWeightedEdge> grafo;

	public Model()
	{
		this.dao = new ExtFlightDelaysDAO();
	}

	public void creaGrafo(int distanzaMin)
	{
		// ripulisco mappa e grafo
		this.vertici = new HashMap<>();
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class); //

		this.dao.loadAllAirports(vertici); // riempio la mappa
		List<Adiacenza> adiacenze = new ArrayList<>(this.dao.getAdiacenze(vertici, distanzaMin));
		for (Adiacenza a : adiacenze)
		{
			Airport a1 = a.getA1();
			Airport a2 = a.getA2();
			if (!this.grafo.containsVertex(a1)) this.grafo.addVertex(a1);
			if (!this.grafo.containsVertex(a2)) this.grafo.addVertex(a2);

			if (!this.grafo.containsEdge(a1, a2)) Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			else this.grafo.setEdgeWeight(this.grafo.getEdge(a1, a2),
					(a.getPeso() + this.grafo.getEdgeWeight(this.grafo.getEdge(a1, a2))) / 2);
		}
	}

	public int getNumVertici()
	{
		return this.grafo.vertexSet().size();
	}

	public int getNumArchi()
	{
		return this.grafo.edgeSet().size();
	}

	public Collection<Airport> getVertici()
	{
		List<Airport> vertici = new ArrayList<>(this.grafo.vertexSet());
		vertici.sort((v1,v2)->v1.getId()-v2.getId());
		return vertici;
	}

	public Collection<DefaultWeightedEdge> getArchi()
	{
		return this.grafo.edgeSet();
	}

	public String connessi(Airport partenza)
	{
		String s = "";
		List<Airport> adiacenti = new ArrayList<>(Graphs.neighborListOf(this.grafo, partenza));
		adiacenti.sort((v1, v2) -> -Double.compare(this.grafo.getEdgeWeight(this.grafo.getEdge(v1, partenza)),
				this.grafo.getEdgeWeight(this.grafo.getEdge(v2, partenza))));
		for (Airport airport : adiacenti)
			s += "\n" + airport + " (" + this.grafo.getEdgeWeight(this.grafo.getEdge(airport, partenza)) + ")";
		return s;
	}

	/// RICORSIONE

	List<Airport> itinerario;
	Integer maxMiglia;
	Double distPercorsa; 

	public String calcolaItinerario(Airport partenza, int maxMiglia)
	{
		this.itinerario = new ArrayList<>();
		this.maxMiglia = maxMiglia;
		System.out.println(this.maxMiglia);
		this.distPercorsa = 0.0; 
		List<Airport> parziale = new ArrayList<>();
		parziale.add(partenza);

		this.cerca(parziale);
		
		return this.itinerario + "\n(" + this.distPercorsa + ")"; 
	}

	private void cerca(List<Airport> parziale)
	{ 
		if(calcolaMiglia(parziale) > maxMiglia)
			return; 
		
		if (parziale.size() > this.itinerario.size())
		{
			this.itinerario = new ArrayList<>(parziale);
			this.distPercorsa = this.calcolaMiglia(itinerario); 
		}

		Airport ultimo = parziale.get(parziale.size() - 1);
		for (Airport a : Graphs.neighborListOf(this.grafo, ultimo))
		{
			if (!parziale.contains(a))
			{
				parziale.add(a);
				this.cerca(parziale);
				parziale.remove(a);
			}
		}
	}

	private Double calcolaMiglia(List<Airport> lista)
	{
		Double sum = 0.0;
		if (lista.size() >= 2)
		{
			for (int i = 1; i < lista.size(); i++)
			{
				sum += this.grafo.getEdgeWeight(this.grafo.getEdge(lista.get(i - 1), lista.get(i)));
			}
		} 
		return sum;
	}

}