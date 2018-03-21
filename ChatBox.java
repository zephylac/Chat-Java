import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
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

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.Node;

public class ChatBox extends Parent{

	private TextFlow chat = new TextFlow();
	private Label label;
	private VBox vbox = new VBox(10);
	private Text textCo;

	private ObservableList<String> otherList;
	private ObservableList<String> messageList;
	private ObservableList<UserData> userList;

	public ChatBox(ObservableList<String> otherList,ObservableList<String> messageList,ObservableList<UserData> userList,ConnexionBox connection){

		this.otherList = otherList;
		this.messageList = messageList;
		this.userList = userList;

		// Label for ChatBox
		label = new Label("Discussion");
		label.setMinWidth(50);
		label.setMaxWidth(100);
		label.setAlignment(Pos.CENTER);

		// ChatBox
		chat.setPrefWidth(600);
		chat.setPrefHeight(400);
		chat.setBorder(new Border(new BorderStroke(Color.GRAY,BorderStrokeStyle.SOLID,new CornerRadii(15), BorderWidths.DEFAULT)));
		chat.setPadding(new Insets(2, 2, 2, 2));

		// VBox containing label and ChatBox
		vbox.setAlignment(Pos.CENTER);
		vbox.setPrefWidth(600);
		vbox.getChildren().addAll(label,chat);

		this.setTranslateX(360);
		this.setTranslateY(280);
		this.getChildren().add(vbox);

		//To avoid duplicate text children
		List<Node> tempList = new ArrayList<>();


		messageList.addListener(new ListChangeListener<String>() {
			public void onChanged(Change<? extends String> change){
				while(change.next()){
					if( change.wasAdded()){
						List<? extends String> list = change.getAddedSubList();
						for(String m : list){
									if (m.startsWith("Whispering to ")){
									String cutResponse = m.substring(14);
									String[] splitted = cutResponse.split(" : ",2);

									Text begin = new Text("Whispering to ");
									begin.setFill(Color.GRAY);
									begin.setFont(Font.font("Tahoma",FontWeight.NORMAL,12));

									Text userColoredName = new Text(splitted[0]);
									userColoredName.setFill(getUserDataFromString(splitted[0],userList).getColor());
									userColoredName.setFont(Font.font("Tahoma",FontWeight.NORMAL,12));

									Image image = checkImg(splitted[1]);

									tempList.add(begin);
									tempList.add(userColoredName);

									if(image != null){
										ImageView iv = new ImageView();
										iv.setPreserveRatio(true);
										iv.setFitHeight(50);
										iv.setImage(image);

										Text tempTxt = new Text(" : ");
										tempTxt.setFill(Color.BLACK);
										tempTxt.setFont(Font.font("Tahoma",FontWeight.NORMAL,12));

										textCo = new Text("\n");
										textCo.setFill(Color.BLACK);
										textCo.setFont(Font.font("Tahoma",FontWeight.NORMAL,12));

										tempList.add(tempTxt);
										tempList.add(iv);

									}
									else{
										textCo = new Text(" : " + splitted[1] + "\n");
										textCo.setFill(Color.BLACK);
										textCo.setFont(Font.font("Tahoma",FontWeight.NORMAL,12));
									}

									System.out.println("Whispering to " + splitted[0] + " : " + splitted[1]);



								}
								else if(m.startsWith("Whispering (")){
									String cutResponse = m.substring(12);
									String[] splitted = cutResponse.split("\\) : ",2);

									Text begin = new Text("Whispering (");
									begin.setFill(Color.GRAY);
									begin.setFont(Font.font("Tahoma",FontWeight.NORMAL,12));

									Text userColoredName = new Text(splitted[0]);
									userColoredName.setFill(getUserDataFromString(splitted[0],userList).getColor());
									userColoredName.setFont(Font.font("Tahoma",FontWeight.NORMAL,12));

									Text text = new Text(")");
									text.setFill(Color.GRAY);
									text.setFont(Font.font("Tahoma",FontWeight.NORMAL,12));

									Image image = checkImg(splitted[1]);

									tempList.add(begin);
									tempList.add(userColoredName);
									tempList.add(text);

									if(image != null){
										ImageView iv = new ImageView();
										iv.setPreserveRatio(true);
										iv.setFitHeight(50);
										iv.setImage(image);

										Text tempTxt = new Text(" : ");
										tempTxt.setFill(Color.BLACK);
										tempTxt.setFont(Font.font("Tahoma",FontWeight.NORMAL,12));

										textCo = new Text("\n");
										textCo.setFill(Color.BLACK);
										textCo.setFont(Font.font("Tahoma",FontWeight.NORMAL,12));

										tempList.add(tempTxt);
										tempList.add(iv);

									}
									else{
										textCo = new Text(" : " + splitted[1] + "\n");
										textCo.setFill(Color.BLACK);
										textCo.setFont(Font.font("Tahoma",FontWeight.NORMAL,12));
									}


									System.out.println("Whispering (" + splitted[0] + ") : " + splitted[1]);

								}
								else{
									String[] splitted = m.split(" :",2);

									Text userColoredName = new Text(splitted[0]);
									userColoredName.setFill(getUserDataFromString(splitted[0],userList).getColor());
									userColoredName.setFont(Font.font("Tahoma",FontWeight.NORMAL,12));

									tempList.add(userColoredName);

									Image image = checkImg(splitted[1]);

									if(image != null){
										ImageView iv = new ImageView();
										iv.setPreserveRatio(true);
										iv.setFitHeight(50);
										iv.setImage(image);

										Text tempTxt = new Text(" : ");
										tempTxt.setFill(Color.BLACK);
										tempTxt.setFont(Font.font("Tahoma",FontWeight.NORMAL,12));

										textCo = new Text("\n");
										textCo.setFill(Color.BLACK);
										textCo.setFont(Font.font("Tahoma",FontWeight.NORMAL,12));

										tempList.add(tempTxt);
										tempList.add(iv);

										image = null;
									}
									else{
										textCo = new Text(" : " + splitted[1] + "\n");
										textCo.setFill(Color.BLACK);
										textCo.setFont(Font.font("Tahoma",FontWeight.NORMAL,12));
									}
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

		//To avoid duplicate text children
		List<Text> tempList2 = new ArrayList<>();

		otherList.addListener(new ListChangeListener<String>() {
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
									textCo.setFill(Color.ORANGE);
									textCo.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
									Platform.runLater(() ->{
										connection.switchBtn(true);
									});
									break;
								case "!LOST" :
									textCo = new Text("Connexion au serveur perdu\n");
									textCo.setFill(Color.RED);
									textCo.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
									Platform.runLater(() ->{
										connection.switchBtn(true);
									});
									break;
								case "!COLOR" :
									textCo = new Text("Available color : RED, YELLOW, ORANGE, BLACK, BLUE, GREEN, PINK, PURPLE, CYAN\n");
									textCo.setFill(Color.GRAY);
									textCo.setFont(Font.font("Tahoma",FontWeight.NORMAL,12));
									break;
								case "!SMILEY" :
									textCo = new Text("Available smiley : !DENIS, !NYAN, !PEPE, !POTATO\n");
									textCo.setFill(Color.GRAY);
									textCo.setFont(Font.font("Tahoma",FontWeight.NORMAL,12));
									break;
							}
							tempList2.add(textCo);
								// Since it's not JavaFX thread. It will add the messages to
								// the box when JavaFX thread allow it.
							Platform.runLater(() ->{
								chat.getChildren().addAll(tempList2);
								tempList2.clear();
							});
						}
					}
				}
			}
		});
	}



	// Retrieve userData from string
	private UserData getUserDataFromString(String user,List<UserData> list){
		for(UserData u : list){
			if(u.getUsername().equals(user)){
				return u;
			}
		}
		return null;
	}

	//Return ChatBox
	public TextFlow getChat(){
		return chat;
	}

	// This method takes a String as parameter and return an Image if it's valid , null if not
	public Image checkImg(String str){
		Image image;
		switch(str.toUpperCase()){
			case "!POTATO":
				image = new Image("./image/potato.png", true);
				break;
			case "!NYAN" :
				image = new Image("./image/nyan.png", true);
				break;
			case "!DENIS" :
			System.out.println("DENIS");
				image = new Image("./image/denis.jpg", true);
				break;
			case "!PEPE":
				image = new Image("./image/pepe.jpeg", true);
				break;
			default :
				image = null;
		}
		return image;
	}
}
