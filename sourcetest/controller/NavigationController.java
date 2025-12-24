package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class NavigationController {
	private static NavigationController instance;
	private Stage stage;

	private NavigationController() {}

	public static NavigationController getInstance() {
		if (instance == null) instance = new NavigationController();
		return instance;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void navigateTo(String fxmlPath) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
			Parent root = loader.load();
			Scene scene = new Scene(root, 1000, 700);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}