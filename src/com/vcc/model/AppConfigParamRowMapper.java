package com.vcc.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.vcc.model.AppConfigParam;

public class AppConfigParamRowMapper  implements RowMapper<AppConfigParam> {
	@Override
	public AppConfigParam mapRow(ResultSet rs, int rownumber) throws SQLException {
		AppConfigParam param = new AppConfigParam();
		param.setParamName(rs.getString("PARAM_NAME"));
		param.setParamValue(rs.getString("PARAM_VALUE"));
		return param;
	};
}
