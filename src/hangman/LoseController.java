package hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class LoseController {

    @FXML
    private Label answerLabel;

    public void setAnswer(String answer) {
        answerLabel.setText("The word was: " + answer);
    }

    @FXML
    private void onPlayAgain(ActionEvent e) {
        try {
            Stage stage = (Stage) answerLabel.getScene().getWindow();
            stage.setScene(new Scene(
                    FXMLLoader.load(getClass().getResource("/hangman/game.fxml"))
            ));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onExit(ActionEvent e) {
        Stage stage = (Stage) answerLabel.getScene().getWindow();
        stage.close();
    }
}