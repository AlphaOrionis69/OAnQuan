package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MessageOverlay extends StackPane {
	private Label msgTitle, msgContent;
	private Button msgButton;
	public MessageOverlay() {
		VBox container = new VBox(); 
		msgTitle = new Label(""); msgTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold");
		msgContent = new Label(""); msgContent.setWrapText(true);
		msgButton = new Button("OK");
		
		//StackPane.setMargin(container, new Insets(200, 200, 200, 200));
		
		container.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10; -fx-max-width: 400; -fx-max-height: 300");
		container.setSpacing(10);
		container.getChildren().addAll(msgTitle, msgContent, msgButton);
		container.setAlignment(Pos.CENTER);
		
		this.getChildren().add(container);
		this.setStyle("-fx-background-color: rgba(0,0,0,0.5)");
		StackPane.setAlignment(container, Pos.CENTER);
		setVisible(false);
	}
	public void setTitle(String title) {
		msgTitle.setText(title);
	}
	public void setContent(String content) {
		msgContent.setText(content);
	}
	public Button getButton() {
		return msgButton;
	}
}
