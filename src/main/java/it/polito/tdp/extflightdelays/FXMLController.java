package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController
{
	private Model model;

	@FXML private ResourceBundle resources;

	@FXML private URL location;

	@FXML private TextArea txtResult;

	@FXML private TextField distanzaMinima;

	@FXML private Button btnAnalizza;

	@FXML private ComboBox<Airport> cmbBoxAeroportoPartenza;

	@FXML private Button btnAeroportiConnessi;

	@FXML private TextField numeroVoliTxtInput;

	@FXML private Button btnCercaItinerario;

//	private final String ERRORE = "\n\nERRORE! verificare che i dati inseriti siano corretti";

	@FXML void doAnalizzaAeroporti(ActionEvent event)
	{
		Integer x;
		try
		{
			x = Integer.parseInt(this.distanzaMinima.getText());
			if(x <= 0)
				throw new NumberFormatException();
		}
		catch(NumberFormatException nfe)
		{
			this.txtResult.appendText("\n\nErrore, inserire un numero corretto");
			return;
		} 

		//resetto testo
		this.txtResult.clear();
    	this.txtResult.appendText("Crea grafo...\n");

    	//creo grafo
    	this.model.creaGrafo(x);
    	txtResult.appendText(String.format("\nGRAFO CREATO CON:\n#Vertici: %d\n#Archi: %d",
				this.model.getNumVertici(),
				this.model.getNumArchi()));

		//cliccabili
    	this.btnAeroportiConnessi.setDisable(false);
    	this.btnCercaItinerario.setDisable(false);
    	this.cmbBoxAeroportoPartenza.setDisable(false);
		this.numeroVoliTxtInput.setDisable(false);

		//aggiungo risultati alla combobox 
		this.cmbBoxAeroportoPartenza.getItems().clear();
		this.cmbBoxAeroportoPartenza.getItems().addAll(this.model.getVertici()); 
	}

	@FXML void doCalcolaAeroportiConnessi(ActionEvent event)
	{
		Airport partenza = this.cmbBoxAeroportoPartenza.getValue(); 
		if (partenza == null)
		{
			this.txtResult.appendText("\n\nErrore, scegliere elemento dalla lista");
			return;
		} 
		
		this.txtResult.appendText("\n\nAEROPORTI CONNESSI:\n" + this.model.connessi(partenza));
	}

	@FXML void doCercaItinerario(ActionEvent event)
	{
		Integer migliaMax;
		try
		{
			migliaMax = Integer.parseInt(this.numeroVoliTxtInput.getText());
			if(migliaMax <= 0)
				throw new NumberFormatException();
		}
		catch(NumberFormatException nfe)
		{
			this.txtResult.appendText("\n\nErrore, inserire un numero corretto");
			return;
		} 
		
		Airport partenza = this.cmbBoxAeroportoPartenza.getValue(); 
		if (partenza == null)
		{
			this.txtResult.appendText("\n\nErrore, scegliere elemento dalla lista");
			return;
		} 
		
		this.txtResult.appendText("\n\nITINERARIO CALCOLATO:\n" + this.model.calcolaItinerario(partenza, migliaMax));
	}

	@FXML void initialize()
	{
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
		assert distanzaMinima != null
				: "fx:id=\"distanzaMinima\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
		assert btnAnalizza != null
				: "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
		assert cmbBoxAeroportoPartenza != null
				: "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
		assert btnAeroportiConnessi != null
				: "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
		assert numeroVoliTxtInput != null
				: "fx:id=\"numeroVoliTxtInput\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
		assert btnCercaItinerario != null
				: "fx:id=\"btnCercaItinerario\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";

	}

	public void setModel(Model model)
	{
		this.model = model;
	}
}
