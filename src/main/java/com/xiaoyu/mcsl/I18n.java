package com.xiaoyu.mcsl;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class I18n {
    private static final ResourceBundle bundle;

    static {
        // 创建UTF-8控制对象
        ResourceBundle.Control utf8Control = new UTF8Control();
        Locale locale = Locale.getDefault();
        bundle = ResourceBundle.getBundle("messages", locale, utf8Control);
    }

    public static String getI18nMessage(String key, Object... args) {
        return String.format(bundle.getString(key), args);
    }

    // 自定义Control类处理UTF-8
    private static class UTF8Control extends ResourceBundle.Control {
        public ResourceBundle newBundle(String baseName, Locale locale, String format,
                                        ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, java.io.IOException {

            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            try (var is = loader.getResourceAsStream(resourceName)) {
                if (is != null) {
                    try (var reader = new java.io.InputStreamReader(is, StandardCharsets.UTF_8)) {
                        return new PropertyResourceBundle(reader);
                    }
                }
            }
            return super.newBundle(baseName, locale, format, loader, reload);
        }
    }
}
