package org.tennez.common.preferences;

import android.content.SharedPreferences;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tenne on 3/17/2016.
 */
public interface ComplexPreferencesType {



    public boolean isCompatible(Field field);

    public void storeValue(SharedPreferences.Editor editor, String preferencesKey, Object fieldValue);

    public boolean loadValue(Map<String, ?> allPreferences, String preferencesKey, Object preferencesObject, Field field, Preferences.Value value);

    public Object createDefaultValue(Field field, Object defaultValue);
}
