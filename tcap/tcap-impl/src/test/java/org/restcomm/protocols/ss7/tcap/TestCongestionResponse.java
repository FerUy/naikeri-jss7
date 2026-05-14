package org.restcomm.protocols.ss7.tcap;

import static org.testng.Assert.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author nosach konstantin
 *
 */
public class TestCongestionResponse extends SccpHarness {

    private static final Logger logger = LogManager.getLogger(TestCongestionResponse.class.getName());

    public static final long WAIT_TIME = 1000;

    private TCAPStackImpl tcapStack1;
    private TCAPStackImpl tcapStack2;
    private Client client;
    private Server server;

    public TestCongestionResponse() {
    }

    @BeforeClass
    public void setUpClass() {
        this.sccpStack1Name = "TCAPCongestionTestSccpStack1";
        this.sccpStack2Name = "TCAPCongestionTestSccpStack2";
        logger.info("setUpClass");
    }

    @AfterClass
    public void tearDownClass() throws Exception {
        logger.info("tearDownClass");
    }

    @BeforeMethod
    public void setUp() throws Exception {
        logger.info("setUp");
        super.setUp();

        SccpAddress peer1Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
        SccpAddress peer2Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

        TestSccpListener sccpListener = new TestSccpListener();
        this.sccpProvider1.registerSccpListener(1, sccpListener);
        ((SccpStackImpl)this.sccpProvider1.getSccpStack()).setCongControlM(1);
        this.tcapStack1 = new TCAPStackImpl("TCAPCongestionTest1", this.sccpProvider1, 8);
        this.tcapStack2 = new TCAPStackImpl("TCAPCongestionTest2", this.sccpProvider2, 8);

        
        this.tcapStack1.start();
        this.tcapStack2.start();
        
        this.tcapStack1.setDoNotSendProtocolVersion(false);
        this.tcapStack2.setDoNotSendProtocolVersion(false);
        this.tcapStack1.setInvokeTimeout(0);
        this.tcapStack2.setInvokeTimeout(0);

        this.client = new Client(this.tcapStack1, super.parameterFactory, peer1Address, peer2Address);
        this.server = new Server(this.tcapStack2, super.parameterFactory, peer2Address, peer1Address);

    }

    @AfterMethod
    public void tearDown() {
        this.tcapStack1.stop();
        this.tcapStack2.stop();
        super.tearDown();

    }

    @Test(groups = { "congestion" })
    public void simpleTest() throws Exception {
    	 this.tcapStack2.setCongControl_MemoryThreshold_1(77);
         this.tcapStack2.setCongControl_BackToNormalMemoryThreshold_1(72);
         this.tcapStack2.setCongControl_MemoryThreshold_2(87);
         this.tcapStack2.setCongControl_BackToNormalMemoryThreshold_2(82);
         this.tcapStack2.setCongControl_blockingIncomingTcapMessages(false);

        // user congestion
        this.tcapStack2.getProvider().setUserPartCongestionLevel("a1", 1);
        EventTestHarness.waitFor(1100);
        client.startClientDialog();
        client.sendBegin();
        EventTestHarness.waitFor(WAIT_TIME);
        server.sendContinue();
        EventTestHarness.waitFor(WAIT_TIME);

        client.releaseDialog();
        server.releaseDialog();
        assertEquals(server.observedEvents.size(), 3);
        assertEquals(client.observedEvents.size(), 3);

        client.startClientDialog();
        client.sendBegin();
        EventTestHarness.waitFor(WAIT_TIME);
        client.releaseDialog();
        server.releaseDialog();
        assertEquals(server.observedEvents.size(), 5);
        assertEquals(client.observedEvents.size(), 5);
//        assertTrue(sccpListener.isCongestedStatusReceived());

    }

}
