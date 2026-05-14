package org.restcomm.protocols.ss7.map.anonymousAssociaton.server;

public class ServerProperties {
    private final String name;
    private final String host;
    private final int port;
    private final String path;

    public ServerProperties(String name, String host, int port, String path) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }
}
