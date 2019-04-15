package it.polito.tdp.meteo;

import java.time.Month;

import it.polito.tdp.meteo.bean.Citta;

public class TestModel {

	public static void main(String[] args) {

		Model m = new Model();
		
		Citta c = new Citta("Genova");	
		
		System.out.println(m.getUmiditaMedia(12, c));
		
		System.out.println(m.trovaSequenza(5));
		
//		System.out.println(m.trovaSequenza(4));
	}

}
