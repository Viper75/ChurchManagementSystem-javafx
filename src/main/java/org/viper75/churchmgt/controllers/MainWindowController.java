package org.viper75.churchmgt.controllers;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML private BorderPane mainWindowBp;
    @FXML private VBox sideBarVB;

    @FXML private Button toggleMenuBtn;
    @FXML private Button membershipBtn;
    @FXML private Button optionsBtn;
    @FXML private Button accountingBtn;
    @FXML private Button reportsBtn;

    public void initialize(URL location, ResourceBundle resources) {
        handleMembershipBtn_Click();

        membershipBtn.setOnAction(e -> handleMembershipBtn_Click());
        optionsBtn.setOnAction(this::handleOptionsBtn_Click);
        toggleMenuBtn.setOnAction(this::handleToggleMenuBtn_Click);
    }

    private void handleMembershipBtn_Click(){
        BorderPane membershipBp = null;

        try{
            membershipBp = FXMLLoader.load(getClass().getClassLoader().getResource("views/Membership.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainWindowBp.setCenter(membershipBp);
    }

    private void handleOptionsBtn_Click(ActionEvent event){
        BorderPane optionsBp = null;

        try{
            optionsBp = FXMLLoader.load(getClass().getClassLoader().getResource("views/Settings.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainWindowBp.setCenter(optionsBp);
    }

    private void handleToggleMenuBtn_Click(ActionEvent event) {
        FadeTransition showFileRootTransition = new FadeTransition(Duration.millis(500), sideBarVB);
        showFileRootTransition.play();

        if (sideBarVB.getPrefWidth() == 160){
            sideBarVB.setPrefWidth(15);
            toggleMenuBtn.setText("");
            membershipBtn.setText("");
            accountingBtn.setText("");
            optionsBtn.setText("");
            reportsBtn.setText("");
        }
        else{
            sideBarVB.setPrefWidth(160);
            toggleMenuBtn.setText("Menu");
            membershipBtn.setText("Membership");
            accountingBtn.setText("Accounting");
            optionsBtn.setText("Settings");
            reportsBtn.setText("Reports");
        }

    }
}
