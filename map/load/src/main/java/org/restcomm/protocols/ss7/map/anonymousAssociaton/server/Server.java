package org.restcomm.protocols.ss7.map.anonymousAssociaton.server;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.ServerListener;
import org.mobicents.protocols.sctp.netty.NettySctpManagementImpl;
import org.restcomm.protocols.ss7.m3ua.As;
import org.restcomm.protocols.ss7.m3ua.Asp;
import org.restcomm.protocols.ss7.m3ua.AspFactory;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.restcomm.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.restcomm.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.restcomm.protocols.ss7.ss7ext.Ss7ExtInterfaceImpl;


public class Server implements ServerListener {
    private static final Logger log = LogManager.getLogger(Server.class);
    private final ParameterFactoryImpl factory = new ParameterFactoryImpl();

    private final ServerProperties serverProperties;

    private NettySctpManagementImpl sctpManagement;
    private M3UAManagementImpl m3uaManagement;


    public Server(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    public void start() throws Exception {
        log.info("Starting Server...");
        initSctp();
        initM3ua();
    }

    private void initSctp() throws Exception {
        this.sctpManagement = new NettySctpManagementImpl(serverProperties.getName());
        this.sctpManagement.setPersistDir(serverProperties.getPath());
        this.sctpManagement.start();
        this.sctpManagement.setConnectDelay(10000);
        this.sctpManagement.removeAllResources();
        // create SCTP Server
        this.sctpManagement.addServer(serverProperties.getName(), serverProperties.getHost(), serverProperties.getPort(), IpChannelType.SCTP, true, 0, null);
        // since we are accepting anonymous connections we need to set a server listener to receive the anonymous connections
        log.info("Setting Server Listener...");
        this.sctpManagement.setServerListener(this);
        this.sctpManagement.startServer(serverProperties.getName());
    }

    private void initM3ua() throws Exception {
        this.m3uaManagement = new M3UAManagementImpl(serverProperties.getName(), null, new Ss7ExtInterfaceImpl());
        this.m3uaManagement.setPersistDir(serverProperties.getPath());
        this.m3uaManagement.setTransportManagement(this.sctpManagement);
        this.m3uaManagement.setDeliveryMessageThreadCount(4);
        this.m3uaManagement.start();
        this.m3uaManagement.removeAllResources();
    }

    @Override
    public void onNewRemoteConnection(org.mobicents.protocols.api.Server server, Association association) {
        log.info("New Remote connection from " + association.getPeerAddress() + ":" + association.getPeerPort() + " to "
                + server.getHostAddress() + ":" + server.getHostPort());
        try {
            String nameForAnonymousAssociation = association.getPeerAddress() + ":" + association.getPeerPort() + "_anonymous";
            this.sctpManagement.addServerAssociation(association.getPeerAddress(), association.getPeerPort(), server.getName(),
                    nameForAnonymousAssociation, IpChannelType.SCTP);
            // Step 1 : Create App Server
            RoutingContext rc = factory.createRoutingContext(new long[] { 101L });
            TrafficModeType trafficModeType = factory.createTrafficModeType(TrafficModeType.Loadshare);
            NetworkAppearance na = factory.createNetworkAppearance(102L);
            As as = this.m3uaManagement.createAs("AS1", Functionality.IPSP, ExchangeType.SE, IPSPType.SERVER, rc, trafficModeType, 1, na);

            // Step 2 : Create ASP
            AspFactory aspFactory = this.m3uaManagement.createAspFactory("ASP1", nameForAnonymousAssociation);

            // Step3 : Assign ASP to AS
            Asp asp = this.m3uaManagement.assignAspToAs("AS1", "ASP1");

            // Step 4: Add Route. Remote point code is 2
            this.m3uaManagement.addRoute(200, 100, 3, "AS1");

            log.debug("AS="+as+", ASP factory="+aspFactory+", ASP="+asp);
            m3uaManagement.startAsp("ASP1");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
