package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SignupMenu extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SignupMenu.class.getResource("/fxml/signupMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 400);
        if (MainMenu.getStylesheetAddress().equals(MainMenu.class.getResource("/css/darkStyle.css").toString()))
            scene.getStylesheets().add(MainMenu.class.getResource("/css/darkStyle.css").toString());
        stage.setScene(scene);
        stage.show();
    }
}