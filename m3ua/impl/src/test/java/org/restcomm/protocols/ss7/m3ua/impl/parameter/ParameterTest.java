package org.restcomm.protocols.ss7.m3ua.impl.parameter;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.m3ua.parameter.DeregistrationStatus;
import org.restcomm.protocols.ss7.m3ua.parameter.DestinationPointCode;
import org.restcomm.protocols.ss7.m3ua.parameter.LocalRKIdentifier;
import org.restcomm.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.restcomm.protocols.ss7.m3ua.parameter.OPCList;
import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;
import org.restcomm.protocols.ss7.m3ua.parameter.RegistrationStatus;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;
import org.restcomm.protocols.ss7.m3ua.parameter.ServiceIndicators;
import org.restcomm.protocols.ss7.m3ua.parameter.Status;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.restcomm.protocols.ss7.m3ua.parameter.CongestedIndication.CongestionLevel;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class ParameterTest {

    private static final Logger logger = LogManager.getLogger(ParameterTest.class.getName());

    private final ParameterFactoryImpl factory = new ParameterFactoryImpl();
    private ByteBuf out = null;

    public ParameterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() {
        out = Unpooled.buffer(8192);
    }

    @AfterMethod
    public void tearDown() {
    }

    private short getTag(byte[] data) {
        return (short) ((data[0] & 0xff) << 8 | (data[1] & 0xff));
    }

    private short getLen(byte[] data) {
        return (short) ((data[2] & 0xff) << 8 | (data[3] & 0xff));
    }

    private byte[] getValue(byte[] data) {
        // reduce 4 for Tag + length bytes
        short length = (short) (getLen(data) - 4);
        byte[] value = new byte[length];
        System.arraycopy(data, 4, value, 0, length);
        return value;
    }

    @Test
    public void testProtocolData() throws IOException {

        // Trace from wireshark
        byte[] userData = new byte[] { 0x09, (byte) 0x80, 0x03, 0x0c, 0x15, 0x09, 0x12, 0x05, 0x00, 0x12, 0x04, 0x55, 0x16,
                0x09, (byte) 0x90, 0x09, 0x12, 0x07, 0x00, 0x12, 0x04, 0x55, 0x16, 0x09, 0x00, 0x5f, 0x62, 0x5d, 0x48, 0x04,
                0x3a, (byte) 0x8c, 0x10, 0x04, 0x6b, 0x3f, 0x28, 0x3d, 0x06, 0x07, 0x00, 0x11, (byte) 0x86, 0x05, 0x01, 0x01,
                0x01, (byte) 0xa0, 0x32, 0x60, 0x30, (byte) 0x80, 0x02, 0x07, (byte) 0x80, (byte) 0xa1, 0x09, 0x06, 0x07, 0x04,
                0x00, 0x00, 0x01, 0x00, 0x13, 0x02, (byte) 0xbe, 0x1f, 0x28, 0x1d, 0x06, 0x07, 0x04, 0x00, 0x00, 0x01, 0x01,
                0x01, 0x01, (byte) 0xa0, 0x12, (byte) 0xa0, 0x10, (byte) 0x80, 0x07, (byte) 0x91, 0x55, 0x16, 0x28,
                (byte) 0x81, 0x00, 0x70, (byte) 0x81, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x00, 0x6c, 0x14, (byte) 0xa1, 0x12,
                0x02, 0x01, 0x00, 0x02, 0x01, 0x3b, 0x30, 0x0a, 0x04, 0x01, 0x0f, 0x04, 0x05, 0x2a, (byte) 0xd9, (byte) 0x8c,
                0x36, 0x02 };

        byte[] protocolData = new byte[userData.length + 12];

        System.arraycopy(userData, 0, protocolData, 12, userData.length);
        protocolData[0] = 0x00;
        protocolData[1] = 0x00;
        protocolData[2] = 0x1e;
        protocolData[3] = (byte) 0xd4;
        protocolData[4] = 0x00;
        protocolData[5] = 0x00;
        protocolData[6] = 0x08;
        protocolData[7] = (byte) 0x98;
        protocolData[8] = 0x03;
        protocolData[9] = 0x03;
        protocolData[10] = 0x00;
        protocolData[11] = 0x0f;

        int si = 3;
        int mp = 0;
        int ni = 3;
        int dpc = 2200;
        int opc = 7892;
        int sls = 15;
        ProtocolDataImpl p1 = (ProtocolDataImpl) factory.createProtocolData(opc, dpc, si, ni, mp, sls, userData);

        assertTrue(Arrays.equals(protocolData, p1.getValue()));

        ProtocolDataImpl p2 = (ProtocolDataImpl) factory.createProtocolData(protocolData);

        assertEquals(p1.getTag(), p2.getTag());
        assertEquals(p1.getOpc(), p2.getOpc());
        assertEquals(p1.getDpc(), p2.getDpc());
        assertEquals(p2.getSI(), p2.getSI());
        assertEquals(p2.getNI(), p2.getNI());
        assertEquals(p2.getMP(), p2.getMP());
        assertEquals(p2.getSLS(), p2.getSLS());

        boolean isDataCorrect = Arrays.equals(p2.getData(), p2.getData());
        assertTrue(isDataCorrect, "Data mismatch");
    }

    /**
     * Test of getOpc method, of class ProtocolDataImpl.
     */
    @Test
    public void testProtocolData1() throws IOException {
        ProtocolDataImpl p1 = (ProtocolDataImpl) factory.createProtocolData(1408, 14150, 1, 1, 0, 1, new byte[] { 1, 2, 3, 4 });
        p1.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        ProtocolDataImpl p2 = (ProtocolDataImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(p1.getTag(), p2.getTag());
        assertEquals(p1.getOpc(), p2.getOpc());
        assertEquals(p1.getDpc(), p2.getDpc());
        assertEquals(p1.getSI(), p2.getSI());
        assertEquals(p1.getNI(), p2.getNI());
        assertEquals(p1.getMP(), p2.getMP());
        assertEquals(p1.getSLS(), p2.getSLS());

        boolean isDataCorrect = Arrays.equals(p1.getData(), p2.getData());
        assertTrue(isDataCorrect, "Data mismatch");
    }

    @Test
    public void testNetworkAppearance() throws IOException, XMLStreamException {

        NetworkAppearanceImpl np = (NetworkAppearanceImpl) factory.createNetworkAppearance(123);
        np.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        NetworkAppearanceImpl np2 = (NetworkAppearanceImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals((int) np2.getNetApp(), 123);

        // Test Serialization
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t");
        writer.write(np, "NetworkAppearanceImpl", NetworkAppearanceImpl.class);
        writer.close();

        logger.info(output);

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        NetworkAppearanceImpl np3 = reader.read("NetworkAppearanceImpl", NetworkAppearanceImpl.class);

        assertEquals((int) np3.getNetApp(), 123);
        assertEquals(np3.getTag(), Parameter.Network_Appearance);
    }

    @Test
    public void testRoutingContext() throws IOException, XMLStreamException {
        RoutingContextImpl rc = (RoutingContextImpl) factory.createRoutingContext(new long[] { 4294967295L });
        rc.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        RoutingContextImpl rc2 = (RoutingContextImpl) factory.createParameter(getTag(data), getValue(data));

        assertTrue(Arrays.equals(new long[] { 4294967295L }, rc2.getRoutingContexts()));

        // Test Serialization
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t");
        writer.write(rc, "RoutingContextImpl", RoutingContextImpl.class);
        writer.close();

        logger.info(output);

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        RoutingContextImpl rc3 = reader.read("RoutingContextImpl", RoutingContextImpl.class);

        assertTrue(Arrays.equals(new long[] { 4294967295L }, rc3.getRoutingContexts()));
        assertEquals(rc3.getTag(), Parameter.Routing_Context);
    }

    @Test
    public void testRoutingContexts() throws IOException, XMLStreamException {
        RoutingContextImpl rc = (RoutingContextImpl) factory.createRoutingContext(new long[] { 123L, 4294967295L });
        rc.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        RoutingContextImpl rc2 = (RoutingContextImpl) factory.createParameter(getTag(data), getValue(data));

        assertTrue(Arrays.equals(new long[] { 123L, 4294967295L }, rc2.getRoutingContexts()));

        // Test Serialization
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t");
        writer.write(rc, "RoutingContextImpl", RoutingContextImpl.class);
        writer.close();

        logger.info(output);

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        RoutingContextImpl rc3 = reader.read("RoutingContextImpl", RoutingContextImpl.class);

        assertTrue(Arrays.equals(new long[] { 123L, 4294967295L }, rc3.getRoutingContexts()));
        assertEquals(rc3.getTag(), Parameter.Routing_Context);
    }

    @Test
    public void testCorrelationId() throws IOException {
        CorrelationIdImpl crrId = (CorrelationIdImpl) factory.createCorrelationId(4294967295L);
        crrId.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        CorrelationIdImpl crrId2 = (CorrelationIdImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(crrId2.getCorrelationId(), 4294967295L);
    }

    @Test
    public void testAffectedPointCode() throws IOException {

        AffectedPointCodeImpl affectedPc = (AffectedPointCodeImpl) factory.createAffectedPointCode(new int[] { 123 },
                new short[] { 0 });
        affectedPc.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        AffectedPointCodeImpl affectedPc2 = (AffectedPointCodeImpl) factory.createParameter(getTag(data), getValue(data));

        assertTrue(Arrays.equals(new int[] { 123 }, affectedPc2.getPointCodes()));
        assertTrue(Arrays.equals(new short[] { 0 }, affectedPc2.getMasks()));
    }

    @Test
    public void testAffectedPointCodes() throws IOException {
        AffectedPointCodeImpl affectedPc = (AffectedPointCodeImpl) factory.createAffectedPointCode(new int[] { 123, 456 },
                new short[] { 0, 1 });
        affectedPc.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        AffectedPointCodeImpl affectedPc2 = (AffectedPointCodeImpl) factory.createParameter(getTag(data), getValue(data));

        assertTrue(Arrays.equals(new int[] { 123, 456 }, affectedPc2.getPointCodes()));
        assertTrue(Arrays.equals(new short[] { 0, 1 }, affectedPc2.getMasks()));
    }

    @Test
    public void testInfoString() throws IOException {
        InfoStringImpl infoStr = (InfoStringImpl) factory.createInfoString("Hello World");
        infoStr.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        InfoStringImpl infoStr2 = (InfoStringImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(infoStr2.getString(), "Hello World");

    }

    @Test
    public void testConcernedDPC() throws IOException {
        ConcernedDPCImpl concernedDPC = (ConcernedDPCImpl) factory.createConcernedDPC(123);
        concernedDPC.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        ConcernedDPCImpl concernedDPC2 = (ConcernedDPCImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(concernedDPC.getPointCode(), concernedDPC2.getPointCode());

    }

    @Test
    public void testCongestedIndication() throws IOException {
        CongestedIndicationImpl congIndImpl = (CongestedIndicationImpl) factory
                .createCongestedIndication(CongestionLevel.LEVEL2);
        congIndImpl.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        CongestedIndicationImpl congIndImpl2 = (CongestedIndicationImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(congIndImpl.getCongestionLevel(), congIndImpl2.getCongestionLevel());

    }

    @Test
    public void testUserCause() throws IOException {
        UserCauseImpl usrCa = (UserCauseImpl) factory.createUserCause(5, 0);
        usrCa.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        UserCauseImpl usrCa2 = (UserCauseImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(usrCa.getUser(), usrCa2.getUser());

        assertEquals(usrCa.getCause(), usrCa2.getCause());

    }

    @Test
    public void testASPIdentifier() throws IOException {
        ASPIdentifierImpl rc = (ASPIdentifierImpl) factory.createASPIdentifier(12234445);
        rc.write(out);
        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        ASPIdentifierImpl rc2 = (ASPIdentifierImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(rc2.getAspId(), 12234445L);
    }

    @Test
    public void testDestinationPointCode() throws IOException, XMLStreamException {

        DestinationPointCodeImpl affectedPc = (DestinationPointCodeImpl) factory.createDestinationPointCode(123, (short) 0);
        affectedPc.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        DestinationPointCodeImpl affectedPc2 = (DestinationPointCodeImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(affectedPc2.getPointCode(), 123);
        assertEquals(affectedPc2.getMask(), (short) 0);

        // Test Serialization
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t");
        writer.write(affectedPc, "DestinationPointCodeImpl", DestinationPointCodeImpl.class);
        writer.close();

        logger.info(output);

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        DestinationPointCodeImpl affectedPc3 = reader.read("DestinationPointCodeImpl", DestinationPointCodeImpl.class);

        assertEquals(affectedPc3.getPointCode(), 123);
        assertEquals(affectedPc3.getMask(), (short) 0);
        assertEquals(affectedPc3.getTag(), Parameter.Destination_Point_Code);
    }

    @Test
    public void testLocalRKIdentifier() throws IOException, XMLStreamException {
        LocalRKIdentifierImpl crrId = (LocalRKIdentifierImpl) factory.createLocalRKIdentifier(4294967295L);
        crrId.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        LocalRKIdentifierImpl crrId2 = (LocalRKIdentifierImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(crrId2.getId(), 4294967295L);

        // Test Serialization
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t");
        writer.write(crrId, "LocalRKIdentifierImpl", LocalRKIdentifierImpl.class);
        writer.close();

        logger.info(output);

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        LocalRKIdentifierImpl crrId3 = reader.read("LocalRKIdentifierImpl", LocalRKIdentifierImpl.class);

        assertEquals(crrId3.getId(), 4294967295L);
        assertEquals(crrId3.getTag(), Parameter.Local_Routing_Key_Identifier);
    }

    @Test
    public void testOPCList() throws IOException, XMLStreamException {
        OPCListImpl opcList = (OPCListImpl) factory.createOPCList(new int[] { 123, 456 }, new short[] { 0, 1 });
        opcList.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        OPCListImpl opcList1 = (OPCListImpl) factory.createParameter(getTag(data), getValue(data));

        assertTrue(Arrays.equals(new int[] { 123, 456 }, opcList1.getPointCodes()));
        assertTrue(Arrays.equals(new short[] { 0, 1 }, opcList1.getMasks()));

        // Test Serialization
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t");
        writer.write(opcList, "OPCListImpl", OPCListImpl.class);
        writer.close();

        logger.info(output);

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        OPCListImpl opcList3 = reader.read("OPCListImpl", OPCListImpl.class);

        assertTrue(Arrays.equals(new int[] { 123, 456 }, opcList3.getPointCodes()));
        assertTrue(Arrays.equals(new short[] { 0, 1 }, opcList3.getMasks()));
        assertEquals(opcList3.getTag(), Parameter.Originating_Point_Code_List);
    }

    @Test
    public void testServiceIndicators() throws IOException, XMLStreamException {
        ServiceIndicatorsImpl siList = (ServiceIndicatorsImpl) factory.createServiceIndicators(new short[] { 1, 2, 3, 4 });
        siList.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        ServiceIndicatorsImpl siList1 = (ServiceIndicatorsImpl) factory.createParameter(getTag(data), getValue(data));

        assertTrue(Arrays.equals(new short[] { 1, 2, 3, 4 }, siList1.getIndicators()));

        // Test Serialization
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t");
        writer.write(siList, "ServiceIndicatorsImpl", ServiceIndicatorsImpl.class);
        writer.close();

        logger.info(output);

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        ServiceIndicatorsImpl siList3 = reader.read("ServiceIndicatorsImpl", ServiceIndicatorsImpl.class);

        assertTrue(Arrays.equals(new short[] { 1, 2, 3, 4 }, siList3.getIndicators()));
        assertEquals(siList3.getTag(), Parameter.Service_Indicators);
    }

    @Test
    public void testTrafficModeType() throws IOException, XMLStreamException {
        TrafficModeTypeImpl rc = (TrafficModeTypeImpl) factory.createTrafficModeType(1);
        rc.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        TrafficModeTypeImpl rc2 = (TrafficModeTypeImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(rc2.getMode(), 1);

        // Test Serialization
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t");
        writer.write(rc, "TrafficModeTypeImpl", TrafficModeTypeImpl.class);
        writer.close();

        logger.info(output);

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        TrafficModeTypeImpl crrId3 = reader.read("TrafficModeTypeImpl", TrafficModeTypeImpl.class);

        assertEquals(crrId3.getMode(), 1);
        assertEquals(crrId3.getTag(), Parameter.Traffic_Mode_Type);
    }

    @Test
    public void testRoutingKey() throws IOException, XMLStreamException {
        LocalRKIdentifier localRkId = factory.createLocalRKIdentifier(12);
        RoutingContext rc = factory.createRoutingContext(new long[] { 1 });
        TrafficModeType trafficModeType = factory.createTrafficModeType(1);
        NetworkAppearance netApp = factory.createNetworkAppearance(1);
        DestinationPointCode[] dpc = new DestinationPointCode[] { factory.createDestinationPointCode(123, (short) 0),
                factory.createDestinationPointCode(456, (short) 1) };
        ServiceIndicators[] serviceIndicators = new ServiceIndicators[] { factory.createServiceIndicators(new short[] { 1, 2 }),
                factory.createServiceIndicators(new short[] { 1, 2 }) };
        OPCList[] opcList = new OPCList[] { factory.createOPCList(new int[] { 1, 2, 3 }, new short[] { 0, 0, 0 }),
                factory.createOPCList(new int[] { 4, 5, 6 }, new short[] { 0, 0, 0 }) };

        RoutingKeyImpl routKey = (RoutingKeyImpl) factory.createRoutingKey(localRkId, rc, trafficModeType, netApp, dpc, serviceIndicators,
                opcList);
        routKey.write(out);

        byte[] data = out.array();

        RoutingKeyImpl routeKey2 = (RoutingKeyImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(localRkId.getId(), routeKey2.getLocalRKIdentifier().getId());
        assertTrue(Arrays.equals(routKey.getRoutingContext().getRoutingContexts(), routeKey2.getRoutingContext()
                .getRoutingContexts()));

        // Test Serialization
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t");
        writer.write(routKey, "RoutingKeyImpl", RoutingKeyImpl.class);
        writer.close();

        logger.info(output);

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        RoutingKeyImpl routeKey3 = reader.read("RoutingKeyImpl", RoutingKeyImpl.class);

        assertEquals(localRkId.getId(), routeKey3.getLocalRKIdentifier().getId());
        assertTrue(Arrays.equals(routKey.getRoutingContext().getRoutingContexts(), routeKey3.getRoutingContext()
                .getRoutingContexts()));
        assertEquals(routeKey3.getTag(), Parameter.Routing_Key);
    }

    @Test
    public void testRoutingKeySerialization1() throws IOException, XMLStreamException {
        LocalRKIdentifier localRkId = factory.createLocalRKIdentifier(12);
        RoutingContext rc = factory.createRoutingContext(new long[] { 1 });
        TrafficModeType trafficModeType = factory.createTrafficModeType(1);
        NetworkAppearance netApp = factory.createNetworkAppearance(1);
        DestinationPointCode[] dpc = new DestinationPointCode[] { factory.createDestinationPointCode(123, (short) 0),
                factory.createDestinationPointCode(456, (short) 1) };
        ServiceIndicators[] serviceIndicators = new ServiceIndicators[] { factory.createServiceIndicators(new short[] { 1, 2 }),
                factory.createServiceIndicators(new short[] { 1, 2 }) };
        OPCList[] opcList = new OPCList[] { factory.createOPCList(new int[] { 1, 2, 3 }, new short[] { 0, 0, 0 }),
                factory.createOPCList(new int[] { 4, 5, 6 }, new short[] { 0, 0, 0 }) };

        RoutingKeyImpl routKey = (RoutingKeyImpl) factory.createRoutingKey(null, rc, trafficModeType, null, dpc, null, null);
        routKey.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        RoutingKeyImpl routeKey2 = (RoutingKeyImpl) factory.createParameter(getTag(data), getValue(data));

        assertNull(routeKey2.getLocalRKIdentifier());
        assertTrue(Arrays.equals(routKey.getRoutingContext().getRoutingContexts(), routeKey2.getRoutingContext()
                .getRoutingContexts()));

        // Test Serialization
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t");
        writer.write(routKey, "RoutingKeyImpl", RoutingKeyImpl.class);
        writer.close();

        logger.info(output);

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        RoutingKeyImpl routeKey3 = reader.read("RoutingKeyImpl", RoutingKeyImpl.class);

        assertNull(routeKey3.getLocalRKIdentifier());
        assertTrue(Arrays.equals(routKey.getRoutingContext().getRoutingContexts(), routeKey3.getRoutingContext()
                .getRoutingContexts()));
        assertEquals(routeKey3.getTag(), Parameter.Routing_Key);
    }

    @Test
    public void testRegistrationStatus() throws IOException {
        RegistrationStatusImpl crrId = (RegistrationStatusImpl) factory.createRegistrationStatus(11);
        crrId.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        RegistrationStatusImpl crrId2 = (RegistrationStatusImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(crrId2.getStatus(), 11);
    }

    @Test
    public void testRegistrationResult() throws IOException {
        LocalRKIdentifier localRkId = factory.createLocalRKIdentifier(12);
        RoutingContext rc = factory.createRoutingContext(new long[] { 1 });
        RegistrationStatus status = factory.createRegistrationStatus(0);

        RegistrationResultImpl routKey = (RegistrationResultImpl) factory.createRegistrationResult(localRkId, status, rc);
        routKey.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        RegistrationResultImpl rc2 = (RegistrationResultImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(localRkId.getId(), rc2.getLocalRKIdentifier().getId());
        assertTrue(Arrays
                .equals(routKey.getRoutingContext().getRoutingContexts(), rc2.getRoutingContext().getRoutingContexts()));
        assertEquals(status.getStatus(), rc2.getRegistrationStatus().getStatus());
    }

    @Test
    public void testDeregistrationStatus() throws IOException {
        DeregistrationStatusImpl crrId = (DeregistrationStatusImpl) factory.createDeregistrationStatus(5);
        crrId.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        DeregistrationStatusImpl crrId2 = (DeregistrationStatusImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(crrId2.getStatus(), 5);
    }

    @Test
    public void testDeregistrationResult() throws IOException {
        RoutingContext rc = factory.createRoutingContext(new long[] { 1 });
        DeregistrationStatus status = factory.createDeregistrationStatus(0);

        DeregistrationResultImpl routKey = (DeregistrationResultImpl) factory.createDeregistrationResult(rc, status);
        routKey.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        DeregistrationResultImpl rc2 = (DeregistrationResultImpl) factory.createParameter(getTag(data), getValue(data));

        assertTrue(Arrays
                .equals(routKey.getRoutingContext().getRoutingContexts(), rc2.getRoutingContext().getRoutingContexts()));
        assertEquals(status.getStatus(), rc2.getDeregistrationStatus().getStatus());
    }

    @Test
    public void testStatus() throws IOException {

        Status routKey = factory.createStatus(1, 4);
        ((StatusImpl) routKey).write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        StatusImpl rc2 = (StatusImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(routKey.getType(), rc2.getType());
        assertEquals(routKey.getInfo(), rc2.getInfo());
    }
}