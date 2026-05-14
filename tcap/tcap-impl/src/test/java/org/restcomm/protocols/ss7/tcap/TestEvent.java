package org.restcomm.protocols.ss7.tcap;

import java.io.Serializable;

/**
 * @author baranowb
 *
 */
public class TestEvent implements Serializable {

    private final EventType eventType;
    private final boolean sent;
    private final long timestamp;
    private final Object event;
    private final int sequence;

    public static TestEvent createReceivedEvent(EventType eventType, Object eventSource, int sequence) {
        return new TestEvent(eventType, false, System.currentTimeMillis(), eventSource, sequence);
    }

    public static TestEvent createSentEvent(EventType eventType, Object eventSource, int sequence) {
        return new TestEvent(eventType, true, System.currentTimeMillis(), eventSource, sequence);
    }

    public static TestEvent createReceivedEvent(EventType eventType, Object eventSource, int sequence, long stamp) {
        return new TestEvent(eventType, false, stamp, eventSource, sequence);
    }

    public static TestEvent createSentEvent(EventType eventType, Object eventSource, int sequence, long stamp) {
        return new TestEvent(eventType, true, stamp, eventSource, sequence);
    }

    /**
     * @param eventType type of TCAP event
     *                  (Begin, Continue, End, Uni, UAbort, PAbort, Notice, InvokeTimeout, DialogTimeout, DialogRelease,
     *                  Invoke, ReturnResult, ReturnResultLast, ReturnError, Reject)
     * @param sent boolean determining if sent or not
     * @param timestamp timestamp of the event
     * @param event the event object
     */
    public TestEvent(EventType eventType, boolean sent, long timestamp, Object event, int sequence) {
        super();
        this.eventType = eventType;
        this.sent = sent;
        this.timestamp = timestamp;
        this.event = event;
        this.sequence = sequence;
    }

    public EventType getEventType() {
        return eventType;
    }

    public boolean isSent() {
        return sent;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Object getEvent() {
        return event;
    }

    public int getSequence() {
        return sequence;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        // result = prime * result + ((eventSource == null) ? 0 : eventSource.hashCode());
        result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
        result = prime * result + (sent ? 1231 : 1237);
        result = prime * result + sequence;
        // dont use this ?
        // result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TestEvent other = (TestEvent) obj;
        // if (eventSource == null) {
        // if (other.eventSource != null)
        // return false;
        // } else if (!eventSource.equals(other.eventSource))
        // return false;
        if (eventType != other.eventType)
            return false;
        if (sent != other.sent)
            return false;
        if (sequence != other.sequence)
            return false;
        // If one of the timestamps is 0, we ignore timestamp comparison completely
        // This is useful for tests where we don't care about exact timing
        if (timestamp != 0 && other.timestamp != 0 && timestamp != other.timestamp) {
            long v = timestamp - other.timestamp;
            v = Math.abs(v);
            // 600ms, this can happen if we run tests concurrently and its not a big deal :)
            return v <= 600;
        }

        // now compare source!

        return true;
    }

    @Override
    public String toString() {
        return "TestEvent [eventType=" + eventType + ", sent=" + sent + ", timestamp=" + timestamp + ", eventSource=" + event
                + ", sequence=" + sequence + "]";
    }

}
