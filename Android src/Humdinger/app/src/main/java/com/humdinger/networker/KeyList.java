package com.humdinger.networker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andres on 26.07.16.
 * Threadsafe data structure for storing button's states
 */
public class KeyList {

    //Two separate lists for keys and their states pressed(1) not pressed(0)
    private HashMap<String, Key> keyMap;

    public KeyList(HashMap<String, Key> keyMap){
        this.keyMap = keyMap;
    }

    public Key[] getKeys(){
        synchronized (this) {
            return (Key[]) keyMap.values().toArray();
        }
    }

    public void setKey(String key){
        synchronized (this){
            Key val = keyMap.get(key);
            val.state = (char) 1;
            keyMap.put(key, val);
        }
    }


}
