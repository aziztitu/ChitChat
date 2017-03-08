package com.ash.chitchat.chatcore;

import com.ash.chitchat.Profile.ChatManager;
import com.ash.chitchat.others.QuickTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    Thread thread;
    private AshSocket ashSocket;
    private PrintWriter printWriter;
    private BufferedReader receiveReader;

    public OnDataReceivedListener onDataReceivedListener;

    class ClientRunnable implements Runnable {

        @Override
        public void run() {
            try {
                QuickTools.print("Started Client Thread");
                listenForIncomingData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public Client() {
        thread = new Thread(new ClientRunnable());
        thread.setDaemon(true);
    }

    public void sendMessageToAll(String msg){
        ashSocket.sendTextData(msg);
    }

    public void listenForIncomingData() {
        QuickTools.print("Started Listening as client");
        String data;
        while (ashSocket != null && ashSocket.isConnected()) {
            try {
                data=ashSocket.getIncomingData();
                if(!data.equals(""))
                    processIncomingData(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void processIncomingData(String data) {
        onDataReceivedListener.onGetMessage(data);
    }

    public boolean initSocket() {

        try {
            InetAddress serverAddress = InetAddress.getByName(ChatManager.host_address);
            QuickTools.print("sever address: "+serverAddress);
            ashSocket = new AshSocket(new Socket(serverAddress, ChatManager.CHAT_PORT));
            QuickTools.print("Socket created as client");
            ashSocket.setKeepAlive(true);
            ashSocket.initIOManagers();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        thread.start();
        return true;
    }

    public void closeSocket() {
        QuickTools.print("Socket closed");
        try {
            ashSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnDataReceivedListener(OnDataReceivedListener onDataReceivedListener) {
        this.onDataReceivedListener = onDataReceivedListener;
    }

    public interface OnDataReceivedListener {
        void onGetMessage(String msg);
    }
}
