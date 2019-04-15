package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;

public class MeteoDAO {

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
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
	
	
	public List<Citta> getAllCitta() {
		
		final String sql = "SELECT DISTINCT localita FROM situazione ORDER BY localita";
		
		List<Citta> result = new ArrayList<Citta>();
		
		try {
			
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				Citta c = new Citta(rs.getString("localita"));
				result.add(c);
			}
			
			conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public Double getUmiditaMedia(int mese, Citta citta) {
		
		final String sql = "SELECT AVG(Umidita) AS U FROM situazione\n" + 
				"WHERE Localita = ?\n" + 
				"AND MONTH(DATA)= ?";
		
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, citta.getNome());
			st.setInt(2, mese);
			
			ResultSet rs = st.executeQuery();
			
			rs.next();
			
			Double u = rs.getDouble("U");
			
			conn.close();
			
			return u;
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	
	
	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, Citta citta) {
		
		final String sql = "SELECT * FROM situazione \n" + 
				"WHERE localita = ?\n" + 
				"AND MONTH(DATA) = ?\n" + 
				"ORDER BY DATA ASC ";
		
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();
		
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, citta.getNome());
			st.setInt(2, mese);
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
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
