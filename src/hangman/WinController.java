package hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WinController {
    public void onPlayAgain(ActionEvent e) {
        try {
            Stage stage = (Stage)((javafx.scene.Node)e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/hangman/game.fxml"))));
        } catch (Exception ex) { ex.printStackTrace(); }
    }
    public void onExit(ActionEvent e) {
        Stage stage = (Stage)((javafx.scene.Node)e.getSource()).getScene().getWindow();
        stage.close();
    }
}