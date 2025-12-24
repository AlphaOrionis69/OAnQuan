package controller;

import model.game.OAnQuanGame;
import model.entity.Square;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML private Label lblScoreP1;
    @FXML private Label lblScoreP2;
    @FXML private Label lblTurnInfo;
    @FXML private GridPane gridBoard;

    private OAnQuanGame gameModel;
    
    private Map<Integer, SquareController> squareControllerMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameModel = new OAnQuanGame();
        squareControllerMap = new HashMap<>();
        
        setupBoardUI();
        updateUI();
    }

    private void setupBoardUI() {
        try {
            loadAndAddSquare(11, true, 0, 0, 1, 2);
            loadAndAddSquare(5, true, 6, 0, 1, 2);

            for (int i = 0; i < 5; i++) {
                loadAndAddSquare(10 - i, false, i + 1, 0, 1, 1);
            }

            for (int i = 0; i < 5; i++) {
                loadAndAddSquare(i, false, i + 1, 1, 1, 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Lỗi: Không thể load file Square.fxml");
        }
    }


    private void loadAndAddSquare(int id, boolean isMandarin, int col, int row, int colSpan, int rowSpan) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Square.fxml"));
        Parent squareNode = loader.load();

        SquareController sqCtrl = loader.getController();
        sqCtrl.setup(id, isMandarin);

        squareNode.setOnMouseClicked(e -> handleSquareAction(id));

        gridBoard.add(squareNode, col, row, colSpan, rowSpan);

        squareControllerMap.put(id, sqCtrl);
    }

    private void handleSquareAction(int squareId) {
        if (gameModel.isGameOver()) return;

        boolean isClockwise = showDirectionDialog();
        
        boolean success = gameModel.play(squareId, isClockwise);

        if (success) {
            updateUI();
            if (gameModel.isGameOver()) showWinnerDialog();
        } else {
            new Alert(Alert.AlertType.WARNING, "Nước đi không hợp lệ!").show();
        }
    }

    private boolean showDirectionDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Chọn hướng");
        alert.setHeaderText("Chọn chiều rải quân:");
        ButtonType right = new ButtonType("Chiều kim đồng hồ");
        ButtonType left = new ButtonType("Ngược chiều");
        alert.getButtonTypes().setAll(right, left);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == right;
    }
    
    private void showWinnerDialog() {
         String msg = "Kết thúc! P1: " + gameModel.getPlayer1().getScore() + " - P2: " + gameModel.getPlayer2().getScore();
         new Alert(Alert.AlertType.INFORMATION, msg).show();
    }

    private void updateUI() {
        lblScoreP1.setText("Score: " + gameModel.getPlayer1().getScore());
        lblScoreP2.setText("Score: " + gameModel.getPlayer2().getScore());
        lblTurnInfo.setText("Lượt: " + gameModel.getCurrentPlayer().getName());
        
        for (Map.Entry<Integer, SquareController> entry : squareControllerMap.entrySet()) {
            int id = entry.getKey();
            SquareController sqCtrl = entry.getValue();  
            int stones = gameModel.getBoard().getSquare(id).getStones();
            sqCtrl.setStones(stones);
        }
    }

    @FXML
    public void handleBackToMenu() {
        NavigationController.getInstance().showMainMenu();
    }
}