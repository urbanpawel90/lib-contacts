package pl.urban.android.lib.contactmodule;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Contact {
    private final Integer mId;
    private final String mName;
    private final String mNumber;

    public Contact(@Nullable Integer id, @NonNull final String name, @NonNull final String number) {
        mId = id;
        mName = name;
        mNumber = number;
    }

    public Integer getId() {
        return mId;
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