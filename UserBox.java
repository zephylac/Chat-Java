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
import javafx.scene.control.TextArea;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.ArrayList;

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

import javafx.application.Platform;

public class UserBox extends Parent{

	private TextFlow user = new TextFlow();
	private Label label;
	private VBox vbox = new VBox(10);
	private ObservableList<UserData> userList;

	public UserBox(ObservableList<UserData> userList){

		label = new Label("Connectés");
		label.setMinWidth(50);
		label.setMaxWidth(100);

		user.setPrefWidth(200);
		user.setPrefHeight(600);
		user.setBorder(new Border(new BorderStroke(Color.GRAY,BorderStrokeStyle.SOLID,new CornerRadii(15), BorderWidths.DEFAULT)));
		user.setPadding(new Insets(2, 2, 2, 2));

		vbox.setAlignment(Pos.CENTER);
		vbox.setPrefWidth(200);
		vbox.getChildren().addAll(label,user);

		//user.appendText("user" + i + "\n");

		this.setTranslateX(20);
		this.setTranslateY(280);
		this.getChildren().add(vbox);

		this.userList = userList;

		List<Text> tempList = new ArrayList<>();

		userList.addListener(new ListChangeListener<UserData>() {
			public void onChanged(Change<? extends UserData> change){
				while(change.next()){
					if(change.wasAdded()){
						List<? extends UserData> list = change.getAddedSubList();
						for(UserData u : list){
							tempList.add(userDataToText(u));
						}
					}
				}
				Platform.runLater(() ->{
					user.getChildren().clear();
					user.getChildren().addAll(tempList);
					tempList.clear();
				});
			}
		});
	}

	public Text userDataToText(UserData u){
		Text combined = new Text(u.getUsername() + "\n");
		combined.setFill(u.getColor());
		combined.setFont(Font.font("Helvetica", FontWeight.NORMAL, 16));

		return combined;
	}
}
