package com.xiaoyu.mcsl;

import java.util.List;
import java.util.Scanner;

public class MinecraftServerLuncher {

    public static String ServerCore;

    public static String InputMessage(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        return scanner.nextLine().trim(); // 去除首尾空白字符
    }

    public static void PrintReleaseVersions(List<ReleaseVersionAPI.CoreVersionInfo> versions) {
        if (versions.isEmpty()) {
            System.out.println(I18n.getI18nMessage("mcsl.printreleaseversions.norelease"));
            return;
        }

        // 找到最长的版本号长度
        int maxVersionLength = versions.stream()
                .mapToInt(v -> v.CoreVersionID.length())
                .max()
                .orElse(8); // 默认最小宽度

        // 计算每列宽度（版本号长度+2个空格）
        int columnWidth = maxVersionLength + 2;
        int columns = 3; // 每行显示3个版本

        System.out.println("\n" + ServerCore + I18n.getI18nMessage("mcsl.printreleaseversions.release") + "\n");

        int count = 0;
        for (ReleaseVersionAPI.CoreVersionInfo version : versions) {
            // 格式化每个版本号，固定宽度右对齐
            String formatted = String.format("%-" + columnWidth + "s", version.CoreVersionID);
            System.out.print(formatted);

            // 每3个版本换行
            if (++count % columns == 0) {
                System.out.println();
            }
        }

        System.out.println();
        System.out.println("\nLatest version: " + ReleaseVersionAPI.CoreLatestVersion);
        System.out.println();
        System.out.println("Total releases: " + versions.size());
    }


    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println(I18n.getI18nMessage("no.arguments"));
            return;
        }

        switch (args[0]) {

            case "--InstallServer":
                System.out.println(I18n.getI18nMessage("parameters.text.InstallServer"));
                System.out.println();
                PrintServerCoreList.PrintAllServerCoreList();
                ServerCore = InputMessage(I18n.getI18nMessage("parameters.text.InstallServer.Input"));

                // 检查输入是否为空
                if (ServerCore.isEmpty()) {
                    System.out.println(I18n.getI18nMessage("parameters.text.InstallServer.UserInput.Error.InputNull"));
                    return;
                }

                // 检查输入是否有效
                if (!PrintServerCoreList.isValidCore(ServerCore)) {
                    System.out.println(ServerCore + I18n.getI18nMessage("parameters.text.InstallServer.UserInput.Error.InputUnknown"));
                    return;
                }

                // 请求API，获取正式版本版本列表
                ReleaseVersionAPI.ReturnVersionAPI(ServerCore);

                // 打印结果
                PrintReleaseVersions(ReleaseVersionAPI.ReleaseVersions);



                break;

            case "--PrintServerCoreList":
                System.out.println(I18n.getI18nMessage("parameters.text.PrintServerCoreList"));
                PrintServerCoreList.PrintAllServerCoreList();
                break;

            case "--PrintJavaVersion":
                System.out.println(I18n.getI18nMessage("parameters.text.PrintJavaVersion") + Environment.NowJavaVersion);
                break;

            case "--PrintSystemInfo":
                System.out.println(I18n.getI18nMessage("parameters.text.PrintSystemInfo.System") + Environment.OS);
                System.out.println();
                System.out.println(I18n.getI18nMessage("parameters.text.PrintSystemInfo.System.Boolean"));
                System.out.println(I18n.getI18nMessage("parameters.text.PrintSystemInfo.System.Boolean.Windows") + Environment.Windows);
                System.out.println(I18n.getI18nMessage("parameters.text.PrintSystemInfo.System.Boolean.Linux") + Environment.Linux);
                System.out.println();
                System.out.println(I18n.getI18nMessage("parameters.text.PrintSystemInfo.JavaVersion") + Environment.DefaultJavaVersion);
                System.out.println();
                System.out.println(I18n.getI18nMessage("parameters.text.PrintSystemInfo.JavaVersion.Boolean"));
                System.out.println(I18n.getI18nMessage("parameters.text.PrintSystemInfo.JavaVersion.8") + Environment.Java8);
                System.out.println(I18n.getI18nMessage("parameters.text.PrintSystemInfo.JavaVersion.17") + Environment.Java17);
                System.out.println(I18n.getI18nMessage("parameters.text.PrintSystemInfo.JavaVersion.21") + Environment.Java21);
                System.out.println();
                System.out.println(I18n.getI18nMessage("parameters.text.PrintSystemInfo.JavaPath"));
                System.out.println();
                System.out.println(I18n.getI18nMessage("parameters.text.PrintSystemInfo.JavaVersion.8") + Environment.Java8Path);
                System.out.println(I18n.getI18nMessage("parameters.text.PrintSystemInfo.JavaVersion.17") + Environment.Java17Path);
                System.out.println(I18n.getI18nMessage("parameters.text.PrintSystemInfo.JavaVersion.21") + Environment.Java21Path);
                break;

            case "--help":
                System.out.println(I18n.getI18nMessage("parameters.text.help"));
                System.out.println();
                System.out.println(I18n.getI18nMessage("parameters.name.InstallServer"));
                System.out.println(I18n.getI18nMessage("parameters.explanation.InstallServer"));
                System.out.println();
                System.out.println(I18n.getI18nMessage("parameters.name.PrintServerCoreList"));
                System.out.println(I18n.getI18nMessage("parameters.explanation.PrintServerCoreList"));
                System.out.println();
                System.out.println(I18n.getI18nMessage("parameters.name.PrintJavaVersion"));
                System.out.println(I18n.getI18nMessage("parameters.explanation.PrintJavaVersion"));
                System.out.println();
                System.out.println(I18n.getI18nMessage("parameters.name.PrintSystemInfo"));
                System.out.println(I18n.getI18nMessage("parameters.explanation.PrintSystemInfo"));
                System.out.println();
                System.out.println(I18n.getI18nMessage("parameters.name.help"));
                System.out.println(I18n.getI18nMessage("parameters.explanation.help"));
                break;

            default:
                System.out.println(I18n.getI18nMessage("parameters.text.unknown") + args[0]);
                break;
        }
    }
}