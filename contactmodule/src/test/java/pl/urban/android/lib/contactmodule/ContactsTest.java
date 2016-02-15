package pl.urban.android.lib.contactmodule;

import org.junit.Before;
import org.junit.Test;

import android.test.mock.MockContentResolver;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ContactsTest {

    private ContactProvider mContactProvider;

    @Before
    public void setUp() throws Exception {
        mContactProvider = new ContactProvider(new MockContentResolver());
    }

    @Test
    public void testReturnsNonNullContactsList() throws Exception {
        List<Contact> contacts = mContactProvider.getContactsList();
        assertNotNull(contacts);
    }

    @Test
    public void testCanListContactsWithSpecifiedSuffix() throws Exception {
        String testSuffix = "p";
        List<Contact> contacts = mContactProvider.getContactsList(testSuffix);
        assertNotNull(contacts);
        for (final Contact contact : contacts) {
            assertNotNull(contact);
            assertTrue(contact.getName().startsWith(testSuffix.toUpperCase()) || contact.getName().startsWith(testSuffix.toLowerCase()));
        }
    }
}