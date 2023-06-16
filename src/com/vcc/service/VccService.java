package com.vcc.service;


//import org.apache.log4j.Logger;

public class VccService
{
	//private static Logger logger = Logger.getLogger(VccService.class);

    public String unSubscribeNotifyMe(String msisdn, String transactionId, String language)
    {
    	VccNotifyMeUnsub notifyUnsub = new VccNotifyMeUnsubscribe();
    	String result = notifyUnsub.unSubscibe(msisdn, transactionId, language);
     	return result;
    }

	public String unSubscribeVoiceNote(String msisdn, String transactionId, String language) 
    {

    	VccVnUnsub vnUnsub = new VccVnUnsubscribe();
    	String result = vnUnsub.unSubscibe(msisdn, transactionId, language);
     	return result;

    }
    public String subscribeVoiceMail(String msisdn, String transactionId, String userProfile, String language, String subType)
    {

    	VccVmSubUnsub vmSub =  new VccVmSubscribeUnsubscribe();
    	String result = vmSub.subscibe(msisdn, transactionId,userProfile,language,subType);
     	return result;

    }
    public String unSubscribeVoiceMail(String msisdn, String transactionId, String language)
    {
    	VccVmSubUnsub vmUnsub =  new VccVmSubscribeUnsubscribe();
    	String result = vmUnsub.unSubscibe(msisdn, transactionId,language);

    	return result;

    }
   
}
