package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Player;
import view.*;

public class MainController {
    @FXML
    private Button exitButton;
    @FXML
    public void exitGame(MouseEvent mouseEvent) {
        Stage stage = (Stage) this.exitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void enterSettingMenu(MouseEvent mouseEvent) throws Exception {
        new Setting().start(LoginMenu.stage);
    }

    public void runScoreboard(MouseEvent mouseEvent) throws Exception {
        new Scoreboard().start(LoginMenu.stage);
    }

    public void enterProfileMenu(MouseEvent mouseEvent) throws Exception {
        new Profile().start(LoginMenu.stage);
    }

    public void startGame(MouseEvent mouseEvent) throws Exception {
        String shootKey = Setting.shootKey;
        double rotationSpeed = Setting.rotationSpeed;
        double windSpeed = Setting.windSpeed;
        int freezeTime = Setting.freezeTime;
        int ballNumbers = Setting.ballNumbers;
        Game game = new Game();
        game.start(LoginMenu.stage);
    }
}
