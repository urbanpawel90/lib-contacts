package pl.urban.android.lib.contactmodule.traffic;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TrafficProvider {
    private final PackageManager mPackageManager;

    public TrafficProvider(@NonNull final PackageManager packageManager) {
        mPackageManager = packageManager;
    }

    public List<TrafficEntry> getTrafficStats(final int count) {
        final List<ApplicationInfo> apps = mPackageManager.getInstalledApplications(
                PackageManager.GET_META_DATA);

        List<TrafficEntry> trafficStats = new ArrayList<>();

        for (final ApplicationInfo app : apps) {
            final TrafficEntry traffic = new TrafficEntry(app.uid, app.packageName);

            traffic.setRxBytes(TrafficStats.getUidRxBytes(traffic.getUID()));
            traffic.setTxBytes(TrafficStats.getUidTxBytes(traffic.getUID()));

            trafficStats.add(traffic);
        }

        Collections.sort(trafficStats, new Comparator<TrafficEntry>() {
            @Override
            public int compare(final TrafficEntry lhs, final TrafficEntry rhs) {
                return Long.valueOf(rhs.getTxBytes() + rhs.getRxBytes())
                        .compareTo(lhs.getTxBytes() + lhs.getRxBytes());
            }
        });

        trafficStats = trafficStats.subList(0, count < trafficStats.size() ? count : trafficStats.size());

        return trafficStats;
    }
}
