package app.msv.main;

import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import lib.io.Configuration;

public class Main implements Context {

    private final Map<String, Context.BroadcastReceiver> mReceivers =
        new HashMap<>();

    public static void main(String[] args) {
        Configuration.init("mineraft_statistics_viewer");
        Application.launch(App.class, args);
    }

    @Override
    public void sendBroadcast(String listenerID, Object message) {
        mReceivers.get(listenerID).onReceive(message);
    }

    @Override
    public void addBroadcastReceiver(String id, BroadcastReceiver receiver) {
        mReceivers.put(id, receiver);
    }
}
