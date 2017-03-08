package com.ash.chitchat.chatcore;

import com.ash.chitchat.others.AES;
import com.ash.chitchat.others.QuickTools;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class AshSocket {


    public Socket socket;
    public int socketId = 0;
    private PrintWriter printWriter;
    private BufferedReader receiveReader;

    public OnIncomingMessagesReceived onIncomingMessagesReceived;

    public AshSocket(Socket socket) {
        this.socket = socket;
    }

    public void initIOManagers() throws IOException {
        printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        receiveReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void startListeningForIncomingMessages(int i, OnIncomingMessagesReceived onIncomingMessagesReceived1) {
        socketId = i;
        this.onIncomingMessagesReceived = onIncomingMessagesReceived1;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (Server.SERVER_ON) {
                    try {
                        onIncomingMessagesReceived.incomingMessageReceived(socketId, getIncomingData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public String getIncomingData() throws IOException {
        if (isConnected() && receiveReader != null) {
//            return receiveReader.readLine();
            return AES.decrypt(receiveReader.readLine(),QuickTools.AES_KEY);
        }
        return "";
    }


    public void sendTextData(String text) {
        if (socket.isConnected() && printWriter != null) {
            printWriter.println(AES.encrypt(text,QuickTools.AES_KEY));
            printWriter.flush();
            QuickTools.print("Send AshMessage: " + text);
        } else {
            //Toast.makeText(context,"Client Socket is closed",Toast.LENGTH_SHORT).show();
        }
    }

    public void closeIOManagers() throws IOException {
        if (printWriter != null)
            printWriter.close();
        if (receiveReader != null)
            receiveReader.close();
    }

    public void setKeepAlive(boolean b) throws SocketException {
        socket.setKeepAlive(b);
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public void close() throws IOException {
        socket.close();
    }

    public interface OnIncomingMessagesReceived {
        void incomingMessageReceived(int id, String msg);
    }
}
