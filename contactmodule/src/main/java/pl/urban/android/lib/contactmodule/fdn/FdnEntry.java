package pl.urban.android.lib.contactmodule.fdn;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import pl.urban.android.lib.contactmodule.Contact;

public class FdnEntry extends Contact {
    private String mPin2;

    public FdnEntry(@Nullable Integer id, @NonNull final String name, @NonNull final String number) {
        super(id, name, number);
    }

    public FdnEntry(@Nullable Integer id, @NonNull final String name, @NonNull final String number,
                    @NonNull final String pin2) {
        super(id, name, number);
        mPin2 = pin2;
    }

    public String getPin2() {
        return mPin2;
    }
}
