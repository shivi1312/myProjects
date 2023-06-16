package com.vcc.servicecaller;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.axis.AxisFault;
import org.apache.axis.AxisProperties;
import org.apache.log4j.Logger;

import ae.etisalat.FALCON.BSS.DOM_SCS.DA._2012._06._28.ProvisionRequestedService_xsd.Additional_info;
import ae.etisalat.FALCON.BSS.DOM_SCS.DA._2012._06._28.ProvisionRequestedService_xsd.Additional_information;
import ae.etisalat.FALCON.BSS.DOM_SCS.DA._2012._06._28.ProvisionRequestedService_xsd.ChannelInformation;
import ae.etisalat.FALCON.BSS.DOM_SCS.DA._2012._06._28.ProvisionRequestedService_xsd.MessageBody;
import ae.etisalat.FALCON.BSS.DOM_SCS.DA._2012._06._28.ProvisionRequestedService_xsd.MessageEnvelope;
import ae.etisalat.FALCON.BSS.DOM_SCS.DA._2012._06._28.ProvisionRequestedService_xsd.ProvisionRequestedServiceRequest;
import ae.etisalat.FALCON.BSS.DOM_SCS.DA._2012._06._28.ProvisionRequestedService_xsd.ProvisionRequestedServiceResponse;
import ae.etisalat.FALCON.BSS.DOM_SCS.DA._2012._06._28.ProvisionRequestedService_xsd.Request;
import ae.etisalat.FALCON.BSS.DOM_SCS.DA._2012._06._28.ProvisionRequestedService_xsd.Response;
import ae.etisalat.FALCON.BSS.DOM_SCS.DA._2012._06._28.SelfCareServiceV1_0_wsdl.SelfCareServicePortTypeBindingStub;
import ae.etisalat.www.Middleware.HLRActions.HLRActions.QueryHLREndpoint0BindingStub;
import ae.etisalat.www.Middleware.HLRActions.QueryHLRRequest_xsd.QueryHLRRequest;
import ae.etisalat.www.Middleware.HLRActions.QueryHLRRequest_xsd.QueryHLRRequestDataHeader;
import ae.etisalat.www.Middleware.HLRActions.QueryHLRResponse_xsd.QueryHLRResponse;
import ae.etisalat.www.Middleware.HLRActions.QueryHLRResponse_xsd.QueryHLRResponseResponseHeaderDetails;
import ae.etisalat.www.Middleware.INCommunicationManagement.GetAccountDetailsRequest_xsd.GetAccountDetailsRequest;
import ae.etisalat.www.Middleware.INCommunicationManagement.GetAccountDetailsRequest_xsd.GetAccountDetailsRequestDataHeader;
import ae.etisalat.www.Middleware.INCommunicationManagement.GetAccountDetailsRequest_xsd.GetAccountDetailsRequestDataHeaderAccountDetails;
import ae.etisalat.www.Middleware.INCommunicationManagement.GetAccountDetailsResponse_xsd.GetAccountDetailsResponse;
import ae.etisalat.www.Middleware.SharedResources.Common.ApplicationHeader_xsd.ApplicationHeader;
import ae.etisalat.www.Middleware.SharedResources.Common.Common_xsd.AdditionalInfo;

import com.example.xmlns._1433759742497.INCommunicationManagementEndpointBindingStub;
import com.google.gson.Gson;
import com.tibco.www.Middleware.AutoServiceFulfilment.CheckEligibilityRequest_xsd.CheckEligibilityRequest;
import com.tibco.www.Middleware.AutoServiceFulfilment.CheckEligibilityRequest_xsd.CheckEligibilityRequestDataHeader;
import com.tibco.www.Middleware.AutoServiceFulfilment.CheckEligibilityRequest_xsd.CheckEligibilityRequestDataHeaderOrderItem;
import com.tibco.www.Middleware.AutoServiceFulfilment.CheckEligibilityRequest_xsd.CheckEligibilityRequestDataHeaderOrderItemOfferID;
import com.tibco.www.Middleware.AutoServiceFulfilment.CheckEligibilityRequest_xsd.CheckEligibilityRequestDataHeaderOrderItemOrderInformation;
import com.tibco.www.Middleware.AutoServiceFulfilment.CheckEligibilityResponse_xsd.CheckEligibilityResponse;
import com.vcc.config.AppConfig;
import com.vcc.request.ProfileRequest;
import com.vcc.response.ProfileResponse;

public class RequestSender {

	final static Logger logger = Logger.getLogger(RequestSender.class);
	final static Logger errorLogger = Logger.getLogger("errorLogger");
	final static Logger crmLogger = Logger.getLogger("crmLogger");

	private static Gson gson = new Gson();
	String crmRequest = "";
	String crmResponse = "";
	static {
		System.setProperty(
				"javax.net.ssl.trustStore",
				AppConfig.config
						.getString(
								"trust_store",
								"/home/vccuser/development/apache-tomcat-7.0.30/bin/TibacoCertification/tibaco.jks"));
		System.setProperty("javax.net.ssl.trustStorePassword",
				AppConfig.config.getString("trust_password", "password"));
		System.setProperty(
				"javax.net.ssl.keyStore",
				AppConfig.config
						.getString(
								"key_store",
								"/home/vccuser/development/apache-tomcat-7.0.30/bin/TibacoCertification/tibaco.jks"));
		System.setProperty("javax.net.ssl.keyStorePassword",
				AppConfig.config.getString("key_store_password", "password"));
		logger.info("static block");
	}

