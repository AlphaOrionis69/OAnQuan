package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HelpScreenController {

	@FXML
	public void handleBack(ActionEvent event) {
		NavigationController.getInstance().showMainMenu();
	}
}