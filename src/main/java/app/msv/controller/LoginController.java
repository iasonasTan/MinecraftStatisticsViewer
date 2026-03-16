package app.msv.controller;

import app.msv.main.Context;
import app.msv.main.NetworkHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lib.io.Configuration;
import lib.io.OutputProperties;

public class LoginController extends VBox {

    @FXML
    private TextField usernameField;

    private final Context context;

    public LoginController(Context context) {
        this.context = context;
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        Configuration.storeProperties(
            "settings.properties",
            new OutputProperties().put("username", username)
        );

        context.sendBroadcast("pageman", "home");
    }
}
