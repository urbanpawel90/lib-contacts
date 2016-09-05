package pl.urban.android.lib.contactmodule;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

public class GroupProvider {
    private static final String[] PROJECTION = {ContactsContract.Groups.TITLE};
    private static final String SELECTION_BY_ID = ContactsContract.Groups._ID + "=?";

    private final ContentResolver mContentResolver;

    public GroupProvider(@NonNull final ContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    /**
     * @param groupName Nazwa grupy do dodania
     * @return ID utworzonej grupy
     */
    public String createGroup(@NonNull String groupName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Groups.TITLE, groupName);
        Uri insertedUri = mContentResolver.insert(ContactsContract.Groups.CONTENT_URI, contentValues);
        if (insertedUri == null) {
            return null;
        }
        return insertedUri.getLastPathSegment();
    }

    public boolean isExistingGroup(String groupId) {
        try (Cursor groupCursor = mContentResolver.query(ContactsContract.Groups.CONTENT_URI,
                PROJECTION, SELECTION_BY_ID,
                new String[]{groupId}, null)) {
            return groupCursor != null && groupCursor.getCount() > 0;
        }
    }

    public boolean deleteGroup(String groupId) {
        return mContentResolver.delete(
                Uri.withAppendedPath(ContactsContract.Groups.CONTENT_URI, groupId),
                null, null) > 0;
    }

    public boolean assignContactToGroup(String contactId, String groupId) {
        ContentValues values = new ContentValues();
        values.put(ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID,
                Integer.parseInt(contactId));
        values.put(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,
                Integer.parseInt(groupId));
        values.put(ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE,
                ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE);
        return mContentResolver.insert(ContactsContract.Data.CONTENT_URI, values) != null;
    }
}