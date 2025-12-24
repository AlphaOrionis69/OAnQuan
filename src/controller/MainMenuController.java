package controller;

import javafx.fxml.FXML;

public class MainMenuController {

    @FXML
    public void handleStartGame() {
        NavigationController.getInstance().showGameScreen();
    }

    @FXML
    public void handleHelp() {
        NavigationController.getInstance().showHelpScreen();
    }

    @FXML
    public void handleExit() {
        System.exit(0);
    }
}