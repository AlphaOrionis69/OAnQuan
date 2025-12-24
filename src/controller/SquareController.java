package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SquareController {

    @FXML private StackPane rootPane;
    @FXML private Rectangle bgShape;
    @FXML private Label lblStones;

    private int squareId;

  
    public void setup(int id, boolean isMandarin) {
        this.squareId = id;
        
        if (isMandarin) {
            bgShape.setFill(Color.LIGHTGOLDENRODYELLOW);
            bgShape.setStrokeWidth(2);
        } else {
            bgShape.setFill(Color.LIGHTGRAY);
        }
    }

    public void setStones(int amount) {
        lblStones.setText(String.valueOf(amount));
    }

    public int getSquareId() {
        return squareId;
    }
    
    public StackPane getRoot() {
        return rootPane;
    }
}