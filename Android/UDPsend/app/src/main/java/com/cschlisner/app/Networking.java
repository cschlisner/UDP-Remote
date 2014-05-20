package com.cschlisner.app;

import android.os.AsyncTask;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Cole on 3/13/14.
 */
public class Networking extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... params){
        try {
            int server_port = 9875;
            DatagramSocket s = new DatagramSocket();
            InetAddress local = InetAddress.getByName(params[1]);
            int msg_length = params[0].length();
            byte[] message = params[0].getBytes();
            DatagramPacket p = new DatagramPacket(message, msg_length, local,server_port);
            s.send(p);
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
