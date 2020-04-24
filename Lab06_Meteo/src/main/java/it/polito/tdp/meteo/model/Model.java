package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	MeteoDAO dao;

	public Model() {
		dao = new MeteoDAO();
	}

	public String getUmiditaMedia(String mese) {
		String s = dao.getUmiditaMedia(mese);
		return s;
	}

	public String intToString(int value) {
		if (value < 10)
			return "0" + String.valueOf(value);

		return String.valueOf(value);
	}

	List<Rilevamento> soluzione;
	List<Rilevamento> parziale;
	List<Citta> dati;
	int costoBst;

	public String trovaSequenza(int mese) {

		String esito = "";
		soluzione = new ArrayList<Rilevamento>();
		parziale = new ArrayList<Rilevamento>();
		costoBst = -1;

		dati = new ArrayList<Citta>();
		dati.add(new Citta("Genova", dao.getAllRilevamentiLocalitaMese(intToString(mese), "Genova")));
		dati.add(new Citta("Torino", dao.getAllRilevamentiLocalitaMese(intToString(mese), "Torino")));
		dati.add(new Citta("Milano", dao.getAllRilevamentiLocalitaMese(intToString(mese), "Milano")));

		ricorsiva(0, parziale);

		for (Rilevamento r : soluzione)
			if (esito.equals(""))
				esito += r;
			else
				esito += "\n" + r;

		esito += "\nCosto totale: " + costoBst;

		return esito;
	}

	public void ricorsiva(int livello, List<Rilevamento> parziale) {

		if (costoBst != -1 && calcolaCosto(parziale) > costoBst)
			return;

		if (livello == 15) {
			if (costoBst == -1 || calcolaCosto(parziale) < costoBst)
				if (controllaSoluzione(parziale) == true) {
					soluzione = new ArrayList<Rilevamento>(parziale);
					System.out.println("Soluzione Aggiornata");
					System.out.println(parziale);
					System.out.println(calcolaCosto(parziale) + "\n\n");
				}
			costoBst = calcolaCosto(parziale);
			return;
		}

		for (Citta c : dati) {

			if (c.getCounter() < 6) {

				Rilevamento r = c.getRilevamenti().get(livello);

				parziale.add(r);
				c.increaseCounter();

				ricorsiva(livello + 1, parziale);

				c.setCounter(c.getCounter() - 1);
				parziale.remove(parziale.size() - 1);
			}

		}

	}

	public int calcolaCosto(List<Rilevamento> parz) {

		int costo = 0;

		for (int i = 0; i < parziale.size(); i++) {
			costo += parziale.get(i).getUmidita();
			if ((i != 0) && (parziale.get(i).getLocalita().equals((parziale).get(i - 1).getLocalita()) == false))
				costo += 100;

		}

		return costo;
	}

	public Citta getCittaPerNome(List<Citta> dati, String nome) {
		for (Citta c : dati)
			if (c.getNome().equals(nome))
				return c;
		return null;
	}

	public boolean controllaSoluzione(List<Rilevamento> parz) {
		Set<String> citta = new HashSet<String>();
		int cont = 0;

		for (int i = 0; i < parz.size(); i++) {

			if (citta.contains(parz.get(i).getLocalita()) == false)
				citta.add(parz.get(i).getLocalita());

			if (i != 0) {
				if (parz.get(i).getLocalita().equals(parz.get(i - 1).getLocalita())) {
					cont++;
				} else {
					if (cont < 2)
						return false;
					else
						cont = 0;
				}

			}

		}

		if (citta.size() != 3)
			return false;

		return true;
	}

}
