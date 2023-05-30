package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Circle;
import model.Player;

import java.io.IOException;
import java.util.ArrayList;

public class Game extends Application {
    private Pane gamePane;
    private static Game currentGame;
    private double rotationSpeed;
    private double windSpeed;
    private int freezeTime;
    private int ballNumbers;
    private int firstBallNumbers;
    private String shootKey;
    public boolean music;

    private Stage gameStage;

    private ArrayList<Circle> smallBalls;
    private ArrayList<Line> lines;
    public static AudioClip audioClip;
    private Timeline timeline;

    public Game() {
        Game.audioClip = new AudioClip(Game.class.getResource("/musics/game.wav").toExternalForm());
        Game.audioClip.setCycleCount(-1);
        initializeVariables();
        this.smallBalls = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.music = Setting.music;
        Game.currentGame = this;
    }

    private void initializeVariables() {
        this.rotationSpeed = Setting.rotationSpeed;
        this.windSpeed = Setting.windSpeed;
        this.freezeTime = Setting.freezeTime;
        this.ballNumbers = Setting.ballNumbers;
        this.shootKey = Setting.shootKey;
        this.firstBallNumbers = ballNumbers;
    }


    @Override
    public void start(Stage stage) throws Exception {
        if (this.music)
            audioClip.play();
        Pane pane = FXMLLoader.load(Game.class.getResource("/fxml/game.fxml"));
        this.gamePane = pane;
        this.gameStage = stage;

        Circle bigcircle = new Circle(300, 275, 75);
        ArrayList<Line> firstLines = bigcircle.initializeLines();
        addLines(firstLines);
        ArrayList<Circle> firstBalls = bigcircle.initializeBalls();
        Text text = new Text(String.valueOf(ballNumbers));
        text.setX(295);
        text.setY(655);
        text.setFill(Color.WHITE);

        Text timerTitle = new Text("Timer");
        timerTitle.setX(500);
        timerTitle.setY(40);
        timerTitle.setFill(Color.BLACK);
        timerTitle.setFont(new Font(25));

        Text timerMinute = new Text("02");
        Text point = new Text(":");
        Text timerSecond = new Text("00");
        timerMinute.setX(512);
        point.setX(535);
        timerSecond.setX(543);
        timerMinute.setFill(Color.PURPLE);
        timerSecond.setFill(Color.PURPLE);
        point.setFill(Color.PURPLE);
        timerMinute.setY(65);
        timerSecond.setY(65);
        point.setY(65);
        timerMinute.setFont(new Font(17));
        point.setFont(new Font(17));
        timerSecond.setFont(new Font(17));

        Text leftOverBalls = new Text(String.valueOf(ballNumbers));
        leftOverBalls.setX(60);
        leftOverBalls.setFill(Color.RED);
        leftOverBalls.setFont(new Font(25));
        leftOverBalls.setY(65);

        Text throwRate = new Text("Throw Rate");
        throwRate.setX(450);
        throwRate.setFill(Color.BLACK);
        throwRate.setFont(new Font(25));
        throwRate.setY(660);

        Text degree = new Text("0");
        degree.setX(500);
        degree.setFill(Color.PURPLE);
        degree.setFont(new Font(25));
        degree.setY(685);

        Text score = new Text("0");
        score.setFill(Color.BLUE);
        score.setX(315);
        score.setY(65);
        score.setFont(new Font(25));

        Text playerScoreTitle = new Text("Player Score");
        playerScoreTitle.setX(250);
        playerScoreTitle.setY(40);
        playerScoreTitle.setFill(Color.BLACK);
        playerScoreTitle.setFont(new Font(25));

        Text leftBalls = new Text("Left Over Balls");
        leftBalls.setX(10);
        leftBalls.setY(40);
        leftBalls.setFill(Color.BLACK);
        leftBalls.setFont(new Font(25));

        Text freezeCharge = new Text("Freeze Charge");
        freezeCharge.setFocusTraversable(false);
        freezeCharge.setFill(Color.BLACK);
        freezeCharge.setX(20);
        freezeCharge.setY(660);
        freezeCharge.setFont(new Font(20));
        pane.getChildren().add(freezeCharge);

        Text freeze = new Text("0");
        freeze.setFocusTraversable(false);
        freeze.setFill(Color.BLUE);
        freeze.setX(65);
        freeze.setY(685);
        freeze.setFont(new Font(20));
        pane.getChildren().add(freeze);
        LineTransition lineTransition = new LineTransition(rotationSpeed);
        lineTransition.addNode(new Text("1"));
        lineTransition.play();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> setTimer(timerMinute, timerSecond, score, lineTransition)));
        timeline.setCycleCount(-1);
        timeline.play();
        this.timeline = timeline;
        Circle mainSmallCircle = createMainCircle(pane, text, freeze, lineTransition, leftOverBalls, score, firstBallNumbers, degree, timerMinute, timerSecond, timeline);


        Rotate rotate = new Rotate();
        rotate.setPivotY(275);
        rotate.setPivotX(300);
        rotate.setAngle(45);
        firstLines.get(4).getTransforms().add(rotate);
        firstBalls.get(4).getTransforms().add(rotate);
        this.addBalls(firstBalls);
        for (int i = 0; i < firstLines.size(); i++) {
            lineTransition.addNode(firstLines.get(i));
            lineTransition.addNode(firstBalls.get(i));
        }
        pane.getChildren().addAll(firstLines);
        pane.getChildren().addAll(firstBalls);
        pane.getChildren().add(bigcircle);
        pane.getChildren().add(mainSmallCircle);
        pane.getChildren().add(text);
        pane.getChildren().add(leftOverBalls);
        pane.getChildren().add(leftBalls);
        pane.getChildren().add(score);
        pane.getChildren().add(playerScoreTitle);
        pane.getChildren().add(throwRate);
        pane.getChildren().add(degree);
        pane.getChildren().add(timerTitle);
        pane.getChildren().addAll(timerMinute, point, timerSecond);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        pane.getChildren().get(13).requestFocus();
        this.gamePane = pane;
        stage.show();
    }

    private void setTimer(Text timerMinute, Text timerSecond, Text score, LineTransition lineTransition) {
        if (Integer.parseInt(timerSecond.getText()) == 0 && Integer.parseInt(timerMinute.getText()) == 0 && ballNumbers > 0) {
            loseGame(score, timerSecond, timerMinute, lineTransition);
        } else if (Integer.parseInt(timerSecond.getText()) == 0) {
            timerSecond.setText("59");
            timerMinute.setText("0" + (Integer.parseInt(timerMinute.getText()) - 1));
        } else {
            if (Integer.parseInt(timerSecond.getText()) <= 9) {
                timerSecond.setText("0" + (Integer.parseInt(timerSecond.getText()) - 1));
            } else
                timerSecond.setText(String.valueOf(Integer.parseInt(timerSecond.getText()) - 1));
        }
    }

    private Circle createMainCircle(Pane pane, Text text, Text freeze, LineTransition lineTransition, Text leftBalls, Text score, int firstBallNumbers, Text degree, Text timerMinute, Text timerSecond, Timeline timeline) {
        Circle mainSmallCircle = new Circle(300, 650, 15);
        mainSmallCircle.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String keyName = keyEvent.getCode().getName();
                if (keyName.equals("Space")) {
                    if (ballNumbers == Math.ceil(firstBallNumbers * 0.75)) {
                        increaseRadius(lineTransition, score, timerMinute, timerSecond);
                    }
                    if (ballNumbers == Math.ceil(firstBallNumbers * 0.5))
                        setWind(degree);
                    if (ballNumbers == Math.ceil(firstBallNumbers * 0.5))
                        setVisible(lineTransition);
                    shootKey(ballNumbers, pane, freeze, lineTransition, score, firstBallNumbers, timerMinute, timerSecond, timeline, mainSmallCircle);
                    if (ballNumbers == Math.ceil(firstBallNumbers * 0.75))
                        applyPhase2(lineTransition);
                    if (ballNumbers == Game.this.firstBallNumbers / 2 + 1)
                        leftBalls.setFill(Color.ORANGE);
                    else if (ballNumbers == 6)
                        leftBalls.setFill(Color.GREEN);
                    leftBalls.setText(String.valueOf(Integer.parseInt(text.getText()) - 1));
                    ballNumbers--;
                    text.setText(String.valueOf(Integer.parseInt(text.getText()) - 1));
                } else if (keyName.equals("Tab")) {
                    freeze(pane, freezeTime, lineTransition, freeze);
                } else if (keyName.equals("Esc")) {
                    pause(lineTransition, timeline);
                } else if (keyName.equals("Right"))
                    goRight(mainSmallCircle, text);
                else if (keyName.equals("Left"))
                    goLeft(mainSmallCircle, text);
            }
        });
        return mainSmallCircle;
    }

    private void goLeft(Circle mainSmallCircle, Text text) {
        if (mainSmallCircle.getCenterX() > 45) {
            mainSmallCircle.setCenterX(mainSmallCircle.getCenterX() - 15);
            text.setX(text.getX() - 15);
        }
    }

    private void goRight(Circle mainSmallCircle, Text text) {
        if (mainSmallCircle.getCenterX() < 535) {
            mainSmallCircle.setCenterX(mainSmallCircle.getCenterX() + 15);
            text.setX(text.getX() + 15);
        }
    }

    private void setWind(Text degree) {
        degree.setText("-6");
        this.setWindSpeed(-6);
        Timeline one = new Timeline(new KeyFrame(Duration.seconds(3)));
        one.setCycleCount(1);
        one.play();
        one.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                degree.setText("-12");
                setWindSpeed(-12);
                Timeline two = new Timeline(new KeyFrame(Duration.seconds(3)));
                two.setCycleCount(1);
                two.play();
                two.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        degree.setText("-15");
                        setWindSpeed(-15);
                        Timeline three = new Timeline(new KeyFrame(Duration.seconds(3)));
                        three.setCycleCount(1);
                        three.play();
                        three.setOnFinished(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                degree.setText("0");
                                setWindSpeed(0);
                                Timeline four = new Timeline(new KeyFrame(Duration.seconds(3)));
                                four.setCycleCount(1);
                                four.play();
                                four.setOnFinished(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        degree.setText("6");
                                        setWindSpeed(6);
                                        Timeline five = new Timeline(new KeyFrame(Duration.seconds(3)));
                                        five.setCycleCount(1);
                                        five.play();
                                        five.setOnFinished(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent actionEvent) {
                                                degree.setText("12");
                                                setWindSpeed(12);
                                                Timeline six = new Timeline(new KeyFrame(Duration.seconds(3)));
                                                six.setCycleCount(1);
                                                six.play();
                                                six.setOnFinished(new EventHandler<ActionEvent>() {
                                                    @Override
                                                    public void handle(ActionEvent actionEvent) {
                                                        degree.setText("15");
                                                        setWindSpeed(15);
                                                        Timeline seven = new Timeline(new KeyFrame(Duration.seconds(3)));
                                                        seven.setCycleCount(1);
                                                        seven.play();
                                                        seven.setOnFinished(new EventHandler<ActionEvent>() {
                                                            @Override
                                                            public void handle(ActionEvent actionEvent) {
                                                                degree.setText("-6");
                                                                setWindSpeed(-6);
                                                                one.play();
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    private void setVisible(LineTransition lineTransition) {
        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(1.5)));
        delay.setCycleCount(1);
        delay.play();

        lineTransition.setVisible(false);
        Timeline white = new Timeline(new KeyFrame(Duration.seconds(1)));
        white.setCycleCount(1);
        white.play();
        white.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                lineTransition.setVisible(true);
                Timeline black = new Timeline(new KeyFrame(Duration.seconds(1)));
                black.setCycleCount(1);
                black.play();
                black.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        lineTransition.setVisible(false);
                        white.play();
                    }
                });
            }
        });
    }

    private void increaseRadius(LineTransition lineTransition, Text score, Text timerMinute, Text timerSecond) {
        ArrayList<Circle> balls = lineTransition.getBalls();
        double firstRadius = 15;
        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(1)));
        delay.setCycleCount(1);
        delay.play();
        {
            lineTransition.setRadius(firstRadius * 1.05);
            checkEndGame(score, lineTransition, timerMinute, timerSecond);
            lineTransition.setRadius(firstRadius * 1.07);
            checkEndGame(score, lineTransition, timerMinute, timerSecond);
            lineTransition.setRadius(firstRadius * 1.1);
            checkEndGame(score, lineTransition, timerMinute, timerSecond);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.3)));
            timeline.setCycleCount(1);
            timeline.play();
            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    lineTransition.setRadius(firstRadius * 1.07);
                    lineTransition.setRadius(firstRadius * 1.05);
                    lineTransition.setRadius(firstRadius);
                    Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1.3)));
                    timeline1.setCycleCount(1);
                    timeline1.play();
                    timeline1.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            lineTransition.setRadius(firstRadius * 1.05);
                            checkEndGame(score, lineTransition, timerMinute, timerSecond);
                            lineTransition.setRadius(firstRadius * 1.07);
                            checkEndGame(score, lineTransition, timerMinute, timerSecond);
                            lineTransition.setRadius(firstRadius * 1.1);
                            checkEndGame(score, lineTransition, timerMinute, timerSecond);
                            timeline.play();
                        }
                    });
                }
            });
        }
    }

    private void checkEndGame(Text score, LineTransition lineTransition, Text timerMinute, Text timerSecond) {
        ArrayList<Circle> balls = lineTransition.getBalls();
        for (int i = 0; i < balls.size() - 1; i++) {
            for (int j = i + 1; j < balls.size(); j++) {
                if (balls.get(i).getBoundsInParent().intersects(balls.get(j).getBoundsInParent())) {
                    lineTransition.stop();
                    loseGame(score, timerSecond, timerMinute, lineTransition);
                    break;
                }
            }
        }
    }

    private void loseGame(Text score, Text timerSecond, Text timerMinute, LineTransition lineTransition) {
        this.timeline.stop();
        Game.audioClip.stop();
        lineTransition.stop();
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

    private void pause(LineTransition lineTransition, Timeline timeline) {
        Pane pane = this.gamePane;
        Stage gameStage = this.gameStage;

        lineTransition.stop();
        timeline.pause();

        VBox pauseMenu = new VBox();
        pauseMenu.setAlignment(Pos.CENTER);
        pauseMenu.setLayoutX(200);
        pauseMenu.setLayoutY(150);
        pauseMenu.setPrefHeight(300);
        pauseMenu.setPrefWidth(200);
        pauseMenu.setStyle("-fx-background-color: #9d9393");
        pauseMenu.setSpacing(15);
        pauseMenu.setFocusTraversable(false);

        Button restart = new Button("Restart");
        restart.setFont(new Font(18));
        restart.setTextFill(Color.DARKBLUE);
        restart.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        restart.setStyle("-fx-background-radius: 15");
        restart.setFocusTraversable(false);

        Button exit = new Button("Exit");
        exit.setFont(new Font(18));
        exit.setTextFill(Color.DARKBLUE);
        exit.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        exit.setStyle("-fx-background-radius: 15");
        exit.setFocusTraversable(false);

        Button mute = new Button("Mute");
        mute.setFont(new Font(18));
        mute.setTextFill(Color.DARKBLUE);
        mute.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        mute.setStyle("-fx-background-radius: 15");
        mute.setFocusTraversable(false);

        Button help = new Button("Help");
        help.setFont(new Font(18));
        help.setTextFill(Color.DARKBLUE);
        help.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        help.setStyle("-fx-background-radius: 15");
        help.setFocusTraversable(false);

        Button changeMusic = new Button("Change Music");
        changeMusic.setFont(new Font(18));
        changeMusic.setTextFill(Color.DARKBLUE);
        changeMusic.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        changeMusic.setStyle("-fx-background-radius: 15");
        changeMusic.setFocusTraversable(false);

        Button resume = new Button("Resume");
        resume.setFont(new Font(18));
        resume.setTextFill(Color.DARKBLUE);
        resume.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        resume.setStyle("-fx-background-radius: 15");
        resume.setFocusTraversable(false);

        pauseMenu.getChildren().addAll(resume, mute, changeMusic, help, restart, exit);
        pane.getChildren().add(pauseMenu);

        resume.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pane.getChildren().remove(pauseMenu);
                lineTransition.play();
                timeline.play();
            }
        });

        restart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Game.audioClip.stop();
                    new Game().start(gameStage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        mute.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (music) {
                    Game.audioClip.stop();
                    music = false;
                } else {
                    Game.audioClip.play();
                    music = true;
                }
            }
        });

        exit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Game.audioClip.stop();
                    new MainMenu().start(gameStage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        help.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    showHelpMenu(pane);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        changeMusic.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showChangeMusicMenu(pane);
            }
        });

    }

    private void showChangeMusicMenu(Pane pane) {
        VBox musicMenu = new VBox();
        musicMenu.setAlignment(Pos.CENTER);
        musicMenu.setLayoutX(200);
        musicMenu.setLayoutY(150);
        musicMenu.setPrefHeight(320);
        musicMenu.setPrefWidth(200);
        musicMenu.setStyle("-fx-background-color: #9d9393");
        musicMenu.setSpacing(50);
        musicMenu.setFocusTraversable(false);

        Button music1 = new Button("Music1");
        music1.setFont(new Font(18));
        music1.setTextFill(Color.DARKBLUE);
        music1.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        music1.setStyle("-fx-background-radius: 15");
        music1.setFocusTraversable(false);

        Button music2 = new Button("Music2");
        music2.setFont(new Font(18));
        music2.setTextFill(Color.DARKBLUE);
        music2.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        music2.setStyle("-fx-background-radius: 15");
        music2.setFocusTraversable(false);

        Button music3 = new Button("Music3");
        music3.setFont(new Font(18));
        music3.setTextFill(Color.DARKBLUE);
        music3.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        music3.setStyle("-fx-background-radius: 15");
        music3.setFocusTraversable(false);

        musicMenu.getChildren().addAll(music1, music2, music3);

        pane.getChildren().add(musicMenu);

        music1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Game.audioClip.stop();
                Game.audioClip = new AudioClip(Game.class.getResource("/musics/game.wav").toExternalForm());
                Game.audioClip.play();
                pane.getChildren().remove(musicMenu);
            }
        });

        music2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Game.audioClip.stop();
                Game.audioClip = new AudioClip(Game.class.getResource("/musics/game2.wav").toExternalForm());
                Game.audioClip.play();
                pane.getChildren().remove(musicMenu);
            }
        });

        music3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Game.audioClip.stop();
                Game.audioClip = new AudioClip(Game.class.getResource("/musics/game3.wav").toExternalForm());
                Game.audioClip.play();
                pane.getChildren().remove(musicMenu);
            }
        });

    }

    private void showHelpMenu(Pane pane) {
        VBox helpMenu = new VBox();
        helpMenu.setAlignment(Pos.CENTER);
        helpMenu.setLayoutX(200);
        helpMenu.setLayoutY(150);
        helpMenu.setPrefHeight(320);
        helpMenu.setPrefWidth(200);
        helpMenu.setStyle("-fx-background-color: #9d9393");
        helpMenu.setSpacing(50);
        helpMenu.setFocusTraversable(false);

        ImageView spaceImage = new ImageView(new Image(Game.class.getResource("/images/space.png").toString()));
        spaceImage.setFitWidth(100);
        spaceImage.setFitHeight(40);
        spaceImage.setFocusTraversable(false);

        ImageView tabImage = new ImageView(new Image(Game.class.getResource("/images/tab.png").toString()));
        tabImage.setFitWidth(70);
        tabImage.setFitHeight(40);
        tabImage.setFocusTraversable(false);

        Button back = new Button("Back");
        back.setFont(new Font(18));
        back.setTextFill(Color.DARKBLUE);
        back.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        back.setStyle("-fx-background-radius: 15");
        back.setFocusTraversable(false);

        Text shoot = new Text("Shoot");
        shoot.setFont(new Font(25));
        shoot.setFill(Color.DARKBLUE);
        shoot.setX(270);
        shoot.setY(180);
        shoot.setFocusTraversable(false);

        Text freeze = new Text("Freeze");
        freeze.setFont(new Font(25));
        freeze.setFill(Color.DARKBLUE);
        freeze.setX(265);
        freeze.setY(275);
        freeze.setFocusTraversable(false);

        helpMenu.getChildren().addAll(spaceImage, tabImage, back);

        pane.getChildren().addAll(helpMenu, shoot, freeze);

        back.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pane.getChildren().removeAll(helpMenu, shoot, freeze);
            }
        });

    }


    private void freeze(Pane pane, int freezeTime, LineTransition lineTransition, Text freeze) {
        double firstAngle = lineTransition.getAngle();
        if (freeze.getText().equals("100")) {
            lineTransition.setAngle(0.2);
            Timeline freezeTimeLine = new Timeline(new KeyFrame(Duration.millis(freezeTime * 1000), event -> lineTransition.setAngle(firstAngle)));
            freezeTimeLine.setCycleCount(0);
            freezeTimeLine.play();
            freeze.setText("0");
        }
    }

    private void shootKey(int ballNumbers, Pane pane, Text freeze, LineTransition lineTransition, Text score, int firstBallNumbers, Text timerMinute, Text timerSecond, Timeline timeline, Circle mainSmallCircle) {
        Circle ball = new Circle(mainSmallCircle.getCenterX(), mainSmallCircle.getCenterY(), 15);
        AudioClip audioClip1 = new AudioClip(Game.class.getResource("/musics/shoot.wav").toExternalForm());
        audioClip1.setCycleCount(1);
        audioClip1.play();
        Text ballNumber = new Text(String.valueOf(ballNumbers));
        ballNumber.setFill(Color.WHITE);
        ballNumber.setX(295);
        ballNumber.setY(685);
        this.gamePane.getChildren().add(ball);
        this.gamePane.getChildren().add(ballNumber);
        ShootingAnimation shootingAnimation = new ShootingAnimation(pane, ball, ballNumber, windSpeed, freeze, lineTransition, score, firstBallNumbers, timerMinute, timerSecond, timeline);
        shootingAnimation.play();
    }

    private void applyPhase2(LineTransition lineTransition) {
        int randomTime = (int) (Math.random() * (3) + 4);
        lineTransition.setAngle(-lineTransition.getAngle());
        Timeline right = new Timeline(new KeyFrame(Duration.seconds(randomTime),
                event -> lineTransition.setAngle(-lineTransition.getAngle())));
        right.setCycleCount(1);
        right.play();
        right.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                lineTransition.setAngle(lineTransition.getAngle());
                Timeline left = new Timeline(new KeyFrame(Duration.seconds(randomTime),
                        event -> lineTransition.setAngle(-lineTransition.getAngle())));
                left.setCycleCount(1);
                left.play();
                left.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        lineTransition.setAngle(-lineTransition.getAngle());
                        right.setCycleCount(1);
                        right.play();
                    }
                });
            }
        });
    }


    public static Game getCurrentGame() {
        return currentGame;
    }

    public void setRotationSpeed(int rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setFreezeTime(int freezeTime) {
        this.freezeTime = freezeTime;
    }

    public void setBallNumbers(int ballNumbers) {
        this.ballNumbers = ballNumbers;
    }

    public void setShootKey(String shootKey) {
        this.shootKey = shootKey;
    }

    public String getShootKey() {
        return shootKey;
    }

    private void movePivot(Node node, double x, double y) {
        node.getTransforms().add(new Translate(-x, -y));
        node.setTranslateX(x);
        node.setTranslateY(y);
    }

    public double getRotationSpeed() {
        return rotationSpeed;
    }

    public void addBalls(ArrayList<Circle> balls) {
        Game.getCurrentGame().smallBalls.addAll(balls);
    }

    public void addBall(Circle ball) {
        this.smallBalls.add(ball);
    }

    public ArrayList<Circle> getSmallBalls() {
        return this.smallBalls;
    }

    public void addLines(ArrayList<Line> lines) {
        this.lines.addAll(lines);
    }

    public void addLine(Line line) {
        this.lines.add(line);
    }

    public ArrayList<Line> getLines() {
        return this.lines;
    }


}
