package org.tennez.common.preferences;

import android.content.SharedPreferences;

import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Tenne on 3/17/2016.
 */
public class UIElementsType implements ComplexPreferencesType {

    private static final String TAG = "UIElementsType";

    @Override
    public boolean isCompatible(Field field, Preferences.Value value) {
        return TextView.class.isAssignableFrom(field.getType()) || Checkable.class.isAssignableFrom(field.getType()) || AdapterView.class.isAssignableFrom(field.getType());
    }

    @Override
    public void storeValue(SharedPreferences.Editor editor, String preferencesKey, Object fieldValue, Preferences.Value value) {
        if(fieldValue instanceof Checkable) {
            Checkable checkable = (Checkable)fieldValue;
            editor.putBoolean(preferencesKey, checkable.isChecked());
        } else if(fieldValue instanceof TextView) {
            TextView textView = (TextView)fieldValue;
            editor.putString(preferencesKey, textView.getText().toString());
        } else if(fieldValue instanceof AdapterView) {
            AdapterView adapterView = (AdapterView)fieldValue;
            Object selectedItem = adapterView.getSelectedItem();
            if(selectedItem instanceof Long) {
                editor.putLong(preferencesKey, (Long)selectedItem);
            } else if(selectedItem instanceof Float) {
                editor.putFloat(preferencesKey, (Float) selectedItem);
            } else if(selectedItem instanceof Boolean) {
                editor.putBoolean(preferencesKey, (Boolean) selectedItem);
            } else if(selectedItem instanceof Number) {
                editor.putInt(preferencesKey, ((Number) selectedItem).intValue());
            } else if(selectedItem instanceof String) {
                editor.putString(preferencesKey, (String) selectedItem);
            }
        }
    }

    @Override
    public boolean loadValue(Map<String, ?> allPreferences, String preferencesKey, Object preferencesObject, Field field, Preferences.Value value) {
        try {
            Object storedValue = PreferencesManager.getStoredValue(allPreferences, preferencesKey, preferencesObject, field, value);
            Object uiElement = field.get(preferencesObject);
            if(uiElement != null) {
                if (uiElement instanceof Checkable) {
                    Checkable checkable = (Checkable) uiElement;
                    if (storedValue == null) {
                        checkable.setChecked(false);
                        return true;
                    } else if (storedValue instanceof Boolean) {
                        checkable.setChecked((Boolean) storedValue);
                        return true;
                    } else {
                        checkable.setChecked(Boolean.parseBoolean(storedValue.toString()));
                        return true;
                    }
                } else if (uiElement instanceof TextView) {
                    TextView textView = (TextView) uiElement;
                    textView.setText(storedValue == null ? "" : storedValue.toString());
                    return true;
                } else if (uiElement instanceof AdapterView) {
                    AdapterView adapterView = (AdapterView) uiElement;
                    if(adapterView.getCount() > 0 && adapterView.getAdapter().getItem(0).getClass() == String.class) {
                        storedValue = (storedValue == null ? "" : String.valueOf(storedValue));
                    }
                    for(int i = 0 ; i<adapterView.getCount() ; ++i) {
                        if(storedValue.equals(adapterView.getItemAtPosition(i))) {
                            adapterView.setSelection(i);
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to load ui element value for "+field.getName(), e);
        }
        return false;
    }

    @Override
    public Object createDefaultValue(Field field, Object defaultValue, Preferences.Value value) {
        if(defaultValue == null) {
            if(Checkable.class.isAssignableFrom(field.getType())) {
                return false;
            } else if(TextView.class.isAssignableFrom(field.getType()) || AdapterView.class.isAssignableFrom(field.getType())) {
                return "";
            } else {
                return null;
            }
        } else {
            return defaultValue;
        }
    }
}
