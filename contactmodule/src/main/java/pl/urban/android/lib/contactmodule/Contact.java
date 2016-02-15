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
}