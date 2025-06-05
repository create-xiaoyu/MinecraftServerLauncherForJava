package com.xiaoyu.mcsl;

import java.util.Scanner;

public class MinecraftServerLuncher {

    public static String ServerCore;

    public static String InputMessage(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        return scanner.nextLine().trim(); // 去除首尾空白字符
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
                System.out.println(I18n.getI18nMessage("parameters.text.PrintServerCoreList.Vanilla") + "Vanilla");
                PrintServerCoreList.PrintAllServerCoreList();
                ServerCore = InputMessage(I18n.getI18nMessage("parameters.text.InstallServer.Input"));
                System.out.println();
                System.out.println(I18n.getI18nMessage("parameters.text.InstallServer.UserInput") + ServerCore);

                // 检查输入是否为空
                if (ServerCore.isEmpty()) {
                    System.out.println(I18n.getI18nMessage("parameters.text.InstallServer.UserInput.Error.InputNull"));
                    return;
                }

                // 检查输入是否有效
                if (!PrintServerCoreList.isValidCore(ServerCore)) {
                    System.out.println(I18n.getI18nMessage("parameters.text.InstallServer.UserInput.Error.InputUnknown"));
                    return;
                }

                // 请求API
                API.RequestAPI(ServerCore);

                // API返回结果
                System.out.println("最新正式版：" + API.VanillaAPIRequestLatest);
                System.out.println();
                System.out.println("所有版本：");
                System.out.println();
                for (API.VersionInfo version : API.VanillaVersions) {
                    System.out.println("- " + version.VanillaVersionID + " [" + version.VanillaVersionType + "]");
                }
                break;

            case "--PrintServerCoreList":
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