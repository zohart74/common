package org.tennez.common.preferences.type;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tennez.common.preferences.ComplexPreferencesType;
import org.tennez.common.preferences.Preferences;
import org.tennez.common.preferences.PreferencesManager;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Tenne on 3/17/2016.
 */
public class JsonType implements ComplexPreferencesType {

    private static final String TAG = "JsonType";

    @Override
    public boolean isCompatible(Field field) {
        return field.getType() == JSONObject.class || field.getType() == JSONArray.class;
    }

    @Override
    public void storeValue(SharedPreferences.Editor editor, String preferencesKey, Object fieldValue) {
        editor.putString(preferencesKey, fieldValue.toString());
    }

    @Override
    public boolean loadValue(Map<String, ?> allPreferences, String preferencesKey, Object preferencesObject, Field field, Preferences.Value value) {
        Object storedValue = PreferencesManager.getStoredValue(allPreferences, preferencesKey, preferencesObject, field, value);
        if(storedValue instanceof String) {
            String str = (String)storedValue;
            try {
                if (field.getType() == JSONObject.class) {
                    field.set(preferencesObject, new JSONObject(str));
                } else {
                    field.set(preferencesObject, new JSONArray(str));
                }
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Failed to parse json " + str + " for field " + field.getName(), e);
                return false;
            }
        } else if(storedValue instanceof JSONObject || storedValue instanceof JSONArray) {
            try {
                field.set(preferencesObject, storedValue);
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Failed to set json " + storedValue + " for field " + field.getName(), e);
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public Object createDefaultValue(Field field, Object defaultValue) {
        if(defaultValue instanceof String) {
            String defaultValueStr = (String) defaultValue;
            try {
                if (defaultValueStr.length() == 0) {
                    return null;
                }
                if (field.getType() == JSONObject.class) {
                    new JSONObject(defaultValueStr);
                } else {
                    return new JSONArray(defaultValueStr);
                }
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Failed to create default json " + defaultValue + " for field " + field.getName(), e);
                return false;
            }
        } else if(defaultValue instanceof JSONObject || defaultValue instanceof JSONArray) {
            return defaultValue;
        }
        return null;
    }
}
