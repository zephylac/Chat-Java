package chat;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.ArrayList;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javafx.application.Platform;
import javafx.stage.WindowEvent;


public class Client extends Application {
	private ClientConnexion c1 = new ClientConnexion();

	public static void main(String[] args) {
		Application.launch(Client.class, args);
	}

	public void start(Stage primaryStage) {
		primaryStage.setTitle("This is not well coded");
		Group root = new Group();
		Scene scene = new Scene(root, 1000, 1080, Color.WHITE);

		MessageBox mess = new MessageBox();
		root.getChildren().add(mess);

		UserBox user = new UserBox(c1.getUser());
		root.getChildren().add(user);

		ConnexionBox connect = new ConnexionBox();
		root.getChildren().add(connect);

		ChatBox chat = new ChatBox(c1.getOther(),c1.getMessage(),c1.getUser(),connect);
		root.getChildren().add(chat);

		mess.getButton().setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if(c1 == null || !c1.getEstConnecte()){
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Erreur de connexion");
					alert.setHeaderText("Connexion inexistante !");
					alert.setContentText("La connexion au serveur est inexistante");
					alert.showAndWait();
				}
				else{
					String msg = mess.getMessage();
					if(msg.length() > 0 && msg.length() < 4096 ){
						c1.envoieMessage(msg);
					}
				}
			}
		});

		connect.getCoBtn().setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				String host = connect.getIP();
				int port = connect.getPort();
				String user = connect.getUser();
					if(user.matches("[a-zA-Z0-9_]+") && user.length() > 0 && user.length() < 20){
						if(port > 0 && port < 65536){
							System.out.println("h:"+host+",p:"+port+",u:"+user);
							int error = c1.seConnecter(host,port,user);
							if(error == 1){
								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("Erreur de connexion");
								alert.setHeaderText("Connexion impossible !");
								alert.setContentText("La connexion au serveur a echoué");
								alert.showAndWait();
							}
							else if(error == 2){
								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("Erreur de connexion");
								alert.setHeaderText("Connexion impossible !");
								alert.setContentText("Une personne de ce nom existe deja");
								alert.showAndWait();
							}
							else{
								chat.getChat().getChildren().clear();
								//connect.switchBtn(false);
							}
						}
					}
			}
		});

		connect.getDecoBtn().setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				c1.seDeconnecter();
			}
		});

		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				c1.seDeconnecter();
			}
		});
	}

}
