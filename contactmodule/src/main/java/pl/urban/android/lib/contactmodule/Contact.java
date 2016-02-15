package pl.urban.android.lib.contactmodule;

public class Contact {
    private final String mName;
    private final String mNumber;

    public Contact(final String name, final String number) {
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