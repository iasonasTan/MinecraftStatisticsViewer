package main;

import com.owlike.genson.Genson;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.*;
import java.nio.file.*;

public class Main {

    public static void main(String[] args)
        throws IOException, InterruptedException {
        if (args.length < 2) throw new RuntimeException("No file path passed");

        URI mojangURI = URI.create(
            "https://api.mojang.com/users/profiles/minecraft/" + args[1]
        );
        HttpRequest mojangRequest = HttpRequest.newBuilder()
            .uri(mojangURI)
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        HttpResponse<String> mojangResponse = HttpClient.newHttpClient().send(
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
        HttpResponse<String> mcTiersResponse = HttpClient.newHttpClient().send(
            mcTiersRequest,
            HttpResponse.BodyHandlers.ofString()
        );
        write(mcTiersResponse.body(), Paths.get(args[0]));
    }

    public static void write(String data, Path path) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(path.toFile());
        byte[] bytes = data.getBytes();
        fileOutputStream.write(bytes);
        fileOutputStream.close();
    }

    public static String uuidFromResponse(String reqBody) {
        class Data {

            public String id;
            public String name;
        }

        Genson genson = new Genson();
        Data data = genson.deserialize(reqBody, Data.class);
        return formatUuid(data.id);
    }

    public static String formatUuid(String id) {
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
}
