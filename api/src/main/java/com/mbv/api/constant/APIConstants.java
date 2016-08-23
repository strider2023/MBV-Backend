package com.mbv.api.constant;

import java.text.SimpleDateFormat;

public class APIConstants {

	public static final String AUTH_TOKEN_KEY = "token";

	public static final String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
	
	public static final String AUTH_TOKEN_COOKIE = "AUTH-TOKEN";

	public static final int SESSION_VALIDITY = 365*24*60*60;
	
	public static final String REQUEST_ID = "rId";

	public static final String APP_DATE_FORMAT = "dd-MMM-yyyy hh:mm:ss";

	public static final int MAX_IMPRESSIONS_HIGHER_LIMIT = 511;

	public static final int LOWER_LIMIT = 1;

	public static final int MINUTE_MAX_TIME_UNIT_LIMIT = 120;

	public static final int MONTH_MAX_TIME_UNIT_LIMIT = 6;

	public static final int WEEK_MAX_TIME_UNIT_LIMIT = 26;

	public static final int HOUR_MAX_TIME_UNIT_LIMIT = 48;

	public static final int DAY_MAX_TIME_UNIT_LIMIT = 14;

	public static final String ERROR = "ERROR";

	public static final String MAX_IMPRESSIONS = "Max Impressions";

	public static final String MAX_IMPRESSIONS_ERROR_MESSAGE = "Invalid Max Impression value";

	public static final String MINUTE_TIME_UNIT = "Minute Time Unit";

	public static final String MINUTE_TIME_UNIT_ERROR_MESSAGE = "Invalid TimeUnit value for Minute";

	public static final String MONTH_TIME_UNIT = "Month Time Unit";

	public static final String MONTH_TIME_UNIT_ERROR_MESSAGE = "Invalid TimeUnit value for Month";

	public static final String WEEK_TIME_UNIT = "Week Time Unit";

	public static final String WEEK_TIME_UNIT_ERROR_MESSAGE = "Invalid TimeUnit value for Week";

	public static final String HOUR_TIME_UNIT = "Hour Time Unit";

	public static final String HOUR_TIME_UNIT_ERROR_MESSAGE = "Invalid TimeUnit value for Hour";

	public static final String DAY_TIME_UNIT = "Day Time Unit";

	public static final String DAY_TIME_UNIT_ERROR_MESSAGE = "Invalid TimeUnit value for Day";

	public static final String LIFETIME_TIME_UNIT = "LifeTime Time Unit";

	public static final String LIFETIME_TIME_UNIT_ERROR_MESSAGE = "Invalid TimeUnit value for LifeTime";

	public static final String TIME_RANGE = "Time Range";

	public static final String DUPLICATE_TIME_RANGES_ERROR_MESSAGE = "Duplicate Time Range values found";

	// Parent
	public static final Long DEVICE_LEVEL = 0L;

	public static final SimpleDateFormat MM_DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy");
}
