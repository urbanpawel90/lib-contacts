package pl.urban.android.lib.contactmodule;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactProvider {
    private static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };
    private static final String SUFFIX_SELECTION = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ?";

    private final ContentResolver mContentResolver;

    public ContactProvider(@NonNull final ContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    public List<Contact> getContactsList() {
        return getContactsList("");
    }

    public List<Contact> getContactsList(@NonNull final String testSuffix) {
        try (final Cursor contactsCursor = mContentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION,
                SUFFIX_SELECTION, new String[]{"%" + testSuffix}, null)) {
            if (contactsCursor == null || contactsCursor.getCount() == 0) {
                return Collections.emptyList();
            }

            final List<Contact> result = new ArrayList<>(contactsCursor.getCount());
            contactsCursor.moveToFirst();
            do {
                result.add(mapContactFromCursor(contactsCursor));
            } while (contactsCursor.moveToNext());
            
            return result;
        }
    }

    public void deleteContact(@NonNull final Contact contact) {

    }

    public boolean doesContactExists(@NonNull final Contact contact) {
        return false;
    }

    public void addContact(@NonNull final Contact contact) {

    }

    private Contact mapContactFromCursor(@NonNull final Cursor cursor) {
        return new Contact(
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
        );
    }
}
