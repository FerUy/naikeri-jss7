package org.restcomm.protocols.ss7.map.anonymousAssociaton.client;

public class ClientProperties {

    private final String name;
    private final String host;
    private final int port;
    private final String peer;
    private final int peerPort;
    private final String associationName;
    private final String path;

    public ClientProperties(String name, String host, int port, String peer, int peerPort, String associationName, String path) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.peer = peer;
        this.peerPort = peerPort;
        this.associationName = associationName;
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

    public String getPeer() {
        return peer;
    }

    public int getPeerPort() {
        return peerPort;
    }

    public String getAssociationName() {
        return associationName;
    }

    public String getPath() {
        return path;
    }
}
