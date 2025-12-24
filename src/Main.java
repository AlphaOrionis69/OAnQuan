import controller.NavigationController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        NavigationController.getInstance().setStage(primaryStage);
        
        NavigationController.getInstance().showMainMenu();
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}