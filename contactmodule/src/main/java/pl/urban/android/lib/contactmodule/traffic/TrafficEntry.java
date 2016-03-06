package pl.urban.android.lib.contactmodule.traffic;

public class TrafficEntry {
    private Integer mUID;
    private String mPackage;
    private Long mRxBytes;
    private Long mTxBytes;

    public TrafficEntry(final Integer UID, final String aPackage) {
        mUID = UID;
        mPackage = aPackage;
    }

    public Integer getUID() {
        return mUID;
    }

    public void setUID(Integer UID) {
        mUID = UID;
    }

    public String getPackage() {
        return mPackage;
    }

    public void setPackage(String aPackage) {
        mPackage = aPackage;
    }

    public Long getRxBytes() {
        return mRxBytes;
    }

    public void setRxBytes(Long rxBytes) {
        mRxBytes = rxBytes;
    }

    public Long getTxBytes() {
        return mTxBytes;
    }

    public void setTxBytes(Long txBytes) {
        mTxBytes = txBytes;
    }
}
