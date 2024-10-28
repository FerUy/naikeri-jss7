
package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.primitives.OctetStringBase;

import java.util.HashMap;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class UtranGANSSpositioningDataImpl extends OctetStringBase implements UtranGANSSpositioningData {

    public UtranGANSSpositioningDataImpl() {
        super(1, 9, "UtranGANSSpositioningData");
    }

    public UtranGANSSpositioningDataImpl(byte[] data) {
        super(1, 9, "UtranGANSSpositioningData", data);
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public HashMap<String, String> getLocationGeneratedMethodsAndGANSSId() throws MAPException {
        if (data == null)
            throw new MAPException("UtranGANSSpositioningData data must not be empty");
        if (data.length < 1)
            throw new MAPException("UtranGANSSpositioningData data length must be at least 1");
        if (data.length > 9)
            throw new MAPException("UtranGANSSpositioningData data length must not be higher than 9");

        if (getUtranGanssPositioningDataDiscriminator() != 1) {
            throw new MAPException("positioningDataDiscriminator indicates GANSS-PositioningDataSet is absence or not unique");
        } else {
            HashMap<String, String> methodsAndGANSSId = new HashMap<>();
            String method;
            String ganssId;

            for (int i=1; i<data.length; i++) {
                if ((data[i] & 0x07) == 3) {
                    method = getUtranGanssPossitioningMethod((data[i] & 0xC0) >> 6);
                    ganssId = getGANSSId((data[i] & 0x38) >> 3);
                    methodsAndGANSSId.put(method, ganssId);
                }
            }
            return methodsAndGANSSId;
        }
    }

    public int getUtranGanssPositioningDataDiscriminator() throws MAPException {
        if (data == null)
            throw new MAPException("UtranGANSSpositioningData data must not be empty");
        if (data.length < 1)
            throw new MAPException("UtranGANSSpositioningData data length must be at least 1");
        if (data.length > 9)
            throw new MAPException("UtranGANSSpositioningData data length must not be higher than 9");
        /*
         * The positioning data discriminator defines the type of data provided for each positioning method:
         *
         * 0000 indicates:
         *      the presence of
         *        the Positioning Data Set IE (that reports the usage of each non-GANSS method that was successfully used to obtain the location estimate)
         *        and the optional presence of the GANSS Positioning Data Set IE.
         *      It also indicates the optional presence of the Additional Positioning Data Set IE;
         *
         * 0001 indicates:
         *      the presence of:
         *        the GANSS Positioning Data Set IE (that reports the usage of each GANSS method that was successfully used to obtain the location estimate)
         *      the absence of:
         *        the Positioning Data Set IE.
         *      It also indicates the optional presence of the Additional Positioning Data Set IE;
         *
         * 0010 indicates:
         *      Additional Positioning Data Set IE and
         *      the absence of
         *       the Positioning Data Set and
         *       the GANSS Positioning Data Set IEs;
         *
         * 1 octet of data is provided for each positioning method included.
         *
         * All other values are reserved.
         */
        return data[0] & 0x0F;
    }

    public String getUtranGanssPossitioningMethod(int code) {
        /*
         * Coding of Method (bits 8-7):
         * 00   MS-Based
         * 01   MS-Assisted
         * 10   Conventional
         * 11   Reserved
         */
        String method;
        switch (code) {
            case 0:
                method = "MS-Based";
                break;
            case 1:
                method = "MS-Assisted";
                break;
            case 2:
                method = "Conventional";
                break;
            default:
                method = "Reserved";
                break;
        }
        return method;
    }

    private String getGANSSId(int code) throws MAPException {
        /*
         * Coding of the GANSS Id (bits 6-4) :
         *  000  Galileo
         *  001  Satellite Based Augmentation Systems (SBAS)
         *  010  Modernized GPS
         *  011  Quasi Zenith Satellite System (QZSS)
         *  100  GLONASS
         *  101  BDS
         */
        if (code > 5)
            throw new MAPException("UtranGANSSpositioningData GANSS Id must be an integer value between 0 and 5");

        String ganssId = null;
        switch (code) {
            case 0:
                ganssId = "Galileo";
                break;
            case 1:
                ganssId = "SBAS";
                break;
            case 2:
                ganssId = "Modernized GPS";
                break;
            case 3:
                ganssId = "QZSS";
                break;
            case 4:
                ganssId = "GLONASS";
                break;
            case 5:
                ganssId = "BDS";
                break;
        }
        return ganssId;
    }

    public String getUsage(int u) {
        String usage = null;
        /*
         * Coding of usage (bits 3-1):
         * 000 Attempted unsuccessfully due to failure or interruption - not used.
         * 001 Attempted successfully: results not used to generate location - not used.
         * 010 Attempted successfully: results used to verify but not generate location - not used.
         * 011 Attempted successfully: results used to generate location.
         * 100 Attempted successfully: case where MS supports multiple mobile based positioning methods and the actual method or methods used by the MS cannot be determined.
         *
         */
        switch (u) {
            case 0:
                usage = "Attempted unsuccessfully due to failure or interruption - not used";
                break;
            case 1:
                usage = "Attempted successfully: results not used to generate location - not used";
                break;
            case 2:
                usage = "Attempted successfully: results used to verify but not generate location - not used";
                break;
            case 3:
                usage = "Attempted successfully: results used to generate location";
                break;
            case 4:
                usage = "Attempted successfully: case where MS supports multiple mobile based positioning methods and the actual method or methods used by the MS cannot be determined";
                break;
        }
        return usage;
    }

    /*private static class MultiValueMap<K,V> {
        private final Map<K, Set<V>> mappings = new HashMap<>();

        public Set<V> getValues(K key) {
            return mappings.get(key);
        }

        public void putValue(K key, V value) {
            Set<V> target = mappings.get(key);

            if(target == null) {
                target = new HashSet<>();
                mappings.put(key,target);
            }

            target.add(value);
        }
    }*/

    /*public static void main(String[] args) throws MAPException {
        byte[] data = new byte[] {0x01, 0x63, (byte) 0x8b, 0x02, 0x03};
        UtranGANSSpositioningDataImpl utranGANSSpositioningData = new UtranGANSSpositioningDataImpl(data);
        HashMap<String, String> methodsAndGanssIds = utranGANSSpositioningData.getLocationGeneratedMethodsAndGANSSId();

        for (HashMap.Entry<String, String> entry : methodsAndGanssIds.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("Key=" + key + ", Value=" + value);
        }
    }*/
}
