package co.edu.uptc;

import java.util.concurrent.Semaphore;

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

public class Player implements Runnable{

    private int health;
    private StackPane body;
    private boolean attack;
    private Monster monster;
    private Player p;
    private Control control;
    private int x;
    private int y;
    private Semaphore attackTurn;
    private int speed = 100;

    
    

    public Player(int x, int y, String name, Semaphore attackTurn) {
        health = 100;
        this.x = x;
        this.y = y;
        this.attackTurn = attackTurn;
        Circle c = new Circle(30);
        c.setFill(Color.GREEN);
        Label l = new Label(name);
        l.setPadding(new Insets(0,0,40,0));
        l.setTextFill(Color.WHITE);
        ProgressBar b = new ProgressBar(100);
        b.setPrefSize(60, 10);
        body = new StackPane(c, l, b);
        body.setPrefSize(60, 60);
        body.setLayoutX(x);
        body.setLayoutY(y);

        
    }

    @Override
    public void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.0 / GameView.FPS), event -> {
            updateMove();
            if (attack) {
                attack();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
        // double drawInterval = 1000000000 / GameView.FPS;
        // double nextDrawTime = System.nanoTime() + drawInterval;
        // while (health > 0) {
        //     updateMove();
        //     if(attack){ 
        //         attack();
        //     }
        //     try {
        //         double remainingTime = nextDrawTime - System.nanoTime();
        //         if (remainingTime > 0) {
        //             Thread.sleep((long) remainingTime / 1000000);
        //         }
        //         nextDrawTime += drawInterval;
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
            
        // }
        // Platform.runLater(() -> {
        //     AnchorPane parent = (AnchorPane) body.getParent();
        //     if (parent != null) {
        //         parent.getChildren().remove(body);
        //     }
        // });
    }

    private void updateMove(){

        Platform.runLater(() -> {
            if(control != null){
                double targetX = x;
                double targetY = y;

                if (control.ismR()) {
                    targetX += speed;
                }
                if (control.ismL()) {
                    targetX -= speed;
                }
                if (control.ismD()) {
                    targetY += speed;
                }
                if (control.ismU()) {
                    targetY -= speed;
                }
                control.restart();
                body.setLayoutX(body.getLayoutX() + (targetX - body.getLayoutX()) * 0.1);
                body.setLayoutY(body.getLayoutY() + (targetY - body.getLayoutY()) * 0.1);

                x = (int) body.getLayoutX();
                y = (int) body.getLayoutY();
            }
        });

    }

    private void attack(){
        new Thread(() -> {
            try {
                attackTurn.acquire();
                Platform.runLater(() -> {
                    ((AnchorPane) body.getParent()).setOnMouseClicked(e -> {
                        Attack a = new Attack(body.getLayoutX(), body.getLayoutY(), e.getX(), e.getY(), body, monster.getBody());
                        a.setM(monster);
                        a.setP(p);
                        a.setPlayerToMonster(true);
                        new Thread(a).start();
                        ((AnchorPane) body.getParent()).getChildren().add(a.getBody());
                        attackTurn.release(); // Release the permit after attack
                    });
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
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

    public boolean isAttack() {
        return attack;
    }

    public void setAttack(boolean attack) {
        this.attack = attack;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public Player getP() {
        return p;
    }

    public void setP(Player p) {
        this.p = p;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    
    
}
