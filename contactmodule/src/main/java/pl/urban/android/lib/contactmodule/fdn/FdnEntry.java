package pl.urban.android.lib.contactmodule.fdn;

import android.support.annotation.NonNull;

import pl.urban.android.lib.contactmodule.Contact;

public class FdnEntry extends Contact {
    private String mPin2;

    public FdnEntry(@NonNull final String name, @NonNull final String number) {
        super(name, number);
    }

    public FdnEntry(@NonNull final String name, @NonNull final String number, @NonNull final String pin2) {
        super(name, number);
        mPin2 = pin2;
    }

    public String getPin2() {
        return mPin2;
    }
}
