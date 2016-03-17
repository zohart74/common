package org.tennez.common.preferences;

import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Tenne on 3/17/2016.
 */
public class ContainerType implements ComplexPreferencesType {

    private static final String TAG = "ContainerType";

    public static final String NEW = "new";

    @Override
    public boolean isCompatible(Field field, Preferences.Value value) {
        return field.getType().getAnnotation(Preferences.Container.class)!=null;
    }

    @Override
    public void storeValue(SharedPreferences.Editor editor, String preferencesKey, Object fieldValue, Preferences.Value value) {
        editor.putBoolean(preferencesKey, true);
        Field[] fields = fieldValue.getClass().getDeclaredFields();
        if(fields != null) {
            for(Field field : fields) {
                Preferences.Value innerFieldValue = field.getAnnotation(Preferences.Value.class);
                if(innerFieldValue != null) {
                    PreferencesManager.saveFieldValue(editor, fieldValue, field, preferencesKey+".");
                }
            }
        }
    }

    @Override
    public boolean loadValue(Map<String, ?> allPreferences, String preferencesKey, Object preferencesObject, Field field, Preferences.Value value) {
        boolean valueLoaded = false;
        try {
            Object storedValue = PreferencesManager.getStoredValue(allPreferences, preferencesKey, preferencesObject, field, value);
            if(storedValue == null) {
                return false;
            }
            Object containerObject;
            if(field.getType().isInstance(storedValue)) {
                containerObject = storedValue;
            } else {
                containerObject = field.getType().newInstance();
            }
            Field[] fields = field.getType().getDeclaredFields();
            field.set(preferencesObject, containerObject);
            if (fields != null) {
                for (Field f : fields) {
                    Preferences.Value innerFieldValue = f.getAnnotation(Preferences.Value.class);
                    if (innerFieldValue != null) {
                        PreferencesManager.setFieldValue(allPreferences, containerObject, f, preferencesKey+".");
                        valueLoaded = true;
                    }
                }
            }
            return valueLoaded;
        } catch (Exception e) {
            Log.e(TAG, "Failed to load container to field "+field.getName(), e);
            return false;
        }
    }

    @Override
    public Object createDefaultValue(Field field, Object defaultValue, Preferences.Value value) {
        Class containerClass = field.getType();
        if(defaultValue == null) {
            return null;
        } else if(containerClass.isInstance(defaultValue)) {
            return defaultValue;
        } else {
            try {
                return containerClass.newInstance();
            } catch (Exception e) {
                Log.e(TAG, "Failed to create default container to field "+field.getName(), e);
                return null;
            }
        }
    }
}
