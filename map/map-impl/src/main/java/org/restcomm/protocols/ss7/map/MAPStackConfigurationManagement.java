package org.restcomm.protocols.ss7.map;

import javolution.text.TextBuilder;
import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * read/write MAP layer configuration *.xml file
 */
public class MAPStackConfigurationManagement {

    private static final Logger logger = LogManager.getLogger(MAPStackConfigurationManagement.class);

    private static final String PERSIST_FILE_NAME = "management.xml";
    private static final String MAP_MANAGEMENT_PERSIST_DIR_KEY = "mapmanagement.persist.dir";
    private static final String USER_DIR_KEY = "user.dir";
    private static final String TAB_INDENT = "\t";
    private static final String DEFAULT_CONFIG_FILE_NAME = "MapStack";
    private static final String SHORT_TIMER_VALUE = "shorttimervalue";
    private static final String MEDIUM_TIMER_VALUE = "mediumtimervalue";
    private static final String LONG_TIMER_VALUE = "longtimervalue";

    private static final XMLBinding binding = new XMLBinding();
    private static final MAPStackConfigurationManagement instance = new MAPStackConfigurationManagement();

    private final TextBuilder persistFile = TextBuilder.newInstance();
    private String configFileName = DEFAULT_CONFIG_FILE_NAME;
    private String persistDir = null;

    private int shortTimer = 10000;
    private int mediumTimer = 30000;
    private int longTimer = 600000;

    private MAPStackConfigurationManagement() {
    }

    public static MAPStackConfigurationManagement getInstance() {
        return instance;
    }

    public void setPersistDir(String persistDir) {
        this.persistDir = persistDir;
        this.setPersistFile();
    }

    private void setPersistFile() {
        this.persistFile.clear();

        if (persistDir != null) {
            this.persistFile.append(persistDir).append(File.separator).append(this.configFileName).append("_").append(PERSIST_FILE_NAME);
        } else {
            persistFile.append(System.getProperty(MAP_MANAGEMENT_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY))).append(File.separator).append(this.configFileName)
                    .append("_").append(PERSIST_FILE_NAME);
        }
    }

    /**
     * Persist
     */
    public void store() {
        // TODO : Should we keep reference to Objects rather than recreating
        // everytime?
        try {
            XMLObjectWriter writer = XMLObjectWriter.newInstance(new FileOutputStream(persistFile.toString()));

            writer.setBinding(binding);
            writer.setIndentation(TAB_INDENT);

            writer.write(this.shortTimer, SHORT_TIMER_VALUE, Integer.class);
            writer.write(this.mediumTimer, MEDIUM_TIMER_VALUE, Integer.class);
            writer.write(this.longTimer, LONG_TIMER_VALUE, Integer.class);

            writer.close();
        } catch (Exception e) {
            logger.error("Failed to persist MAP state to file={}", persistFile, e);
        }
    }

    /**
     * Load and create LinkSets and Link from persisted file
     * <p>
     * load() is called from MAPStackImpl
     */
    public void load() {
        try {
            setPersistFile();
            XMLObjectReader reader = XMLObjectReader.newInstance(new FileInputStream(persistFile.toString()));
            reader.setBinding(binding);
            load(reader);
        } catch (FileNotFoundException e) {
            logger.info("No prior MAP state to load; initializing fresh state. File: {}", persistFile);
        } catch (XMLStreamException e) {
            logger.error("Failed to parse MAP state file={}; continuing with current in-memory state", persistFile, e);
        }
    }

    private void load(XMLObjectReader reader) throws XMLStreamException {
        Integer val = reader.read(SHORT_TIMER_VALUE, Integer.class);
        if (val != null)
            this.shortTimer = val;

        val = reader.read(MEDIUM_TIMER_VALUE, Integer.class);
        if (val != null)
            this.mediumTimer = val;

        val = reader.read(LONG_TIMER_VALUE, Integer.class);
        if (val != null)
            this.longTimer = val;

        reader.close();
    }

    public void setConfigFileName(String configFileName) {
        this.configFileName = configFileName;
    }

    public int getShortTimer() {
        return shortTimer;
    }

    public int getMediumTimer() {
        return mediumTimer;
    }

    public int getLongTimer() {
        return longTimer;
    }

    public void setShortTimer(int shortTimer) {
        this.shortTimer = shortTimer;
        this.store();
    }

    public void setMediumTimer(int mediumTimer) {
        this.mediumTimer = mediumTimer;
        this.store();
    }

    public void setLongTimer(int longTimer) {
        this.longTimer = longTimer;
        this.store();
    }
}
