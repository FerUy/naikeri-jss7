package org.restcomm.protocols.ss7.sccp.impl.router;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.LongMessageRule;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.Mtp3Destination;
import org.restcomm.protocols.ss7.sccp.Mtp3ServiceAccessPoint;
import org.restcomm.protocols.ss7.sccp.impl.Mtp3UserPartImpl;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class RouterStoreTest {

    private static final Logger logger = LogManager.getLogger(RouterStoreTest.class.getName());

    @Test
    public void testVer4() throws Exception {
        String name = "RouterStoreTest";
        SccpStackImpl sccpStack = new SccpStackImpl(name, null);
        RouterImpl router = new RouterImpl(name, sccpStack);

        router.start();
        router.removeAllResources();

        Mtp3UserPartImpl mtp3UserPart11 = new Mtp3UserPartImpl(null);
        sccpStack.setMtp3UserPart(2, mtp3UserPart11);
        router.addMtp3ServiceAccessPoint(1, 2, 11, 3, 4, "44445555");
        // router.addMtp3ServiceAccessPoint(id, mtp3Id, opc, ni, networkId, localGtDigits);

        router.addMtp3Destination(1, 2, 101, 102, 0, 15, 255);
        // router.addMtp3Destination(sapId, destId, firstDpc, lastDpc, firstSls, lastSls, slsMask);

        router.addLongMessageRule(5, 201, 202, LongMessageRuleType.XUDT_ENABLED);
        // router.addLongMessageRule(id, firstSpc, lastSpc, ruleType);

        router.store();

        String fn = generatePath(name, "3");
        String content = new String(Files.readAllBytes(Paths.get(fn)));
        logger.info(content);

        router.removeAllResources();
        Files.write(Paths.get(fn), content.getBytes());
        router.load();


        LongMessageRule lmr = router.getLongMessageRule(5);
        assertEquals(lmr.getFirstSpc(), 201);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.XUDT_ENABLED);

        Mtp3ServiceAccessPoint sap = router.getMtp3ServiceAccessPoint(1);
        assertEquals(sap.getOpc(), 11);
        Mtp3Destination dest = sap.getMtp3Destination(2);
        assertEquals(dest.getLastDpc(), 102);
    }

    @Test
    public void testVer3() throws Exception {
        String name = "RouterStoreTest";
        SccpStackImpl sccpStack = new SccpStackImpl(name, null);
        RouterImpl router = new RouterImpl(name, sccpStack);

        router.start();
        router.removeAllResources();

        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<rule>\n" +
                "  <id value=\"3\"/>\n" +
                "  <value ruleType=\"Solitary\" loadSharingAlgo=\"Undefined\" originatingType=\"LocalOriginated\" mask=\"K\" paddress=\"1\" saddress=\"-1\" networkId=\"11\">\n" +
                "        <patternSccpAddress pc=\"0\" ssn=\"8\">\n" +
                "            <ai value=\"82\"/>\n" +
                "            <gt type=\"GT0100\" tt=\"0\" es=\"2\" np=\"1\" nai=\"4\" digits=\"888888\"/>\n" +
                "        </patternSccpAddress>\n" +
                "    </value>\n" +
                "    <id value=\"1\"/>\n" +
                "</rule>\n" +
                "<routingAddress>\n" +
                "    <id value=\"1\"/>\n" +
                "    <sccpAddress pc=\"1\" ssn=\"8\">\n" +
                "        <ai value=\"83\"/>\n" +
                "        <gt type=\"GT0100\" tt=\"0\" es=\"2\" np=\"1\" nai=\"4\" digits=\"000.\"/>\n" +
                "    </sccpAddress>\n" +
                "</routingAddress>\n" +
                "<longMessageRule/>\n" +
                "<sap>\n" +
                "    <id value=\"1\"/>\n" +
                "    <value mtp3Id=\"1\" opc=\"11\" ni=\"2\" networkId=\"11\">\n" +
                "        <mtp3DestinationMap>\n" +
                "            <id value=\"2\"/>\n" +
                "            <value firstDpc=\"1\" lastDpc=\"102\" firstSls=\"0\" lastSls=\"255\" slsMask=\"255\"/>\n" +
                "        </mtp3DestinationMap>\n" +
                "    </value>\n" +
                "</sap>;\n";

        String fn2 = generatePath(name, "2");
        String fn3 = generatePath(name, "3");

        File f3 = new File(fn3);
        f3.delete();
        Files.write(Paths.get(fn2), content.getBytes(), StandardOpenOption.CREATE_NEW);

        router.load();

        Mtp3ServiceAccessPoint sap = router.getMtp3ServiceAccessPoint(1);
        assertEquals(sap.getOpc(), 11);
        Mtp3Destination dest = sap.getMtp3Destination(2);
        assertEquals(dest.getLastDpc(), 102);
    }

    private String generatePath(String name, String ver) {
        return System.getProperty("user.dir") + File.separator + name + "_" + "sccprouter" +
                ver + ".xml";
    }

}