	public void createOrder(ProfileRequest profileRequest,
			ProfileResponse profileResponse) {

		logger.info("CreateOrder : TransactionId ["
				+ profileRequest.getTransactionId() + "]  MethodName ["
				+ profileRequest.getMethodName() + "] PartNum ["
				+ profileRequest.getPartNum() + "] MSISDN ["
				+ profileRequest.getMsisdn() + "] prefferedLanuage ["
				+ profileRequest.getLanguage() + "] Channel ["
				+ profileRequest.getChannel() + "] ChannelType ["
				+ profileRequest.getChannelType() + "] ");
		// String urlhit =
		// "http://www.tibco.com/Middleware/AutoServiceFulfilment/";
		String urlhit = AppConfig.config
				.getString(
						"autofulfilment_url",
						"https://mwsit.etisalat.ae:9157/Etisalat/Falcon/Tibco/IIT/DOM_CC/CustomerManagement/BS_SelfCareService");
		URL url = null;

		ProvisionRequestedServiceRequest provisionRequestedServiceRequest = new ProvisionRequestedServiceRequest();

		provisionRequestedServiceRequest.setTransaction_id(profileRequest
				.getTransactionId());
		provisionRequestedServiceRequest.setPreferred_language(profileRequest
				.getLanguage());
		provisionRequestedServiceRequest.setPart_num(profileRequest
				.getPartNum());
		provisionRequestedServiceRequest.setMode_type(profileRequest
				.getModeType());
		provisionRequestedServiceRequest.setMethod_name(profileRequest
				.getMethodName()); // for add order
		// provisionRequestedServiceRequest.setMethod_name("DeleteService"); //
		// for delete order

		provisionRequestedServiceRequest.setMsisdn(profileRequest.getMsisdn());

		ChannelInformation channelInformation = new ChannelInformation();

		channelInformation.setChannel(profileRequest.getChannel());

		channelInformation.setChannelType(profileRequest.getChannelType());
		provisionRequestedServiceRequest
				.setChannelInformation(channelInformation);

		Additional_info[] additional_info = null;

		Additional_info additionalInfoBusy = null;
		Additional_info additionalInfoNoReply = null;
		Additional_info additionalInfoNotReach = null;
		Additional_info additionalInfoUnConditional = null;
		Additional_info additionalInfoDemandValue = new Additional_info();
		Additional_information additional_information = new Additional_information();
		additionalInfoDemandValue.setName(profileRequest.getDemandId());
		additionalInfoDemandValue.setValue(profileRequest.getDemandValue());
		Additional_info additionalInfoUserId = new Additional_info();
		Additional_info additionalInfoRegionId = new Additional_info();
		additionalInfoUserId.setName("user_id");
		additionalInfoUserId.setValue(AppConfig.config.getString("user_id",
				"Channels"));
		additionalInfoRegionId.setName("region_id");
		additionalInfoRegionId.setValue("1");
		if (profileRequest.getMethodName().equalsIgnoreCase("AddService")) {
			if (profileRequest.getServiceType().equalsIgnoreCase("VM")
					|| profileRequest.getServiceType().equalsIgnoreCase(
							AppConfig.config.getString("VM", "0010"))) {
				if (profileRequest.getActTrigger().equalsIgnoreCase("1")) {
					additionalInfoBusy = new Additional_info();
					additionalInfoBusy.setName("CFB");
					//additionalInfoBusy.setValue("TRUE");
					additionalInfoBusy.setValue(""+profileRequest.isActVal());
					additional_info = new Additional_info[4];
					additional_info[0] = additionalInfoDemandValue;
					additional_info[1] = additionalInfoBusy;
					additional_info[2] = additionalInfoUserId;
					additional_info[3] = additionalInfoRegionId;
					additional_information.setAdditional_info(additional_info);
					provisionRequestedServiceRequest
							.setAdditional_information(additional_information);

				} else if (profileRequest.getActTrigger().equalsIgnoreCase("2")) {
					additionalInfoNotReach = new Additional_info();
					additionalInfoNotReach.setName("CFNRC");
					//additionalInfoNotReach.setValue("TRUE");
					additionalInfoNotReach.setValue(""+profileRequest.isActVal());
					additional_info = new Additional_info[4];
					additional_info[0] = additionalInfoDemandValue;
					additional_info[1] = additionalInfoNotReach;
					additional_info[2] = additionalInfoUserId;
					additional_info[3] = additionalInfoRegionId;
					additional_information.setAdditional_info(additional_info);
					provisionRequestedServiceRequest
							.setAdditional_information(additional_information);

				} else if (profileRequest.getActTrigger().equalsIgnoreCase("3")) {
					additionalInfoNoReply = new Additional_info();
					additionalInfoNoReply.setName("CFNRY");
					//additionalInfoNoReply.setValue("TRUE");
					additionalInfoNoReply.setValue(""+profileRequest.isActVal());
					additional_info = new Additional_info[4];
					additional_info[0] = additionalInfoDemandValue;
					additional_info[1] = additionalInfoNoReply;
					additional_info[2] = additionalInfoUserId;
					additional_info[3] = additionalInfoRegionId;
					additional_information.setAdditional_info(additional_info);
					provisionRequestedServiceRequest
							.setAdditional_information(additional_information);

				} else if (profileRequest.getActTrigger().equalsIgnoreCase("4")) {
					additionalInfoBusy = new Additional_info();
					additionalInfoNoReply = new Additional_info();
					additionalInfoNotReach = new Additional_info();
					additionalInfoUnConditional = new Additional_info();
					additionalInfoBusy.setName("CFB");
					//additionalInfoBusy.setValue("TRUE");
					additionalInfoBusy.setValue(""+profileRequest.isActVal());
					additionalInfoNoReply.setName("CFNRY");
					//additionalInfoNoReply.setValue("TRUE");
					additionalInfoNoReply.setValue(""+profileRequest.isActVal());
					additionalInfoNotReach.setName("CFNRC");
					//additionalInfoNotReach.setValue("TRUE");
					additionalInfoNotReach.setValue(""+profileRequest.isActVal());
					additionalInfoUnConditional.setName("CFU");
					additionalInfoUnConditional.setValue("FALSE");
					additional_info = new Additional_info[7];
					additional_info[0] = additionalInfoDemandValue;
					additional_info[1] = additionalInfoBusy;
					additional_info[2] = additionalInfoNoReply;
					additional_info[3] = additionalInfoNotReach;
					additional_info[4] = additionalInfoUnConditional;
					additional_info[5] = additionalInfoUserId;
					additional_info[6] = additionalInfoRegionId;
					additional_information.setAdditional_info(additional_info);
					provisionRequestedServiceRequest
							.setAdditional_information(additional_information);

				}
			}
			if (profileRequest.getServiceType().equalsIgnoreCase("VN")
					|| profileRequest.getServiceType().equalsIgnoreCase(
							AppConfig.config.getString("VN", "0100"))) {
				additionalInfoNoReply = new Additional_info();
				additionalInfoNotReach = new Additional_info();
				additionalInfoNoReply.setName("CFNRY");
				additionalInfoNoReply.setValue("TRUE");
				additionalInfoNotReach.setName("CFNRC");
				additionalInfoNotReach.setValue("TRUE");
				additional_info = new Additional_info[5];
				additional_info[0] = additionalInfoDemandValue;
				additional_info[1] = additionalInfoNoReply;
				additional_info[2] = additionalInfoNotReach;
				additional_info[3] = additionalInfoUserId;
				additional_info[4] = additionalInfoRegionId;
				additional_information.setAdditional_info(additional_info);
				provisionRequestedServiceRequest
						.setAdditional_information(additional_information);
			}
		} else if (profileRequest.getMethodName().equalsIgnoreCase(
				"DeleteService")) {
			/*
			if (profileRequest.getServiceType().equalsIgnoreCase("VM")
					|| profileRequest.getServiceType().equalsIgnoreCase(
							AppConfig.config.getString("VM", "0010"))
					|| profileRequest.getServiceType().equalsIgnoreCase(
							AppConfig.config.getString("notifyme", "notifyme"))) {
				additional_info = new Additional_info[2];
				additional_info[0] = additionalInfoUserId;
				additional_info[1] = additionalInfoRegionId;
				additional_information.setAdditional_info(additional_info);
				provisionRequestedServiceRequest
						.setAdditional_information(additional_information);
			}
			if (profileRequest.getServiceType().equalsIgnoreCase("VN")
					|| profileRequest.getServiceType().equalsIgnoreCase(
							AppConfig.config.getString("VN", "0100"))) {
				additional_info = new Additional_info[2];
				additional_info[0] = additionalInfoUserId;
				additional_info[1] = additionalInfoRegionId;
				additional_information.setAdditional_info(additional_info);
				provisionRequestedServiceRequest
						.setAdditional_information(additional_information);

			}*/
			
			additional_info = new Additional_info[3];
			additional_info[0] = additionalInfoDemandValue;
			additional_info[1] = additionalInfoUserId;
			additional_info[2] = additionalInfoRegionId;
			additional_information.setAdditional_info(additional_info);
			provisionRequestedServiceRequest.setAdditional_information(additional_information);
		
		}
		MessageBody messageBody = new MessageBody();
		Request request = new Request(provisionRequestedServiceRequest);
		messageBody.setRequest(request);

		MessageEnvelope messageEnvelope = new MessageEnvelope();
		messageEnvelope.setMessageBody(messageBody);

		Response response = new Response();

		org.apache.axis.client.Service service = new org.apache.axis.client.Service();
		AxisProperties.setProperty("axis.socketSecureFactory",
				"org.apache.axis.components.net.SunFakeTrustSocketFactory");
		try {
			url = new URL(urlhit);
			logger.info("MSISDN[" + profileRequest.getMsisdn()
					+ "] TransactionId[" + profileRequest.getTransactionId()
					+ "] URL for CreateOrder[" + url + "]");
			SelfCareServicePortTypeBindingStub bindingStub = new SelfCareServicePortTypeBindingStub(
					url, service);
			bindingStub.setUsername(AppConfig.config.getString(
					"tibco_username", "vcc"));
			bindingStub.setPassword(AppConfig.config.getString(
					"tibco_password", "vcc"));
			bindingStub.setTimeout(AppConfig.config
					.getInt("stub_timeout", 1000));

			//crmRequest = gson.toJson(messageEnvelope);
			messageEnvelope = bindingStub
					.opProvisionRequestedService(messageEnvelope);

			messageBody = messageEnvelope.getMessageBody();

			response = messageBody.getResponse();

			ProvisionRequestedServiceResponse provisionRequestedServiceResponse = response
					.getProvisionRequestedServiceResponse();

			//crmResponse = gson.toJson(provisionRequestedServiceResponse);
			// Getting Response -

			profileResponse.setTransactionId(provisionRequestedServiceResponse
					.getTransaction_id());
			profileResponse.setResponseCode(provisionRequestedServiceResponse
					.getResponse_code());
			profileResponse
					.setResponseDescription(provisionRequestedServiceResponse
							.getResponse_description());
			profileResponse.setMethodName(provisionRequestedServiceResponse
					.getMethod_name());

			if (profileResponse.getResponseCode().equalsIgnoreCase("done")) {
				profileResponse.setStatus("SUCCESS");
				logger.info("Response CreateOrder : TransactionId ["
						+ profileResponse.getTransactionId() + "] MSISDN["
						+ profileRequest.getMsisdn() + "] Status ["
						+ profileResponse.getStatus() + "] ResponseCode["
						+ profileResponse.getResponseCode()
						+ "] ResponseDescription["
						+ profileResponse.getResponseDescription() + "]");
			} else {
				profileResponse.setStatus("FAIL");
				logger.info("Response CreateOrder : TransactionId ["
						+ profileResponse.getTransactionId() + "] MSISDN["
						+ profileRequest.getMsisdn() + "] Status ["
						+ profileResponse.getStatus() + "] ResponseCode ["
						+ profileResponse.getResponseCode()
						+ "] ResponseDescription ["
						+ profileResponse.getResponseDescription() + "]");
			}
		} catch (AxisFault e) {
			if (e.getCause() instanceof java.net.SocketTimeoutException) {
				errorLogger
						.error("ErrorCode ["
								+ AppConfig.config.getString(
										"errorcode_pattern", "VCC-CRMC-")
								+ "90017] MSISDN["
								+ profileRequest.getMsisdn()
								+ "] MethodName["
								+ profileRequest.getMethodName()
								+ "] TransactionId ["
								+ profileRequest.getTransactionId()
								+ "] Language ["
								+ profileRequest.getLanguage()
								+ "] DemandValue ["
								+ profileRequest.getDemandValue()
								+ "][Socket ReadTime out Exception while sending CreateOrder request to Tibaco Interface] Error["
								+ e.getMessage() + "]");
				e.printStackTrace();
				profileResponse.setStatus("SUCCESS");
			} else {
				errorLogger
						.error("ErrorCode ["
								+ AppConfig.config.getString(
										"errorcode_pattern", "VCC-CRMC-")
								+ "00003] MSISDN["
								+ profileRequest.getMsisdn()
								+ "] MethodName["
								+ profileRequest.getMethodName()
								+ "] TransactionId ["
								+ profileRequest.getTransactionId()
								+ "] Language ["
								+ profileRequest.getLanguage()
								+ "] DemandValue ["
								+ profileRequest.getDemandValue()
								+ "][AxisfaultException while sending CreateOrder request to Tibaco Interface] Error["
								+ e.getMessage() + "]");
				e.printStackTrace();
				profileResponse.setStatus("FAIL");
				// profileResponse.setStatus("SUCCESS");
			}
		} catch (Exception e) {
			errorLogger
					.error("ErrorCode ["
							+ AppConfig.config.getString("errorcode_pattern",
									"VCC-CRMC-")
							+ "00004] MSISDN["
							+ profileRequest.getMsisdn()
							+ "] MethodName["
							+ profileRequest.getMethodName()
							+ "] TransactionId ["
							+ profileRequest.getTransactionId()
							+ "] Language ["
							+ profileRequest.getLanguage()
							+ "] DemandValue ["
							+ profileRequest.getDemandValue()
							+ "] [Exception while sending CreateOrder request to Tibaco Interface] Error["
							+ e.getMessage() + "]");
			e.printStackTrace();
			profileResponse.setStatus("FAIL");
			// profileResponse.setStatus("SUCCESS");
		}

	//	crmLogger.info("{req:" + crmRequest + ",resp:" + crmResponse + "}");
	}

