package com.vcc.bean;

public class ResponseData {

	private String transactionId;
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String toString() 
	{
        return " TransactionId ["+transactionId+"]";
	}
}
