package com.xiaoyu.mcsl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class API {

    public static String VanillaAPIRequestLatest;

    public static List<VersionInfo> VanillaVersions = new ArrayList<>();

    public static class VersionInfo {
        public final String VanillaVersionID;
        public final String VanillaVersionType;

        public VersionInfo(String id, String type) {
            this.VanillaVersionID = id;
            this.VanillaVersionType = type;
        }
    }

    public static void RequestAPI(String APIName) {
        switch (APIName) {
            case "Vanilla":
                try {

                    HttpClient httpClient = HttpClient.newHttpClient();

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(new URI("https://launchermeta.mojang.com/mc/game/version_manifest.json"))
                            .timeout(Duration.ofSeconds(10))
                            .build();

                    HttpResponse<String> VanillaAPIResponse = httpClient.send(
                            request, HttpResponse.BodyHandlers.ofString()
                    );

                    Gson gson = new Gson();
                    JsonObject root = gson.fromJson(VanillaAPIResponse.body(), JsonObject.class);

                    JsonObject VanillaAPIRequestVersion = root.getAsJsonObject("latest");
                    VanillaAPIRequestLatest = VanillaAPIRequestVersion.get("release").getAsString();

                    JsonArray VanillaAPIRequestRelease = root.getAsJsonArray("versions");
                    VanillaVersions.clear(); // 清空旧数据

                    for (JsonElement VanillaAPIRequestVersionElement : VanillaAPIRequestRelease) {
                        JsonObject VanillaVersion = VanillaAPIRequestVersionElement.getAsJsonObject();
                        String id = VanillaVersion.get("id").getAsString();
                        String type = VanillaVersion.get("type").getAsString();
                        VanillaVersions.add(new VersionInfo(id, type));
                    }

                } catch (Exception Error) {
                    System.err.println("api.error.println " + Error.getMessage());
                }
                break;

            case "Spigot":
                break;
        }
    }
}
