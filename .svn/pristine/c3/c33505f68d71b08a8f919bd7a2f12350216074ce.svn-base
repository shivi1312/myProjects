package com.vcc.service;


import com.vcc.bean.UpdateResponseData;
import com.vcc.updatebean.ApplicationHeader;
import com.vcc.updatebean.DataHeader;


public class VccUpdateService {
	
	public UpdateResponseData updateOrderStatus(ApplicationHeader applicationHeader,DataHeader dataHeader) {
		VccUpdateOrder vccUpdate = new VccUpdateOrderStatus();
		UpdateResponseData result = vccUpdate.updateOrder(applicationHeader,dataHeader);
		
		return result;
	}
}
