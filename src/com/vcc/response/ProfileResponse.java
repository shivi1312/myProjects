package com.vcc.response;

import java.util.HashMap;

public class ProfileResponse {
	
	private String transactionId;
	private String accountNumber;
	private String status;
	private String errorCode;
	private String errorType;
	private String errorDescription;
	private String methodName;
	private String IMSI;
	private String subType;
	private String responseCode;
	private String responseDescription;
	private String orderItemNumber;
	private String orderNumber;
	private String demandId;
	private String demandValue;
	private HashMap<String,String> activeTrigger;
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorType() {
		return errorType;
	}
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getIMSI() {
		return IMSI;
	}
	public void setIMSI(String iMSI) {
		IMSI = iMSI;
	}
	
	public String getSubType() {
		return subType;
	}
	public void setSubType(String subType) {
		this.subType = subType;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseDescription() {
		return responseDescription;
	}
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}
	public String getOrderItemNumber() {
		return orderItemNumber;
	}
	public void setOrderItemNumber(String orderItemNumber) {
		this.orderItemNumber = orderItemNumber;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getDemandId() {
		return demandId;
	}
	public void setDemandId(String demandId) {
		this.demandId = demandId;
	}
	public String getDemandValue() {
		return demandValue;
	}
	public void setDemandValue(String demandValue) {
		this.demandValue = demandValue;
	}
	public HashMap<String, String> getactiveTrigger() {
		return activeTrigger;
	}
	public void setactiveTrigger(HashMap<String, String> activeTrigger) {
		this.activeTrigger = activeTrigger;
	}
}
