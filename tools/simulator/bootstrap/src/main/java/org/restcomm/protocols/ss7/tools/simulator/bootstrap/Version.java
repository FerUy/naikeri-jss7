package org.restcomm.protocols.ss7.tools.simulator.bootstrap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * TestVersion class reads the version.properties packaged with media-server.jar for run time display of Media Server
 * TestVersion
 *
 * @author amit.bhayani
 *
 */
public final class Version {

    /**
     * The single instance.
     */
    public static final Version instance = new Version();

    /**
     * The version properties.
     */
    private Properties props;

    /**
     * Do not allow direct public construction.
     */
    private Version() {
        props = loadProperties();
    }

    /**
     * Returns an unmodifiable map of version properties.
     *
     * @return map of version properties.
     */
    public Map getProperties() {
        return Collections.unmodifiableMap(props);
    }

    /**
     * Returns the value for the given property name.
     *
     * @param name - The name of the property.
     * @return The property value or null if the property is not set.
     */
    public String getProperty(final String name) {
        return props.getProperty(name);
    }

    /**
     * Returns the version information as a string.
     *
     * @return Basic information as a string.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("Naikeri jSS7 Simulator: ");
        boolean first = true;
        for (Object key : props.keySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }
            sb.append(key).append('=').append(props.get(key));
        }
        return sb.toString();
    }

    /**
     * Load the version properties from a resource.
     */
    private Properties loadProperties() {

        props = new Properties();

        try (InputStream in = Version.class.getResourceAsStream("version.properties")) {
            if (in == null) {
                throw new Error("Missing version.properties");
            }
            props.load(in);
        } catch (IOException e) {
            throw new Error("Failed to load version.properties", e);
        }

        return props;
    }

}
