package org.androidtown.beacontest2;

/**
 * Created by hscom-013 on 2018-02-27.
 */

public interface RssiFilter {
    double applyFilter(double rssi);
}
