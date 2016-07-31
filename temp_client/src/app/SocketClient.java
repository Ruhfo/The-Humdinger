package app;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by andres on 26.07.16.
 */
public class SocketClient implements Runnable{
    private final String IP;
    private final int PORT;
    private final KeyList keys;

    SocketClient( final String IP, final int PORT, final KeyList keys){
        //For passing variables to run method
        this.IP = IP;
        this.PORT = PORT;
        this.keys = keys;
    }

    @Override public void run(){
        try{
            Socket client = new Socket(IP, PORT);

            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

            while (true){
                //infinite loop for doing networking stuff
                Key[] currentKeys =keys.getKeys();
                String data = "";
                for (int i=0; i<currentKeys.length;i++){
                    Character key = new Character(currentKeys[i].key);
                    Integer state= new Integer(currentKeys[i].state);

                    data+= key.toString() + state.toString();
                }
                System.out.println(data);
                out.writeUTF(data); //Send current gamepad state
                boolean msg  = in.readBoolean();
                if (msg == true){
                }else{
                    break;
                }
                //format (keycode)(state) example'A1B0'
                try{
                    Thread.sleep(30); //
                } catch (InterruptedException e){
                    System.out.println("Exception trying to put networking to sleep: " + e);
                }

            }
        client.close();
        } catch (IOException e){
            System.out.println("IOException"+ e + " in app.SocketClient init");
        }

    }
}
