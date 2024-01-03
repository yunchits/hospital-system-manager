package com.solvd.hospital.common;

import java.io.IOException;
import java.util.Properties;

public final class AppProperties {
    private static final Properties properties = new Properties();

    private AppProperties() {
    }

    static {
        try {
            properties.load(
                AppProperties.class
                    .getClassLoader()
                    .getResourceAsStream("application.properties")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
