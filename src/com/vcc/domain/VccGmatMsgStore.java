package com.vcc.domain;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.vcc.bean.NotifyMeRequest;
import com.vcc.config.AppConfig;
import com.vcc.util.AppContext;

public class VccGmatMsgStore {
	final static Logger logger = Logger.getLogger(VccGmatMsgStore.class);

	DataSource dataSource = null;

	VccLbsTemplates vccLbsTemp = null;

	public VccGmatMsgStore() {

		logger.info("VccGmatMsgStore bean is created ");

	}

	public boolean insertIntoGmatMsg(NotifyMeRequest vnRequest, String key) 
	{
		dataSource = (DataSource) AppContext.getContext().getBean("dataSource");
		vccLbsTemp = (VccLbsTemplates) AppContext.getContext().getBean("vccLbsTemplates");
		try 
		{
			String senderId = AppConfig.config.getString("NA_SMS_SENDER_ID");
			logger.debug("Inside insert into gmat_message_store sender id ["+ senderId + "] key [" + key + "]");

			String tempMessage = "Error";
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

			logger.info("jdbcTemplate is [" + jdbcTemplate + "]");

			tempMessage = vccLbsTemp.getLbsMessage(key);

			String query = "insert into GMAT_MESSAGE_STORE(ORIGINATING_NUMBER,DESTINATION_NUMBER,MESSAGE_TEXT,SUBMIT_TIME,STATUS,MESSAGE_TYPE,DESTINATION_PORT,LANGUAGE_ID) values (?,?,?,now(),'R',1,0,?)";
			logger.info(String.format("[%s] [%s]  query [%s]",
					vnRequest.getMsisdn(), tempMessage, query));

			int count = jdbcTemplate.update(query, new Object[] { senderId,
					vnRequest.getMsisdn(), tempMessage, vnRequest.getLang() });
			if (count > 0)
				return true;
			else
				return false;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception while save logs: " + e.getMessage());
			return false;
		}
	}
}
