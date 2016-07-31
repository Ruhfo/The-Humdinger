package app;

import app.Key;
import app.SocketClient;

/**
 * Created by andres on 26.07.16.
 */
public class Networker {

    public static void main (String[] args){
        //Temp testing input
        Key[] listOfKeys = {new Key('a', 0), new Key('b', 0)};
        KeyList gameKeys = new KeyList(listOfKeys);

        SocketClient sockClient = new SocketClient("127.0.0.1", 12345, gameKeys); //pass IP and port to client socket
        new Thread(sockClient).start(); // Start new client socket
    }
}

