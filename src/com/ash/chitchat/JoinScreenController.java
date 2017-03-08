package com.ash.chitchat;

import com.ash.chitchat.Profile.ChatManager;
import com.ash.chitchat.others.QuickTools;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by aziz titu2 on 12/3/2015.
 */
public class JoinScreenController implements Initializable {
    @FXML
    Button joinBtn;
    @FXML
    TextField NameTF;
    @FXML
    TextField HostAddressTF;
    @FXML
    Label msgLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void joinChat() {
        if(!NameTF.getText().equals("") && !HostAddressTF.getText().equals(""))
        {
            QuickTools.print("Joining a chat... ");
            ChatManager.setRole(ChatManager.ROLE_CLIENT);
            ChatManager.name=NameTF.getText();
            ChatManager.host_address=HostAddressTF.getText();
            msgLabel.setText("Connecting to the host... ");
            if(ChatManager.init())
            {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("res/chat_room.fxml"));
                    Main.stage.setScene(new Scene(root, 600, 500));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                //TODO: Update onError
                msgLabel.setText("Error connecting to the host! Check the host address!");
            }
        }
    }
}
