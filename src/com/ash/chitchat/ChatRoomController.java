package com.ash.chitchat;

import com.ash.chitchat.Profile.ChatManager;
import com.ash.chitchat.others.AshMessage;
import com.ash.chitchat.others.QuickTools;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Created by aziz titu2 on 12/3/2015.
 */
public class ChatRoomController implements Initializable {

    @FXML
    VBox chatBox;
    @FXML
    ScrollPane scrollPane;
    @FXML
    TextField msgTF;

    public final int SELF=0, OTHER=1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.stage.setTitle("Chit Chat  -  "+ChatManager.name + ((ChatManager.getRole()==ChatManager.ROLE_SERVER)?" (Host)":""));
        ChatManager.setOnMessageReceivedListener(new ChatManager.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived(AshMessage msg) {
                QuickTools.print("AshMessage Received --> " + msg);
                class SyncMessageDisplay implements Runnable {
                    AshMessage msg;
                    public SyncMessageDisplay(AshMessage ashMessage){
                        msg=ashMessage;
                    }

                    @Override
                    public void run() {
                        displayMessage(OTHER, msg);
                    }
                }

                Platform.runLater(new SyncMessageDisplay(msg));
            }
        });

        chatBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollPane.setVvalue((Double)newValue);
            }
        });
    }

    @FXML
    protected void sendMessage() {
        String text=msgTF.getText();
        if(!text.equals("")) {
            ChatManager.sendMessage(text);
            displayMessage(SELF,text);
            msgTF.setText("");
        }
    }

    private void displayMessage(int from, String msg){
        displayMessage(from, AshMessage.parseMessage(msg));
    }

    private void displayMessage(int from, AshMessage msg){


        AnchorPane anchorPane;

        try {

        String disp_text="";
        if(from==SELF)
        {
            disp_text=msg.getData();
            anchorPane= FXMLLoader.load(getClass().getResource("res/chat_template.fxml"));
            Parent parent = (Parent) anchorPane.getChildren().get(0);
            Label label = (Label)parent.getChildrenUnmodifiable().get(0);
            label.setText(disp_text);
        }
        else
        {
            String disp_name=msg.getSender();
            disp_text=msg.getData();
            anchorPane= FXMLLoader.load(getClass().getResource("res/chat_template2.fxml"));
            Pane pane = (Pane) anchorPane.getChildren().get(0);
            VBox vBox = (VBox) pane.getChildren().get(0);
            Label labelName = (Label)vBox.getChildren().get(0);
            Label labelMsg = (Label)vBox.getChildren().get(1);
            labelName.setText(disp_name);
            labelMsg.setText(disp_text);
        }



            chatBox.getChildren().add(anchorPane);

        } catch (IOException ex) {
        }
    }
}
