package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;

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

		ricorsiva(0, 0, parziale);

		for (Rilevamento r : soluzione)
			if (esito.equals(""))
				esito += r;
			else
				esito += "\n" + r;

		return esito;
	}

	public void ricorsiva(int livello, int cons, List<Rilevamento> parziale) {

		if (costoBst != -1 && calcolaCosto(parziale) > costoBst)
			return;

		if (livello == 15) {
			if (costoBst == -1 || calcolaCosto(parziale) < costoBst)
				soluzione = new ArrayList<Rilevamento>(parziale);
			costoBst = calcolaCosto(parziale);
			return;
		}

		if (parziale.size() != 0 && cons < 3) {

			Citta c = getCittaPerNome(dati, parziale.get(parziale.size() - 1).getLocalita());

			if (c.getCounter() < 6) {

				Rilevamento r = c.getRilevamenti().get(livello);
				parziale.add(r);
				c.increaseCounter();

				if (parziale.size() > 1
						&& r.getLocalita().equals(parziale.get(parziale.size() - 2).getLocalita()) == false)
					ricorsiva(livello + 1, 1, parziale);
				else
					ricorsiva(livello + 1, cons + 1, parziale);

				c.setCounter(c.getCounter() - 1);
				parziale.remove(r);
			}

			return;
		}

		for (Citta c : dati) {

			if (c.getCounter() < 6) {

				Rilevamento r = c.getRilevamenti().get(livello);
				parziale.add(r);
				c.increaseCounter();

				if (parziale.size() > 1 && r.getLocalita() != parziale.get(parziale.size() - 2).getLocalita())
					ricorsiva(livello + 1, 1, parziale);
				else
					ricorsiva(livello + 1, cons + 1, parziale);

				c.setCounter(c.getCounter() - 1);
				parziale.remove(r);
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

}
