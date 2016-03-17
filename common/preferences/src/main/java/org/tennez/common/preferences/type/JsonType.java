package org.tennez.common.preferences.type;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tennez.common.preferences.ComplexPreferencesType;

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
    public void loadValue(Map<String, ?> allPreferences, String preferencesKey, Object preferencesObject, Field field) {
        String str = (String)allPreferences.get(preferencesKey);
        try {
            if(field.getType() == JSONObject.class) {
                field.set(preferencesObject, new JSONObject(str));
            } else {
                field.set(preferencesObject, new JSONArray(str));
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse json " + str + " for field " + field.getName(), e);
        }
    }
}
