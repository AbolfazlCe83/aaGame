package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Player;

import java.io.IOException;

public class LoginMenu extends Application {

    public static Stage stage;
    public static void main(String[] args) {
        Player.recoveryPlayers();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        LoginMenu.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(SignupMenu.class.getResource("/fxml/loginmenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 400);
        if (MainMenu.getStylesheetAddress().equals(MainMenu.class.getResource("/css/darkStyle.css").toString()))
            scene.getStylesheets().add(MainMenu.class.getResource("/css/darkStyle.css").toString());
        stage.getIcons().add(new Image(LoginMenu.class.getResource("/images/icon.png").toExternalForm()));
        stage.setScene(scene);
        stage.show();
    }
}