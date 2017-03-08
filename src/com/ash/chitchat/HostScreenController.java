package com.ash.chitchat;

import com.ash.chitchat.Profile.ChatManager;
import com.ash.chitchat.others.QuickTools;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by aziz titu2 on 12/3/2015.
 */
public class HostScreenController implements Initializable {
    @FXML
    Button hostBtn;
    @FXML
    TextField NameTF;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    protected void startHosting(){
        if(!NameTF.getText().equals(""))
        {
            QuickTools.print("Hosting the chat... ");
            ChatManager.setRole(ChatManager.ROLE_SERVER);
            ChatManager.name=NameTF.getText();
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
            }
        }
    }
}
