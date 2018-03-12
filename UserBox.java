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

public class UserBox extends Parent{

	private TextArea user = new TextArea();
	private Label label;
	private VBox vbox = new VBox(10);
	private ObservableList<String> userList;

	public UserBox(ObservableList<String> userList){

		label = new Label("Connectés");
		label.setMinWidth(50);
		label.setMaxWidth(100);

		user.setEditable(false);
		user.setPrefWidth(200);
		user.setPrefHeight(600);
		user.setWrapText(true);
		user.setPadding(new Insets(2, 2, 2, 2));

		vbox.setAlignment(Pos.CENTER);
		vbox.setPrefWidth(200);
		vbox.getChildren().addAll(label,user);

		//user.appendText("user" + i + "\n");

		this.setTranslateX(20);
		this.setTranslateY(280);
		this.getChildren().add(vbox);

		this.userList = userList;

		userList.addListener(new ListChangeListener<String>() {
			public void onChanged(Change<? extends String> change){
				while(change.next()){
					if(change.wasAdded()){
						List<? extends String> list = change.getAddedSubList();
						for(String m : list){
							user.appendText(m + "\n");
						}
					}
					if(change.wasRemoved()){
						user.clear();
						for(String m : change.getList()){
							user.appendText(m + "\n");
						}
					}
				}
			}
		});
	}
}
