package org.tennez.common.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PreferencesManager {

    public static final String TAG = "PreferencesManager";

    private static final List<ComplexPreferencesType> SUPPORTED_TYPES = new LinkedList<ComplexPreferencesType>();
    static {
        SUPPORTED_TYPES.add(new DateType());
        SUPPORTED_TYPES.add(new JsonType());
        SUPPORTED_TYPES.add(new ContainerType());
    }

    public static void addSupportedType(ComplexPreferencesType type) {
        synchronized (SUPPORTED_TYPES) {
            SUPPORTED_TYPES.add(type);
        }
    }

    public static Object loadFromFile(Context context, Class preferencesObjectClass) {
        try {
            Object preferencesObject = preferencesObjectClass.newInstance();
            syncFromFile(context, preferencesObject);
            return preferencesObject;
        }catch (Exception e) {
            Log.e(TAG,"Failed to initiate preferences object from "+preferencesObjectClass, e);
            return null;
        }
    }

    public static void syncFromFile(Context context, Object preferencesObject) {
        if(preferencesObject != null) {

            Preferences preferences = preferencesObject.getClass().getAnnotation(Preferences.class);
            if(preferences != null) {
                String preferencesName = preferences.name();
                SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, preferences.mode());
                Map<String, ?> allPreferences = sharedPreferences.getAll();
                Field[] fields = preferencesObject.getClass().getDeclaredFields();
                if(fields != null) {
                    for(Field field : fields) {
                        setFieldValue(allPreferences, preferencesObject, field, null);
                    }
                }
            }
        }
    }

    public static void syncToFile(Context context, Object preferencesObject) {
        if(preferencesObject != null) {
            Preferences preferences = preferencesObject.getClass().getAnnotation(Preferences.class);
            if(preferences != null) {
                String preferencesName = preferences.name();
                SharedPreferences.Editor editor = context.getSharedPreferences(preferencesName, preferences.mode()).edit();
                Field[] fields = preferencesObject.getClass().getDeclaredFields();
                if(fields != null) {
                    for(Field field : fields) {
                        saveFieldValue(editor, preferencesObject, field, null);
                    }
                }
                editor.commit();
            }
        }
    }

    private static Object getDefaultValue(Field field, Object defaultValue) {
        if(defaultValue == null) {
            return null;
        } else if (field.getType() == Byte.TYPE || field.getType() == Byte.class) {
            if(defaultValue instanceof Number) {
                return ((Number) defaultValue).byteValue();
            } else if(defaultValue instanceof String) {
                if((((String) defaultValue).length() > 0)){
                    return Byte.parseByte((String) defaultValue);
                } else {
                    if (field.getType().isPrimitive()) {
                        return (byte)0;
                    } else {
                        return null;
                    }
                }
            } else {
                return (byte)0;
            }
        } else if (field.getType() == Short.TYPE || field.getType() == Short.class) {
            if(defaultValue instanceof Number) {
                return ((Number) defaultValue).shortValue();
            } else if(defaultValue instanceof String) {
                if((((String) defaultValue).length() > 0)){
                    return Short.parseShort((String) defaultValue);
                } else {
                    if (field.getType().isPrimitive()) {
                        return (short)0;
                    } else {
                        return null;
                    }
                }
            } else {
                return (short)0;
            }
        } else if (field.getType() == Integer.TYPE || field.getType() == Integer.class) {
            if(defaultValue instanceof Number) {
                return ((Number) defaultValue).intValue();
            } else if(defaultValue instanceof String) {
                if((((String) defaultValue).length() > 0)){
                    return Integer.parseInt((String) defaultValue);
                } else {
                    if (field.getType().isPrimitive()) {
                        return 0;
                    } else {
                        return null;
                    }
                }
            } else {
                return 0;
            }
        } else if (field.getType() == Long.TYPE || field.getType() == Long.class) {
            if(defaultValue instanceof Number) {
                return ((Number) defaultValue).longValue();
            } else if(defaultValue instanceof String) {
                if((((String) defaultValue).length() > 0)){
                    return Long.parseLong((String) defaultValue);
                } else {
                    if (field.getType().isPrimitive()) {
                        return 0L;
                    } else {
                        return null;
                    }
                }
            } else {
                return 0L;
            }
        } else if (field.getType() == Float.TYPE || field.getType() == Float.class) {
            if(defaultValue instanceof Float) {
                return ((Number) defaultValue).floatValue();
            } else if(defaultValue instanceof String) {
                if((((String) defaultValue).length() > 0)){
                    return Float.parseFloat((String) defaultValue);
                } else {
                    if (field.getType().isPrimitive()) {
                        return (float)0;
                    } else {
                        return null;
                    }
                }
            } else {
                return (float)0;
            }
        } else if (field.getType() == Double.TYPE || field.getType() == Double.class) {
            if(defaultValue instanceof Double) {
                return ((Number) defaultValue).doubleValue();
            } else if(defaultValue instanceof String) {
                if((((String) defaultValue).length() > 0)){
                    return Double.parseDouble((String) defaultValue);
                } else {
                    if (field.getType().isPrimitive()) {
                        return (double)0;
                    } else {
                        return null;
                    }
                }
            } else {
                return (double)0;
            }
        } else if (field.getType() == Boolean.TYPE || field.getType() == Boolean.class) {
            if(defaultValue instanceof Boolean) {
                return defaultValue;
            } else if(defaultValue instanceof String) {
                if((((String) defaultValue).length() > 0)){
                    return Boolean.parseBoolean((String) defaultValue);
                } else {
                    if (field.getType().isPrimitive()) {
                        return false;
                    } else {
                        return null;
                    }
                }
            } else {
                return false;
            }
        } else {
            synchronized (SUPPORTED_TYPES) {
                for(ComplexPreferencesType type : SUPPORTED_TYPES) {
                    if(type.isCompatible(field)) {
                        return type.createDefaultValue(field, defaultValue);
                    }
                }
            }
            return null;
        }
    }

    private static Object getDefaultValueFromAnnotation(Object fieldHolder, Preferences.Value value) {
        if(!value.useDefaultValue()) {
            return null;
        } else {
            if(value.defaultValueField().length() > 0) {
                String defaultValueFieldStr = value.defaultValueField();
                try {
                    Class defaultValueFieldClass = fieldHolder.getClass();
                    String fieldName;
                    if(!defaultValueFieldStr.startsWith(".")) {
                        int index = defaultValueFieldStr.lastIndexOf('.');
                        defaultValueFieldClass = Class.forName(defaultValueFieldStr.substring(0, index));
                        fieldName = defaultValueFieldStr.substring(index + 1);
                    } else {
                        fieldName = defaultValueFieldStr.substring(1);
                    }
                    Field defaultValueField = null;
                    try {
                        defaultValueField = defaultValueFieldClass.getDeclaredField(fieldName);
                    } catch (Exception e) {
                        defaultValueField = defaultValueFieldClass.getField(fieldName);
                    }
                    if(defaultValueField == null) {
                        Log.e(TAG, "Failed to load default value field "+defaultValueFieldStr+" - field not found");
                        return null;
                    } else {
                        if(!defaultValueField.isAccessible()) {
                            defaultValueField.setAccessible(true);
                        }
                        return defaultValueField.get(null);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Failed to load default value field "+defaultValueFieldStr, e);
                    return null;
                }
            } else {
                return value.defaultValue();
            }
        }
    }

    static Object getStoredValue(Map<String, ?> allPreferences, String preferencesKey, Object preferencesObject, Field field, Preferences.Value value) {
        Object storedValue = allPreferences.get(preferencesKey);
        if(storedValue == null) {
            if(value.backwardCompatibility().length() > 0) {
                storedValue = allPreferences.get(value.backwardCompatibility());
            }
            if(storedValue == null && value.useDefaultValue()) {
                storedValue = getDefaultValue(field, getDefaultValueFromAnnotation(preferencesObject, value));
            }
        }
        return storedValue;
    }

    static void setFieldValue(Map<String, ?> allPreferences, Object preferencesObject, Field field, String prefix) {
        Preferences.Value value = field.getAnnotation(Preferences.Value.class);
        if(value != null) {
            String preferencesKey = getPreferencesKey(value, field, prefix);
            Object storedValue = getStoredValue(allPreferences, preferencesKey, preferencesObject, field, value);
            if (storedValue != null) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                try {
                    if(field.getType().isPrimitive()) {
                        if (field.getType() == Byte.TYPE) {
                            field.setByte(preferencesObject, ((Number) storedValue).byteValue());
                        } else if (field.getType() == Short.TYPE) {
                            field.setShort(preferencesObject, ((Number) storedValue).shortValue());
                        } else if (field.getType() == Integer.TYPE) {
                            field.setInt(preferencesObject, ((Number) storedValue).intValue());
                        } else if (field.getType() == Long.TYPE) {
                            field.setLong(preferencesObject, ((Number) storedValue).longValue());
                        } else if (field.getType() == Float.TYPE) {
                            field.setFloat(preferencesObject, ((Number) storedValue).floatValue());
                        } else if (field.getType() == Double.TYPE) {
                            field.setDouble(preferencesObject, Double.parseDouble((String)storedValue));
                        } else if (field.getType() == Boolean.TYPE) {
                            field.setBoolean(preferencesObject, (Boolean) storedValue);
                        }
                    } else if (field.getType() == Byte.class) {
                        field.set(preferencesObject, ((Number) storedValue).byteValue());
                    } else if (field.getType() == Short.class) {
                        field.set(preferencesObject, ((Number) storedValue).shortValue());
                    } else if (field.getType() == Integer.class) {
                        field.set(preferencesObject,((Number) storedValue).intValue());
                    } else if (field.getType() == Long.class) {
                        field.set(preferencesObject, ((Number) storedValue).longValue());
                    } else if (field.getType() == Float.class) {
                        field.set(preferencesObject, ((Number) storedValue).floatValue());
                    } else if (field.getType() == Double.class) {
                        field.set(preferencesObject, Double.parseDouble((String)storedValue));
                    } else {
                        boolean found = false;
                        synchronized (SUPPORTED_TYPES) {
                            for(ComplexPreferencesType type : SUPPORTED_TYPES) {
                                if(type.isCompatible(field)) {
                                    type.loadValue(allPreferences, preferencesKey, preferencesObject, field, value);
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if(!found) {
                            field.set(preferencesObject, storedValue);
                        }
                    }
                } catch (IllegalAccessException iae) {
                    Log.e(TAG,"Failed to set field value for "+field.getName(), iae);
                }
            }
        }
    }

    static void saveFieldValue(SharedPreferences.Editor editor, Object preferencesObject, Field field, String prefix) {
        Preferences.Value value = field.getAnnotation(Preferences.Value.class);
        if(value != null) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            String preferencesKey = getPreferencesKey(value, field, prefix);
            try {
                Object fieldValue = field.get(preferencesObject);
                if(fieldValue == null) {
                    editor.remove(preferencesKey);
                } else {
                    if (fieldValue instanceof Byte ||
                            fieldValue instanceof Short ||
                            fieldValue instanceof Integer) {
                        editor.putInt(preferencesKey, ((Number) fieldValue).intValue());
                    } else if (fieldValue instanceof Long) {
                        editor.putLong(preferencesKey, ((Number) fieldValue).longValue());
                    } else if (fieldValue instanceof Float) {
                        editor.putFloat(preferencesKey, ((Number) fieldValue).floatValue());
                    } else if (fieldValue instanceof Double) {
                        editor.putString(preferencesKey, fieldValue.toString());
                    } else if (fieldValue instanceof Boolean) {
                        editor.putBoolean(preferencesKey, (Boolean)fieldValue);
                    } else if (fieldValue instanceof String) {
                        editor.putString(preferencesKey, (String) fieldValue);
                    } else if (fieldValue instanceof Set && ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0] == String.class) {
                        editor.putStringSet(preferencesKey, (Set<String>) fieldValue);
                    } else {
                        synchronized (SUPPORTED_TYPES) {
                            for(ComplexPreferencesType type : SUPPORTED_TYPES) {
                                if(type.isCompatible(field)) {
                                    type.storeValue(editor, preferencesKey, fieldValue);
                                    break;
                                }
                            }
                        }
                    }
                    if(value.backwardCompatibility().length()>0) {
                        editor.remove(value.backwardCompatibility());
                    }
                }
            } catch (IllegalAccessException iae) {
                Log.e(TAG,"Failed to store field value for "+field.getName(), iae);
            }
        }
    }

    static String getPreferencesKey(Preferences.Value value, Field field, String prefix) {
        String preferencesKey = value.name();
        if (preferencesKey.length() == 0) {
            preferencesKey = field.getName();
        }
        if(prefix != null && !value.overridePrefix()) {
            preferencesKey = prefix + preferencesKey;
        }
        return  preferencesKey;
    }
}
