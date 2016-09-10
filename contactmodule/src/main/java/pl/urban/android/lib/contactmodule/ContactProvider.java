package pl.urban.android.lib.contactmodule;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactProvider {
    private static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone._ID,
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
            while (contactsCursor.moveToNext()) {
                result.add(mapContactFromCursor(contactsCursor));
            }

            return result;
        }
    }

    public void deleteContact(@NonNull final Contact contact) {
        final Uri contactLookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(contact.getNumber()));
        try (final Cursor lookupCursor = quickQuery(contactLookupUri)) {
            if (lookupCursor == null || lookupCursor.getCount() == 0) {
                return;
            }

            while (lookupCursor.moveToNext()) {
                if (lookupCursor.getString(lookupCursor.getColumnIndex(
                        ContactsContract.PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(contact.getName())) {
                    final String lookupKey = lookupCursor.getString(
                            lookupCursor.getColumnIndex(ContactsContract.PhoneLookup.LOOKUP_KEY));
                    final Uri removeUri = Uri.withAppendedPath(
                            ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                    mContentResolver.delete(removeUri, null, null);

                    return;
                }
            }
        }
    }

    public boolean doesContactExists(int contactId) {
        final Uri contactUri = Uri.withAppendedPath(
                ContactsContract.RawContacts.CONTENT_URI, String.valueOf(contactId));
        try (final Cursor contactCursor = mContentResolver.query(contactUri,
                new String[]{ContactsContract.RawContacts._ID}, null, null, null)) {
            return !(contactCursor == null || contactCursor.getCount() == 0);
        }
    }

    public boolean doesContactExists(@NonNull final Contact contact) {
        final Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(contact.getNumber()));
        try (final Cursor lookupCursor = mContentResolver.query(lookupUri,
                new String[]{ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.LABEL},
                null, null, null)) {
            if (lookupCursor == null || lookupCursor.getCount() == 0) {
                return false;
            }

            while (lookupCursor.moveToNext()) {
                final String contactName = lookupCursor.getString(
                        lookupCursor.getColumnIndex(ContactsContract.PhoneLookup.LABEL));
                if (contact.getName().equalsIgnoreCase(contactName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Integer addContact(@NonNull final Contact contact) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        // Phone Number
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getNumber())
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, "1").build());

        // Display name/Contact name
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName())
                .build());

        ContentProviderResult[] results = mContentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
        if (results.length > 0 && results[0].uri != null) {
            return Integer.parseInt(results[0].uri.getLastPathSegment());
        }
        return null;
    }

    private Cursor quickQuery(@NonNull final Uri contentUri) {
        return mContentResolver.query(contentUri, null, null, null, null);
    }

    private Contact mapContactFromCursor(@NonNull final Cursor cursor) {
        return new Contact(
                cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)),
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
        );
    }
}
