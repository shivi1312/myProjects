package com.vcc.updatebean;

public class OrderItem {

	private String AccountNumber;
	private String Status;
	private String SubRequestId;
	private OfferID OfferID;
	private OrderInformation OrderInformation;
	
	public String getAccountNumber() {
		return AccountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		AccountNumber = accountNumber;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getSubRequestId() {
		return SubRequestId;
	}
	public void setSubRequestId(String subRequestId) {
		SubRequestId = subRequestId;
	}
	public OfferID getOfferID() {
		return OfferID;
	}
	public void setOfferID(OfferID offerID) {
		OfferID = offerID;
	}
	public OrderInformation getOrderInformation() {
		return OrderInformation;
	}
	public void setOrderInformation(OrderInformation orderInformation) {
		OrderInformation = orderInformation;
	}
	
	
	
}
