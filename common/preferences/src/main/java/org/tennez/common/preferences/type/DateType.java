package org.tennez.common.preferences.type;

import android.content.SharedPreferences;
import android.util.Log;

import org.tennez.common.preferences.ComplexPreferencesType;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Tenne on 3/17/2016.
 */
public class DateType implements ComplexPreferencesType {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static final String TAG = "DateType";


    @Override
    public boolean isCompatible(Field field) {
        return field.getType() == Date.class;
    }

    @Override
    public void storeValue(SharedPreferences.Editor editor, String preferencesKey, Object fieldValue) {
        editor.putString(preferencesKey, DATE_FORMAT.format((Date) fieldValue));
    }

    @Override
    public void loadValue(Map<String, ?> allPreferences, String preferencesKey, Object preferencesObject, Field field) {
        String dateStr = (String)allPreferences.get(preferencesKey);
        try {
            field.set(preferencesObject, DATE_FORMAT.parse(dateStr));
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse date " + dateStr + " for field " + field.getName(), e);
        }
    }
}
