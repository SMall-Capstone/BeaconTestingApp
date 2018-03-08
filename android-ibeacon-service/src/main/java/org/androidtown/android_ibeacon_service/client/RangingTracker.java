package org.androidtown.android_ibeacon_service.client;

//import org.androidtown.android_ibeacon_service.client.ibeacon.IBeacon;

import org.androidtown.android_ibeacon_service.IBeacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RangingTracker {
	private Map<IBeacon,RangedIBeacon> rangedIBeacons = new HashMap<IBeacon,RangedIBeacon>();
	public void addIBeacon(IBeacon iBeacon) {
		if (rangedIBeacons.containsKey(iBeacon)) {
			rangedIBeacons.get(iBeacon).addRangeMeasurement(iBeacon.getRssi());
		}
		else {
			rangedIBeacons.put(iBeacon, new RangedIBeacon(iBeacon));
		}
	}
	public synchronized Collection<IBeacon> getIBeacons() {
		ArrayList<IBeacon> iBeacons = new ArrayList<IBeacon>();
		Iterator<RangedIBeacon> iterator = rangedIBeacons.values().iterator();
		while (iterator.hasNext()) {
			RangedIBeacon rangedIBeacon = iterator.next();
			if (!rangedIBeacon.allMeasurementsExpired()) {
				iBeacons.add(rangedIBeacon);				
			}
		}
		return iBeacons;
	}
	

}
