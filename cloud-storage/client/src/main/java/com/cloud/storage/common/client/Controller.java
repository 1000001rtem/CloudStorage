package com.cloud.storage.common.client;

import com.cloud.storage.common.Directorys;
import com.cloud.storage.common.FileInfo;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.File;

public class Controller implements Directorys, Controllers {

    @FXML
    TableView<FileInfo> clientTable;

    @FXML
    TableView<FileInfo> serverTable;

    @FXML
    TableColumn<FileInfo, String> nameColumnClient;

    @FXML
    TableColumn<FileInfo, String> sizeColumnClient;

    @FXML
    TableColumn<FileInfo, String> nameColumnServer;

    @FXML
    TableColumn<FileInfo, String> sizeColumnServer;

    @FXML
    Pane pane;

    @FXML
    HBox box;

    @FXML
    TextField loginArea;

    @FXML
    PasswordField passArea;

    Network network = Network.getInstance();

    @FXML
    public void initialize() {
        nameColumnClient.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("fileName"));
        sizeColumnClient.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("fileSize"));
        nameColumnServer.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("fileName"));
        sizeColumnServer.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("fileSize"));

        clientTable.setItems(ClientUtilities.fillClientTableList());
        serverTable.setItems(ClientUtilities.getServerTableList());
    }

    public void refreshTables() {
        clientTable.refresh();
        serverTable.refresh();
    }

    public void authAction() {
        network.sendAuthInfo(loginArea.getText(), passArea.getText());
        loginArea.clear();
        passArea.clear();
        //pane.getChildren().add(box);
    }

    public void regAction(){
        network.sendRegInfo();
    }

    public void sendFile() {
        File file = new File(CLIENT_DIRECTORY + "/" + clientTable.getSelectionModel().getSelectedItem().getFileName());
        network.sendFile(file);
        serverTable.refresh();
    }

    public void loadFile() {
        network.downLoadFile(serverTable.getSelectionModel().getSelectedItem().getFileName());
    }

    @Override
    public void changeScene() {

    }

//    public void loginMeth (){
//        //pane.getChildren().remove(0,1);
//    }
}
