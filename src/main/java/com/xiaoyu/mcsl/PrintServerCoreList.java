package com.xiaoyu.mcsl;

import java.util.*;

public class PrintServerCoreList {
    // 存储所有有效核心名称（忽略大小写）
    private static final Set<String> ValidCores = new HashSet<>();
    private static final Map<String, List<String>> CoreCategories = new LinkedHashMap<>();

    static {
        // 初始化核心类别和名称
        CoreCategories.put("parameters.text.PrintServerCoreList.Plugins", Arrays.asList("Spigot", "Paper", "Pupur", "Leaves", "Folia", "Lumina", "Luminol", "LightingLuminol"));
        CoreCategories.put("parameters.text.PrintServerCoreList.Mods", Arrays.asList("Fabric", "Quilt", "Forge", "NeoForge"));
        CoreCategories.put("parameters.text.PrintServerCoreList.Mixing", Arrays.asList("Mohist", "Crucible", "CatServer", "Banner", "Youer"));

        // 填充有效核心集合（统一转换为小写）
        for (List<String> cores : CoreCategories.values()) {
            for (String core : cores) {
                ValidCores.add(core.toLowerCase());
            }
        }
    }

    public static boolean isValidCore(String input) {
        return input != null && ValidCores.contains(input.toLowerCase());
    }

    public static void PrintAllServerCoreList() {
        for (Map.Entry<String, List<String>> entry : CoreCategories.entrySet()) {
            String joinedCores = String.join("   ", entry.getValue());
            PrintCoreCategory(entry.getKey(), joinedCores);
        }
    }

    private static void PrintCoreCategory(String i18nKey, String cores) {
        System.out.println(I18n.getI18nMessage(i18nKey));
        System.out.println(cores);
        System.out.println();
    }
}