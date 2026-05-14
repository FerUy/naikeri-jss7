package org.restcomm.protocols.ss7.map.anonymousAssociaton.client;

import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.sctp.netty.NettySctpManagementImpl;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.restcomm.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.restcomm.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.restcomm.protocols.ss7.ss7ext.Ss7ExtInterfaceImpl;

public class Client {

    private final ClientProperties clientProperties;

    private NettySctpManagementImpl sctpManagement;
    private M3UAManagementImpl m3uaManagement;

    public Client(ClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }

    public void start() throws Exception {
        initSctp();
        initM3ua();
        this.m3uaManagement.startAsp("ASP1");
    }

    private void initSctp() throws Exception {
        this.sctpManagement = new NettySctpManagementImpl(clientProperties.getName());
        this.sctpManagement.setPersistDir(clientProperties.getPath());
        this.sctpManagement.start();
        this.sctpManagement.setConnectDelay(10000);
        this.sctpManagement.removeAllResources();
        this.sctpManagement.addAssociation(clientProperties.getHost(), clientProperties.getPort(), clientProperties.getPeer(), clientProperties.getPeerPort(),
                clientProperties.getAssociationName(), IpChannelType.SCTP, null);
    }

    private void initM3ua() throws Exception {
        this.m3uaManagement = new M3UAManagementImpl(clientProperties.getName(), null, new Ss7ExtInterfaceImpl());
        this.m3uaManagement.setTransportManagement(this.sctpManagement);
        this.m3uaManagement.setPersistDir(clientProperties.getPath());
        this.m3uaManagement.start();
        this.m3uaManagement.removeAllResources();
        this.m3uaManagement.setHeartbeatTime(2000);
        ParameterFactoryImpl factory = new ParameterFactoryImpl();
        RoutingContext rc = factory.createRoutingContext(new long[] { 101L });
        TrafficModeType trafficModeType = factory.createTrafficModeType(TrafficModeType.Loadshare);
        NetworkAppearance na = factory.createNetworkAppearance(102L);
        this.m3uaManagement.createAs("AS1", Functionality.IPSP, ExchangeType.SE, IPSPType.CLIENT, rc, trafficModeType, 1, na);
        // Step 2 : Create ASP
        this.m3uaManagement.createAspFactory("ASP1", clientProperties.getAssociationName());
        // Step3 : Assign ASP to AS
        this.m3uaManagement.assignAspToAs("AS1", "ASP1");

        // Step 4: Add Route. Remote point code is 2
        this.m3uaManagement.addRoute(100, 200, 3, "AS1");
    }
}
