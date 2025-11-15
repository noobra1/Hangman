package hangman;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GameController {
    @FXML private Label wordLabel;
    @FXML private ImageView hangImage;
    private String targetLower;

    private String target;
    private StringBuilder mask;
    private int lives;
    private final Set<Character> used = new HashSet<>();

    @FXML
    public void initialize() {
        resetModelOnly();          // init game state
        // Defer any Scene/Node lookups until after the Scene is ready
        Platform.runLater(() -> {
             updateUIFromModel();
            enableLetterButtons(wordLabel.getScene());
        });
    }

    @FXML
    private void onLetter(ActionEvent e) {
        Button b = (Button) e.getSource();
        b.setDisable(true);

        char c = Character.toLowerCase(b.getText().charAt(0));
        if (used.contains(c) || lives == 0) return;
        used.add(c);

        boolean hit = false;
        for (int i = 0; i < target.length(); i++) {
            char t = Character.toLowerCase(target.charAt(i));
            if (t == c) {
                // reveal the original character (preserve case if you ever use mixed case)
                mask.setCharAt(i, target.charAt(i));
                hit = true;
            }
        }
        if (!hit) { lives--; updateImage(); }

        wordLabel.setText(formatForDisplay(mask.toString()));

        if (mask.indexOf("_") < 0) {
            showWin();
        }
        else if (lives == 0) showLose();
    }


    private String formatForDisplay(String plainMask) {
        StringBuilder sb = new StringBuilder(plainMask.length() * 2 - 1);
        for (int i = 0; i < plainMask.length(); i++) {
            sb.append(plainMask.charAt(i));   // letter or '_'
            if (i < plainMask.length() - 1) sb.append(' ');
        }
        return sb.toString();
    }

    @FXML
    private void onReset() {
        resetModelOnly();
        updateUIFromModel();
        enableLetterButtons(wordLabel.getScene());
    }

    /* ---------------- helpers ---------------- */

    private void resetModelOnly() {
        target = WordBank.randomWord();
        targetLower = target.toLowerCase();                 // <-- set it first
        mask = new StringBuilder(targetLower.replaceAll("[a-z]", "_"));
        lives  = 6;
        used.clear();
        System.out.println("Target word: " + target);
    }

    private void updateUIFromModel() {
        if (wordLabel != null) wordLabel.setText(formatForDisplay(mask.toString()));
        updateImage();
    }

    private void enableLetterButtons(Scene scene) {
        if (scene == null) return; // just in case
        scene.getRoot().lookupAll(".button").forEach(n -> {
            if (n instanceof Button btn && btn.getOnAction() != null) btn.setDisable(false);
        });
    }

    private void updateImage() {
        int stage = 6 - lives;                   // 0..6
        String path = "/hangman/images/h" + stage + ".png";
        var url = getClass().getResource(path);

        if (url == null) {
            System.err.println("Image NOT found on classpath: " + path);
            return;
        }

        System.out.println("Loading image: " + url);
        hangImage.setImage(new Image(url.toExternalForm(), true));
    }

    private void showWin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hangman/win.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) wordLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) { e.printStackTrace(); }
    }
    private void showLose() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hangman/lose.fxml"));
            Scene scene = new Scene(loader.load());
            LoseController loseController = loader.getController();
            loseController.setAnswer(target);
            Stage stage = (Stage) wordLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void swapScene(String fxml) {
        try {
            Stage stage = (Stage) wordLabel.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource(fxml))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}