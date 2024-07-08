package co.edu.uptc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class GameView{
    

    @FXML
    private AnchorPane opPanel;

    @FXML
    private AnchorPane wonPanel;

    @FXML
    private AnchorPane playPanel;

    private ArrayList<Player> players;

    private Semaphore attackTurn;

    public static final int FPS = 60;

    @FXML
    void play(ActionEvent event) {

        Button b = (Button) event.getSource();
        int num = Integer.parseInt(b.getText().split(" ")[0]);
        opPanel.setVisible(false);
        playPanel.setVisible(true);
        playPanel.setFocusTraversable(true);
        
        playing(num);
        
    }

    public void playing(int num){
        attackTurn = new Semaphore(1);
        players = new ArrayList<>();
        Monster m = new Monster(200, 500, "MONSTER");
        m.setM(m);
        m.setPlayers(players);

        for(int i = 0; i < num; i++){
            Player p = new Player(60*i, 0, "p" + (i + 1), attackTurn);
            p.setP(p);
            p.setMonster(m);
            players.add(p);
        }

        players.forEach(p -> {
            p.setControl(new Control(p, new String[]{"D", "A", "S", "W", "Q"})); 
        });
        
        
        playPanel.setOnMousePressed(e -> {
            players.forEach(p -> p.setAttack(true));
        });

        
        
        Platform.runLater(() -> {
            List<StackPane> nodes = players.stream().map(p -> p.getBody()).collect(Collectors.toList());
            playPanel.getChildren().addAll(nodes);
            playPanel.getChildren().add(m.getBody());
        });

        playPanel.setOnKeyPressed(e -> {
            players.forEach(p ->{p.getControl().handleKeyPressed(e);});
        });
        

        playPanel.setOnKeyReleased(e -> {
            players.forEach(p -> p.getControl().handleKeyReleased(e));
        });

        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        List<Thread> playerThreads = new ArrayList<>();
        players.forEach(p -> {
            Thread t = new Thread(p);
            playerThreads.add(t);
            t.start();
        });
        Thread t = new Thread(m);
        playerThreads.add(t);
        t.start();

        new Thread(() -> {
            while (playPanel.isVisible()) {
                for (Iterator<Player> iterator = players.iterator(); iterator.hasNext();) {
                    Player p = iterator.next();
                    if (p.getHealth() <= 0) {
                        iterator.remove(); 
                    } else if (m.getHealth() <= 0) {
                        Platform.runLater(() -> {
                            playPanel.setVisible(false);
                            ((Label) wonPanel.getChildren().get(0)).setText("PLAYERS WIN!");
                            wonPanel.setVisible(true);

                        });
                        break; 
                    }
                }
                if(players.isEmpty()){
                    Platform.runLater(() -> {
                        playPanel.setVisible(false);
                        ((Label) wonPanel.getChildren().get(0)).setText("MONSTER WIN!");
                        wonPanel.setVisible(true);

                    });
                    
                }

                try {
                    double remainingTime = (nextDrawTime + drawInterval) - System.nanoTime();
                    if (remainingTime > 0) {
                        Thread.sleep((long) remainingTime / 1000000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
        
        

    }

}
