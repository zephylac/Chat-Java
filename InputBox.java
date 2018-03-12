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

import javafx.beans.property.StringProperty;

public class InputBox extends Parent{

	private Label label;
	private TextField input = new TextField();
	private HBox hbox = new HBox(40);

	public InputBox(String str){

		label = new Label(str);
		label.setMinWidth(50);
		label.setMaxWidth(100);

		input.setMinWidth(80);
		input.setMaxWidth(400);
		input.setPrefHeight(40);

		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().addAll(label,input);
		hbox.setMaxWidth(500);
		hbox.setMinWidth(100);
	}

	public HBox getInputBox(){
		return hbox;
	}

	public TextField getInput(){
		return input;
	}
}
