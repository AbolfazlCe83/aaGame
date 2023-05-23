package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class Circle extends javafx.scene.shape.Circle {

    private ArrayList<Circle> smallCircles;
    private ArrayList<Line> firstLines;
    private double x;
    private double y;

    public Circle(double x, double y, double radius) {
        super(x, y, radius);
        this.setFill(Color.BLACK);
        this.smallCircles = new ArrayList<>();
        this.firstLines = new ArrayList<>(5);
        this.x = x;
        this.y = y;
    }

    public ArrayList<Line> initializeLines() {
        Line line = new Line();
        line.setStartX(300);
        line.setStartY(350);
        line.setEndX(300);
        line.setEndY(470);
        line.setFill(Color.BLACK);
        this.firstLines.add(line);
        Line line1 = new Line();
        line1.setStartX(375);
        line1.setStartY(275);
        line1.setEndX(495);
        line1.setEndY(275);
        line1.setFill(Color.BLACK);
        this.firstLines.add(line1);
        Line line2 = new Line();
        line2.setStartX(225);
        line2.setStartY(275);
        line2.setEndX(105);
        line2.setEndY(275);
        line2.setFill(Color.BLACK);
        this.firstLines.add(line2);
        Line line3 = new Line();
        line3.setStartX(300);
        line3.setStartY(200);
        line3.setEndX(300);
        line3.setEndY(80);
        line3.setFill(Color.BLACK);
        this.firstLines.add(line3);
        Line line4 = new Line();
        line4.setStartX(300);
        line4.setStartY(200);
        line4.setEndX(300);
        line4.setEndY(80);
        line4.setFill(Color.BLACK);
        this.firstLines.add(line4);
        return this.firstLines;
    }

    public ArrayList<Circle> initializeBalls() {
        ArrayList<Circle> balls = new ArrayList<>();
        Circle ball1 = new Circle(300, 485, 15);
        Circle ball2 = new Circle(510 , 275, 15);
        Circle ball3 = new Circle(90, 275, 15);
        Circle ball4 = new Circle(300, 65, 15);
        Circle ball5 = new Circle(300, 65, 15);
        balls.add(ball1);
        balls.add(ball2);
        balls.add(ball3);
        balls.add(ball4);
        balls.add(ball5);
        return balls;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
