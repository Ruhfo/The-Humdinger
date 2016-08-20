package com.humdinger.networker;
import android.util.Log;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.UnresolvedAddressException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by andres on 26.07.16.
 */
public class SocketClient implements Runnable{
    private final String IP;
    private final int PORT;
    private final ArrayBlockingQueue<Key> keys;

    public SocketClient(final String IP, final int PORT, final ArrayBlockingQueue<Key> keys){
        //For passing variables to run method
        this.IP = IP;
        this.PORT = PORT;
        this.keys = keys;
    }

    @Override public void run(){

        try{
            Socket client = new Socket(IP, PORT);

            Log.v("Message", "Connection on IP: "+IP+" PORT: "+PORT);
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

            while (true){
                // loop for sending keypresses

                //send all active keys
                ArrayList<Key> currentKeys  = new ArrayList<Key>(keys.size());
                keys.drainTo(currentKeys);
                for (Key k:currentKeys) {
                    if (k.state != 0){
                        out.writeByte(k.key);
                        //out.writeByte(k.state); We don't need to send satate @moment

                        //get reply
                        boolean msg  = in.readBoolean();
                        if (msg == true){
                        }else{
                            break;
                        }
                    }
                    Log.v("Message", "Successfully sent keypresses");
                }



                //Pause sending thread for some time
                try{
                    Thread.sleep(5); //
                } catch (InterruptedException e){
                    Log.v("Message", "Couldn't put networking thread to sleep");
                }

            }

        } catch (IOException e){
            Log.v("Message", "Couldn't initiate connection IOException");
        } catch (SecurityException e){
            Log.v("Message", "Couldn't initiate connection SecurityException");
        }

    }
}
