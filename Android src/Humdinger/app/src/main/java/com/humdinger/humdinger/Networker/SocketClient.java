package com.humdinger.humdinger.Networker;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by andres on 26.07.16.
 */
public class SocketClient implements Runnable {
    private final String IP;
    private final int PORT;
    private final ArrayBlockingQueue<Key> keys;
    private final String LOG_TAG = SocketClient.class.getSimpleName();

    public SocketClient(final String IP, final int PORT, final ArrayBlockingQueue<Key> keys) {
        //For passing variables to run method
        this.IP = IP;
        this.PORT = PORT;
        this.keys = keys;
    }

    @Override
    public void run() {

        try {
            Socket client = new Socket(IP, PORT);

            Log.v("Message", "Connection on IP: " + IP + " PORT: " + PORT);
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

            while (true) {
                // loop for sending keypresses

                //send all active keys
                ArrayList<Key> currentKeys = new ArrayList<Key>(keys.size());
                keys.drainTo(currentKeys);
                for (Key k : currentKeys) {
                    if (k.state != 0) {
                        out.writeByte(k.key);
                        out.writeByte(k.state);

                        //get reply
                        boolean msg = in.readBoolean();
                        if (msg) {
                        } else {
                            break;
                        }
                    }
                    Log.v(LOG_TAG, "Successfully sent keypresses");
                }


                //Pause sending thread for some time
                try {
                    Thread.sleep(30); //
                } catch (InterruptedException e) {
                    Log.e(LOG_TAG, "Couldn't put networking thread to sleep");
                }

            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Couldn't initiate connection IOException");
        } catch (SecurityException e) {
            Log.e(LOG_TAG, "Couldn't initiate connection SecurityException");
        }

    }
}