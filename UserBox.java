import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;

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

import javafx.scene.Node;

import javafx.application.Platform;

public class UserBox extends Parent{

	private TextFlow user = new TextFlow();
	private Label label;
	private VBox vbox = new VBox(10);
	private ObservableList<UserData> userList;

	public UserBox(ObservableList<UserData> userList){

		// Label for the Textflow
		label = new Label("Connectés");
		label.setMinWidth(50);
		label.setMaxWidth(100);

		//Textflow
		user.setPrefWidth(200);
		user.setPrefHeight(600);
		user.setBorder(new Border(new BorderStroke(Color.GRAY,BorderStrokeStyle.SOLID,new CornerRadii(15), BorderWidths.DEFAULT)));
		user.setPadding(new Insets(2, 2, 2, 2));

		//Vbox for the label and the textflow
		vbox.setAlignment(Pos.CENTER);
		vbox.setPrefWidth(200);
		vbox.getChildren().addAll(label,user);

		// Positionning the UserBox
		this.setTranslateX(20);
		this.setTranslateY(280);
		this.getChildren().add(vbox);

		this.userList = userList;

		List<Text> tempList = new ArrayList<>();
		List<Text> tempList2 = new ArrayList<>();

		/* To avoid duplicate children inside the TextFlow, 
		 * we need to use a temporary list
		 *And since it's not javafx Main thread we use Platform.runLater
		 */

		// Adding listener on the list
		userList.addListener(new ListChangeListener<UserData>() {
			public void onChanged(Change<? extends UserData> change){
				while(change.next()){
					// If user(s) was/were removed
					if(change.wasRemoved()){
						List<? extends UserData> list2 = change.getRemoved();
						for(UserData u : list2){
							for(Node n : user.getChildren()){
								Platform.runLater(() ->{
									String tempStr = ((Text)n).getText().replaceAll("(\\n)", "");
									if(u.getUsername().equals(tempStr)){
										tempList2.add((Text)n);
									}
								});
							}
							Platform.runLater(() ->{
								user.getChildren().removeAll(tempList2);
								tempList2.clear();
							});
						}
					}
					// If user(s) was/were added
					if(change.wasAdded()){
						List<? extends UserData> list = change.getAddedSubList();
						for(UserData u : list){
							tempList.add(userDataToText(u));
						}
						Platform.runLater(() ->{
							user.getChildren().clear();
							user.getChildren().addAll(tempList);
							tempList.clear();
						});
					}
				}
			}
		});
	}

	// Transform UserData to colored username
	public Text userDataToText(UserData u){
		Text combined = new Text(u.getUsername() + "\n");
		combined.setFill(u.getColor());
		combined.setFont(Font.font("Helvetica", FontWeight.NORMAL, 16));

		return combined;
	}
}
