package co.edu.uptc;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class Attack implements Runnable{

    private double fromX;
    private double fromY;
    private double toX;
    private double toY;
    private Circle body;
    private StackPane source;
    private StackPane target;
    private Player p;
    private Monster m;
    private boolean playerToMonster;
    private boolean action;


    private static final double SPEED = 3.5d;

    
    public Attack(double fromX, double fromY, double toX, double toY, StackPane source, StackPane target) {
        action = false;
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        body = new Circle(10);
        body.setLayoutX(fromX);
        body.setLayoutY(fromY);
        this.source = source;
        this.target = target;
        action = true;
    }

    @Override
    public void run() {

        double dx = toX - fromX;
        double dy = toY - fromY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double directionX = dx / distance;
        double directionY = dy / distance;
        double drawInterval = 1000000000 / GameView.FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (action) {
            Platform.runLater(() -> {
                double currentX = body.getLayoutX();
                double currentY = body.getLayoutY();
                double nextX = currentX + directionX * SPEED;
                double nextY = currentY + directionY * SPEED;

                // Verifica si el ataque alcanzó el objetivo
                if (Math.abs(nextX - toX) < SPEED && Math.abs(nextY - toY) < SPEED || body.getBoundsInParent().intersects(target.getBoundsInParent())) {
                    nextX = toX;
                    nextY = toY;
                    action = false;
                    checkCollision();

                    AnchorPane parent = (AnchorPane) body.getParent();
                    if (parent != null) {
                        parent.getChildren().remove(body);
                    }
                }

                body.setLayoutX(nextX);
                body.setLayoutY(nextY);
            });

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                if (remainingTime > 0) {
                    Thread.sleep((long) remainingTime / 1000000);
                }
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkCollision() {
        if (body.getBoundsInParent().intersects(target.getBoundsInParent())) {
            if(playerToMonster){
                Platform.runLater(() -> {
                    m.setHealth(m.getHealth() - 2);
                    ProgressBar pb = (ProgressBar) (m.getBody().getChildren().get(2));
                    
                    pb.setProgress(pb.getProgress() - 0.2);
                });
            }else{
                Platform.runLater(() -> {
                    p.setHealth(p.getHealth() - 2);
                    ProgressBar pb = (ProgressBar) (p.getBody().getChildren().get(2));
                    
                    pb.setProgress(pb.getProgress() - 0.2);
                });
            }
            return true;
        }
        return false;
    }

    



    public Circle getBody() {
        return body;
    }



    public void setBody(Circle body) {
        this.body = body;
    }

    public double getFromX() {
        return fromX;
    }

    public void setFromX(double fromX) {
        this.fromX = fromX;
    }

    public double getFromY() {
        return fromY;
    }

    public void setFromY(double fromY) {
        this.fromY = fromY;
    }

    public double getToX() {
        return toX;
    }

    public void setToX(double toX) {
        this.toX = toX;
    }

    public double getToY() {
        return toY;
    }

    public void setToY(double toY) {
        this.toY = toY;
    }

    public StackPane getSource() {
        return source;
    }

    public void setSource(StackPane source) {
        this.source = source;
    }

    public StackPane getTarget() {
        return target;
    }

    public void setTarget(StackPane target) {
        this.target = target;
    }

    public boolean isAction() {
        return action;
    }

    public void setAction(boolean action) {
        this.action = action;
    }


    public static double getSpeed() {
        return SPEED;
    }

    public Player getP() {
        return p;
    }

    public void setP(Player p) {
        this.p = p;
    }

    public Monster getM() {
        return m;
    }

    public void setM(Monster m) {
        this.m = m;
    }

    public boolean isPlayerToMonster() {
        return playerToMonster;
    }

    public void setPlayerToMonster(boolean playerToMonster) {
        this.playerToMonster = playerToMonster;
    }

    
}
