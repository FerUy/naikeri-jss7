package org.restcomm.protocols.ss7.map.anonymousAssociaton;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.restcomm.protocols.ss7.map.anonymousAssociaton.client.Client;
import org.restcomm.protocols.ss7.map.anonymousAssociaton.client.ClientProperties;
import org.restcomm.protocols.ss7.map.anonymousAssociaton.server.Server;
import org.restcomm.protocols.ss7.map.anonymousAssociaton.server.ServerProperties;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @author <a href="mailto:enmanuelcalero61@gmail.com"> Enmanuel Calero </a>
 */
public class Test {

    private static final Logger log = LogManager.getLogger(Test.class);

    public static void main(String[] args) throws Exception {
        Path configFiles = Files.createDirectories(Path.of(System.getProperty("user.dir"), "config"));

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    dropDirectory(configFiles.toFile());
                } catch (Exception e) {
                    log.error("Error drop config files", e);
                }
            }
        });


        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerProperties serverProperties = new ServerProperties(
                            "server-test",
                            "127.0.0.1",
                            2906,
                            configFiles.toString()
                    );
                    Server server = new Server(serverProperties);
                    server.start();
                } catch (Exception e) {
                    log.error("Error on start Server", e);
                }
            }
        });
        serverThread.start();
        Thread.sleep(4000);
        ClientProperties clientProperties = new ClientProperties("client-test", "127.0.0.1", 2907, "127.0.0.1", 2906, "test_assoc", configFiles.toString());
        Client client = new Client(clientProperties);
        client.start();
    }

    public static void dropDirectory(File file) {
        if (file.isDirectory()) {
            for (File sub : Objects.requireNonNull(file.listFiles())) {
                dropDirectory(sub);
            }
        }
        file.delete();
    }
}
