package pl.urban.android.lib.contactmodule.call;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CallsProvider {
    private final ContentResolver mContentResolver;

    public CallsProvider(@NonNull final ContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    public List<CallEntry> getCallList() {
        return getCallList(0);
    }

    public List<CallEntry> getCallList(final long since) throws SecurityException {
        try (final Cursor cursor = mContentResolver.query(CallLog.Calls.CONTENT_URI, null,
                CallLog.Calls.DATE + " >= ?", new String[]{Long.toString(since)}, null)) {
            if (cursor == null || cursor.getCount() == 0) return Collections.emptyList();

            final List<CallEntry> calls = new ArrayList<>(cursor.getCount());

            while (cursor.moveToNext()) {
                calls.add(mapCallFromCursor(cursor));
            }

            return calls;
        }
    }

    public int deleteAllCalls() throws SecurityException {
        return mContentResolver.delete(CallLog.Calls.CONTENT_URI, null, null);
    }

    private CallEntry mapCallFromCursor(final Cursor cursor) {
        final CallEntry call = new CallEntry();
        call.setNumber(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
        call.setDuration(cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION)));
        call.setDate(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
        call.setType(cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)));
        return call;
    }
}