	public void checkEligibility(ProfileRequest profileRequest,
			ProfileResponse profileResponse) {

		logger.info("CheckEligibility : TransactionID ["
				+ profileRequest.getTransactionId() + "] MSISDN ["
				+ profileRequest.getMsisdn() + "] ActionType ["
				+ profileRequest.getActionType() + "] Language ["
				+ profileRequest.getLanguage() + "]");

		// String urlhit =
		// "http://www.tibco.com/Middleware/AutoServiceFulfilment/";
		String urlhit = AppConfig.config
				.getString(
						"autofulfilment_url",
						"https://mwsit.etisalat.ae:9157/Etisalat/Falcon/Tibco/IIT/DOM_CC/CustomerManagement/BS_SelfCareService");
		URL url = null;

		CheckEligibilityRequest checkEligibilityRequest = new CheckEligibilityRequest();

		ApplicationHeader applicationHeader = new ApplicationHeader();
		// applicationHeader.setRequestedDate(profileRequest.getRequestedDate());
		applicationHeader.setRequestedSystem(profileRequest
				.getRequestedSystem());
		applicationHeader.setRetryLimit(profileRequest.getRetryLimit());
		applicationHeader.setTransactionID(profileRequest.getTransactionId());
		
		
		
		logger.info("applicationHeader======"+applicationHeader.getRequestedDate());

		CheckEligibilityRequestDataHeader dataHeader = new CheckEligibilityRequestDataHeader();

		dataHeader.setChannel(profileRequest.getChannel());

		dataHeader.setSubChannel(profileRequest.getSubChannel());
		dataHeader.setLanguage(profileRequest.getLanguage());
		dataHeader.setReceivedTime(profileRequest.getReceivedTime());

		CheckEligibilityRequestDataHeaderOrderItem[] dataHeaderOrderArray = new CheckEligibilityRequestDataHeaderOrderItem[1];

		CheckEligibilityRequestDataHeaderOrderItem dataHeaderOrderItem = new CheckEligibilityRequestDataHeaderOrderItem();
		dataHeaderOrderItem.setAccountNumber(profileRequest.getMsisdn());
		dataHeaderOrderItem.setActionType(profileRequest.getActionType());
		dataHeaderOrderItem.setParentOrderNumber(Integer
				.parseInt(profileRequest.getParentOrderNumber()));
		dataHeaderOrderItem.setOrderItemNumber(Integer.parseInt(profileRequest
				.getOrderItemNumber()));

		dataHeaderOrderArray[0] = dataHeaderOrderItem;
		dataHeader.setOrderItem(dataHeaderOrderArray);

		CheckEligibilityRequestDataHeaderOrderItemOfferID orderItemOfferID = new CheckEligibilityRequestDataHeaderOrderItemOfferID();
		orderItemOfferID.setName(profileRequest.getDemandId());
		orderItemOfferID.setValue(profileRequest.getDemandValue());

		CheckEligibilityRequestDataHeaderOrderItemOfferID[] dataHeaderOrderItemOfferID = new CheckEligibilityRequestDataHeaderOrderItemOfferID[1];
		dataHeaderOrderItemOfferID[0] = orderItemOfferID;
		dataHeaderOrderItem.setOfferID(dataHeaderOrderItemOfferID);

		CheckEligibilityRequestDataHeaderOrderItemOrderInformation orderInformation = new CheckEligibilityRequestDataHeaderOrderItemOrderInformation();
		orderInformation.setAttributeName("user_id");
		orderInformation.setAttributeValue(AppConfig.config.getString(
				"user_id", "Channels"));

		CheckEligibilityRequestDataHeaderOrderItemOrderInformation[] itemOrderInformations = new CheckEligibilityRequestDataHeaderOrderItemOrderInformation[1];
		itemOrderInformations[0] = orderInformation;
		dataHeaderOrderItem.setOrderInformation(itemOrderInformations);

		checkEligibilityRequest.setApplicationHeader(applicationHeader);
		checkEligibilityRequest.setDataHeader(dataHeader);

		org.apache.axis.client.Service service = new org.apache.axis.client.Service();
		/*
		 * AxisProperties.setProperty("axis.socketSecureFactory",
		 * "org.apache.axis.components.net.SunFakeTrustSocketFactory");
		 */
		try {
			url = new URL(urlhit);
			logger.info("MSISDN[" + profileRequest.getMsisdn()
					+ "] TransactionId[" + profileRequest.getTransactionId()
					+ "] URL for CheckEligibility[" + url + "]");
			SelfCareServicePortTypeBindingStub bindingStub = new SelfCareServicePortTypeBindingStub(
					url, service);
			bindingStub.setUsername(AppConfig.config.getString(
					"tibco_username", "vcc"));
			bindingStub.setPassword(AppConfig.config.getString(
					"tibco_password", "vcc"));
			bindingStub.setTimeout(AppConfig.config
					.getInt("stub_timeout", 1000));
			CheckEligibilityResponse checkEligibilityResponse = bindingStub
					.opCheckEligibility(checkEligibilityRequest);

			//crmRequest = gson.toJson(checkEligibilityRequest);
			//crmResponse = gson.toJson(checkEligibilityResponse);
			// Getting Response -

			profileResponse.setStatus(checkEligibilityResponse.getAckMessage()
					.getStatus().getValue());
			profileResponse.setResponseCode(checkEligibilityResponse
					.getResponseData().getResponseCode());
			if (checkEligibilityResponse.getAckMessage().getErrorCode() != null) {
				profileResponse.setErrorCode(checkEligibilityResponse
						.getAckMessage().getErrorCode());
				profileResponse.setErrorType(checkEligibilityResponse
						.getAckMessage().getErrorType());
				profileResponse.setErrorDescription(checkEligibilityResponse
						.getAckMessage().getErrorDescription());
			}
			profileResponse.setAccountNumber(checkEligibilityResponse
					.getResponseData().getOrderItemResponse(0)
					.getAccountNumber());

			logger.info("Response from CheckEligibility. TransactionID ["
					+ profileRequest.getTransactionId() + "] MSISDN["
					+ profileRequest.getMsisdn() + "] Status ["
					+ checkEligibilityResponse.getAckMessage().getStatus()
					+ "] ResponseCode[" + profileResponse.getResponseCode()
					+ "] ResponseDescription["
					+ profileResponse.getResponseDescription() + "]");

		} catch (AxisFault e) {
			errorLogger
					.error("ErrorCode ["
							+ AppConfig.config.getString("errorcode_pattern",
									"VCC-CRMC-")
							+ "00003] MSISDN["
							+ profileRequest.getMsisdn()
							+ "] MethodName["
							+ profileRequest.getMethodName()
							+ "] TransactionId ["
							+ profileRequest.getTransactionId()
							+ "] Language ["
							+ profileRequest.getLanguage()
							+ "] DemandValue ["
							+ profileRequest.getDemandValue()
							+ "][Axisfault Exception while sending CheckEligibility request to Tibaco Interface] Error["
							+ e.getMessage() + "] ");
			e.printStackTrace();
			profileResponse.setStatus("FAIL");
			// profileResponse.setStatus("SUCCESS");

		} catch (Exception e) {
			errorLogger
					.error("ErrorCode ["
							+ AppConfig.config.getString("errorcode_pattern",
									"VCC-CRMC-")
							+ "00005] MSISDN["
							+ profileRequest.getMsisdn()
							+ "] MethodName["
							+ profileRequest.getMethodName()
							+ "] TransactionId ["
							+ profileRequest.getTransactionId()
							+ "] Language ["
							+ profileRequest.getLanguage()
							+ "] DemandValue ["
							+ profileRequest.getDemandValue()
							+ "] [Exception while sending CheckEligibility request to Tibaco Interface] Error["
							+ e.getMessage() + "] ");
			e.printStackTrace();
			profileResponse.setStatus("FAIL");
			// profileResponse.setStatus("SUCCESS");
		}
		//crmLogger.info("{req:" + crmRequest + ",resp:" + crmResponse + "}");
	}

