package com.ash.chitchat.Profile;

import com.ash.chitchat.chatcore.Client;
import com.ash.chitchat.chatcore.Server;
import com.ash.chitchat.others.AshMessage;
import com.ash.chitchat.others.QuickTools;

/**
 * Created by aziz titu2 on 12/3/2015.
 */
public class ChatManager {
    final public static int ROLE_IDLE = -1, ROLE_CLIENT = 1, ROLE_SERVER = 2, CHAT_PORT = 2092;

    private static int ROLE = ROLE_IDLE;
    private static Client client;
    private static Server server;
    private static OnMessageReceivedListener onMessageReceivedListener;

    public static String name, host_address = "192.168.1.1";

    public static void setRole(int i) {
        if (i >= ROLE_IDLE && i <= ROLE_SERVER && i != 0) {
            ROLE = i;
        }
    }

    public static int getRole() {
        return ROLE;
    }

    public static boolean init() {

        if (ChatManager.getRole() == ChatManager.ROLE_SERVER) {
            if (client != null)
                client.closeSocket();
            client = null;
            server = new Server();
            server.setOnDataReceivedListener(new Server.OnDataReceivedListener() {
                @Override
                public void onGetMessage(String msg) {
                    onMessageReceivedListener.onMessageReceived(AshMessage.parseMessage(msg));
                }
            });
            return server.initSocket();
        } else if (ChatManager.getRole() == ChatManager.ROLE_CLIENT) {
            if (server != null)
                server.closeSocket();
            server = null;
            client = new Client();
            client.setOnDataReceivedListener(new Client.OnDataReceivedListener() {
                @Override
                public void onGetMessage(String msg) {
                    onMessageReceivedListener.onMessageReceived(AshMessage.parseMessage(msg));
                }
            });
            return client.initSocket();
        } else
            return false;
    }

    public static void sendMessage(String text) {
        text=text.replace("\n", QuickTools.SPLITTER_NEW_LINE);

        text = ChatManager.name + QuickTools.SPLITTER_MSG + text;
        if (ChatManager.getRole() == ChatManager.ROLE_SERVER) {
            if (server != null)
                server.sendMessageToAll(text);
        } else if (ChatManager.getRole() == ChatManager.ROLE_CLIENT) {
            if (client != null)
                client.sendMessageToAll(text);
        }
    }

    public static void setOnMessageReceivedListener(OnMessageReceivedListener onMessageReceivedListener) {
        ChatManager.onMessageReceivedListener = onMessageReceivedListener;
    }

    public interface OnMessageReceivedListener {
        void onMessageReceived(AshMessage msg);
    }
}
