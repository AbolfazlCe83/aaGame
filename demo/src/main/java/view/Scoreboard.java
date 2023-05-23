package view;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Scoreboard extends Application {
    @FXML
    public Text username1;
    public Text username2;
    public Text username3;
    public Text username4;
    public Text username5;
    public Text username6;
    public Text username7;
    public Text username8;
    public Text username9;
    public Text username10;
    public Text score1;
    public Text score2;
    public Text score3;
    public Text score4;
    public Text score5;
    public Text score6;
    public Text score7;
    public Text score8;
    public Text score9;
    public Text score10;
    public Text time1;
    public Text time2;
    public Text time3;
    public Text time4;
    public Text time5;
    public Text time6;
    public Text time7;
    public Text time8;
    public Text time9;
    public Text time10;

    @Override
    @FXML
    public void start(Stage stage) throws Exception {
        Pane pane = FXMLLoader.load(Objects.requireNonNull(Scoreboard.class.getResource("/fxml/scoreboard.fxml")));
        int playerSize = Player.getAllPlayers().size();
        if (playerSize < 10)
            Player.initializePlayers(playerSize);
        username1 = (Text) pane.lookup("#username1");
        username2 = (Text) pane.lookup("#username2");
        username3 = (Text) pane.lookup("#username3");
        username4 = (Text) pane.lookup("#username4");
        username5 = (Text) pane.lookup("#username5");
        username6 = (Text) pane.lookup("#username6");
        username7 = (Text) pane.lookup("#username7");
        username8 = (Text) pane.lookup("#username8");
        username9 = (Text) pane.lookup("#username9");
        username10 = (Text) pane.lookup("#username10");
        score1 = (Text) pane.lookup("#score1");
        score2 = (Text) pane.lookup("#score2");
        score3 = (Text) pane.lookup("#score3");
        score4 = (Text) pane.lookup("#score4");
        score5 = (Text) pane.lookup("#score5");
        score6 = (Text) pane.lookup("#score6");
        score7 = (Text) pane.lookup("#score7");
        score8 = (Text) pane.lookup("#score8");
        score9 = (Text) pane.lookup("#score9");
        score10 = (Text) pane.lookup("#score10");
        time1 = (Text) pane.lookup("#time1");
        time2 = (Text) pane.lookup("#time2");
        time3 = (Text) pane.lookup("#time3");
        time4 = (Text) pane.lookup("#time4");
        time5 = (Text) pane.lookup("#time5");
        time6 = (Text) pane.lookup("#time6");
        time7 = (Text) pane.lookup("#time7");
        time8 = (Text) pane.lookup("#time8");
        time9 = (Text) pane.lookup("#time9");
        time10 = (Text) pane.lookup("#time10");
        ArrayList<Player> players = getOrderedPlayers();
        this.username1.setText(players.get(0).getUsername());
        this.username2.setText(players.get(1).getUsername());
        this.username3.setText(players.get(2).getUsername());
        this.username4.setText(players.get(3).getUsername());
        this.username5.setText(players.get(4).getUsername());
        this.username6.setText(players.get(5).getUsername());
        this.username7.setText(players.get(6).getUsername());
        this.username8.setText(players.get(7).getUsername());
        this.username9.setText(players.get(8).getUsername());
        this.username10.setText(players.get(9).getUsername());
        score1.setText(String.valueOf(players.get(0).getScore()));
        score2.setText(String.valueOf(players.get(1).getScore()));
        score3.setText(String.valueOf(players.get(2).getScore()));
        score4.setText(String.valueOf(players.get(3).getScore()));
        score5.setText(String.valueOf(players.get(4).getScore()));
        score6.setText(String.valueOf(players.get(5).getScore()));
        score7.setText(String.valueOf(players.get(6).getScore()));
        score8.setText(String.valueOf(players.get(7).getScore()));
        score9.setText(String.valueOf(players.get(8).getScore()));
        score10.setText(String.valueOf(players.get(9).getScore()));
        time1.setText(String.valueOf(players.get(0).getLevelTime()));
        time2.setText(String.valueOf(players.get(1).getLevelTime()));
        time3.setText(String.valueOf(players.get(2).getLevelTime()));
        time4.setText(String.valueOf(players.get(3).getLevelTime()));
        time5.setText(String.valueOf(players.get(4).getLevelTime()));
        time6.setText(String.valueOf(players.get(5).getLevelTime()));
        time7.setText(String.valueOf(players.get(6).getLevelTime()));
        time8.setText(String.valueOf(players.get(7).getLevelTime()));
        time9.setText(String.valueOf(players.get(8).getLevelTime()));
        time10.setText(String.valueOf(players.get(9).getLevelTime()));
        Scene scene = new Scene(pane);
        if (MainMenu.getStylesheetAddress().equals(MainMenu.class.getResource("/css/darkStyle.css").toString()))
            scene.getStylesheets().add(MainMenu.class.getResource("/css/darkStyle.css").toString());
        stage.setScene(scene);
        stage.show();
    }

    private static ArrayList<Player> getOrderedPlayers() {
        ArrayList<Player> players = Player.getAllPlayers();
        for (int i = 0; i < players.size() - 1; i++)
            for (int j = i + 1; j < players.size(); j++)
                if (players.get(i).getScore() < players.get(j).getScore())
                    Collections.swap(players, i, j);
        for (int i = 0; i < players.size() - 1; i++)
            for (int j = i + 1; j < players.size(); j++)
                if ((players.get(i).getScore() == players.get(j).getScore())
                        && (players.get(i).getLevelTime() > players.get(j).getLevelTime()))
                    Collections.swap(players, i, j);
        return players;
    }

    public void backToMainMenu(MouseEvent mouseEvent) throws Exception {
        new MainMenu().start(LoginMenu.stage);
    }
}
