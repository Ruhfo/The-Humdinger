package com.humdinger.networker;
import android.util.Log;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.UnresolvedAddressException;

/**
 * Created by andres on 26.07.16.
 */
public class SocketClient implements Runnable{
    private final String IP;
    private final int PORT;
    private final KeyList keys;

    public SocketClient(final String IP, final int PORT, final KeyList keys){
        //For passing variables to run method
        this.IP = IP;
        this.PORT = PORT;
        this.keys = keys;
    }

    @Override public void run(){
        Log.v("Message", "IP: "+IP+" PORT: "+PORT);
        try{
            Log.v("Message", "IP: "+IP+" PORT: "+PORT);
            Socket client = new Socket(IP, PORT);

            Log.v("Message", "Successfully initiated socket");
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

            while (true){
                // loop for sending keypresses

                //send all active keys
                Key[] currentKeys =keys.getKeys();
                for (Key k:currentKeys) {
                    if (k.state != 0){
                        out.writeByte(k.key);
                        out.writeByte(k.state);
                    }
                }
                Log.v("Message", "Successfully sent keypresses");
                boolean msg  = in.readBoolean();
                if (msg == true){
                    Log.v("Message", "Recieved confirmation from server");
                }else{
                    break;
                }

                //Pause sending thread for some time
                try{
                    Thread.sleep(30); //
                } catch (InterruptedException e){
                    Log.getStackTraceString(e);
                }

            }
        client.close();

        } catch (IOException e){
            Log.v("Message", "Couldn't initiate connection IOException");
        } catch (SecurityException e){
            Log.v("Message", "Couldn't initiate connection SecurityException");
        }

    }
}
