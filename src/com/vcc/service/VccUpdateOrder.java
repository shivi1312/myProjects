package com.vcc.service;

import com.vcc.updatebean.ApplicationHeader;
import com.vcc.bean.UpdateResponseData;
import com.vcc.updatebean.DataHeader;
public interface VccUpdateOrder {

	public UpdateResponseData updateOrder(ApplicationHeader applicationHeader,
			DataHeader dataHeader);
}
