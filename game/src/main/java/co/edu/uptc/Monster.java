package co.edu.uptc;


import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Monster implements Runnable{
    private int health;
    private StackPane body;
    private ArrayList<Player> players;
    private Monster m;
    private double x;
    private boolean mR;
    private boolean mL;
    private double y;
    private Timeline attackTimeline;

    private static final double speed = 1d;
    public static final double attackSpeed = 2d;

    

    public Monster(int x, int y, String name) {
        health = 100;
        Circle c = new Circle(50);
        c.setFill(Color.RED);
        Label l = new Label(name);
        l.setPadding(new Insets(0,0,40,0));
        l.setTextFill(Color.WHITE);
        ProgressBar b = new ProgressBar(100);
        b.setPrefSize(60, 10);
        health = 100;
        body = new StackPane(c, l, b);
        body.setPrefSize(100, 100);
        body.setLayoutX(x);
        body.setLayoutY(y);
        this.x = x;
        this.y = y;
        players = new ArrayList<>();
        mR = true;
        attackTimeline = new Timeline(new KeyFrame(Duration.seconds(attackSpeed), e -> attack()));
        attackTimeline.setCycleCount(Timeline.INDEFINITE);
    }



    @Override
    public void run() {
        double drawInterval = 1000000000 / GameView.FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        Platform.runLater(() -> attackTimeline.play());

        while (health > 0) {
            updateMove();
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
        Platform.runLater(() -> {
            AnchorPane parent = (AnchorPane) body.getParent();
            if (parent != null) {
                parent.getChildren().remove(body);
            }
        });
    }

    public void updateMove(){
        Platform.runLater(() ->{
            if(mR){
                if(x + 0.1 < 500){
                    x += speed;
                }else{
                    mR = false;
                    mL = true;
                }
            }else if(mL){
                if(x - 0.1 > 0){
                    x -= speed;
                }else{
                    mR = true;
                    mL = false;
                }
            }
            body.setLayoutX(x);
        });
    }


    private void attack(){
        Platform.runLater(() -> {
            for(Player pla : players){
                Attack a = new Attack(body.getLayoutX(), body.getLayoutY(), pla.getBody().getLayoutX(), pla.getBody().getLayoutY(), body, pla.getBody());
                a.setM(m);
                a.setP(pla);
                a.setPlayerToMonster(false);
                new Thread(a).start();
                Platform.runLater(() -> {
                    AnchorPane parent = (AnchorPane) body.getParent();
                    if (parent != null) {
                        parent.getChildren().add(a.getBody());
                    }
                });
            }
        });
        
        
    }



    public int getHealth() {
        return health;
    }



    public void setHealth(int health) {
        this.health = health;
        Platform.runLater(() -> {
            ProgressBar pb = (ProgressBar) body.getChildren().get(2);
            pb.setProgress((double) health / 100);
        });
    }



    public StackPane getBody() {
        return body;
    }



    public void setBody(StackPane body) {
        this.body = body;
    }



    public ArrayList<Player> getPlayers() {
        return players;
    }



    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }



    public Monster getM() {
        return m;
    }



    public void setM(Monster m) {
        this.m = m;
    }
    
    
}
