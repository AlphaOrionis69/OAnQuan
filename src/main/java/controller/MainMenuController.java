package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class MainMenuController {

	@FXML
	public void handleStart(ActionEvent event) {
		NavigationController.getInstance().showGameScreen();
	}

	@FXML
	public void handleHelp(ActionEvent event) {
		NavigationController.getInstance().showHelpScreen();
	}

	@FXML
	public void handleExit(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to quit?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.YES) {
				Platform.exit();
			}
		});
	}
}