package view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.FXPermission;
import model.Player;

import java.io.File;
import java.io.IOException;

public class Profile extends Application {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private ImageView myAvatar;
    @FXML
    private ImageView avatar1;
    @FXML
    private ImageView avatar2;
    @FXML
    private ImageView avatar3;
    @FXML
    private ImageView avatar4;
    @FXML
    private ImageView avatar5;
    @FXML
    private ImageView avatar6;
    private static AnchorPane avatarAnchorPane;

    static {
        try {
            avatarAnchorPane = FXMLLoader.load(Profile.class.getResource("/fxml/avatar.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stage stage;
    @Override
    public void start(Stage stage) throws Exception {
        Profile.stage = stage;
        AnchorPane anchorPane = FXMLLoader.load(Profile.class.getResource("/fxml/profile.fxml"));
        Scene scene = new Scene(anchorPane , 520, 400);
        anchorPane.getStylesheets().add(Profile.class.getResource("/css/styleSheet.css").toString());
        stage.setScene(scene);
        stage.show();
    }

    public void enterChangeMenu(MouseEvent mouseEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(Profile.class.getResource("/fxml/changeUsername.fxml"));
        Scene scene = new Scene(anchorPane , 520, 400);
        if (MainMenu.getStylesheetAddress().equals(MainMenu.class.getResource("/css/darkStyle.css").toString()))
            scene.getStylesheets().add(MainMenu.class.getResource("/css/darkStyle.css").toString());
        stage.setScene(scene);
        stage.show();
    }

    public void backToProfile(MouseEvent mouseEvent) throws Exception {
        start(stage);
    }

    public void setNewUsername(MouseEvent mouseEvent) throws Exception {
        String username = this.username.getText();
        if (Player.getPlayerByUsername(username) != null)
            showAlert(Alert.AlertType.ERROR, "Change Error!", "A user with this username already exists!");
        else if (username.isEmpty())
            showAlert(Alert.AlertType.ERROR, "Change Error!", "New username is empty!");
        else {
            Player.getLoggedInPlayer().setUsername(username);
            Player.savePlayers();
            showAlert(Alert.AlertType.INFORMATION, "Change Successful!", "Your username successfully changed!");
            start(stage);
        }
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void enterChangePassword(MouseEvent mouseEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(Profile.class.getResource("/fxml/changePassword.fxml"));
        Scene scene = new Scene(anchorPane , 520, 400);
        if (MainMenu.getStylesheetAddress().equals(MainMenu.class.getResource("/css/darkStyle.css").toString()))
            scene.getStylesheets().add(MainMenu.class.getResource("/css/darkStyle.css").toString());
        stage.setScene(scene);
        stage.show();
    }

    public void setNewPassword(MouseEvent mouseEvent) throws Exception {
        String newPassword = this.password.getText();
        if (newPassword.isEmpty())
            showAlert(Alert.AlertType.ERROR, "Change Error!", "Password is empty!");
        else {
            Player player = Player.getLoggedInPlayer();
            player.setPassword(newPassword);
            Player.savePlayers();
            showAlert(Alert.AlertType.INFORMATION, "Change Successful!", "Your password successfully changed!");
            start(stage);
        }

    }

    public void logout(MouseEvent mouseEvent) throws IOException {
        Player.setLoggedInPlayer(null);
        new LoginMenu().start(stage);
    }

    public void deleteAccount(MouseEvent mouseEvent) throws IOException {
        Player player = Player.getLoggedInPlayer();
        Player.setLoggedInPlayer(null);
        Player.removePlayer(player);
        showAlert(Alert.AlertType.INFORMATION, "Delete Account", "Your account successfully deleted!");
        new LoginMenu().start(stage);
    }

    public void backToMainMenu(MouseEvent mouseEvent) throws IOException {
        new MainMenu().start(stage);
    }

    public void enterChooseAvatar(MouseEvent mouseEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(Profile.class.getResource("/fxml/avatar.fxml"));
        myAvatar = (ImageView) anchorPane.lookup("#myAvatar");
        if (Player.getLoggedInPlayer().getAvatarResource() != null)
            myAvatar.setImage(new Image(Player.getLoggedInPlayer().getAvatarResource()));
        FileChooser fileChooser = new FileChooser();
        Button chooseFile = new Button("Choose your photo");
        chooseFile.setLayoutX(10);
        chooseFile.setLayoutY(360);
        chooseFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                File selectedFile = fileChooser.showOpenDialog(stage);
                try {
                    setAvatar(selectedFile);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        anchorPane.getChildren().add(chooseFile);
        Scene scene = new Scene(anchorPane , 520, 400);
        if (MainMenu.getStylesheetAddress().equals(MainMenu.class.getResource("/css/darkStyle.css").toString()))
            scene.getStylesheets().add(MainMenu.class.getResource("/css/darkStyle.css").toString());
        stage.setScene(scene);
        stage.show();
    }

    private void setAvatar(File selectedFile) throws Exception {
        String url = selectedFile.getAbsolutePath();
        Player player = Player.getLoggedInPlayer();
        player.setAvatarResource(url);
        Player.savePlayers();
        start(stage);
    }

    public void chooseAvatar(MouseEvent mouseEvent) throws Exception {
        ImageView imageView = (ImageView) mouseEvent.getSource();
        Player player = Player.getLoggedInPlayer();
        player.setAvatarResource(imageView.getImage().getUrl());
        Player.savePlayers();
        start(stage);
    }
}
