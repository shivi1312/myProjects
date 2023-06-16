package com.vcc.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.vcc.cache.VccCache;
import com.vcc.model.VccTemplateModel;
import com.vcc.util.AppContext;


public class VccLbsTemplates {
	final static Logger logger = Logger.getLogger(VccLbsTemplates.class);
	
	DataSource dataSource = null; 
	
	public static HashMap<String, String> map = new HashMap<String, String>();
	private Gson gson = new Gson();
	private JsonParser parser = new JsonParser();

	
	public  VccLbsTemplates() {

		logger.info("VccLbsTemplates bean is created ");

	}

	public String getLbsMessage(String key) 
	{
		dataSource=(DataSource)AppContext.getContext().getBean("dataSource");
		if (VccCache.map.containsKey("lbs_template")) 
		{	
			return parser.parse(VccCache.map.get("lbs_template")).getAsJsonObject().get(key).getAsString();
		}
		else
		{
			this.getTemplateList();
			return parser.parse(VccCache.map.get("lbs_template")).getAsJsonObject().get(key).getAsString();
		}
	}

	public void getTemplateList() 
	{
		try
		{
			dataSource=(DataSource)AppContext.getContext().getBean("dataSource");

			String query = "select TEMPLATE_ID,TEMPLATE_MESSAGE,LANGUAGE_ID from LBS_TEMPLATES";
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			
			logger.info("jdbctemplate  iss  >>>>>>>>>>>	["+jdbcTemplate+"]");
			
			
			jdbcTemplate.query(query, new RowMapper<VccTemplateModel>() 
					{

						@Override
						public VccTemplateModel mapRow(ResultSet rs, int rownumber)throws SQLException 
						{
							String key = "";
							key = rs.getInt("TEMPLATE_ID") + "-"+ rs.getInt("LANGUAGE_ID");

							map.put(key, rs.getString("TEMPLATE_MESSAGE"));
							return null;
						}
					});
			VccCache.map.put("lbs_template", gson.toJson(map), 10,TimeUnit.HOURS);
			
		} 
		catch (Exception e) 
		{
			logger.error("Exception inside getTemplateList()" + e.getMessage());
			e.printStackTrace();
		}
	}
}
