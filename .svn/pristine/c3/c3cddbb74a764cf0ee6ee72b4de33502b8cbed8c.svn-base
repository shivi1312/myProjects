package com.vcc.cache;

import com.telemune.vcc.expiringmap.ExpirationListener;

public class VccListener implements ExpirationListener<String, String> {
	@Override
	public void expired(String key, String value) {
		System.out.println("Expiring key: "+key+" value "+value);
	}
}
