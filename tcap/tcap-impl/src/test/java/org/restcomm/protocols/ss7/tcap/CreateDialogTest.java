package org.restcomm.protocols.ss7.tcap;

import static org.testng.Assert.*;

import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.mtp.Mtp3UserPart;
import org.restcomm.protocols.ss7.sccp.NetworkIdState;
import org.restcomm.protocols.ss7.sccp.Router;
import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.SccpManagementEventListener;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.sccp.SccpResource;
import org.restcomm.protocols.ss7.sccp.SccpStack;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.message.MessageFactory;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.ss7ext.Ss7ExtSccpInterface;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.ss7.congestion.ExecutorCongestionMonitor;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class CreateDialogTest {

    private static final Logger logger = LogManager.getLogger(CreateDialogTest.class.getName());

    private final SccpHarnessPreview sccpProv = new SccpHarnessPreview();
    private TCAPStackImplWrapper tcapStack1;

    @BeforeClass
    public void setUpClass() {
        logger.info("setUpClass");
    }

    @AfterClass
    public void tearDownClass() throws Exception {
        logger.info("tearDownClass");
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @BeforeMethod
    public void setUp() throws Exception {
        logger.info("setUp");

        this.tcapStack1 = new TCAPStackImplWrapper(this.sccpProv, 8, "CreateDialogTest");

        this.tcapStack1.start();
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @AfterMethod
    public void tearDown() {
        this.tcapStack1.stop();
    }

    @Test(groups = { "functional.flow" })
    public void createDialogTest() throws Exception {

        SccpAddress localAddress = new SccpAddressImpl();
        SccpAddress remoteAddress = new SccpAddressImpl();

        Dialog dlg1 = this.tcapStack1.getProvider().getNewDialog(localAddress, remoteAddress);
        assertEquals((long) dlg1.getLocalDialogId(), 1L);

        try {
            this.tcapStack1.getProvider().getNewDialog(localAddress, remoteAddress, 1L);
            fail("Must be failure because dialogID==1 is busy");
        } catch (Exception ignored) {
        }

        Dialog dlg3 = this.tcapStack1.getProvider().getNewDialog(localAddress, remoteAddress, 2L);
        assertEquals((long) dlg3.getLocalDialogId(), 2L);

        Dialog dlg4 = this.tcapStack1.getProvider().getNewDialog(localAddress, remoteAddress);
        assertEquals((long) dlg4.getLocalDialogId(), 3L);
    }

    private static class SccpHarnessPreview implements SccpProvider {

        @Override
        public void deregisterSccpListener(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public int getMaxUserDataLength(SccpAddress arg0, SccpAddress arg1, int networkId) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public MessageFactory getMessageFactory() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ParameterFactory getParameterFactory() {
            // TODO Auto-generated method stub
            return null;
        }

        protected SccpListener sccpListener;

        @Override
        public void registerSccpListener(int arg0, SccpListener listener) {
            sccpListener = listener;
        }

        @Override
        public void send(SccpDataMessage msg) {
            // we check here that no messages go from TCAP previewMode

            fail("No message must go from TCAP previewMode");
        }

        @Override
        public void registerManagementEventListener(SccpManagementEventListener listener) {
            // TODO Auto-generated method stub

        }

        @Override
        public void deregisterManagementEventListener(SccpManagementEventListener listener) {
            // TODO Auto-generated method stub

        }

        @Override
        public void coordRequest(int ssn) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public FastMap<Integer, NetworkIdState> getNetworkIdStateList() {
            return new FastMap<>();
        }

        @Override
        public ExecutorCongestionMonitor[] getExecutorCongestionMonitorList() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public SccpConnection newConnection(int localSsn, ProtocolClass protocolClass) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public FastMap<LocalReference, SccpConnection> getConnections() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void send(SccpNoticeMessage message) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public SccpStack getSccpStack() {
            // Mock implementation for test
            return new MockSccpStack();
        }

        @Override
        public void updateSPCongestion(Integer ssn, Integer congestionLevel) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public FastList<SccpManagementEventListener> getManagementEventListeners() {
            return null;
        }
        
        // Mock SccpStack for test purposes
        private static class MockSccpStack implements SccpStack {
            @Override
            public Ss7ExtSccpInterface getSs7ExtSccpInterface() {
                return null;
            }

            @Override
            public void start() {
                // Implementation without exceptions
            }

            @Override
            public void stop() {
                // Implementation
            }

            @Override
            public boolean isStarted() {
                return false;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public SccpProvider getSccpProvider() {
                return null;
            }

            @Override
            public String getPersistDir() {
                return null;
            }

            @Override
            public void setPersistDir(String persistDir) {
                // Implementation
            }

            @Override
            public void setRemoveSpc(boolean removeSpc) {
                // Implementation
            }

            @Override
            public boolean isRemoveSpc() {
                return false;
            }

            @Override
            public void setRespectPc(boolean respectPc) {
                // Implementation
            }

            @Override
            public boolean isRespectPc() {
                return false;
            }

            @Override
            public void setPreviewMode(boolean previewMode) {
                // Implementation
            }

            @Override
            public boolean isPreviewMode() {
                return false;
            }

            @Override
            public void setSccpProtocolVersion(SccpProtocolVersion sccpProtocolVersion) {
                // Implementation
            }

            @Override
            public SccpProtocolVersion getSccpProtocolVersion() {
                return null;
            }

            @Override
            public SccpResource getSccpResource() {
                return null;
            }

            @Override
            public int getSstTimerDuration_Min() {
                return 0;
            }

            @Override
            public void setSstTimerDuration_Min(int sstTimerDuration_Min) {
                // Implementation
            }

            @Override
            public int getSstTimerDuration_Max() {
                return 0;
            }

            @Override
            public void setSstTimerDuration_Max(int sstTimerDuration_Max) {
                // Implementation
            }

            @Override
            public double getSstTimerDuration_IncreaseFactor() {
                return 0;
            }

            @Override
            public void setSstTimerDuration_IncreaseFactor(double sstTimerDuration_IncreaseFactor) {
                // Implementation
            }

            @Override
            public int getZMarginXudtMessage() {
                return 0;
            }

            @Override
            public void setZMarginXudtMessage(int zMarginXudtMessage) {
                // Implementation
            }

            @Override
            public int getMaxDataMessage() {
                return 0;
            }

            @Override
            public void setMaxDataMessage(int maxDataMessage) {
                // Implementation
            }

            @Override
            public int getPeriodOfLogging() {
                return 0;
            }

            @Override
            public void setPeriodOfLogging(int periodOfLogging) {
                // Implementation
            }

            @Override
            public int getReassemblyTimerDelay() {
                return 0;
            }

            @Override
            public void setReassemblyTimerDelay(int reassemblyTimerDelay) {
                // Implementation
            }

            @Override
            public boolean isCanRelay() {
                return false;
            }

            @Override
            public void setCanRelay(boolean canRelay) {
                // Implementation
            }

            @Override
            public int getConnEstTimerDelay() {
                return 0;
            }

            @Override
            public void setConnEstTimerDelay(int connEstTimerDelay)  {
                // Implementation
            }

            @Override
            public int getIasTimerDelay() {
                return 0;
            }

            @Override
            public void setIasTimerDelay(int iasTimerDelay) {
                // Implementation
            }

            @Override
            public int getIarTimerDelay() {
                return 0;
            }

            @Override
            public void setIarTimerDelay(int iarTimerDelay) {
                // Implementation
            }

            @Override
            public int getRelTimerDelay() {
                return 0;
            }

            @Override
            public void setRelTimerDelay(int releaseTimerDelay)  {
                // Implementation
            }

            @Override
            public int getRepeatRelTimerDelay() {
                return 0;
            }

            @Override
            public void setRepeatRelTimerDelay(int repeatRelTimerDelay)  {
                // Implementation
            }

            @Override
            public int getIntTimerDelay() {
                return 0;
            }

            @Override
            public void setIntTimerDelay(int intTimerDelay)  {
                // Implementation
            }

            @Override
            public int getGuardTimerDelay() {
                return 0;
            }

            @Override
            public void setGuardTimerDelay(int guardTimerDelay)  {
                // Implementation
            }

            @Override
            public int getResetTimerDelay() {
                return 0;
            }

            @Override
            public void setResetTimerDelay(int resetTimerDelay)  {
                // Implementation
            }

            @Override
            public void setMtp3UserParts(Map<Integer, Mtp3UserPart> mtp3UserPartsTemp) {
                // Implementation
            }

            @Override
            public Map<Integer, Mtp3UserPart> getMtp3UserParts() {
                return null;
            }

            @Override
            public Mtp3UserPart getMtp3UserPart(int id) {
                return null;
            }

            @Override
            public Router getRouter() {
                return null;
            }

            @Override
            public int getCongControlTIMER_A() {
                return 0;
            }

            @Override
            public void setCongControlTIMER_A(int value) {
                // Implementation
            }

            @Override
            public int getCongControlTIMER_D() {
                return 0;
            }

            @Override
            public void setCongControlTIMER_D(int value) {
                // Implementation
            }

            @Override
            public String getCongControl_Algo() {
                return null;
            }

            @Override
            public void setCongControl_Algo(String sccpCongestionControlAlgo)  {
                // Implementation
            }

            @Override
            public boolean isCongControl_blockingOutgoingSccpMessages() {
                return false;
            }

            @Override
            public void setCongControl_blockingOutgoingSccpMessages(boolean value) {
                // Implementation
            }

            @Override
            public int getFirstSls() {
                return 0;
            }

            @Override
            public int getLastSls() {
                // This value is used in getNextSeqControl method of TCAPProviderImpl
                return 255;
            }

            @Override
            public int getSlsMask() {
                return 0;
            }
        }
    }
}