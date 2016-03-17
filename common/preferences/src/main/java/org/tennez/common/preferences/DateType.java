package org.tennez.common.preferences;

import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Tenne on 3/17/2016.
 */
public class DateType implements ComplexPreferencesType {

    private static final String TAG = "DateType";

    private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    public static final String DATE_FORMAT_PARAM_KEY = "dateFormat";

    public static final String CURRENT_TIME ="currentTime";

    private static final long SECOND_IN_MILLIS = 1000;
    private static final long MINUTE_IN_MILLIS = 60 * SECOND_IN_MILLIS;
    private static final long HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS;
    private static final long DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS;
    private static final long WEEK_IN_MILLIS = 7 * DAY_IN_MILLIS;


    public static String createDurationDelta(boolean forward, long quantity, Duration units) {
        if(forward) {
            return PLUS+(quantity)+" "+units;
        } else {
            return MINUS+(quantity)+" "+units;
        }
    }

    private static long getDurationDelta(String deltaStr) {
        StringTokenizer tok = new StringTokenizer(deltaStr," ");
        long quantity = Long.parseLong(tok.nextToken());
        Duration units = Duration.valueOf(tok.nextToken());
        switch (units) {
            case milliseconds:
                return quantity;
            case seconds:
                return quantity * SECOND_IN_MILLIS;
            case minutes:
                return quantity * MINUTE_IN_MILLIS;
            case hours:
                return quantity * HOUR_IN_MILLIS;
            case days:
                return quantity * DAY_IN_MILLIS;
            case weeks:
                return quantity * WEEK_IN_MILLIS;
            default:
                return quantity;
        }
    }

    private static boolean isDurationDelta(String str) {
        return str.startsWith(PLUS) || str.startsWith(MINUS);
    }

    private static final String PLUS ="+";
    private static final String MINUS ="-";

    public static enum Duration {
        milliseconds, seconds, minutes, hours, days, weeks
    }


    @Override
    public boolean isCompatible(Field field, Preferences.Value value) {
        return field.getType() == Date.class;
    }

    @Override
    public void storeValue(SharedPreferences.Editor editor, String preferencesKey, Object fieldValue, Preferences.Value value) {
        editor.putLong(preferencesKey, ((Date) fieldValue).getTime());
    }

    @Override
    public boolean loadValue(Map<String, ?> allPreferences, String preferencesKey, Object preferencesObject, Field field, Preferences.Value value) {
        Object storedValue = PreferencesManager.getStoredValue(allPreferences, preferencesKey, preferencesObject, field, value);
        Date date;
        if(storedValue instanceof Date) {
            date = (Date)storedValue;
        } else if(storedValue instanceof String) {
            DateFormat dateFormat = getDateFormat(value);
            date = getDateFromString((String) storedValue, dateFormat);
        } else {
            date = new Date(((Number) storedValue).longValue());
        }
        try {
            field.set(preferencesObject, date);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to set date " + date + " for field " + field.getName(), e);
            return false;
        }
    }

    private DateFormat getDateFormat(Preferences.Value value) {
        Map<String, String> paramsMap = PreferencesManager.paramsToMap(value.params());
        DateFormat dateFormat = DEFAULT_DATE_FORMAT;
        if(paramsMap.containsKey(DATE_FORMAT_PARAM_KEY)) {
            dateFormat = new SimpleDateFormat(paramsMap.get(DATE_FORMAT_PARAM_KEY));
        }
        return dateFormat;
    }

    @Override
    public Object createDefaultValue(Field field, Object defaultValue, Preferences.Value value) {
        if(defaultValue instanceof Date) {
            return defaultValue;
        } else if(defaultValue instanceof String) {
            return getDateFromString((String) defaultValue, getDateFormat(value));
        } else {
            return null;
        }
    }

    private Date getDateFromString(String defaultValue, DateFormat dateFormat) {
        if(CURRENT_TIME.equals(defaultValue)) {
            return new Date();
        } else if(isDurationDelta(defaultValue)) {
            return new Date(System.currentTimeMillis() + getDurationDelta(defaultValue));
        } else {
            try {
                return dateFormat.parse(defaultValue);
            } catch (Exception e) {
                Log.e(TAG, "Failed to create date from " + defaultValue, e);
                return null;
            }
        }
    }
}
