package com.ash.chitchat.chatcore;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by aziz titu2 on 12/3/2015.
 */
class ServerHoster extends ServerSocket{

    private OnNewClientReceivedListener onNewClientReceivedListener;
    private int clientCount=0;

    ServerHoster(int port, OnNewClientReceivedListener onNewClientReceivedListener) throws IOException {
        super(port);
        this.onNewClientReceivedListener=onNewClientReceivedListener;
    }

    void startHosting(final AshSocket.OnIncomingMessagesReceived onIncomingMessagesReceived){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    try {
                        AshSocket ashSocket = new AshSocket(accept());
                        ashSocket.setKeepAlive(true);
                        ashSocket.initIOManagers();
                        ashSocket.startListeningForIncomingMessages(clientCount,onIncomingMessagesReceived);
                        clientCount++;
                        if(onNewClientReceivedListener!=null)
                        {
                            onNewClientReceivedListener.onNewClientReceived(ashSocket);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    interface OnNewClientReceivedListener{
        void onNewClientReceived(AshSocket ashSocket);
    }
}
