package app;

/**
 * Created by andres on 26.07.16.
 * Object for storing data of single controller button
 */
public class Key {
    public char key;
    public int state;
    Key(char key, int state){
        this.key = key;
        //state can't be larger than 9 (single digit on string) //TODO: think better solution for this (state as char?)
        if (state < 9){
            this.state = state;
        } else{
            this.state = 9;
        }
    }
}
