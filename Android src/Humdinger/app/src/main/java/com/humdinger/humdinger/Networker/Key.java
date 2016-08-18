package com.humdinger.humdinger.Networker;

/**
 * Created by andres on 26.07.16.
 * synchronized Object for storing data of single controller button
 *
 */
public class Key {
    public char key;
    public char state;
    public Key(char key, char state){
        this.key = key;
        this.state = state;
    }

}