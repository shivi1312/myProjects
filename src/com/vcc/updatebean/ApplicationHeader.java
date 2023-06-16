package com.vcc.updatebean;

public class ApplicationHeader {
	
	private String TransactionID;
	private String RequestedSystem;
	private String RetryLimit;
	private String RequestedDate;
	
	public String getTransactionID() {
		return TransactionID;
	}
	public void setTransactionID(String transactionID) {
		TransactionID = transactionID;
	}
	public String getRequestedSystem() {
		return RequestedSystem;
	}
	public void setRequestedSystem(String requestedSystem) {
		RequestedSystem = requestedSystem;
	}
	public String getRetryLimit() {
		return RetryLimit;
	}
	public void setRetryLimit(String retryLimit) {
		RetryLimit = retryLimit;
	}
	public String getRequestedDate() {
		return RequestedDate;
	}
	public void setRequestedDate(String requestedDate) {
		RequestedDate = requestedDate;
	}
}
