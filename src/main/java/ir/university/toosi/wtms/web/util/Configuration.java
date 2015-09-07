package ir.university.toosi.wtms.web.util;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
public class Configuration {

    static private Properties configuration = null;

    public static void load() throws IOException {
        if (configuration != null)
            configuration = readFile();
    }

    public static Properties getProperties() {
        if (configuration == null) {
            try {
                configuration = readFile();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return configuration;
    }

    public static String getProperty(String key) {
        if (configuration == null || key == null) {
            try {
                configuration = readFile();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return configuration.getProperty(key);
    }


    public static long getConversationTimeout() {
        return Long.valueOf(getProperty("conversation.timeout"));
    }

    public static String getProperty(String key, String defaultValue) {
        if (configuration == null || key == null) {
            try {
                configuration = readFile();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return configuration.getProperty(key, defaultValue);
    }

    private static Properties readFile() throws IOException {

        Properties props = new Properties();
        try {
            ClassLoader loader = Configuration.class.getClassLoader();
            InputStream in = loader.getResourceAsStream("wtms.properties");
            props.load(in);
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
        return props;
    }

}