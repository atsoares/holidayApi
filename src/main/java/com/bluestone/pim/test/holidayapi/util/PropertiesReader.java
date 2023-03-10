package com.bluestone.pim.test.holidayapi.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * The class Properties reader.
 */
@Slf4j
public final class PropertiesReader {

    /**
     * Constant PROPERTIES.
     */
    private static final Properties PROPERTIES;

    /**
     * Constant PROP_FILE.
     */
    private static final String PROP_FILE = "external-api.properties";

    /**
     * Default private constructor PropertiesReader.
     */
    private PropertiesReader() {
    }

    static {
        PROPERTIES = new Properties();
        final URL props = ClassLoader.getSystemResource(PROP_FILE);
        try {
            PROPERTIES.load(props.openStream());
        } catch (IOException ex) {

            if (log.isDebugEnabled()) {
                log.debug(ex.getClass().getName() + "PropertiesReader method");
            }
        }
    }

    /**
     * Method getProperty.
     *
     * @param name String name file.
     * @return Return property
     */
    public static String getProperty(final String name) {

        return PROPERTIES.getProperty(name);
    }
}