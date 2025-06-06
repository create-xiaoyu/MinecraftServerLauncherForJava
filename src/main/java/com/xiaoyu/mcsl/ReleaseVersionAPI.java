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

    private static JsonArray CoreVersion;

    private static JsonObject NormalAPIRequestVersion;
    private static JsonArray RequestListAPIVersionList;

    public static String CoreLatestVersion;

    public static List<CoreVersionInfo> ReleaseVersions = new ArrayList<>();

    private static void RequestNormalAPI(String API, String LatestValueName, String ReleaseValueName) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();

            HttpRequest APIRequest = HttpRequest.newBuilder()
                    .uri(new URI(API))
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> NormalAPIResponse = httpClient.send(
                    APIRequest, HttpResponse.BodyHandlers.ofString()
            );

            Gson gson = new Gson();

            JsonObject root = gson.fromJson(NormalAPIResponse.body(), JsonObject.class);

            if (!LatestValueName.isEmpty()) {
                NormalAPIRequestVersion = root.getAsJsonObject(LatestValueName);
            }

            CoreLatestVersion = NormalAPIRequestVersion.get(ReleaseValueName).getAsString();

            CoreVersion = root.getAsJsonArray("versions");
            ReleaseVersions.clear(); // 清空旧数据

        } catch (Exception Error) {
            System.err.println(I18n.getI18nMessage("api.error.print") + Error.getMessage());
        }
    }

    private static void RequestListAPI(String API, String VersionGroupName, String VersionListName) {
        try {

            HttpClient httpClient = HttpClient.newHttpClient();

            HttpRequest APIRequest = HttpRequest.newBuilder()
                    .uri(new URI(API))
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> ListAPIResponse = httpClient.send(
                    APIRequest, HttpResponse.BodyHandlers.ofString()
            );

            Gson gson = new Gson();
            JsonObject root = gson.fromJson(ListAPIResponse.body(), JsonObject.class);

            // 直接获取版本数组
            if (VersionGroupName.isEmpty()) {
                RequestListAPIVersionList = root.getAsJsonArray(VersionListName);
            } else {
                // 只有当VersionGroupName非空时才尝试获取对象
                if (root.has(VersionGroupName)) {
                    JsonObject versionGroup = root.getAsJsonObject(VersionGroupName);
                    if (versionGroup != null) {
                        RequestListAPIVersionList = versionGroup.getAsJsonArray(VersionListName);
                    }
                }
            }

            switch (MinecraftServerLuncher.ServerCore){
                case "Spigot": {
                    CoreLatestVersion = RequestListAPIVersionList.get(0).getAsString();
                    break;
                }
                case "Paper", "Pupur": {
                    int LastVersion = RequestListAPIVersionList.size() - 1;
                    CoreLatestVersion = RequestListAPIVersionList.get(LastVersion).getAsString();
                    break;
                }
            }

            ReleaseVersions.clear();

        } catch (Exception Error) {
            System.err.println(I18n.getI18nMessage("api.error.print") + Error.getMessage());
        }
    }

    public static void ReturnVersionAPI(String CoreName) {
        switch (CoreName) {
            case "Vanilla": {
                RequestNormalAPI("https://launchermeta.mojang.com/mc/game/version_manifest.json", "latest", "release");
                for (JsonElement NormalAPIRequestVersionElement : CoreVersion) {
                    JsonObject NormalVersion = NormalAPIRequestVersionElement.getAsJsonObject();
                    String id = NormalVersion.get("id").getAsString();
                    String type = NormalVersion.get("type").getAsString();
                    if ("release".equals(type)) {
                        ReleaseVersions.add(new CoreVersionInfo(id, type));
                    }
                }
                break;
            }
            case "Spigot": {
                RequestListAPI("https://api.mslmc.cn/v3/query/available_versions/spigot", "data", "versionList");
                for (JsonElement ListVersionElement : RequestListAPIVersionList) {
                    String version = ListVersionElement.getAsString();
                    ReleaseVersions.add(new CoreVersionInfo(version, "release"));
                }
                break;
            }
            case "Paper": {
                RequestListAPI("https://api.papermc.io/v2/projects/paper", "", "versions");
                for (JsonElement ListVersionElement : RequestListAPIVersionList) {
                    String version = ListVersionElement.getAsString();
                    ReleaseVersions.add(new CoreVersionInfo(version, "release"));
                }
                break;
            }
            case "Pupur": {
                RequestListAPI("https://api.purpurmc.org/v2/purpur/", "", "versions");
                for (JsonElement ListVersionElement : RequestListAPIVersionList) {
                    String version = ListVersionElement.getAsString();
                    ReleaseVersions.add(new CoreVersionInfo(version, "release"));
                }
                break;
            }
            case "Leaves": {
                RequestListAPI("https://api.leavesmc.org/v2/projects/leaves", "", "versions");
                for (JsonElement ListVersionElement : RequestListAPIVersionList) {
                    String version = ListVersionElement.getAsString();
                    ReleaseVersions.add(new CoreVersionInfo(version, "release"));
                }
                break;
            }
            case "Folia": {
                RequestListAPI("https://api.mslmc.cn/v3/query/available_versions/folia", "data", "versionList");
                for (JsonElement ListVersionElement : RequestListAPIVersionList) {
                    String version = ListVersionElement.getAsString();
                    ReleaseVersions.add(new CoreVersionInfo(version, "release"));
                }
                break;
            }
        }
    }
}
