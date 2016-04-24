package com.aolisov.EDI.properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class PropertiesHandler {

    private static final String PROPERTIES_FILE = "app.properties";
    private Map<String,String> cache;

    public String getBotToken() {
        return getProperty("botToken");
    }

    public String getTelegramApiFormatUrl() {
        return getProperty("telegramApiFormatUrl");
    }

    private String getProperty(String propertyName) {
        if(cache == null) {
            cache = Collections.synchronizedMap(new HashMap<>());
        }
        if(cache.get(propertyName) == null) {
            try {
                Resource resource = new ClassPathResource(PROPERTIES_FILE);
                InputStream inputStream = resource.getInputStream();

                Properties properties = new Properties();
                properties.load(inputStream);

                Object value = properties.get(propertyName);
                if(value == null) {
                    value = "";
                }

                cache.put(propertyName, String.valueOf(value));
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
        return cache.get(propertyName);
    }
}
