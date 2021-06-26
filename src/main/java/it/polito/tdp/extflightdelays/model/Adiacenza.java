package it.polito.tdp.extflightdelays.model;

public class Adiacenza
{
	private Airport a1; 
	private Airport a2;
	private Double peso;
	public Adiacenza(Airport id1, Airport id2, Double peso)
	{
		this.a1 = id1;
		this.a2 = id2;
		this.peso = peso;
	}
	public Airport getA1()
	{
		return a1;
	}
	public void setId1(Airport id1)
	{
		this.a1 = id1;
	}
	public Airport getA2()
	{
		return a2;
	}
	public void setId2(Airport id2)
	{
		this.a2 = id2;
	}
	public Double getPeso()
	{
		return peso;
	}
	public void setPeso(Double peso)
	{
		this.peso = peso;
	}
	@Override public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a1 == null) ? 0 : a1.hashCode());
		result = prime * result + ((a2 == null) ? 0 : a2.hashCode());
		result = prime * result + ((peso == null) ? 0 : peso.hashCode());
		return result;
	}
	@Override public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Adiacenza other = (Adiacenza) obj;
		if (a1 == null)
		{
			if (other.a1 != null) return false;
		}
		else if (!a1.equals(other.a1)) return false;
		if (a2 == null)
		{
			if (other.a2 != null) return false;
		}
		else if (!a2.equals(other.a2)) return false;
		if (peso == null)
		{
			if (other.peso != null) return false;
		}
		else if (!peso.equals(other.peso)) return false;
		return true;
	}
	@Override public String toString()
	{
		return String.format("%s - %s (%d)", a1, a2, peso);
	}  
}