	public void getAccountDetail(ProfileRequest profileRequest,
			ProfileResponse profileResponse) {

		logger.info("getAccountDetail : TransactionID ["
				+ profileRequest.getTransactionId() + "] MSISDN ["
				+ profileRequest.getMsisdn() + "] ActionType ["
				+ profileRequest.getActionType() + "] ModeType ["
				+ profileRequest.getModeType() + "] NoofAccount["
				+ profileRequest.getNoOfAccounts() + "]");

		org.apache.axis.client.Service service = new org.apache.axis.client.Service();
		AxisProperties.setProperty("axis.socketSecureFactory",
				"org.apache.axis.components.net.SunFakeTrustSocketFactory");

		String urlhit = AppConfig.config
				.getString("incomm_url",
						"https://mwsit.etisalat.ae:9135/Middleware/INCommunicationManagement");
		URL url = null;
		int serviceClassCurrent = 100;

		GetAccountDetailsRequest accountDetailsRequest = new GetAccountDetailsRequest();

		ApplicationHeader applicationHeader = new ApplicationHeader();
		// applicationHeader.setRequestedDate(profileRequest.getRequestedDate());
		applicationHeader.setRequestedSystem(profileRequest
				.getRequestedSystem());
		applicationHeader.setRetryLimit(profileRequest.getRetryLimit());
		applicationHeader.setTransactionID(profileRequest.getTransactionId());

		GetAccountDetailsRequestDataHeader accountDetailsRequestDataHeader = new GetAccountDetailsRequestDataHeader();
		GetAccountDetailsRequestDataHeaderAccountDetails[] accountDetailsArray = new GetAccountDetailsRequestDataHeaderAccountDetails[1];

		GetAccountDetailsRequestDataHeaderAccountDetails accountDetails = new GetAccountDetailsRequestDataHeaderAccountDetails();
		accountDetails.setAccountNo(profileRequest.getMsisdn());

		accountDetailsArray[0] = accountDetails;

		accountDetailsRequestDataHeader.setNoOfAccount(profileRequest
				.getNoOfAccounts());
		accountDetailsRequestDataHeader.setActionType(profileRequest
				.getActionType());
		accountDetailsRequestDataHeader.setModeType(profileRequest
				.getModeType());
		accountDetailsRequestDataHeader.setAccountDetails(accountDetailsArray);

		accountDetailsRequest.setApplicationHeader(applicationHeader);
		accountDetailsRequest.setDataHeader(accountDetailsRequestDataHeader);

		try {
			url = new URL(urlhit);

			logger.info("MSISDN[" + profileRequest.getMsisdn()
					+ "] TransactionId[" + profileRequest.getTransactionId()
					+ "] URL for getAccountDetails[" + url + "]");

			INCommunicationManagementEndpointBindingStub bindingStub = new INCommunicationManagementEndpointBindingStub(
					url, service);
			bindingStub.setTimeout(AppConfig.config
					.getInt("stub_timeout", 1000));
			bindingStub.setUsername(AppConfig.config.getString(
					"tibco_username", "vcc"));
			bindingStub.setPassword(AppConfig.config.getString(
					"tibco_password", "vcc"));

			GetAccountDetailsResponse accountDetailsResponse = bindingStub
					.opGetAccountDetails(accountDetailsRequest);
			//crmRequest = gson.toJson(accountDetailsRequest);
			//crmResponse = gson.toJson(accountDetailsResponse);
			
			
			profileResponse.setTransactionId(accountDetailsResponse
					.getResponseHeader().getTransactionID());
			profileResponse.setStatus(accountDetailsResponse.getAckMessage()
					.getStatus().getValue());

			serviceClassCurrent = Integer.parseInt(accountDetailsResponse
					.getResponseHeader().getBalanceAndDate()
					.getServiceClassCurrent());

			String postpaid[] = AppConfig.config
					.getStringArray("service_class_range_postpaid");
			String prepaid[] = AppConfig.config
					.getStringArray("service_class_range_prepaid");

			logger.info("Prepaid Range [" + Arrays.toString(prepaid)
					+ "] PostPaid Range [" + Arrays.toString(postpaid)
					+ "] CurrentServiceClass [" + serviceClassCurrent + "]");

			if (postpaid.length == 2
					&& serviceClassCurrent >= Integer.parseInt(postpaid[0])
					&& serviceClassCurrent < Integer.parseInt(postpaid[1])) {
				profileResponse.setSubType(AppConfig.config.getString(
						"sub_type_postpaid", "O"));
				/*
				 * logger.info("Response from getAccountDetail : TransactionId ["
				 * + profileResponse.getTransactionId() + "] MSISDN[" +
				 * profileRequest.getMsisdn() + "] SubType[" +
				 * profileResponse.getSubType() + "]");
				 */
			} else if (prepaid.length >= 1) {
				for (String number : prepaid) {
					if (String.valueOf(serviceClassCurrent).startsWith(number)) {
						profileResponse.setSubType(AppConfig.config.getString(
								"sub_type_prepaid", "P"));
						break;
					} else {
						profileResponse.setSubType(AppConfig.config.getString(
								"sub_type_default", "N"));
					}
				}
				/*
				 * logger.info("Response from getAccountDetail : TransactionId ["
				 * + profileResponse.getTransactionId() + "] MSISDN[" +
				 * profileRequest.getMsisdn() + "] SubType[" +
				 * profileResponse.getSubType() + "]");
				 */
			} else
				profileResponse.setSubType(AppConfig.config.getString(
						"sub_type_default", "N"));

			logger.info("Response from getAccountDetail : TransactionId ["
					+ profileResponse.getTransactionId() + "] MSISDN["
					+ profileRequest.getMsisdn() + "] SubType["
					+ profileResponse.getSubType() + "]");
		} catch (NumberFormatException nfe) {
			errorLogger
					.error("ErrorCode ["
							+ AppConfig.config.getString("errorcode_pattern",
									"VCC-CRMC-")
							+ "90004] MSISDN["
							+ profileRequest.getMsisdn()
							+ "]  TransactionId ["
							+ profileRequest.getTransactionId()
							+ "] [NumberFormatException while getting subtype from Tibaco Interface] Error["
							+ nfe.getMessage() + "]");
			nfe.printStackTrace();
			profileResponse.setStatus("FAIL");
		} catch (AxisFault e) {
			errorLogger
					.error("ErrorCode ["
							+ AppConfig.config.getString("errorcode_pattern",
									"VCC-CRMC-")
							+ "00003] MSISDN["
							+ profileRequest.getMsisdn()
							+ "] MethodName["
							+ profileRequest.getMethodName()
							+ "] TransactionId ["
							+ profileRequest.getTransactionId()
							+ "] Language ["
							+ profileRequest.getLanguage()
							+ "] DemandValue ["
							+ profileRequest.getDemandValue()
							+ "][Axisfault Exception while sending getAccountDetails request to Tibaco Interface] Error["
							+ e.getMessage() + "]");
			e.printStackTrace();
			profileResponse.setStatus("FAIL");

		} catch (Exception e) {
			errorLogger
					.error("ErrorCode ["
							+ AppConfig.config.getString("errorcode_pattern",
									"VCC-CRMC-")
							+ "00006] MSISDN["
							+ profileRequest.getMsisdn()
							+ "] MethodName["
							+ profileRequest.getMethodName()
							+ "] TransactionId ["
							+ profileRequest.getTransactionId()
							+ "] Language ["
							+ profileRequest.getLanguage()
							+ "] DemandValue ["
							+ profileRequest.getDemandValue()
							+ "][Exception while sending getAccountDetails request to Tibaco Interface] Error["
							+ e.getMessage() + "]");
			e.printStackTrace();
			profileResponse.setStatus("FAIL");
		}
		//crmLogger.info("{req:" + crmRequest + ",resp:" + crmResponse + "}");
	}

