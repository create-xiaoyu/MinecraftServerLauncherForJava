package com.xiaoyu.mcsl;

public class MinecraftServerLuncher {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println(I18n.getI18nMessage("no.arguments"));
            return;
        }

        switch (args[0]) {

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
                System.out.println(I18n.getI18nMessage("parameters.text.PrintSystemInfo.Java") + Environment.DefaultJavaVersion);
                System.out.println();
                System.out.println(I18n.getI18nMessage("parameters.text.PrintSystemInfo.Java.Boolean"));
                System.out.println(I18n.getI18nMessage("parameters.text.PrintSystemInfo.Java.Boolean.8") + Environment.Java8);
                System.out.println(I18n.getI18nMessage("parameters.text.PrintSystemInfo.Java.Boolean.17") + Environment.Java17);
                System.out.println(I18n.getI18nMessage("parameters.text.PrintSystemInfo.Java.Boolean.21") + Environment.Java21);
                break;

            case "--help":
                System.out.println(I18n.getI18nMessage("parameters.text.help"));
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