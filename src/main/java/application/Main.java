package application;
import controller.NavigationController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("O An Quan Game");
		NavigationController.getInstance().setStage(primaryStage);
		NavigationController.getInstance().showMainMenu();
	}

	public static void main(String[] args) {
		launch(args); 
	}

	
}