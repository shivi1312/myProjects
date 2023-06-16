package com.vcc.bean;

public class OrderItem {

	private String ActionType;
	private String AccountNumber;
	private String ParentOrderNumber;
	private String OrderItemNumber;
	private OfferID OfferID;
	private OrderInformation OrderInformation;
	public String getActionType() {
		return ActionType;
	}
	public void setActionType(String actionType) {
		ActionType = actionType;
	}
	public String getAccountNumber() {
		return AccountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		AccountNumber = accountNumber;
	}
	public String getParentOrderNumber() {
		return ParentOrderNumber;
	}
	public void setParentOrderNumber(String parentOrderNumber) {
		ParentOrderNumber = parentOrderNumber;
	}
	public String getOrderItemNumber() {
		return OrderItemNumber;
	}
	public void setOrderItemNumber(String orderItemNumber) {
		OrderItemNumber = orderItemNumber;
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
