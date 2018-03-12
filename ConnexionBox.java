import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.scene.effect.Reflection;
import javafx.scene.paint.LinearGradient;
import javafx.scene.text.Font;
import javafx.scene.paint.CycleMethod;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;

import javafx.beans.binding.BooleanBinding;
import javax.naming.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;

import java.util.function.UnaryOperator;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

//import org.apache.commons.validator.routines.InetAddressValidator;

public class ConnexionBox extends Parent{

	private InputBox nom  = new InputBox("Nom");
	private InputBox ip  = new InputBox("IP");
	private InputBox port  = new InputBox("Port");
	private Button btnConnecte = new Button("Connexion");
	private Button btnDeconnecte = new Button("Deconnexion");
	private HBox hbBtn = new HBox(40);

	public ConnexionBox(){

		Rectangle rect = new Rectangle();
		rect.setWidth(80);
		rect.setHeight(40);
		rect.setArcWidth(15);
		rect.setArcHeight(15);

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(20, 20, 20, 20));
		grid.setVgap(20);
		grid.setHgap(20);
		grid.setPrefWidth(1000);
		grid.setPrefHeight(300);
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(50);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(50);
		grid.getColumnConstraints().addAll(column1,column2);

		ip.getInput().setText("127.0.0.1");
		port.getInput().setText("2346");

		grid.add(nom.getInputBox(),0,0);
		grid.add(ip.getInputBox(),0,1);
		grid.add(port.getInputBox(),1,1);

		btnConnecte.setMaxWidth(300);
		btnConnecte.setPrefWidth(300);
		btnConnecte.setMinWidth(100);
		btnConnecte.setAlignment(Pos.CENTER);

		btnDeconnecte.setMaxWidth(300);
		btnDeconnecte.setPrefWidth(300);
		btnDeconnecte.setMinWidth(100);
		btnDeconnecte.setAlignment(Pos.CENTER);


		hbBtn.setAlignment(Pos.CENTER);
		hbBtn.setPrefWidth(300);
		hbBtn.getChildren().add(btnConnecte);
		grid.add(hbBtn,1,0);


		this.setTranslateX(0);
		this.setTranslateY(0);
		this.getChildren().add(grid);

		BooleanBinding userField = Bindings.createBooleanBinding(()->{
			if (nom.getInput().getText().length() != 0){
				return true;
			}
			else{
				return false;
			}
		},nom.getInput().textProperty());
		BooleanBinding ipField   = Bindings.createBooleanBinding(()->{
			if (ip.getInput().getText().length() != 0){
				return true;
			}
			else{
				return false;
			}
		},ip.getInput().textProperty());
		BooleanBinding portField = Bindings.createBooleanBinding(()->{
			if (port.getInput().getText().length() != 0){
				return true;
			}
			else{
				return false;
			}
		},port.getInput().textProperty());

		btnConnecte.disableProperty().bind(userField.not().or(ipField.not().or(portField.not())));


		// UnaryOperator<Change> portFilter = change -> {
		// 	String input = change.getText();
		// 	if(input.matches("^(6553[0-5]|655[0-2]\\d|65[0-4]\\d\\d|6[0-4]\\d{3}|[1-5]\\d{4}|[2-9]\\d{3}|1[1-9]\\d{2}|10[3-9]\\d|102[4-9])$")){
		// 		return change;
		// 	}
		// 	return null;
		// };
		// port.getInput().setTextFormatter(new TextFormatter<String>(portFilter));
		//
		// UnaryOperator<Change> ipFilter = change -> {
		// 	String input = change.getText();
		// 	if(isValidInet4Address(input)){
		// 		return change;
		// 	}
		// 	return null;
		// };
		//
		// ip.getInput().setTextFormatter(new TextFormatter<String>(ipFilter));


	}

	public Button getCoBtn(){
		return btnConnecte;
	}

	public Button getDecoBtn(){
		return btnDeconnecte;
	}

	public String getUser(){
		return nom.getInput().getText();
	}

	public String getIP(){
		return ip.getInput().getText();
	}

	public int getPort(){
		String temp = port.getInput().getText();
		if(!temp.equals("")){
			return Integer.parseInt(temp);
		}
		else{
			return -1;
		}
	}

	public void switchBtn(boolean b1){
		// If value is set to true then the button will be on connect mode
		if(b1){
			hbBtn.getChildren().remove(btnDeconnecte);
			hbBtn.getChildren().add(btnConnecte);
			nom.getInput().setEditable(true);
			ip.getInput().setEditable(true);
			port.getInput().setEditable(true);
		}// Else the button will be on disconnect mode
		else{
			hbBtn.getChildren().remove(btnConnecte);
			hbBtn.getChildren().add(btnDeconnecte);
			nom.getInput().setEditable(false);
			ip.getInput().setEditable(false);
			port.getInput().setEditable(false);
		}
	}

}
