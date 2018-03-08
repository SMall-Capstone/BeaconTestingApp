package org.androidtown.beacontest2;

/**
 * Created by 이예지 on 2018-03-07.
 */

public class BeaconInfo {
    private String name,major,minor;
    private int filteredRSSIvalue;
    private boolean isFirst;
    private double maxRSSI,minRSSI;
    private int location_x,location_y;

    public BeaconInfo(){

    }

    public BeaconInfo(String name, String major, String minor) {
        this.name = name;
        this.major = major;
        this.minor = minor;
        this.isFirst = true;
        this.maxRSSI=-100;
        this.minRSSI=0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public int getFilteredRSSIvalue() {
        return filteredRSSIvalue;
    }

    public void setFilteredRSSIvalue(int filteredRSSIvalue) {
        this.filteredRSSIvalue = filteredRSSIvalue;
    }

    public boolean getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public double getMaxRSSI() {
        return maxRSSI;
    }

    public void setMaxRSSI(double maxRSSI) {
        this.maxRSSI = maxRSSI;
    }

    public double getMinRSSI() {
        return minRSSI;
    }

    public void setMinRSSI(double minRSSI) {
        this.minRSSI = minRSSI;
    }

    public int getLocation_x() {
        return location_x;
    }

    public void setLocation_x(int location_x) {
        this.location_x = location_x;
    }

    public int getLocation_y() {
        return location_y;
    }

    public void setLocation_y(int location_y) {
        this.location_y = location_y;
    }
}
