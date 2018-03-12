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
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class MessageBox extends Parent{

	private Button btnSend = new Button("Envoyer");
	private TextField message = new TextField();
	private Label label;
	private VBox vbox = new VBox(10);
	private Rectangle rect = new Rectangle();

	public MessageBox(){

		label = new Label("Message");
		label.setMinWidth(50);
		label.setMaxWidth(100);
		label.setAlignment(Pos.CENTER);

		rect.setWidth(400);
		rect.setHeight(200);
		rect.setArcWidth(30);
		rect.setArcHeight(30);

		message.setShape(rect);
		message.setPrefWidth(600);
		message.setPrefHeight(100);
		message.setPadding(new Insets(2, 2, 2, 2));

		btnSend.setPrefWidth(200);

		vbox.setAlignment(Pos.CENTER);
		vbox.setPrefWidth(600);
		vbox.getChildren().addAll(label,message,btnSend);

		this.setTranslateX(360);
		this.setTranslateY(730);
		this.getChildren().add(vbox);
	}

	public Button getButton(){
		return btnSend;
	}

	public String getMessage(){
		String text = message.getText();
		message.clear();
		return text;
	}
}
