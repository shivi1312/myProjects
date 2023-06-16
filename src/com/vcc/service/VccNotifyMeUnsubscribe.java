/**This class handles the Deactivation request of NotifyMe service and
 * send request to Mobelium for deactivation
 * @author Abhishek Rana
 */
package com.vcc.service;

import java.net.URL;

import org.apache.axis.AxisProperties;
import org.apache.log4j.Logger;

//import com.telemune.vcc.service.MCAUnsubscriptionRequest;
//import com.telemune.vcc.service.MCAUnsubscriptionResponse;
//import com.telemune.vcc.service.NotifyCallerSoapBindingStub;
import com.vcc.bean.NotifyMeRequest;
import com.vcc.bean.NotifyMeResponse;
import com.vcc.config.AppConfig;
import com.vcc.domain.VccGmatMsgStore;
import com.vcc.util.AppContext;

public class VccNotifyMeUnsubscribe implements VccNotifyMeUnsub {

	VccGmatMsgStore vccGmatMsgStore = (VccGmatMsgStore) AppContext.getContext()
			.getBean("vccGmatMsgStore");

	private static Logger logger = Logger
			.getLogger(VccNotifyMeUnsubscribe.class);

	//private NotifyCallerSoapBindingStub stub = null;

	String MSISDN;
	String tid;
	String lang;

	@Override
	public String unSubscibe(String MSISDN, String tid, String lang) {
		logger.info("Inside Unsubscribe method of NotifyMe service [" + MSISDN
				+ "]");
		this.MSISDN = MSISDN;
		this.tid = tid;
		this.lang = lang;
		String status = "";
		if (this.MSISDN != null && this.tid != null) {
			if (this.lang == null || this.lang.equals("")) {
				this.lang = "EN";
			}
			if (this.MSISDN.charAt(0) == '0') {
				this.MSISDN = this.MSISDN.substring(1);
			}
			if (!this.MSISDN.startsWith(AppConfig.config
					.getString("country_code"))) {
				// logger.info("Adding country code "+AppConfig.config.getString("country_code"));
				this.MSISDN = AppConfig.config.getString("country_code")
						+ this.MSISDN;
			}
			logger.info("MSISDN[" + this.MSISDN + "] TransactionID[" + this.tid
					+ "] language[" + this.lang + "]");

			NotifyMeRequest notifyMeRequest = new NotifyMeRequest();
			notifyMeRequest.setMsisdn(this.MSISDN);
			if (this.lang.equalsIgnoreCase("EN")) {
				notifyMeRequest.setLang(1);
			} else if (this.lang.equalsIgnoreCase("AR")) {
				notifyMeRequest.setLang(2);
			} else {
				notifyMeRequest.setLang(1);
			}

			NotifyMeResponse response = sendDeActivateNotifyMeReq(notifyMeRequest);
			status = response.toString();
		} else {
			logger.info("Unable to unsubscribe MSISDN [" + this.MSISDN
					+ "] TransactionID [" + this.tid + "]");
			status = "NotifyMeResponse [ msisdn=" + this.MSISDN
					+ ", Status= FAIL]";
		}
		return status;
	}

	/**
	 * This method is used to send Deactivation request of NotifyMe service to
	 * Mobelium.
	 * 
	 * @param notifyMeRequest
	 *            Contains the information of msisdn and language
	 */
	public NotifyMeResponse sendDeActivateNotifyMeReq(
			NotifyMeRequest notifyMeRequest) {/*
		logger.info("Inside sendDeActivateNotifyMeReq method ["
				+ AppConfig.config.getString("notify.me.act.dact.url") + "]");
		AxisProperties.setProperty("axis.socketSecureFactory",
				"org.apache.axis.components.net.SunFakeTrustSocketFactory");
		String response = "";
		NotifyMeResponse notifyMeResponse = null;
		try {
			this.stub = new NotifyCallerSoapBindingStub(
					new URL(
							AppConfig.config
									.getString("notify.me.act.dact.url",
											"https://115.112.137.229:8443/mca_service/services/NotifyCallerPort")),
					new org.apache.axis.client.Service());

			MCAUnsubscriptionRequest mcaUnSubscriptionRequest = new MCAUnsubscriptionRequest();
			mcaUnSubscriptionRequest.setMSISDN(notifyMeRequest.getMsisdn());
			mcaUnSubscriptionRequest.setServiceType(AppConfig.config.getString(
					"notify.me.service.type", "00001"));
			MCAUnsubscriptionResponse mcaUnSubscriptionResponse = this.stub
					.MCAUnsubscription(mcaUnSubscriptionRequest);
			logger.info("["
					+ notifyMeRequest.getMsisdn()
					+ "] Url: ["
					+ AppConfig.config
							.getString("notify.me.act.dact.url",
									"https://115.112.137.229:8443/mca_service/services/NotifyCallerPort")
					+ "]");
			response = mcaUnSubscriptionResponse.getResponseMessage();
			String tempId = null;
			logger.info("Response : " + response);
			response = response.substring(response.lastIndexOf(":") + 1);

			if (response.indexOf("DELETED") != -1) {
				tempId = AppConfig.config.getString("notifyUnSubTempId", "1");
				String key = tempId + "-" + notifyMeRequest.getLang();
				boolean result = vccGmatMsgStore.insertIntoGmatMsg(
						notifyMeRequest, key);
				logger.info("Result :" + result);

				notifyMeResponse = new NotifyMeResponse();
				notifyMeResponse.setMsisdn(notifyMeRequest.getMsisdn());
				notifyMeResponse.setStatus("Unsubscribed Successfully");
			} else if (response.indexOf("FAILED") != -1) {
				tempId = AppConfig.config.getString("notifyUnSubFailTempId",
						"2");
				String key = tempId + "-" + notifyMeRequest.getLang();
				boolean result = vccGmatMsgStore.insertIntoGmatMsg(
						notifyMeRequest, key);
				logger.info("Result :" + result);

				notifyMeResponse = new NotifyMeResponse();
				notifyMeResponse.setMsisdn(notifyMeRequest.getMsisdn());
				notifyMeResponse.setStatus("Already Unsubscribed");
			} else {
				logger.info("Unsubscription request is fail");

				notifyMeResponse = new NotifyMeResponse();
				notifyMeResponse.setMsisdn(notifyMeRequest.getMsisdn());
				notifyMeResponse.setStatus(response);
			}

			logger.info("["
					+ notifyMeRequest.getMsisdn()
					+ "] Service Type ["
					+ AppConfig.config.getString("notify.me.service.type",
							"00001") + "] Response [" + response + "]");
			logger.info("["
					+ notifyMeRequest.getMsisdn()
					+ "] Cos ["
					+ AppConfig.config.getString("notify.me.cos", "1")
					+ "] Lang ["
					+ AppConfig.config.getString(
							"lang." + notifyMeRequest.getLang(), "EN") + "]");
			logger.info("["
					+ notifyMeRequest.getMsisdn()
					+ "] Service Type ["
					+ AppConfig.config.getString("notify.me.service.type",
							"00001") + "] Response [" + response + "]");

		} catch (Exception e) {
			logger.error("[" + notifyMeRequest.getMsisdn()
					+ "] Exception in sending unsubscription request "
					+ e.getMessage());
			e.printStackTrace();
		}
		return notifyMeResponse;
	*/ return null;}
}