	public void getActiveTriggers(ProfileRequest profileRequest,
			ProfileResponse profileResponse) {

		logger.info("Inside getActiveTRiggers. TransactionID ["
				+ profileRequest.getTransactionId() + "] MSISDN ["
				+ profileRequest.getMsisdn() + "] Action ["
				+ profileRequest.getAction() + "] Operation ["
				+ profileRequest.getOperation() + "]");
		org.apache.axis.client.Service service = new org.apache.axis.client.Service();
		AxisProperties.setProperty("axis.socketSecureFactory",
				"org.apache.axis.components.net.SunFakeTrustSocketFactory");

		// String urlhit =
		// "http://www.tibco.com/Middleware/AutoServiceFulfilment/";
		String urlhit = AppConfig.config
				.getString("queryhlr_url",
						"https://dx519:9201/Services/HLRActions-HTTPS.serviceagent/QueryHLREndpoint0");
		URL url = null;

		QueryHLRRequest queryHLRRequest = new QueryHLRRequest();

		ApplicationHeader applicationHeader = new ApplicationHeader();
		applicationHeader.setRequestedDate(profileRequest.getRequestedDate());
		applicationHeader.setRequestedSystem(profileRequest
				.getRequestedSystem());
		applicationHeader.setRetryLimit(profileRequest.getRetryLimit());
		applicationHeader.setTransactionID(profileRequest.getTransactionId());

		QueryHLRRequestDataHeader dataHeader = new QueryHLRRequestDataHeader();
		dataHeader.setMSISDN(profileRequest.getMsisdn());
		dataHeader.setIMSI(profileRequest.getIMSI());
		dataHeader.setAction(profileRequest.getAction());
		dataHeader.setOperation(profileRequest.getOperation());

		queryHLRRequest.setApplicationHeader(applicationHeader);
		queryHLRRequest.setDataHeader(dataHeader);

		try {
			url = new URL(urlhit);
			logger.info("URL in getActiveTriggers [" + url + "]");
			QueryHLREndpoint0BindingStub stub = new QueryHLREndpoint0BindingStub(
					url, service);
			stub.setTimeout(AppConfig.config.getInt("stub_timeout", 1000));
			stub.setUsername(AppConfig.config
					.getString("tibco_username", "vcc"));
			stub.setPassword(AppConfig.config
					.getString("tibco_password", "vcc"));

			QueryHLRResponse hlrResponse = stub.queryHLR(queryHLRRequest);

			profileResponse.setAccountNumber(hlrResponse.getResponseHeader()
					.getMSISDN());
			profileResponse.setStatus(hlrResponse.getAckMessage().getStatus()
					.toString());
			QueryHLRResponseResponseHeaderDetails header[] = hlrResponse
					.getResponseHeader().getDetails();

			HashMap<String, String> activeTriggerMap = new HashMap<String, String>();
			for (QueryHLRResponseResponseHeaderDetails queryHLRResponseResponseHeaderDetails : header) {

				if (queryHLRResponseResponseHeaderDetails.getID()
						.equalsIgnoreCase(
								AppConfig.config.getString("activeTrigger_id",
										"Call Forward"))) {
					AdditionalInfo additionalInfo[] = queryHLRResponseResponseHeaderDetails
							.getAdditionalInfo();
					for (AdditionalInfo additionalInfo2 : additionalInfo) {
						activeTriggerMap.put(additionalInfo2.getName(),
								additionalInfo2.getValue());
					}
				}
			}

			profileResponse.setactiveTrigger(activeTriggerMap);

			logger.info("Response from QueryHLR for ActiveTriggers : MSISDN ["
					+ profileResponse.getAccountNumber() + "] Status ["
					+ profileResponse.getStatus() + "] IMSI ["
					+ hlrResponse.getResponseHeader().getIMSI() + "]");
		} catch (AxisFault e) {

			logger.info("Exception in Sending Request. " + e);
			e.printStackTrace();
			profileResponse.setStatus("FAIL");
			// profileResponse.setStatus("SUCCESS");

		} catch (Exception e) {
			logger.info("Exception in Sending Request. " + e);
			e.printStackTrace();
			profileResponse.setStatus("FAIL");
			// profileResponse.setStatus("SUCCESS");
		}
	}

