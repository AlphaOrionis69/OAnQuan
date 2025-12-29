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

	private void navigateTo(String fxmlPath, String title) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
			Parent root = loader.load();
			Scene scene = new Scene(root, 1000, 700);
			stage.setScene(scene);
			stage.setTitle(title);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void showMainMenu() {
		stage.setMinWidth(600); stage.setMinHeight(300);
		NavigationController.getInstance().navigateTo("/view/MainMenu.fxml", "O An Quan - Menu");
	}
	public void showHelpScreen() {
		stage.setMinWidth(500); stage.setMinHeight(300);
		NavigationController.getInstance().navigateTo("/view/HelpScreen.fxml", "O An Quan - Help");
	}
	public void showGameScreen() {
		stage.setMinWidth(950); stage.setMinHeight(600);
		NavigationController.getInstance().navigateTo("/view/GameScreen.fxml", "O An Quan - Playing");
	}
}