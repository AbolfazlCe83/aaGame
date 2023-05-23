package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.Player;
import view.LoginMenu;
import view.MainMenu;
import view.SignupMenu;

import java.io.IOException;

public class LoginController {
    @FXML
    private PasswordField password;
    @FXML
    private TextField username;

    public void runSignupMenu(MouseEvent mouseEvent) throws IOException {
        new SignupMenu().start(LoginMenu.stage);
    }

    public void login(MouseEvent mouseEvent) throws IOException {
        String username = this.username.getText();
        String password = this.password.getText();
        Player player = Player.getPlayerByUsername(username);
        if (username.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Error!", "username is empty!");
            resetFields();
        } else if (password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Error!", "password is empty!");
            resetFields();
        } else if (player == null) {
            showAlert(Alert.AlertType.ERROR, "Login Error!", "user doesn't exist!");
            resetFields();
        } else if (!player.isPasswordCorrect(password)) {
            showAlert(Alert.AlertType.ERROR, "Login Error!", "username or password is incorrect!");
            resetFields();
        } else {
            Player.setLoggedInPlayer(player);
            new MainMenu().start(LoginMenu.stage);
        }
    }

    private void resetFields() {
        this.username.setText("");
        this.password.setText("");
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void playAsGuest(MouseEvent mouseEvent) throws IOException {
        Player.setLoggedInPlayer(null);
        new MainMenu().start(LoginMenu.stage);
    }
}