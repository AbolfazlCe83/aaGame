package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.Circle;
import model.Player;

import java.io.IOException;
import java.util.ArrayList;


public class ShootingAnimation extends Transition {
    private Text freeze;
    private Pane gamePane;
    private Circle ball;
    private Text ballNumber;
    private double windSpeed;
    private LineTransition lineTransition;
    private Text score;
    private int firstBallNumbers;
    private Text timerMinute;
    private Text timerSecond;
    private Timeline timeline;

    public ShootingAnimation(Pane gamePane, Circle ball, Text ballNumber, double windSpeed, Text freeze, LineTransition lineTransition, Text score, int firstBallNumbers, Text timerMinute, Text timerSecond, Timeline timeline) {
        this.freeze = freeze;
        this.ballNumber = ballNumber;
        this.gamePane = gamePane;
        this.ball = ball;
        this.windSpeed = windSpeed;
        this.lineTransition = lineTransition;
        this.score = score;
        this.firstBallNumbers = firstBallNumbers;
        this.timerMinute = timerMinute;
        this.timerSecond = timerSecond;
        this.timeline = timeline;
        this.setCycleCount(-1);
        this.setCycleDuration(Duration.millis(1000));
    }

    @Override
    protected void interpolate(double v) {
        double bigX = 300;
        double bigY = 275;
        double smallX = ball.getCenterX();
        double smallY = ball.getCenterY();
        double distance = Math.abs(Math.sqrt(Math.pow((smallX - bigX), 2) + Math.pow((smallY - bigY), 2)));
        double x = 0;
        if (windSpeed == 6) {
            x = ball.getCenterX() + 1;
        } else if (windSpeed == 12) {
            x = ball.getCenterX() + 1.5;
        } else if (windSpeed == 0) {
            x = ball.getCenterX() + 0;
        } else if (windSpeed == 15)
            x = ball.getCenterX() + 1.8;
        else if (windSpeed == -6) {
            x = ball.getCenterX() - 1;
        } else if (windSpeed == -12) {
            x = ball.getCenterX() - 1.5;
        } else if (windSpeed == -15)
            x = ball.getCenterX() - 1.8;
        double y = ball.getCenterY() - 10;
        if (distance <= 230) {
            if (!endGame()) {
                if (ballNumber.getText().equals("1")) {
                    Line line = new Line();
                    line.setStartX(ball.getCenterX());
                    line.setEndX(bigX);
                    line.setStartY(ball.getCenterY());
                    line.setEndY(bigY + 75);
                    this.gamePane.getChildren().add(line);
                    score.setText(String.valueOf(Integer.parseInt(score.getText()) + 2));
                    checkEndGame();
                } else {
                    this.stop();
                    if (freeze.getText().equals("100")) {

                    } else
                        freeze.setText(String.valueOf(Integer.parseInt(freeze.getText()) + 10));
                    score.setText(String.valueOf(Integer.parseInt(score.getText()) + 2));
                    Line line = new Line();
                    line.setStartX(ball.getCenterX());
                    line.setEndX(bigX);
                    line.setStartY(ball.getCenterY());
                    line.setEndY(bigY + 75);
                    ball.setX(x);
                    ball.setY(y);
                    this.gamePane.getChildren().add(line);
                    this.lineTransition.addNode(line);
                    Game.getCurrentGame().addLine(line);
                    this.lineTransition.addNode(ball);
                    this.lineTransition.addNode(ballNumber);
                    Game.getCurrentGame().addBall(ball);
                }
            } else {
                gamePane.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
                this.stop();
                this.lineTransition.stop();
                endTheGame();
            }
        }
        ball.setCenterY(y);
        ball.setCenterX(x);
        ballNumber.setY(y);
        ballNumber.setX(x);
    }

