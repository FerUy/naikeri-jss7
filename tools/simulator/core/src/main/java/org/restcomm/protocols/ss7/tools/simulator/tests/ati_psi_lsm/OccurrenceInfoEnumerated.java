package org.restcomm.protocols.ss7.tools.simulator.tests.ati_psi_lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.OccurrenceInfo;
import org.restcomm.protocols.ss7.tools.simulator.common.EnumeratedBase;

import java.util.Hashtable;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class OccurrenceInfoEnumerated extends EnumeratedBase {

    private static final Hashtable<String, Integer> stringMap = new Hashtable<>();
    private static final Hashtable<Integer, String> intMap = new Hashtable<>();

    static {
        intMap.put(OccurrenceInfo.oneTimeEvent.getInfo(), OccurrenceInfo.oneTimeEvent.toString());
        intMap.put(OccurrenceInfo.multipleTimeEvent.getInfo(), OccurrenceInfo.multipleTimeEvent.toString());

        stringMap.put(OccurrenceInfo.oneTimeEvent.toString(), OccurrenceInfo.oneTimeEvent.getInfo());
        stringMap.put(OccurrenceInfo.multipleTimeEvent.toString(), OccurrenceInfo.multipleTimeEvent.getInfo());
    }

    public OccurrenceInfoEnumerated(int val) throws IllegalArgumentException {
        super(val);
    }

    public OccurrenceInfoEnumerated(Integer val) throws IllegalArgumentException {
        super(val);
    }

    public static OccurrenceInfoEnumerated createInstance(String s) {
        Integer instance = doCreateInstance(s, stringMap, intMap);
        if (instance == null)
            return new OccurrenceInfoEnumerated(OccurrenceInfo.oneTimeEvent.getInfo());
        else
            return new OccurrenceInfoEnumerated(instance);
    }

    @Override
    protected Hashtable<Integer, String> getIntTable() {
        return intMap;
    }

    @Override
    protected Hashtable<String, Integer> getStringTable() {
        return stringMap;
    }
}
