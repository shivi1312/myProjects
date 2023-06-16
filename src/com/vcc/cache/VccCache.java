package com.vcc.cache;

import java.util.concurrent.TimeUnit;

import com.telemune.vcc.expiringmap.ExpirationPolicy;
import com.telemune.vcc.expiringmap.ExpiringMap;

public class VccCache {
		public static ExpiringMap<String, String> map;
		
		static {
			map = ExpiringMap.builder().expirationListener(new VccListener())
					.variableExpiration().expiration(5, TimeUnit.HOURS).build();
		}

		public static boolean exist(String key) {
			return map.containsKey(key);
		}

		public static void put(String key, String value, int expiryTime) {
			map.put(key, value, ExpirationPolicy.ACCESSED, expiryTime,
					TimeUnit.HOURS);
		}

		public static String get(String key) {
			return map.get(key);
		}
		
		public static void remove(String key){
			map.remove(key);
		}
	


}
