/**
 * This class is responsible to update status of VoiceMail and VoiceNote Subscription.
 */
package com.vcc.service;

import org.apache.log4j.Logger;

import com.vcc.bean.AckMessage;
import com.vcc.bean.ResponseData;
import com.vcc.bean.UpdateResponseData;
import com.vcc.config.AppConfig;
import com.vcc.updatebean.AdditionalInfo;
import com.vcc.updatebean.ApplicationHeader;
import com.vcc.updatebean.DataHeader;

public class VccUpdateOrderStatus implements VccUpdateOrder {

	private static Logger logger = Logger.getLogger(VccUpdateOrderStatus.class);
	String transactionId;
	String actionType;
	String msisdn;
	String language;
	String channel;
	String status;
	String requestedDate;
	AdditionalInfo AdditionalInfo;

	ResponseData response = null;
	AckMessage ackMessage = null;
	UpdateResponseData updateResponseData = null;

	@Override
	public UpdateResponseData updateOrder(ApplicationHeader applicationHeader,
			DataHeader dataHeader) {

		logger.info("Inside UpdateOrderStatus Method.");

		this.transactionId = applicationHeader.getTransactionID();
		// this.actionType = actionType;
		this.msisdn = dataHeader.getOrderItem().getAccountNumber();
		this.status = dataHeader.getOrderItem().getStatus();
		this.requestedDate = applicationHeader.getRequestedDate();
		this.AdditionalInfo = dataHeader.getAdditionalInfo();
		UpdateResponseData result = null;
		if (this.transactionId != null && this.msisdn != null
				&& this.status != null) {

			this.msisdn = AppConfig.config.getString("country_code", "971")
					+ msisdn.substring(this.msisdn.length()
							- AppConfig.config.getInt("msisdn_length", 9));

			logger.info("Going to Update Status for MSISDN [" + this.msisdn
					+ "] TransactionId [" + this.transactionId + "] Status ["
					+ this.status + "] ActionType [" + this.actionType
					+ "] RequestedDate [" + this.requestedDate + "]");

			/*
			 * UpdateStatusRequestBean requestBean = new
			 * UpdateStatusRequestBean();
			 * 
			 * requestBean.setTid(this.transactionId);
			 * requestBean.setActionType(this.actionType);
			 * requestBean.setMsisdn(this.msisdn);
			 * requestBean.setLang(this.language);
			 * requestBean.setStatus(this.status);
			 * 
			 * String json = new Gson().toJson(requestBean);
			 * 
			 * requestToRuleEngine(json);
			 */
			response = new ResponseData();
			response.setTransactionId(this.transactionId);

			ackMessage = new AckMessage();
			ackMessage.setStatus("SUCCESS");

			updateResponseData = new UpdateResponseData();
			updateResponseData.setResponseData(response);
			updateResponseData.setAckMessage(ackMessage);

			result = updateResponseData;

		} else {

			logger.info("Some Parameters are missing. MSISDN [" + this.msisdn
					+ "] TransactionId [" + this.transactionId + "] Language ["
					+ this.language + "] ActionType [" + this.actionType + "]");

			response = new ResponseData();
			response.setTransactionId(this.transactionId);

			ackMessage = new AckMessage();
			ackMessage.setStatus("FAIL");
			ackMessage.setErrorCode("TIB-002");
			ackMessage.setErrorDescription("Input Validation Error");

			updateResponseData = new UpdateResponseData();
			updateResponseData.setResponseData(response);
			updateResponseData.setAckMessage(ackMessage);

			result = updateResponseData;
		}
		return result;
	}

	/*
	 * public String requestToRuleEngine(String json) { //
	 * logger.info("Going to request Rule Engine"); Connection socketConnection
	 * = null; String response = null; edu.emory.mathcs.util.net.ConnectionPool
	 * connectionPool = null; Socket client = null; OutputStream outToServer =
	 * null; DataOutputStream out = null; InputStream inFromServer = null;
	 * DataInputStream in = null; String result = ""; try { connectionPool =
	 * TcpPool.getRuleEngineConPool(); socketConnection =
	 * connectionPool.getConnection();
	 * 
	 * if (connectionPool != null && socketConnection != null) {
	 * logger.info("Rule Engine Server has connected!\n");
	 * logger.info("Rule Engine Sending string: '" + json + "'\n"); client =
	 * socketConnection.getSocket(); outToServer = client.getOutputStream(); out
	 * = new DataOutputStream(outToServer); out.writeUTF(json.toString());
	 * out.flush(); inFromServer = client.getInputStream(); in = new
	 * DataInputStream(inFromServer); response = in.readUTF();
	 * socketConnection.returnToPool();
	 * 
	 * if (response != null) { logger.info(" Rule Engine response is: " +
	 * response); ResponseBean responseBean = new Gson().fromJson(response,
	 * ResponseBean.class); result = "Result : " + responseBean.getResult() +
	 * " , Message :" + responseBean.getMsg(); } else {
	 * logger.error("Rule engine response is : " + response); } } else {
	 * logger.info("Rule is not connected so can't be perform any opearation ");
	 * } } catch (Exception e) { socketConnection.close(); e.printStackTrace();
	 * 
	 * } finally { try { if (out != null) out.close(); if (in != null)
	 * in.close();
	 * 
	 * } catch (Exception e) {
	 * logger.error("Error in closing socket connection [" + e + "]"); } }
	 * return result; }
	 */
}