	public void updateProfile(ProfileRequest profileRequest,
			ProfileResponse profileResponse) {
		logger.info("Inside updateProfile. TransactionID ["
				+ profileRequest.getTransactionId() + "] MSISDN ["
				+ profileRequest.getMsisdn() + "]");

	}

	public void notifyMeOptInOptOut(ProfileRequest profileRequest,
			ProfileResponse profileResponse) {

		logger.info("Noitify Me : TransactionId ["
				+ profileRequest.getTransactionId() + "]  MethodName ["
				+ profileRequest.getMethodName() + "] PartNum ["
				+ profileRequest.getPartNum() + "] MSISDN ["
				+ profileRequest.getMsisdn() + "] DemandId ["+profileRequest.getDemandValue()+"] prefferedLanuage ["
				+ profileRequest.getLanguage() + "] Channel ["
				+ profileRequest.getChannel() + "] ChannelType ["
				+ profileRequest.getChannelType() + "] ");
		// String urlhit =
		// "http://www.tibco.com/Middleware/AutoServiceFulfilment/";
		String urlhit = AppConfig.config
				.getString(
						"autofulfilment_url",
						"https://mwsit.etisalat.ae:9157/Etisalat/Falcon/Tibco/IIT/DOM_CC/CustomerManagement/BS_SelfCareService");
		URL url = null;

		ProvisionRequestedServiceRequest provisionRequestedServiceRequest = new ProvisionRequestedServiceRequest();

		provisionRequestedServiceRequest.setTransaction_id(profileRequest
				.getTransactionId());
		provisionRequestedServiceRequest.setPreferred_language(profileRequest
				.getLanguage());
		provisionRequestedServiceRequest.setPart_num(profileRequest
				.getPartNum());
		provisionRequestedServiceRequest.setMode_type(profileRequest
				.getModeType());
		provisionRequestedServiceRequest.setMethod_name(profileRequest
				.getMethodName()); 

		provisionRequestedServiceRequest.setMsisdn(profileRequest.getMsisdn());

		ChannelInformation channelInformation = new ChannelInformation();
		channelInformation.setChannel(profileRequest.getChannel());
		channelInformation.setChannelType(profileRequest.getChannelType());
		provisionRequestedServiceRequest
				.setChannelInformation(channelInformation);

		Additional_info[] additional_info = null;

		Additional_info additionalInfoDemandValue = new Additional_info();
		Additional_information additional_information = new Additional_information();
		additionalInfoDemandValue.setName(profileRequest.getDemandId());
		additionalInfoDemandValue.setValue(profileRequest.getDemandValue());
		Additional_info additionalInfoUserId = new Additional_info();
		Additional_info additionalInfoRegionId = new Additional_info();
		additionalInfoUserId.setName("user_id");
		additionalInfoUserId.setValue(AppConfig.config.getString("user_id",
				"Channels"));
		additionalInfoRegionId.setName("region_id");
		additionalInfoRegionId.setValue("1");

		additional_info = new Additional_info[3];
		additional_info[0] = additionalInfoDemandValue;
		additional_info[1] = additionalInfoUserId;
		additional_info[2] = additionalInfoRegionId;
		additional_information.setAdditional_info(additional_info);
		provisionRequestedServiceRequest
				.setAdditional_information(additional_information);

		MessageBody messageBody = new MessageBody();
		Request request = new Request(provisionRequestedServiceRequest);
		messageBody.setRequest(request);

		MessageEnvelope messageEnvelope = new MessageEnvelope();
		messageEnvelope.setMessageBody(messageBody);

		Response response = new Response();

		org.apache.axis.client.Service service = new org.apache.axis.client.Service();
		AxisProperties.setProperty("axis.socketSecureFactory",
				"org.apache.axis.components.net.SunFakeTrustSocketFactory");
		try {
			url = new URL(urlhit);
			logger.info("MSISDN[" + profileRequest.getMsisdn()
					+ "] TransactionId[" + profileRequest.getTransactionId()
					+ "] URL for NotifyMe[" + url + "]");
			SelfCareServicePortTypeBindingStub bindingStub = new SelfCareServicePortTypeBindingStub(
					url, service);
			bindingStub.setUsername(AppConfig.config.getString(
					"tibco_username", "vcc"));
			bindingStub.setPassword(AppConfig.config.getString(
					"tibco_password", "vcc"));
			bindingStub.setTimeout(AppConfig.config
					.getInt("stub_timeout", 1000));

			messageEnvelope = bindingStub
					.opProvisionRequestedService(messageEnvelope);
			messageBody = messageEnvelope.getMessageBody();

			response = messageBody.getResponse();

			ProvisionRequestedServiceResponse provisionRequestedServiceResponse = response
					.getProvisionRequestedServiceResponse();

			profileResponse.setTransactionId(provisionRequestedServiceResponse
					.getTransaction_id());
			profileResponse.setResponseCode(provisionRequestedServiceResponse
					.getResponse_code());
			profileResponse
					.setResponseDescription(provisionRequestedServiceResponse
							.getResponse_description());
			profileResponse.setMethodName(provisionRequestedServiceResponse
					.getMethod_name());

			if (profileResponse.getResponseCode().equalsIgnoreCase("done")) {
				profileResponse.setStatus("SUCCESS");
				logger.info("Response NotifyMe : TransactionId ["
						+ profileResponse.getTransactionId() + "] MSISDN["
						+ profileRequest.getMsisdn() + "] Status ["
						+ profileResponse.getStatus() + "] ResponseCode["
						+ profileResponse.getResponseCode()
						+ "] ResponseDescription["
						+ profileResponse.getResponseDescription() + "]");
			} else {
				
				profileResponse.setStatus("FAIL");
				logger.info("Response NotifyMe : TransactionId ["
						+ profileResponse.getTransactionId() + "] MSISDN["
						+ profileRequest.getMsisdn() + "] Status ["
						+ profileResponse.getStatus() + "] ResponseCode ["
						+ profileResponse.getResponseCode()
						+ "] ResponseDescription ["
						+ profileResponse.getResponseDescription() + "]");
			}
		} catch (Exception e) {
			logger.info("Exception while sending notifyme request" + e);
			e.printStackTrace();
			profileResponse.setStatus("FAIL");
		}
		//crmLogger.info("{req:" + crmRequest + ",resp:" + crmResponse + "}");
	}
}