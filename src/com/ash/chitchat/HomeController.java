package com.ash.chitchat;

import com.ash.chitchat.others.QuickTools;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @FXML
    protected void startAsServer() {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("res/host_screen.fxml"));
            Main.stage.setScene(new Scene(root, 600, 500));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void startAsClient() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("res/join_screen.fxml"));
            Main.stage.setScene(new Scene(root, 600, 500));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
