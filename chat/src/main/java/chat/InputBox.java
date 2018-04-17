package chat;

import javafx.scene.Parent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;


/* This class is used as an Input
 * It's constituted of a Label and a TextField
 */
public class InputBox extends Parent{

	private Label label;
	private TextField input = new TextField();
	private HBox hbox = new HBox(40);

	/* String is used for Label */
	public InputBox(String str){

		// Label for input box
		label = new Label(str);
		label.setMinWidth(50);
		label.setMaxWidth(100);

		// Input field
		input.setMinWidth(80);
		input.setMaxWidth(400);
		input.setPrefHeight(40);

		// HBox containing label and input field
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().addAll(label,input);
		hbox.setMaxWidth(500);
		hbox.setMinWidth(100);
	}

	//Return HBox
	public HBox getInputBox(){
		return hbox;
	}

	//Return input field
	public TextField getInput(){
		return input;
	}
}
