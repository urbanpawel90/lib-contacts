package pl.urban.android.lib.contactmodule;

import android.support.annotation.NonNull;

public class Contact {
    private final String mName;
    private final String mNumber;

    public Contact(@NonNull final String name, @NonNull final String number) {
        mName = name;
        mNumber = number;
    }

    public String getName() {
        return mName;
    }

    public String getNumber() {
        return mNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;

        Contact contact = (Contact) o;

        if (!mName.equals(contact.mName)) return false;
        return mNumber.equals(contact.mNumber);

    }

    @Override
    public int hashCode() {
        int result = mName.hashCode();
        result = 31 * result + mNumber.hashCode();
        return result;
    }
}