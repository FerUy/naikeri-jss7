package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranAdditionalPositioningData;
import org.restcomm.protocols.ss7.map.primitives.OctetStringBase;

import java.util.HashMap;

/**
 *
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 *
 */
public class UtranAdditionalPositioningDataImpl extends OctetStringBase implements UtranAdditionalPositioningData {

    public UtranAdditionalPositioningDataImpl() {
        super(1, 8, "UtranAdditionalPositioningData");
    }

    public UtranAdditionalPositioningDataImpl(byte[] data) {
        super(1, 8, "UtranAdditionalPositioningData", data);
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public HashMap<String, String> getUtranAdditionalPositioningDataSet() throws MAPException {
        if (data == null)
            throw new MAPException("UtranAdditionalPositioningData data must not be null");
        if (data.length < 1)
            throw new MAPException("UtranAdditionalPositioningData data length must be at least 1");
        if (data.length > 8)
            throw new MAPException("UtranAdditionalPositioningData data length must not be higher than 8");

        HashMap<String, String> posMethodsAndAddPosId = new HashMap<>();
        String positioningMethod;
        String additionalPosId;

        for (int i = 0; i < data.length; i++) {
            positioningMethod = getPositioningMethod((data[i] & 0xc0) >> 6);
            additionalPosId = getAdditionalPositioningId((data[i] & 0x38) >> 3);
            posMethodsAndAddPosId.put(positioningMethod, additionalPosId);
        }
        return posMethodsAndAddPosId;
    }

    public String getPositioningMethod(int code) {
        /*
         * Coding of positioning method (bits 8-7):
         * 00 Reserved;
         * 01 MS-Assisted;
         * 10 Standalone;
         * 11 Reserved.
         */
        String posMethod;
        switch (code) {
            case 1:
                posMethod = "MS-Assisted";
                break;
            case 2:
                posMethod = "Standalone";
                break;
            default:
                posMethod = "Reserved";
                break;
        }
        return posMethod;
    }

    public String getAdditionalPositioningId(int id) {
        /*
         * Coding of Additional Positioning ID (bits 6-4):
         * 000 Barometric Pressure;
         * 001 WLAN;
         * 010 Bluetooth;
         * 011 MBS;
         * other values reserved.
         */
        String additionalPositioningId;
        switch (id) {
            case 0:
                additionalPositioningId = "Barometric Pressure";
                break;
            case 1:
                additionalPositioningId = "WLAN";
                break;
            case 2:
                additionalPositioningId = "Bluetooth";
                break;
            case 3:
                additionalPositioningId = "MBS";
                break;
            default:
                additionalPositioningId = "reserved";
                break;
        }
        return additionalPositioningId;
    }

    private String getUsage(int u) {
        /*
         * Coding of usage (bits 3-1):
         * 011 Attempted successfully: results used to generate location;
         * 100 Attempted successfully: case where MS supports multiple mobile based positioning methods and the actual method or methods used by the MS cannot be determined.
         *
         */
        String usage = null;
        switch (u) {
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
    }

    public static void main(String[] args) throws MAPException {
        byte[] data = new byte[] {0x57, (byte) 0x8F};
        UtranAdditionalPositioningDataImpl utranAdditionalPositioningData = new UtranAdditionalPositioningDataImpl(data);
        HashMap<String, String> methodsAndAddPosIds = utranAdditionalPositioningData.getUtranAdditionalPositioningDataSet();

        for (HashMap.Entry<String, String> entry : methodsAndAddPosIds.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("Method=" + key + ", AddPosId=" + value);
        }
    }*/
}
