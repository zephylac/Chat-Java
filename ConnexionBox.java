import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

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

/* This class is constituted of 3 InputBox and Button
 *
 */
public class ConnexionBox extends Parent{

	private InputBox nom  = new InputBox("Nom");
	private InputBox ip  = new InputBox("IP");
	private InputBox port  = new InputBox("Port");
	private Button btnConnecte = new Button("Connexion");
	private Button btnDeconnecte = new Button("Deconnexion");
	private HBox hbBtn = new HBox(40);

	public ConnexionBox(){

		// Shape for ConnexionBox
		Rectangle rect = new Rectangle();
		rect.setWidth(80);
		rect.setHeight(40);
		rect.setArcWidth(15);
		rect.setArcHeight(15);

		//GridPane for all the input box and button
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

		//Set default values
		ip.getInput().setText("127.0.0.1");
		port.getInput().setText("2346");

		//Fill the grid
		grid.add(nom.getInputBox(),0,0);
		grid.add(ip.getInputBox(),0,1);
		grid.add(port.getInputBox(),1,1);

		// Button Connect
		btnConnecte.setMaxWidth(300);
		btnConnecte.setPrefWidth(300);
		btnConnecte.setMinWidth(100);
		btnConnecte.setAlignment(Pos.CENTER);

		// button Disconnect
		btnDeconnecte.setMaxWidth(300);
		btnDeconnecte.setPrefWidth(300);
		btnDeconnecte.setMinWidth(100);
		btnDeconnecte.setAlignment(Pos.CENTER);

		//Hbox for Button
		hbBtn.setAlignment(Pos.CENTER);
		hbBtn.setPrefWidth(300);
		hbBtn.getChildren().add(btnConnecte);
		grid.add(hbBtn,1,0);


		this.setTranslateX(0);
		this.setTranslateY(0);
		this.getChildren().add(grid);

		/* Creates Binding to check input values
		 */

		BooleanBinding userField = Bindings.createBooleanBinding(()->{
			if (nom.getInput().getText().length() != 0){return true;}
			return false;
		},nom.getInput().textProperty());

		BooleanBinding ipField   = Bindings.createBooleanBinding(()->{
			if (ip.getInput().getText().length() != 0 ){return true;}
			return false;
		},ip.getInput().textProperty());

		BooleanBinding portField = Bindings.createBooleanBinding(()->{
			if (port.getInput().getText().length() != 0){
				int test = Integer.parseInt(port.getInput().getText());
				if( test > 0 && test < 65536){return true;}
			}
			return false;
		},port.getInput().textProperty());

		// Button is disabled if either one of the bindings is false
		btnConnecte.disableProperty().bind(userField.not().or(ipField.not().or(portField.not())));

		// Set TextFormatter on IP input to only allow valid ip
		String regex = makePartialIPRegex();
		final UnaryOperator<Change> ipAddressFilter = c -> {
		String text = c.getControlNewText();
			if  (text.matches(regex)) {return c ;}
			else {return null ;}
		};

		ip.getInput().setTextFormatter(new TextFormatter<>(ipAddressFilter));

		// Set TextFormatter on port input to only allow valid port number
		String regex2 = "^$|^(0|[0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])";
		final UnaryOperator<Change> portFilter = c -> {
		String text = c.getControlNewText();
			if  (text.matches(regex2)) {return c ;}
			else {return null ;}
		};

		port.getInput().setTextFormatter(new TextFormatter<>(portFilter));


	}
	//Get connect button
	public Button getCoBtn(){
		return btnConnecte;
	}

	//get disconnect button
	public Button getDecoBtn(){
		return btnDeconnecte;
	}

	// Get user input
	public String getUser(){
		return nom.getInput().getText();
	}

	// Get ip input
	public String getIP(){
		return ip.getInput().getText();
	}

	// Get port input
	public int getPort(){
		String temp = port.getInput().getText();
		if(!temp.equals("")){return Integer.parseInt(temp);}
		else{return -1;}
	}

	// Switch between connect and disconnect button
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

	/* Return a string that filter Ip Adress */
	private String makePartialIPRegex() {
		String partialBlock = "(([01]?[0-9]{0,2})|(2[0-4][0-9])|(25[0-5]))" ;
		String subsequentPartialBlock = "(\\."+partialBlock+")" ;
		String ipAddress = partialBlock+"?"+subsequentPartialBlock+"{0,3}";
		return "^"+ipAddress ;
	}
}
