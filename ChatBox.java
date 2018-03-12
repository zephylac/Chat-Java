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

import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ChatBox extends Parent{

	private TextFlow chat = new TextFlow();
	private Label label;
	private VBox vbox = new VBox(10);
	private Text textCo;
	private ObservableList<Text> messageList;

	public ChatBox(ObservableList<Text> messageList,ConnexionBox connection){

		this.messageList = messageList;

		label = new Label("Discussion");
		label.setMinWidth(50);
		label.setMaxWidth(100);
		label.setAlignment(Pos.CENTER);

		//chat.setEditable(false);
		chat.setPrefWidth(600);
		chat.setPrefHeight(400);
		//chat.setWrapText(true);
		chat.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,new CornerRadii(15), BorderWidths.DEFAULT)));
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


		messageList.addListener(new ListChangeListener<Text>() {
			public void onChanged(Change<? extends Text> change){
				while(change.next()){
					if( change.wasAdded()){
						List<? extends Text> list = change.getAddedSubList();
						System.out.println("Liste message : " + list);
						for(Text m : list){
							//switch(m){
								// case "!CONNECT" :
								// 	textCo = new Text("Connexion au serveur reussi\n");
								// 	textCo.setFill(Color.GREEN);
								// 	textCo.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
								// 	Platform.runLater(() ->{
								// 		connection.switchBtn(false);
								// 	});
								// 	break;
								// case "!DISCONNECT" :
								// 	textCo = new Text("Deconnexion au serveur reussi\n");
								// 	textCo.setFill(Color.RED);
								// 	textCo.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
								// 	Platform.runLater(() ->{
								// 		connection.switchBtn(true);
								// 	});
								// 	break;
							// 	default :
							// 		textCo = m;
							// }
							tempList.add(m);
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

	public TextFlow getChat(){
		return chat;
	}
}
