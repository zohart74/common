package org.tennez.common.preferences.type;

import android.content.SharedPreferences;
import android.util.Log;

import org.tennez.common.preferences.ComplexPreferencesType;
import org.tennez.common.preferences.Preferences;
import org.tennez.common.preferences.PreferencesManager;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Tenne on 3/17/2016.
 */
public class ContainerType implements ComplexPreferencesType {

    private static final String TAG = "ContainerType";

    @Override
    public boolean isCompatible(Field field) {
        return field.getType().getAnnotation(Preferences.Container.class)!=null;
    }

    @Override
    public void storeValue(SharedPreferences.Editor editor, String preferencesKey, Object fieldValue) {
        editor.putBoolean(preferencesKey, true);
        Field[] fields = fieldValue.getClass().getDeclaredFields();
        if(fields != null) {
            for(Field field : fields) {
                Preferences.Value value = field.getAnnotation(Preferences.Value.class);
                if(value != null) {
                    PreferencesManager.saveFieldValue(editor, fieldValue, field, preferencesKey+".");
                }
            }
        }
    }

    @Override
    public void loadValue(Map<String, ?> allPreferences, String preferencesKey, Object preferencesObject, Field field) {
        Field[] fields = field.getType().getDeclaredFields();
        try {
            Object containerObject = field.getType().newInstance();
            field.set(preferencesObject, containerObject);
            if (fields != null) {
                for (Field f : fields) {
                    Preferences.Value value = f.getAnnotation(Preferences.Value.class);
                    if (value != null) {
                        PreferencesManager.setFieldValue(allPreferences, containerObject, f, preferencesKey+".");
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to load container to field "+field.getName(), e);
        }
    }
}
