package chat;

import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.shape.Rectangle;


/* This class is used as an input for message */
public class MessageBox extends Parent{

	private Button btnSend = new Button("Envoyer");
	private TextField message = new TextField();
	private Label label;
	private VBox vbox = new VBox(10);
	private Rectangle rect = new Rectangle();

	public MessageBox(){

		//Label for MessageBox
		label = new Label("Message");
		label.setMinWidth(50);
		label.setMaxWidth(100);
		label.setAlignment(Pos.CENTER);

		// Shape for MessageBox
		rect.setWidth(400);
		rect.setHeight(200);
		rect.setArcWidth(30);
		rect.setArcHeight(30);

		// Input field
		message.setShape(rect);
		message.setPrefWidth(600);
		message.setPrefHeight(100);
		message.setPadding(new Insets(2, 2, 2, 2));

		// btnSend
		btnSend.setPrefWidth(200);

		//VBox containing label, Input field and Button
		vbox.setAlignment(Pos.CENTER);
		vbox.setPrefWidth(600);
		vbox.getChildren().addAll(label,message,btnSend);

		this.setTranslateX(360);
		this.setTranslateY(730);
		this.getChildren().add(vbox);
	}

	//Return the button
	public Button getButton(){
		return btnSend;
	}

	// Return text contained in input box
	public String getMessage(){
		String text = message.getText();
		message.clear();
		return text;
	}
}
