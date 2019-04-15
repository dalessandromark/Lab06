package it.polito.tdp.meteo;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	private List<Citta> leCitta;
	private MeteoDAO dao;
	private List<Citta> best;

	public Model() {
		dao = new MeteoDAO();
		leCitta = dao.getAllCitta();
	}
	
	public List<Citta> getAllCitta() {
		return leCitta;
	}

	public Double getUmiditaMedia(int mese, Citta citta) {
		return dao.getUmiditaMedia(mese, citta);
	}
	
	public List<Citta> trovaSequenza(int mese) {
		
		List<Citta> parziale = new ArrayList<Citta>();
		this.best = null;
		
		for(Citta c : leCitta) {
			c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, c));
		}
		
		trova(parziale, 0);
		return best;
	}

	private void trova(List<Citta> parziale, int livello) {
		
		if(livello == NUMERO_GIORNI_TOTALI) {
			
			Double costo = punteggioSoluzione(parziale);
			if(best==null || costo<punteggioSoluzione(best)) {
				best = new ArrayList<Citta>(parziale);
			}
			
		} else {
			
			for(Citta prova : leCitta) {
				
				if(controllaParziale(prova, parziale)) {
					parziale.add(prova);
					trova(parziale, livello+1);
					parziale.remove(parziale.size()-1);
				}
			}
		}
		
	}

	public Double punteggioSoluzione(List<Citta> soluzioneCandidata) {

		double score = 0.0;
		
		for(int giorno=1; giorno<=NUMERO_GIORNI_TOTALI; giorno++) {
			//Dove mi trovo?
			Citta c = soluzioneCandidata.get(giorno-1);
			
			//Che umidita ho quel giorno in quella citta
			double umid = c.getRilevamenti().get(giorno-1).getUmidita();
			
			score += umid;
		}
		
		for(int giorno=2; giorno<=NUMERO_GIORNI_TOTALI; giorno++) {
			if(!soluzioneCandidata.get(giorno-1).equals(soluzioneCandidata.get(giorno-2))) {
				score += COST;
			}
		}
		
		return score;
	}

	private boolean controllaParziale(Citta prova, List<Citta> parziale) {
		
		//Verifico num max di giorni
		int conta = 0;
		for(Citta precedente : parziale) {
			if(precedente.equals(prova))
				conta++;
		}
		if(conta >= NUMERO_GIORNI_CITTA_MAX)
			return false;
		
		//Verifico num giorni minimi
		if(parziale.size() == 0)
			return true;
		
		if(parziale.size() == 1 || parziale.size() == 2) 
			return parziale.get(parziale.size()-1).equals(prova);
		
		if(parziale.get(parziale.size()-1).equals(prova))
			return true;
		
		//Se cambio citta
		if(parziale.get(parziale.size() - 1).equals(parziale.get(parziale.size() - 2)) 
				&& parziale.get(parziale.size() - 2).equals(parziale.get(parziale.size() - 3)))
			return true;
	
		return false;
		
		
	}

}
