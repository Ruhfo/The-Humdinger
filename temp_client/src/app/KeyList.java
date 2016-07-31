package app;

import app.Key;

/**
 * Created by andres on 26.07.16.
 * Threadsafe datastructure for storing button's states
 */
public class KeyList {

    //Two separate lists for keys and their states pressed(1) not pressed(0)
    private Key[] keys;

    KeyList(Key[] keys){
        this.keys = keys;
    }

    public Key[] getKeys(){
        synchronized (this) {
            return keys;
        }
    }

    public void setKeys(Key[] newKeys){
        synchronized (this){
            keys = newKeys;
        }
    }


}
