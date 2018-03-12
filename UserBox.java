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
import javafx.scene.control.TextArea;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.ArrayList;

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
	private ObservableList<Text> userList;

	public UserBox(ObservableList<Text> userList){

		label = new Label("Connectés");
		label.setMinWidth(50);
		label.setMaxWidth(100);

		user.setPrefWidth(200);
		user.setPrefHeight(600);
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

		userList.addListener(new ListChangeListener<Text>() {
			public void onChanged(Change<? extends Text> change){
				while(change.next()){
					if(change.wasAdded()){
						List<? extends Text> list = change.getAddedSubList();
						for(Text m : list){

							Text combined = new Text(m + "\n");
							combined.setFill(Color.GREEN);
							combined.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));

							tempList.add(combined);
						}

						Platform.runLater(() ->{
							user.getChildren().addAll(tempList);
							tempList.clear();
						});

					}
					// if(change.wasRemoved()){
					//
					// 	for(Text m : change.getList()){
					//
					// 		Text combined = new Text(m + "\n");
					// 		combined.setFill(Color.GREEN);
					// 		combined.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
					//
					// 		user.add(combined);
					// 	}
					// }
				}
			}
		});
	}
}
