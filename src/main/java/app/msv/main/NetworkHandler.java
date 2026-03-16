package app.msv.main;

import com.owlike.genson.Genson;
import java.net.*;
import java.net.http.*;

public final class NetworkHandler {

    private OnDataReceivedListener mListener;

    public NetworkHandler() {
        this(null);
    }

    public NetworkHandler(OnDataReceivedListener listener) {
        mListener = listener;
    }

    public synchronized void requestFor(final String userName) throws Exception {
        final URI mojangURI = URI.create(
            "https://api.mojang.com/users/profiles/minecraft/" + userName
        );
        final HttpRequest mojangRequest = HttpRequest.newBuilder()
            .uri(mojangURI)
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();

        new Thread(() -> {
            try {
                HttpResponse<String> mojangResponse =
                    HttpClient.newHttpClient().send(
                        mojangRequest,
                        HttpResponse.BodyHandlers.ofString()
                    );
                IO.println(mojangResponse.body());
                String uuid = uuidFromResponse(mojangResponse.body());
                URI mcTiersURI = URI.create(
                    "https://mctiers.com/api/v2/profile/%7B" + uuid + "%7D"
                );
                HttpRequest mcTiersRequest = HttpRequest.newBuilder()
                    .uri(mcTiersURI)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
                HttpResponse<String> mcTiersResponse =
                    HttpClient.newHttpClient().send(
                        mcTiersRequest,
                        HttpResponse.BodyHandlers.ofString()
                    );
                IO.println(mcTiersResponse.body());
                handleAnswer(mcTiersResponse.body());
            } catch (Exception e) {
                mListener.onReceive(null);
                IO.println(e);
            }
        })
            .start();
    }

    private void handleAnswer(String body) {
        Genson genson = new Genson();
        UserData data = genson.deserialize(body, UserData.class);
        mListener.onReceive(data);
    }

    public static class Data {
        public String id;
        public String name;

        public Data() {}
    }

    private String uuidFromResponse(String reqBody) {
        IO.println("Desirializing...");
        IO.println("Data " + reqBody);

        Genson genson = new Genson();
        Data data = genson.deserialize(reqBody, Data.class);
        return formatUuid(data.id);
    }

    private static String formatUuid(String id) {
        // UUID format: 8-4-4-4-12
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < id.length(); i++) {
            if (i == 8 || i == 12 || i == 16 || i == 20) {
                builder.append("-");
            }
            builder.append(id.charAt(i));
        }
        return builder.toString();
    }

    public static interface OnDataReceivedListener {
        void onReceive(UserData userData);
    }

    public static final class UserData {
        public String uuid;
        public String name;
        public String region;
        public int points;
        public int overall;

        @Override
        public String toString() {
            return String.format(
                "UUID: %s, Name: %s, Points: %d, Overall: %d, Region: %s",
                uuid,
                name,
                points,
                overall,
                region
            );
        }

        // end of UserData
    }
}
