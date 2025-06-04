package com.xiaoyu.mcsl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Environment {

    // 获取当前使用的Java版本
    public static final String NowJavaVersion = System.getProperty("java.version");

    // 获取当前使用的系统类型
    public static final String OS = System.getProperty("os.name").toLowerCase();

    // 使用正则表达式获取Java主版本
    public static final String DefaultJavaVersion = NowJavaVersion.replaceAll("^([0-9]+)\\..*", "$1");

    // 设置根据获取的结果设置布尔值
    public static final boolean Java21 = DefaultJavaVersion.equals("21");
    public static final boolean Windows = OS.contains("win");
    public static final boolean Linux = OS.contains("nix") || OS.contains("nux") || OS.contains("aix");

    // 设置默认属性
    public static boolean Java8 = false;
    public static boolean Java17 = false;

    // 存储 Java 安装路径和对应的版本标识
    public static final Map<String, Path> JavaPaths = new HashMap<>();
    public static final Map<String, Boolean> JavaVersions = new HashMap<>();

    static {
        // 如果当前已经是Java21，则设置Java21路径为当前环境
        if (Java21) {
            String javaHome = System.getProperty("java.home");
            String execName = Windows ? "java.exe" : "java";
            Path javaPath = Paths.get(javaHome, "bin", execName);

            if (Files.exists(javaPath)) {
                JavaPaths.put("Java21Path", javaPath);
                JavaVersions.put("Java21", true);
            }
        }

        // 操作系统对应的默认安装路径
        final Map<String, Map<String, String>> JavaDefaultInstallPaths = new HashMap<>();

        // Windows 路径
        Map<String, String> WindowsPaths = new HashMap<>();
        WindowsPaths.put("ZuluJava8", "C:\\Program Files\\Zulu\\zulu-8\\bin\\java.exe");
        WindowsPaths.put("OracleJava8", "C:\\Program Files\\Java\\jdk1.8.0\\bin\\java.exe");
        WindowsPaths.put("ZuluJava17", "C:\\Program Files\\Zulu\\zulu-17\\bin\\java.exe");
        WindowsPaths.put("OracleJava17", "C:\\Program Files\\Java\\jdk-17\\bin\\java.exe");
        // 如果当前不是Java21，才需要检查Java21路径
        if (!Java21) {
            WindowsPaths.put("ZuluJava21", "C:\\Program Files\\Zulu\\zulu-21\\bin\\java.exe");
            WindowsPaths.put("OracleJava21", "C:\\Program Files\\Java\\jdk-21\\bin\\java.exe");
        }
        JavaDefaultInstallPaths.put("Windows", WindowsPaths);

        // Linux 路径
        Map<String, String> LinuxPaths = new HashMap<>();
        LinuxPaths.put("ZuluJava8", "/usr/lib/jvm/zulu-8/bin/java");
        LinuxPaths.put("OracleJava8", "/usr/lib/jvm/java-8-oracle/bin/java");
        LinuxPaths.put("ZuluJava17", "/usr/lib/jvm/zulu-17/bin/java");
        LinuxPaths.put("OracleJava17", "/usr/lib/jvm/java-17-oracle/bin/java");
        // 如果当前不是Java21，才需要检查Java21路径
        if (!Java21) {
            LinuxPaths.put("ZuluJava21", "/usr/lib/jvm/zulu-21/bin/java");
            LinuxPaths.put("OracleJava21", "/usr/lib/jvm/java-21-oracle/bin/java");
        }
        JavaDefaultInstallPaths.put("Linux", LinuxPaths);

        // 根据操作系统检查JDK
        if (Windows) {
            checkJDK(JavaDefaultInstallPaths.get("Windows"));
        } else if (Linux) {
            checkJDK(JavaDefaultInstallPaths.get("Linux"));
        }
    }

    // 检查并注册JDK安装
    private static void checkJDK(Map<String, String> paths) {
        // 动态构建优先级列表
        List<String> priorityOrder = new ArrayList<>();

        // 只有当前不是Java21时才需要检查Java21路径
        if (!Java21) {
            priorityOrder.add("ZuluJava21");
            priorityOrder.add("OracleJava21");
        }

        priorityOrder.add("ZuluJava17");
        priorityOrder.add("OracleJava17");
        priorityOrder.add("ZuluJava8");
        priorityOrder.add("OracleJava8");

        // 记录已找到的版本避免重复
        Set<String> foundVersions = new HashSet<>();

        for (String key : priorityOrder) {
            String pathStr = paths.get(key);
            if (pathStr == null) continue;

            Path path = Paths.get(pathStr);
            if (!Files.exists(path)) continue;

            // 提取版本号 (最后出现的数字部分)
            String version = key.replaceAll(".*?(\\d+)$", "$1");
            String versionKey = "Java" + version;

            // 如果该版本尚未注册
            if (!foundVersions.contains(versionKey)) {
                JavaPaths.put(versionKey + "Path", path);
                JavaVersions.put(versionKey, true);
                foundVersions.add(versionKey);

                // 设置对应的静态布尔值
                switch (version) {
                    case "8": Java8 = true; break;
                    case "17": Java17 = true; break;
                    case "21":
                        // Java21 已在静态块中设置，不需要重复设置
                        break;
                }
            }
        }
    }
}