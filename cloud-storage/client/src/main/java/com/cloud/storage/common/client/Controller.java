package com.cloud.storage.common.client;

import com.cloud.storage.common.Directorys;
import com.cloud.storage.common.FileInfo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

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

    Network network = Network.getInstance();


    @FXML
    public void initialize() {
        network.setController(this);
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

    public void sendFile() {
        File file = new File(CLIENT_DIRECTORY + "/" + clientTable.getSelectionModel().getSelectedItem().getFileName());
        network.sendFile(file);
        serverTable.refresh();
    }

    public void loadFile() {
        network.downLoadFile(serverTable.getSelectionModel().getSelectedItem().getPath());
    }

    public void deleteLocalFile() {
        try {
            Files.delete(Paths.get(clientTable.getSelectionModel().getSelectedItem().getPath()));
            ClientUtilities.refreshClientFileList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteServerFile() {
        network.deleteFileFromServer(serverTable.getSelectionModel().getSelectedItem().getPath());
    }


    @Override
    public void changeScene() {

    }
}
