package com.xiaoyu.mcsl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ReleaseVersionAPI {

    public static class CoreVersionInfo {

        public final String CoreVersionID;
        public final String CoreVersionType;

        public CoreVersionInfo(String id, String type) {
            this.CoreVersionID = id;
            this.CoreVersionType = type;
        }
    }

    public static String CoreLatestVersion;

    private static final String VanillaVersionAPI = "https://launchermeta.mojang.com/mc/game/version_manifest.json";

    private static final String SpigotVersionAPI = "https://api.mslmc.cn/v3/query/available_versions/spigot";

    private static final String PaperVersionAPI = "https://api.papermc.io/v2/projects/paper";

    public static List<CoreVersionInfo> ReleaseVersions = new ArrayList<>();

    public static void RequestVersionAPI(String CoreName) {
        switch (CoreName) {
            case "Vanilla": {
                try {

                    HttpClient httpClient = HttpClient.newHttpClient();

                    HttpRequest APIRequest = HttpRequest.newBuilder()
                            .uri(new URI(VanillaVersionAPI))
                            .timeout(Duration.ofSeconds(10))
                            .build();

                    HttpResponse<String> VanillaAPIResponse = httpClient.send(
                            APIRequest, HttpResponse.BodyHandlers.ofString()
                    );

                    Gson gson = new Gson();

                    JsonObject root = gson.fromJson(VanillaAPIResponse.body(), JsonObject.class);

                    JsonObject VanillaAPIRequestVersion = root.getAsJsonObject("latest");
                    CoreLatestVersion = VanillaAPIRequestVersion.get("release").getAsString();

                    JsonArray CoreVersion = root.getAsJsonArray("versions");
                    ReleaseVersions.clear(); // 清空旧数据

                    for (JsonElement VanillaAPIRequestVersionElement : CoreVersion) {
                        JsonObject VanillaVersion = VanillaAPIRequestVersionElement.getAsJsonObject();
                        String id = VanillaVersion.get("id").getAsString();
                        String type = VanillaVersion.get("type").getAsString();
                        if ("release".equals(type)) {
                            ReleaseVersions.add(new CoreVersionInfo(id, type));
                        }
                    }

                } catch (Exception Error) {
                    System.err.println(I18n.getI18nMessage("api.error.print") + Error.getMessage());
                }
                break;

            }
            case "Spigot": {
                try {

                    HttpClient httpClient = HttpClient.newHttpClient();

                    HttpRequest APIRequest = HttpRequest.newBuilder()
                            .uri(new URI(SpigotVersionAPI))
                            .timeout(Duration.ofSeconds(10))
                            .build();

                    HttpResponse<String> SpigotAPIResponse = httpClient.send(
                            APIRequest, HttpResponse.BodyHandlers.ofString()
                    );

                    Gson gson = new Gson();
                    JsonObject root = gson.fromJson(SpigotAPIResponse.body(), JsonObject.class);


                    // 获取数据对象
                    JsonObject data = root.getAsJsonObject("data");
                    JsonArray SpigotVersionList = data.getAsJsonArray("versionList");

                    // 设置最新版本（数组第一个元素）
                    if (!SpigotVersionList.isEmpty()) {
                        CoreLatestVersion = SpigotVersionList.get(0).getAsString();
                    }

                    ReleaseVersions.clear();

                    for (JsonElement SpigotVersionElement : SpigotVersionList) {
                        String version = SpigotVersionElement.getAsString();
                        ReleaseVersions.add(new CoreVersionInfo(version, "release"));
                    }

                } catch (Exception Error) {
                    System.err.println(I18n.getI18nMessage("api.error.print") + Error.getMessage());
                }
                break;
            }
            case "Paper": {
                try {

                } catch (Exception Error) {
                    System.err.println(I18n.getI18nMessage("api.error.print") + Error.getMessage());
                }
                break;
            }
        }
    }
}
