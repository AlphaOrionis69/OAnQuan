package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import java.net.URL;
import java.util.ResourceBundle;

public class HelpScreenController implements Initializable {

    @FXML private TextArea txtContent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtContent.setText(
            "1. Bàn chơi gồm 10 ô dân (mỗi ô 5 quân) và 2 ô quan (mỗi ô 10 điểm).\n" +
            "2. Người chơi chọn 1 ô dân của mình để rải quân.\n" +
            "3. Rải lần lượt từng viên vào các ô tiếp theo theo chiều đã chọn.\n" +
            "4. Nếu viên cuối cùng rơi vào ô có quân, bốc tiếp để rải.\n" +
            "5. Nếu viên cuối cùng rơi vào ô trống, ăn các quân ở ô tiếp theo.\n" +
            "6. Trò chơi kết thúc khi 2 ô Quan bị ăn hết."
        );
    }

    @FXML
    public void handleBack() {
        NavigationController.getInstance().showMainMenu();
    }
}