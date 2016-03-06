package pl.urban.android.lib.contactmodule.call;

import android.provider.CallLog;
import android.support.annotation.StringDef;

import java.text.DateFormat;
import java.util.Date;

public class CallEntry {
    public static final String TYPE_INCOMING = "incoming";
    public static final String TYPE_OUTGOING = "outgoing";
    public static final String TYPE_MISSED = "missed";
    public static final String TYPE_UNKNOWN = "unknown";

    @StringDef({TYPE_INCOMING, TYPE_OUTGOING, TYPE_MISSED, TYPE_UNKNOWN})
    public @interface CallType {
    }

    @CallType
    private static String getTypeFromId(final int id) {
        switch (id) {
            case CallLog.Calls.INCOMING_TYPE:
                return TYPE_INCOMING;
            case CallLog.Calls.OUTGOING_TYPE:
                return TYPE_OUTGOING;
            case CallLog.Calls.MISSED_TYPE:
                return TYPE_MISSED;
            default:
                return TYPE_UNKNOWN;
        }
    }

    private String mNumber;
    private int mDuration;
    private String mDate;
    private String mType;

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = DateFormat.getDateTimeInstance().format(new Date(date));
    }

    public void setDate(String date) {
        mDate = date;
    }

    @CallType
    public String getType() {
        return mType;
    }

    public void setType(int typeId) {
        setType(getTypeFromId(typeId));
    }

    public void setType(@CallType String type) {
        mType = type;
    }
}
