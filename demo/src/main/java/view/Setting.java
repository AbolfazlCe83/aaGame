package view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Setting extends Application {
    public static int rotationSpeed = 10;
    public static double windSpeed = 0;
    public static int freezeTime = 5;
    public static int ballNumbers = 15;
    public static String shootKey = "Space";

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = FXMLLoader.load(Setting.class.getResource("/fxml/setting.fxml"));
        ImageView image = new ImageView(new Image(Setting.class.getResource
                ("/images/setting.jpg").toString(), 520, 400, false, false));
        borderPane.getChildren().add(image);
        ImageView settingIcon = new ImageView(new Image(Setting.class.getResource("/images/gear.png").toExternalForm(), 80, 80, false, false));
        settingIcon.setX(7);
        settingIcon.setY(20);
        borderPane.getChildren().add(settingIcon);
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        {
            Button gameDifficulty = new Button("Game Difficulty");
            gameDifficulty.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        startGameDifficulty(stage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            Button ballNumbers = new Button("Ball Numbers");
            ballNumbers.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        startChooseBallNumbers(stage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            Button selectMap = new Button("Select Map");
            Button controlButton = new Button("Control Button");
            controlButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        startCustomizeKey(stage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            Button darkMood = new Button("Dark Mood");
            darkMood.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    MainMenu.setStylesheetAddress(MainMenu.class.getResource("/css/darkStyle.css").toString());
                    try {
                        start(stage);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            Button mainMenu = new Button("Main Menu");
            mainMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        new MainMenu().start(stage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            vBox.getChildren().addAll(gameDifficulty, ballNumbers, selectMap, controlButton, darkMood, mainMenu);
        }
        borderPane.setCenter(vBox);

        borderPane.getStylesheets().add(MainMenu.getStylesheetAddress());
        Text text = new Text(160, 65, "Setting");
        text.setFill(Color.WHITE);
        borderPane.getChildren().add(text);
        Scene scene = new Scene(borderPane, 520, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void startCustomizeKey(Stage stage) throws IOException {
        BorderPane borderPane = FXMLLoader.load(Setting.class.getResource("/fxml/setting.fxml"));
        ImageView image = new ImageView(new Image(Setting.class.getResource
                ("/images/setting.jpg").toString(), 520, 400, false, false));
        borderPane.getChildren().add(image);
        ImageView settingIcon = new ImageView(new Image(Setting.class.getResource
                ("/images/gear.png").toExternalForm(), 80, 80, false, false));
        Text text = new Text("Choose Own Button");
        text.setFill(Color.WHITE);
        text.setX(100);
        text.setY(150);
        text.setStyle("-fx-font-size: 35");
        borderPane.getChildren().add(text);
        settingIcon.setX(7);
        settingIcon.setY(20);
        borderPane.getChildren().add(settingIcon);
        VBox vBox = new VBox();
        vBox.setPrefSize(50,50);
        TextField chooseKey = new TextField();
        chooseKey.setPrefHeight(50);
        chooseKey.setPrefWidth(50);
        Button button = new Button("Set Key");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    setCustomizeKey(chooseKey.getText(), stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        vBox.getChildren().addAll(chooseKey, button);
        borderPane.setCenter(vBox);
        borderPane.getStylesheets().add(MainMenu.getStylesheetAddress());
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }

    private void setCustomizeKey(String text, Stage stage) throws Exception {
        Setting.shootKey = (text);
        new Setting().start(stage);
    }

    private void startChooseBallNumbers(Stage stage) throws IOException {
        BorderPane borderPane = FXMLLoader.load(Setting.class.getResource("/fxml/setting.fxml"));
        ImageView image = new ImageView(new Image(Setting.class.getResource
                ("/images/setting.jpg").toString(), 520, 400, false, false));
        borderPane.getChildren().add(image);
        ImageView settingIcon = new ImageView(new Image(Setting.class.getResource
                ("/images/gear.png").toExternalForm(), 80, 80, false, false));
        Text text = new Text("Choose Ball Numbers");
        text.setFill(Color.WHITE);
        text.setX(100);
        text.setY(150);
        text.setFont(new Font(40));
        borderPane.getChildren().add(text);
        settingIcon.setX(7);
        settingIcon.setY(20);
        borderPane.getChildren().add(settingIcon);
        ChoiceBox<Integer> chooseBall = new ChoiceBox<>();
        chooseBall.getItems().add(15);
        chooseBall.getItems().add(20);
        chooseBall.getItems().add(25);
        chooseBall.getItems().add(30);
        {
            chooseBall.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    int ballNumbers = chooseBall.getValue();
                    setBallNumbers(ballNumbers, stage);
                }
            });
        }
        chooseBall.setLayoutX(200);
        chooseBall.setLayoutY(200);
        borderPane.getChildren().add(chooseBall);
        Scene scene = new Scene(borderPane);
        if (MainMenu.getStylesheetAddress().equals(MainMenu.class.getResource("/css/darkStyle.css").toString()))
            scene.getStylesheets().add(MainMenu.class.getResource("/css/darkStyle.css").toString());
        stage.setScene(scene);
        stage.show();
    }

    private void setBallNumbers(int ballNumbers, Stage stage) {
        Setting.ballNumbers = (ballNumbers);
        try {
            new Setting().start(stage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void startGameDifficulty(Stage stage) throws IOException {
        BorderPane borderPane = FXMLLoader.load(Setting.class.getResource("/fxml/setting.fxml"));
        ImageView image = new ImageView(new Image(Setting.class.getResource
                ("/images/setting.jpg").toString(), 520, 400, false, false));
        borderPane.getChildren().add(image);
        ImageView settingIcon = new ImageView(new Image(Setting.class.getResource
                ("/images/gear.png").toExternalForm(), 80, 80, false, false));
        Text text = new Text("Choose Game Level");
        text.setFill(Color.WHITE);
        text.setX(100);
        text.setY(150);
        text.setFont(new Font(40));
        borderPane.getChildren().add(text);
        settingIcon.setX(7);
        settingIcon.setY(20);
        borderPane.getChildren().add(settingIcon);
        ChoiceBox<String> choiceLevel = new ChoiceBox<>();
        choiceLevel.getItems().add("Level 1");
        choiceLevel.getItems().add("Level 2");
        choiceLevel.getItems().add("Level 3");
        {
            choiceLevel.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    String level = choiceLevel.getValue();
                    setLevel(level, stage);
                }
            });
        }
        choiceLevel.setLayoutX(200);
        choiceLevel.setLayoutY(200);
        borderPane.getChildren().add(choiceLevel);
        Scene scene = new Scene(borderPane);
        if (MainMenu.getStylesheetAddress().equals(MainMenu.class.getResource("/css/darkStyle.css").toString()))
            scene.getStylesheets().add(MainMenu.class.getResource("/css/darkStyle.css").toString());
        stage.setScene(scene);
        stage.show();
    }

    private void setLevel(String level, Stage stage) {
        int rotationSpeed = 0;
        float windSpeed = 0;
        int freezeTime = 0;
        switch (level) {
            case "Level 1" -> {
                rotationSpeed = 5;
                windSpeed = 1.2F;
                freezeTime = 7;
            }
            case "Level 3" -> {
                rotationSpeed = 15;
                windSpeed = 1.8F;
                freezeTime = 3;
            }
            default -> {
                rotationSpeed = 10;
                windSpeed = 1.5F;
                freezeTime = 5;
            }
        }
        Setting.rotationSpeed = (rotationSpeed);
        Setting.freezeTime = (freezeTime);
        Setting.windSpeed = (windSpeed);
        try {
            new Setting().start(stage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
