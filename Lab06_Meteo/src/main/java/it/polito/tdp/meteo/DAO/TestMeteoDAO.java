package it.polito.tdp.meteo.DAO;

import it.polito.tdp.meteo.model.Rilevamento;

public class TestMeteoDAO {

	public static void main(String[] args) {

		MeteoDAO dao = new MeteoDAO();
		for (Rilevamento r : dao.getAllRilevamentiLocalitaMese("11", "Genova"))
			System.out.println(r.getData());
		
		
	}

}
