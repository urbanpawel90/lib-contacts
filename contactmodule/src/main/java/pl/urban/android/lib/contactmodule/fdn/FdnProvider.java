package pl.urban.android.lib.contactmodule.fdn;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FdnProvider {
    private static final Uri FDN_URI = Uri.parse("content://icc/fdn");
    private static final String[] PROJECTION = new String[]{
            Columns.ID, Columns.NAME, Columns.NUMBER
    };

    private static final String DELETE_WHERE = Columns.TAG + " = ? AND " +
            Columns.NUMBER + " = ? AND " +
            Columns.PIN2 + " = ?";

    private final ContentResolver mContentResolver;

    public FdnProvider(@NonNull final ContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    public Integer insertFdn(@NonNull final FdnEntry entry) throws InvalidParameterException {
        if (entry.getPin2() == null || entry.getPin2().trim().isEmpty()) {
            throw new InvalidParameterException("pin2 == null");
        }

        final ContentValues insertValues = new ContentValues();
        insertValues.put(Columns.TAG, entry.getName());
        insertValues.put(Columns.NUMBER, entry.getNumber());
        insertValues.put(Columns.PIN2, entry.getPin2());

        final Uri rowUri = mContentResolver.insert(FDN_URI, insertValues);
        return rowUri != null ? Integer.parseInt(rowUri.getLastPathSegment()) : null;
    }

    public boolean deleteFdn(@NonNull final FdnEntry entry) throws InvalidParameterException {
        if (entry.getPin2() == null || entry.getPin2().trim().isEmpty()) {
            throw new InvalidParameterException("pin2 == null");
        }

        final List<FdnEntry> fdns = getFdnList();
        if (fdns == null) return false;

        boolean found = false;
        for (final FdnEntry fdn : fdns) {
            if (fdn.equals(entry)) {
                found = true;
                break;
            }
        }

        if (!found) return false;

        final int deletedRows = mContentResolver.delete(FDN_URI, DELETE_WHERE, new String[]{
                entry.getName(),
                entry.getNumber(),
                entry.getPin2()
        });

        return deletedRows > 0;
    }

    @Nullable
    public List<FdnEntry> getFdnList() {
        try (final Cursor cursor = mContentResolver.query(FDN_URI, PROJECTION, null, null, null)) {
            if (cursor == null) return Collections.emptyList();
            final List<FdnEntry> resultList = new ArrayList<>(cursor.getCount());

            while (cursor.moveToNext()) {
                resultList.add(mapEntryFromCursor(cursor));
            }

            return resultList;
        }
    }

    private FdnEntry mapEntryFromCursor(@NonNull final Cursor cursor) {
        return new FdnEntry(cursor.getInt(cursor.getColumnIndex(Columns.ID)),
                cursor.getString(cursor.getColumnIndex(Columns.NAME)),
                cursor.getString(cursor.getColumnIndex(Columns.NUMBER)));
    }

    public static final class Columns {
        public static final String NAME = "name";
        public static final String TAG = "tag";
        public static final String NUMBER = "number";
        public static final String PIN2 = "pin2";
        public static final String ID = "_id";

        private Columns() {
        }
    }
}
