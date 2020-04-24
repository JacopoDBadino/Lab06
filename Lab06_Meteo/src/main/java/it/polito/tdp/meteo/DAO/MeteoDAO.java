package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(String mese, String localita) {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione WHERE (SUBSTRING(DATA, 6, 2)=?) AND localita=? ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();
		int cnt = 0;

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, mese);
			st.setString(2, localita);

			ResultSet rs = st.executeQuery();

			while (rs.next() && cnt < 15) {

				cnt++;
				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public Set<String> getLocalita() {

		final String sql = "SELECT Localita FROM situazione";

		Set<String> localita = new HashSet<String>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				String r = rs.getString("Localita");
				localita.add(r);
			}

			conn.close();
			return localita;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public String getUmiditaMedia(String mese) {

		final String sql = "SELECT Localita, SUM(Umidita)/COUNT(Umidita) FROM situazione WHERE (SUBSTRING(DATA, 6, 2)=?) GROUP BY localita";

		String umidita = new String("");

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, mese);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if (umidita.equals(""))
					umidita += rs.getString("Localita") + " " + rs.getDouble("SUM(Umidita)/COUNT(UMIDITA)");
				else
					umidita += "\n" + rs.getString("Localita") + " " + rs.getDouble("SUM(Umidita)/COUNT(UMIDITA)");

			}

			conn.close();
			return umidita;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public List<Rilevamento> getAllRilevamentiMese(String mese) {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione WHERE (SUBSTRING(DATA, 6, 2)=?) ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();
		int cnt = 0;

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, mese);

			ResultSet rs = st.executeQuery();

			while (rs.next() && cnt < 15) {

				cnt++;
				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
