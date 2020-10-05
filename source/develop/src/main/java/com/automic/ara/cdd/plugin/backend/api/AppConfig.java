package com.automic.ara.cdd.plugin.backend.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    private static final String APP_CONFIG_FILE_NAME = "settings.properties";
    private static final String CONFIG_KEY_GET_INSTALLATION_TIMEOUT = "get_installation_timeout";

    public static long getInstallationTimeOut() {
        return Long.parseLong(getValue(CONFIG_KEY_GET_INSTALLATION_TIMEOUT));
    }

    public static String getValue (String key) {
        InputStream configStream = AppConfig.class.getClassLoader().getResourceAsStream(APP_CONFIG_FILE_NAME);
        if(configStream == null) return null;

        Properties prop = new Properties();
        try {
            prop.load(configStream);
            return prop.getProperty(key);
        } catch (IOException e) {
            logger.error(String.format("Cannot load the configuration property %s.", key), e);
        } finally{
            try {
                configStream.close();
            } catch (IOException e) {
                logger.error("Cannot close the stream.", e);
            }
        }

        return null;
    }
}