    private void increaseRadius(LineTransition lineTransition, Text score) {
        ArrayList<Circle> balls = lineTransition.getBalls();
        double firstRadius = 15;
        {
            balls.forEach(ball -> ball.setRadius(firstRadius * 1.05));
            check(balls, score, lineTransition);
            balls.forEach(ball -> ball.setRadius(firstRadius * 1.07));
            check(balls, score, lineTransition);
            balls.forEach(ball -> ball.setRadius(firstRadius * 1.1));
            check(balls, score, lineTransition);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1)));
            timeline.setCycleCount(1);
            timeline.play();
            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    balls.forEach(ball -> ball.setRadius(firstRadius * 1.07));
                    balls.forEach(ball -> ball.setRadius(firstRadius * 1.05));
                    Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1)));
                    timeline1.setCycleCount(1);
                    timeline1.play();
                    timeline1.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            balls.forEach(ball -> ball.setRadius(firstRadius * 1.05));
                            check(balls, score, lineTransition);
                            balls.forEach(ball -> ball.setRadius(firstRadius * 1.07));
                            check(balls, score, lineTransition);
                            balls.forEach(ball -> ball.setRadius(firstRadius * 1.1));
                            check(balls, score, lineTransition);
                            timeline.play();
                        }
                    });
                }
            });
        }
    }

    private void check(ArrayList<Circle> balls, Text score, LineTransition lineTransition) {
        for (int i = 0; i < balls.size() - 1; i++) {
            for (int j = i + 1; j < balls.size(); j++) {
                if (balls.get(i).getBoundsInParent().intersects(balls.get(j).getBoundsInParent())) {
                    lineTransition.stop();
                    loseGame(score);
                    break;
                }
            }
        }
    }

    private void loseGame(Text score) {
        Game.audioClip.stop();
        this.timeline.stop();
        Pane pane = this.gamePane;
        pane.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        VBox vBox = new VBox();
        vBox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        vBox.setAlignment(Pos.CENTER);
        vBox.setLayoutX(200);
        vBox.setLayoutY(150);
        vBox.setPrefHeight(300);
        vBox.setPrefWidth(200);
        vBox.setStyle("-fx-background-color: #9d9393");
        vBox.setSpacing(50);
        String second = String.valueOf(60 - Integer.parseInt(timerSecond.getText()));
        if (Integer.parseInt(second) <= 9)
            second = "0" + second;
        Text time = new Text("Time: 0" + (1 - Integer.parseInt(timerMinute.getText())) + ":" + second);
        String levelTime = "0" + (1 - Integer.parseInt(timerMinute.getText())) + ":" + second;
        Text scoreBoard = new Text("Score: " + score.getText());
        Button mainMenu = new Button("Main Menu");
        mainMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    new MainMenu().start(LoginMenu.stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        time.setFill(Color.BLACK);
        scoreBoard.setFill(Color.BLACK);
        time.setFont(new Font("SansSerif", 15));
        scoreBoard.setFont(new Font("SansSerif", 15));
        mainMenu.setTextFill(Color.BLACK);
        mainMenu.setFont(new Font("SansSerif", 15));
        vBox.getChildren().addAll(time, scoreBoard, mainMenu);

        Text lose = new Text("You Lose :)!!!!");
        lose.setFont(new Font(30));
        lose.setFill(Color.RED);
        lose.setX(210);
        lose.setY(180);

        Player player = Player.getLoggedInPlayer();
        if (player != null) {
            player.setScore(Integer.parseInt(score.getText()));
            player.setLevelTime(levelTime);
            Player.savePlayers();
        }
        pane.getChildren().addAll(vBox, lose);
    }


    private void endTheGame() {
        Game.audioClip.stop();
        this.timeline.stop();
        Pane pane = this.gamePane;
        VBox vBox = new VBox();
        vBox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        vBox.setAlignment(Pos.CENTER);
        vBox.setLayoutX(200);
        vBox.setLayoutY(150);
        vBox.setPrefHeight(300);
        vBox.setPrefWidth(200);
        vBox.setStyle("-fx-background-color: #9d9393");
        vBox.setSpacing(50);
        String second = String.valueOf(60 - Integer.parseInt(timerSecond.getText()));
        if (Integer.parseInt(second) <= 9)
            second = "0" + second;
        Text time = new Text("Time: 0" + (1 - Integer.parseInt(timerMinute.getText())) + ":" + second);
        String levelTime = "0" + (1 - Integer.parseInt(timerMinute.getText())) + ":" + second;
        Text score = new Text("Score: " + this.score.getText());
        Button mainMenu = new Button("Main Menu");
        mainMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    new MainMenu().start(LoginMenu.stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        time.setFill(Color.BLACK);
        score.setFill(Color.BLACK);
        time.setFont(new Font("SansSerif", 15));
        score.setFont(new Font("SansSerif", 15));
        mainMenu.setTextFill(Color.BLACK);
        mainMenu.setFont(new Font("SansSerif", 15));
        vBox.getChildren().addAll(time, score, mainMenu);

        Text lose = new Text("You Lose :)!!!!");
        lose.setFont(new Font(30));
        lose.setFill(Color.RED);
        lose.setX(210);
        lose.setY(180);

        Player player = Player.getLoggedInPlayer();
        if (player != null) {
            player.setScore(Integer.parseInt(this.score.getText()));
            player.setLevelTime(levelTime);
            Player.savePlayers();
        }
        pane.getChildren().addAll(vBox, lose);
    }

    private boolean endGame() {
        ArrayList<Circle> balls = Game.getCurrentGame().getSmallBalls();
        for (Circle circle : balls) {
            if (ball.getBoundsInParent().intersects(circle.getBoundsInParent()))
                return true;
        }
        return false;
    }

    private void checkEndGame() {
        timeline.stop();
        Game.audioClip.stop();
        gamePane.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
        this.stop();
        this.lineTransition.stop();
        Pane pane = this.gamePane;
        VBox vBox = new VBox();
        vBox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        vBox.setAlignment(Pos.CENTER);
        vBox.setLayoutX(200);
        vBox.setLayoutY(150);
        vBox.setPrefHeight(300);
        vBox.setPrefWidth(200);
        vBox.setStyle("-fx-background-color: #9d9393");
        vBox.setSpacing(50);
        String second = String.valueOf(60 - Integer.parseInt(timerSecond.getText()));
        if (Integer.parseInt(second) <= 9)
            second = "0" + second;
        Text time = new Text("Time: 0" + (1 - Integer.parseInt(timerMinute.getText())) + ":" + second);
        String levelTime = "0" + (1 - Integer.parseInt(timerMinute.getText())) + ":" + second;
        Text PlayerScore = new Text("Score: " + this.score.getText());
        Button mainMenu = new Button("Main Menu");
        mainMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    new MainMenu().start(LoginMenu.stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        time.setFill(Color.BLACK);
        PlayerScore.setFill(Color.BLACK);
        time.setFont(new Font("SansSerif", 15));
        PlayerScore.setFont(new Font("SansSerif", 15));
        mainMenu.setTextFill(Color.BLACK);
        mainMenu.setFont(new Font("SansSerif", 15));
        vBox.getChildren().addAll(time, PlayerScore, mainMenu);

        Text win = new Text("You Win :)!!!!");
        win.setFont(new Font(30));
        win.setFill(Color.GREEN);
        win.setX(210);
        win.setY(180);

        Player player = Player.getLoggedInPlayer();
        if (player != null) {
            player.setScore(Integer.parseInt(this.score.getText()));
            player.setLevelTime(levelTime);
            Player.savePlayers();
        }
        pane.getChildren().addAll(vBox, win);
    }

}
