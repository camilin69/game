package co.edu.uptc;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.input.KeyEvent;

public class Control{
    private Player p;

    //[Right, Left, Down, Up]
    private String[] keys;
    private boolean mR;
    private boolean mL;
    private boolean mU;
    private boolean mD;
    private Set<String> pressedKeys;

    

    public Control(Player p, String[] keys) {
        this.p = p;
        this.keys = keys;
        this.pressedKeys = new HashSet<>();
    }

    public void handleKeyPressed(KeyEvent event) {
        String key = event.getText().toUpperCase();
        pressedKeys.add(key);
        updateMovement();
    }

    public void handleKeyReleased(KeyEvent event) {
        String key = event.getText().toUpperCase();
        pressedKeys.remove(key);
        updateMovement();
    }

    private void updateMovement() {
        mR = pressedKeys.contains(keys[0]) && p.getX() < 540;
        mL = pressedKeys.contains(keys[1]) && p.getX() > 0;
        mD = pressedKeys.contains(keys[2]) && p.getY() < 540;
        mU = pressedKeys.contains(keys[3]) && p.getY() > 0;
        p.setAttack(pressedKeys.contains(keys[4]));
    }

    public void restart(){
        mR = false;
        mL = false;
        mD = false;
        mU = false;
    }

    public Player getP() {
        return p;
    }



    public void setP(Player p) {
        this.p = p;
    }



    public boolean ismR() {
        return mR;
    }



    public void setmR(boolean mR) {
        this.mR = mR;
    }



    public boolean ismL() {
        return mL;
    }



    public void setmL(boolean mL) {
        this.mL = mL;
    }



    public boolean ismU() {
        return mU;
    }



    public void setmU(boolean mU) {
        this.mU = mU;
    }



    public boolean ismD() {
        return mD;
    }



    public void setmD(boolean mD) {
        this.mD = mD;
    }


    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }
    
    
}
