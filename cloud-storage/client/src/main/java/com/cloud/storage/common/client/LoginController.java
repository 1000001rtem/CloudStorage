package com.cloud.storage.common.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController implements Controllers {

    @FXML
    GridPane rootNode;

    @FXML
    TextField loginArea;

    @FXML
    PasswordField passArea;

    Network network = Network.getInstance();

    public void authAction() {
        network.setController(this);
        network.sendAuthInfo(loginArea.getText(), passArea.getText());
        loginArea.clear();
        passArea.clear();
    }

    @Override
    public void changeScene() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    change();
                }
            });
        } else {
            change();
        }
    }

    private void change() {
        try {
            Parent mainScene = FXMLLoader.load(getClass().getResource("/main.fxml"));
            ((Stage) rootNode.getScene().getWindow()).setScene(new Scene(mainScene));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
