package app.msv.controller;

import app.msv.main.*;
import javafx.application.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lib.io.*;

public class HomeController extends VBox {

    @FXML
    private Label nameLabel;

    @FXML
    private Label idLabel;

    @FXML
    private Label pointsLabel;

    @FXML
    private Label overallLabel;

    @FXML
    private Label regionLabel;

    @FXML
    private Label loading;

    private final Context context;

    public HomeController(Context context) {
        this.context = context;
        NetworkHandler netHandler = new NetworkHandler(userData -> {
            if (userData != null) {
                Platform.runLater(() -> {
                    loading.setVisible(false);
                    idLabel.setText("UUID: " + userData.uuid);
                    nameLabel.setText("Name: " + userData.name);
                    pointsLabel.setText("Points: " + userData.points + "");
                    overallLabel.setText("Overall: " + userData.overall + "");
                    regionLabel.setText("Region: " + userData.region);
                });
            } else {
                Platform.runLater(() -> {
                    loading.setText("Error");
                });
            }
        });
        try {
            netHandler.requestFor(
                Configuration.loadProperties("settings.properties").getString(
                    "username"
                )
            );
        } catch (Exception e) {
            IO.println(e);
        }
    }

    @FXML
    public void handleReset() {
        context.sendBroadcast("pageman", "login");
    }
}
