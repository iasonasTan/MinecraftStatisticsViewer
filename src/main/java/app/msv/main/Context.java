package app.msv.main;

public interface Context {
    void sendBroadcast(String listenerID, Object message);
    void addBroadcastReceiver(String id, BroadcastReceiver receiver);

    public interface BroadcastReceiver {
        void onReceive(Object message);
    }
}
