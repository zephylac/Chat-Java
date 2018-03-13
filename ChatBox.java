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
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.ArrayList;

import javafx.application.Platform;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;

import java.io.StringReader;
import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;

public class ChatBox extends Parent{

	private TextFlow chat = new TextFlow();
	private Label label;
	private VBox vbox = new VBox(10);
	private Text textCo;
	private ObservableList<String> messageList;
	private ObservableList<UserData> userList;

	public ChatBox(ObservableList<String> messageList,ObservableList<UserData> userList,ConnexionBox connection){

		this.messageList = messageList;

		label = new Label("Discussion");
		label.setMinWidth(50);
		label.setMaxWidth(100);
		label.setAlignment(Pos.CENTER);

		chat.setPrefWidth(600);
		chat.setPrefHeight(400);
		chat.setBorder(new Border(new BorderStroke(Color.GRAY,BorderStrokeStyle.SOLID,new CornerRadii(15), BorderWidths.DEFAULT)));
		chat.setPadding(new Insets(2, 2, 2, 2));

		vbox.setAlignment(Pos.CENTER);
		vbox.setPrefWidth(600);
		vbox.getChildren().addAll(label,chat);

		//chat.appendText("test" + i + "\n");

		this.setTranslateX(360);
		this.setTranslateY(280);
		this.getChildren().add(vbox);

		//To avoid duplicate text children
		List<Text> tempList = new ArrayList<>();


		messageList.addListener(new ListChangeListener<String>() {
			public void onChanged(Change<? extends String> change){
				while(change.next()){
					if( change.wasAdded()){
						List<? extends String> list = change.getAddedSubList();
						for(String m : list){
							switch(m){
								case "!CONNECT" :
									textCo = new Text("Connexion au serveur reussi\n");
									textCo.setFill(Color.GREEN);
									textCo.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
									Platform.runLater(() ->{
										connection.switchBtn(false);
									});
									break;
								case "!DISCONNECT" :
									textCo = new Text("Deconnexion au serveur reussi\n");
									textCo.setFill(Color.RED);
									textCo.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
									Platform.runLater(() ->{
										connection.switchBtn(true);
									});
									break;
								default :
									String[] splitted = m.split(" :",2);

									Text userColoredName = new Text(splitted[0]);
									userColoredName.setFill(getUserDataFromString(splitted[0],userList).getColor());
									userColoredName.setFont(Font.font("Tahoma",FontWeight.NORMAL,12));

									textCo = new Text(" : " + splitted[1] + "\n");
									textCo.setFill(Color.BLACK);
									textCo.setFont(Font.font("Tahoma",FontWeight.NORMAL,12));


									tempList.add(userColoredName);
							}
							tempList.add(textCo);
						}

						// Since it's not JavaFX thread. It will add the messages to
						// the box when JavaFX thread allow it.
						Platform.runLater(() ->{
							chat.getChildren().addAll(tempList);
							tempList.clear();
						});
					}
				}
			}
		});
	}

	private UserData getUserDataFromString(String user,List<UserData> list){
		for(UserData u : list){	
			if(u.getUsername().equals(user)){
				return u;
			}
		}
		return null;
	}

	public TextFlow getChat(){
		return chat;
	}
}
