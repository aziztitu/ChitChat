package com.ash.chitchat.chatcore;

import com.ash.chitchat.Profile.ChatManager;
import com.ash.chitchat.others.QuickTools;

import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aziz titu2 on 12/3/2015.
 */
public class Server {

    ServerHoster serverHoster;
    List<AshSocket> ashSockets;
    public OnDataReceivedListener onDataReceivedListener;

    public static boolean SERVER_ON = false;

    public Server() {
        ashSockets = new ArrayList<AshSocket>();
    }

    public void sendMessageToAll(String msg) {
        for (int i = 0; i < ashSockets.size(); i++) {
            ashSockets.get(i).sendTextData(msg);
        }
    }

    public void processIncomingData(String data) {
        onDataReceivedListener.onGetMessage(data);
    }

    public boolean initSocket() {
        SERVER_ON = true;
        QuickTools.print("Started Server");
        try {
            serverHoster = new ServerHoster(ChatManager.CHAT_PORT, new ServerHoster.OnNewClientReceivedListener() {
                @Override
                public void onNewClientReceived(AshSocket ashSocket) {
                    ashSockets.add(ashSocket);
                }
            });

            serverHoster.startHosting( new AshSocket.OnIncomingMessagesReceived() {
                @Override
                public void incomingMessageReceived(int socketId, String data) {
                    if (!data.equals("")) {
                        for (int j = 0; j < ashSockets.size(); j++) {
                            if (socketId != ashSockets.get(j).socketId) {
                                ashSockets.get(j).sendTextData(data);
                            }
                        }
                        processIncomingData(data);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        QuickTools.print("Started Server Hosting...");
        return true;
    }

    public void closeSocket() {
        QuickTools.print("Socket closed");
        try {
            for (int i = 0; i < ashSockets.size(); i++) {
                ashSockets.get(i).closeIOManagers();
                ashSockets.get(i).close();
            }
            serverHoster.close();
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
