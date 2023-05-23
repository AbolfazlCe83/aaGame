package view;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import model.Circle;

import java.util.ArrayList;

public class LineTransition extends Transition {
    private ArrayList<Node> nodes;
    private double speed;

    private double angle;

    public LineTransition(double speed) {
        this.speed = speed;
        if (this.speed == 5) {
            this.angle = 0.68;
        } else if (this.speed == 10) {
            this.angle = 0.8;
        } else this.angle = 2;
        this.nodes = new ArrayList<>();
        this.setCycleDuration(Duration.millis(2000));
        this.setCycleCount(-1);
        this.setInterpolator(Interpolator.LINEAR);
    }

    @Override
    protected void interpolate(double v) {
        Rotate rotate = new Rotate();
        rotate.setAngle(this.angle);
        rotate.setPivotX(300);
        rotate.setPivotY(275);
        for (Node node : this.nodes)
            node.getTransforms().add(rotate);
    }


    public void addNode(Node node) {
        this.nodes.add(node);
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return this.angle;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Circle> getBalls() {
        ArrayList<Circle> balls = new ArrayList<>();
        for (Node node : this.nodes)
            if (node instanceof Circle)
                balls.add((Circle) node);
        return balls;
    }
}
