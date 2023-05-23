package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.Player;
import view.LoginMenu;

import java.io.IOException;

public class SignupController {
    @FXML
    private PasswordField password;
    @FXML
    private TextField username;
    @FXML
    private PasswordField passwordConfirm;

    public void backToLoginMenu(MouseEvent mouseEvent) throws IOException {
        new LoginMenu().start(LoginMenu.stage);
    }

    public void register(MouseEvent mouseEvent) throws IOException {
        String username = this.username.getText();
        String password = this.password.getText();
        String passwordConfirm = this.passwordConfirm.getText();
        if (username.isEmpty() || passwordConfirm.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Signup Error!", "a field is empty!");
            resetFields();
        } else if (!username.matches("\\w+")) {
            showAlert(Alert.AlertType.ERROR, "Signup Error!", "username format is incorrect!");
            resetFields();
        } else if (!validPassword(password)) {
            showAlert(Alert.AlertType.ERROR, "Signup Error!", "password is weak!");
            resetFields();
        } else if (Player.getPlayerByUsername(username) != null) {
            showAlert(Alert.AlertType.ERROR, "Signup Error!", "a player with this username already exists!");
            resetFields();
        } else if (!password.equals(passwordConfirm)) {
            showAlert(Alert.AlertType.ERROR, "Signup Error!", "password confirm doesn't match!");
            resetFields();
        } else {
            Player player = new Player(username, password);
            int randomAvatarNumber = (int)(Math.random()*(6)+1);
            String avatarSource = SignupController.class.getResource("/avatars/avatar" + randomAvatarNumber + ".png").toString();
            player.setAvatarResource(avatarSource);
            Player.addPlayer(player);
            Player.savePlayers();
            showAlert(Alert.AlertType.INFORMATION, "Signup successful!", "you successfully registered!");
            new LoginMenu().start(LoginMenu.stage);
        }

    }

    private boolean validPassword(String password) {
        return password.length() >= 6 &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[\\~\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\-\\_\\=\\+\\]\\[\"\\;\\:\\>\\<].*");
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void resetFields() {
        this.username.setText("");
        this.password.setText("");
        this.passwordConfirm.setText("");
    }
}
