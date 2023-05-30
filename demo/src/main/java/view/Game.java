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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
    private  int freezeTime;
    private int ballNumbers;
    private int firstBallNumbers;
    private String shootKey;

    private Stage gameStage;

    private ArrayList<Circle> smallBalls;
    private ArrayList<Line> lines;
    public static AudioClip audioClip;

    public Game() {
        Game.audioClip = new AudioClip(Game.class.getResource("/musics/game.wav").toExternalForm());
        Game.audioClip.setCycleCount(-1);
        initializeVariables();
        this.smallBalls = new ArrayList<>();
        this.lines = new ArrayList<>();
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
        timerTitle.setX(300);
        timerTitle.setY(65);
        timerTitle.setFill(Color.BLACK);
        timerTitle.setFont(new Font(25));

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
        score.setX(515);
        score.setY(65);
        score.setFont(new Font(25));

        Text playerScoreTitle = new Text("Player Score");
        playerScoreTitle.setX(450);
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
        Circle mainSmallCircle = createMainCircle(pane, text, freeze, lineTransition, leftOverBalls, score, firstBallNumbers, degree);


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


        Scene scene = new Scene(pane);
        stage.setScene(scene);
        pane.getChildren().get(13).requestFocus();
        this.gamePane = pane;
        stage.show();
    }

    private Circle createMainCircle(Pane pane, Text text, Text freeze, LineTransition lineTransition, Text leftBalls, Text score, int firstBallNumbers, Text degree) {
        Circle mainSmallCircle = new Circle(300, 650, 15);
        mainSmallCircle.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String keyName = keyEvent.getCode().getName();
                if (keyName.equals("Space")) {
                    if (ballNumbers == Math.ceil(firstBallNumbers * 0.75)) {
                        increaseRadius(lineTransition, score);
                    }
                    if (ballNumbers == Math.ceil(firstBallNumbers * 0.5))
                        setWind(degree);
                    if (ballNumbers == Math.ceil(firstBallNumbers * 0.5))
                        setVisible(lineTransition);
                    shootKey(ballNumbers, pane, freeze, lineTransition, score, firstBallNumbers);
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
                    pause(lineTransition);
                }
            }
        });
        return mainSmallCircle;
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

    private void increaseRadius(LineTransition lineTransition, Text score) {
        ArrayList<Circle> balls = lineTransition.getBalls();
        double firstRadius = 15;
        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(1)));
        delay.setCycleCount(1);
        delay.play();
        {
            lineTransition.setRadius(firstRadius * 1.05);
            checkEndGame(score, lineTransition);
            lineTransition.setRadius(firstRadius * 1.07);
            checkEndGame(score, lineTransition);
            lineTransition.setRadius(firstRadius * 1.1);
            checkEndGame(score, lineTransition);
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
                            checkEndGame(score, lineTransition);
                            lineTransition.setRadius(firstRadius * 1.07);
                            checkEndGame(score, lineTransition);
                            lineTransition.setRadius(firstRadius * 1.1);
                            checkEndGame(score, lineTransition);
                            timeline.play();
                        }
                    });
                }
            });
        }
    }

    private void checkEndGame(Text score, LineTransition lineTransition) {
        ArrayList<Circle> balls = lineTransition.getBalls();
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
            Text time = new Text("Time: ");
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
                Player.savePlayers();
            }
            pane.getChildren().addAll(vBox, lose);
        }

    private void pause(LineTransition lineTransition) {
        Pane pane = this.gamePane;
        Stage gameStage = this.gameStage;

        lineTransition.stop();

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
            }
        });

        restart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    new Game().start(gameStage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        exit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
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

    private void shootKey(int ballNumbers, Pane pane, Text freeze, LineTransition lineTransition, Text score, int firstBallNumbers) {
        Circle ball = new Circle(300, 655, 15);
        AudioClip audioClip1 = new AudioClip(Game.class.getResource("/musics/shoot.wav").toExternalForm());
        audioClip1.setCycleCount(1);
        audioClip1.play();
        Text ballNumber = new Text(String.valueOf(ballNumbers));
        ballNumber.setFill(Color.WHITE);
        ballNumber.setX(295);
        ballNumber.setY(685);
        this.gamePane.getChildren().add(ball);
        this.gamePane.getChildren().add(ballNumber);
        ShootingAnimation shootingAnimation = new ShootingAnimation(pane, ball, ballNumber, windSpeed, freeze, lineTransition, score, firstBallNumbers);
        shootingAnimation.play();
    }

    private void applyPhase2(LineTransition lineTransition) {
        int randomTime = (int)(Math.random()*(3)+4);
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
