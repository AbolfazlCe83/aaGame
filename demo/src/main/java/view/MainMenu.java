package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu extends Application {
    private static String stylesheetAddress = (MainMenu.class.getResource("/css/styleSheet.css").toString());

    public MainMenu() throws IOException {
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource("/fxml/mainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 400);
        scene.getStylesheets().add(stylesheetAddress);
        stage.setScene(scene);
        stage.show();
    }

    public static void setStylesheetAddress(String stylesheetAddress) {
        MainMenu.stylesheetAddress = stylesheetAddress;
    }

    public static String getStylesheetAddress() {
        return stylesheetAddress;
    }
}
