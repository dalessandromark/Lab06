package it.polito.tdp.meteo;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.bean.Citta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class MeteoController {
	
	private Model model;
	
	public void setModel(Model m) {
		this.model = m;
	}

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Integer> boxMese;

    @FXML
    private Button btnUmidita;

    @FXML
    private Button btnCalcola;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalcolaSequenza(ActionEvent event) {
    	txtResult.clear();
    	int mese = boxMese.getValue();
    	long start = System.nanoTime();
    	List<Citta> best = model.trovaSequenza(mese);
    	long stop = System.nanoTime();
    	txtResult.appendText("Sequenza ottima per il mese "+mese+":\n");
    	txtResult.appendText(best+"\n");
    	double score = model.punteggioSoluzione(best);
    	txtResult.appendText("Costo soluzione: "+score+"\n");
    	txtResult.appendText("Tempo di esucuzione per trovare la sequenza: "+((stop-start)/1e9)+" secondi");
    	
    }

    @FXML
    void doCalcolaUmidita(ActionEvent event) {
    	txtResult.clear();
    	int mese = boxMese.getValue();
    	
    	txtResult.appendText("Dati del mese "+mese+":\n");
    		
    	for(Citta c : model.getAllCitta()) {
    		Double u = model.getUmiditaMedia(mese, c);
    		txtResult.appendText(String.format("Città %s: Umidità %f \n", c.getNome(), u));
    	}

    }

    @FXML
    void initialize() {
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
        assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";
        
        for(int i=1; i<=12; i++) {
        	boxMese.getItems().add(i);
        }

    }
}
