package org.restcomm.protocols.ss7.tools.simulator.tests.ati_psi_lsm;

import org.restcomm.protocols.ss7.tools.simulator.common.EnumeratedBase;

import java.util.Hashtable;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>*
 */
public class SRISMReaction extends EnumeratedBase {

    private static final long serialVersionUID = 1L;

    public static final int VAL_RETURN_SUCCESS = 1;
    public static final int VAL_RETURN_SUCCESS_WITH_LMSI = 2;
    public static final int VAL_ERROR_SYSTEM_FAILURE = 3;
    public static final int VAL_ERROR_CALL_BARRED = 4;
    public static final int VAL_ERROR_ABSENT_SUBSCRIBER = 5;

    private static final Hashtable<String, Integer> stringMap = new Hashtable<>();
    private static final Hashtable<Integer, String> intMap = new Hashtable<>();

    static {
        intMap.put(VAL_RETURN_SUCCESS, "Return success");
        intMap.put(VAL_RETURN_SUCCESS_WITH_LMSI, "Return success with LMSI");
        intMap.put(VAL_ERROR_SYSTEM_FAILURE, "Return error system failure");
        intMap.put(VAL_ERROR_CALL_BARRED, "Return error call barred");
        intMap.put(VAL_ERROR_ABSENT_SUBSCRIBER, "Return error absent subscriber");

        stringMap.put("Return success", VAL_RETURN_SUCCESS);
        stringMap.put("Return success with LMSI", VAL_RETURN_SUCCESS_WITH_LMSI);
        stringMap.put("Return error system failure", VAL_ERROR_SYSTEM_FAILURE);
        stringMap.put("Return error call barred", VAL_ERROR_CALL_BARRED);
        stringMap.put("Return error absent subscriber", VAL_ERROR_ABSENT_SUBSCRIBER);
    }

    public SRISMReaction() {
    }

    public SRISMReaction(int val) throws IllegalArgumentException {
        super(val);
    }

    public SRISMReaction(Integer val) throws IllegalArgumentException {
        super(val);
    }

    public SRISMReaction(String val) throws IllegalArgumentException {
        super(val);
    }

    public static SRISMReaction createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new SRISMReaction(VAL_RETURN_SUCCESS);
        else
            return new SRISMReaction(i1);
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
