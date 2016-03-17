package org.tennez.common.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.tennez.common.preferences.type.ContainerType;
import org.tennez.common.preferences.type.DateType;
import org.tennez.common.preferences.type.JsonType;

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
                SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
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
                SharedPreferences.Editor editor = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).edit();
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

    public static void setFieldValue(Map<String, ?> allPreferences, Object preferencesObject, Field field, String prefix) {
        Preferences.Value value = field.getAnnotation(Preferences.Value.class);
        if(value != null) {
            String preferencesKey = getPreferencesKey(value, field, prefix);
            if (allPreferences.containsKey(preferencesKey)) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                try {
                    Object storedValue = allPreferences.get(preferencesKey);
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
                        field.set(preferencesObject, new Byte(((Number) storedValue).byteValue()));
                    } else if (field.getType() == Short.class) {
                        field.set(preferencesObject, new Short(((Number) storedValue).shortValue()));
                    } else if (field.getType() == Integer.class) {
                        field.set(preferencesObject, new Integer(((Number) storedValue).intValue()));
                    } else if (field.getType() == Long.class) {
                        field.set(preferencesObject, new Long(((Number) storedValue).longValue()));
                    } else if (field.getType() == Float.class) {
                        field.set(preferencesObject, new Float(((Number) storedValue).floatValue()));
                    } else if (field.getType() == Double.class) {
                        field.set(preferencesObject, Double.parseDouble((String)storedValue));
                    } else {
                        boolean found = false;
                        synchronized (SUPPORTED_TYPES) {
                            for(ComplexPreferencesType type : SUPPORTED_TYPES) {
                                if(type.isCompatible(field)) {
                                    type.loadValue(allPreferences, preferencesKey, preferencesObject, field);
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

    public static void saveFieldValue(SharedPreferences.Editor editor, Object preferencesObject, Field field, String prefix) {
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
                        editor.putBoolean(preferencesKey, ((Boolean) fieldValue).booleanValue());
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
                }
            } catch (IllegalAccessException iae) {
                Log.e(TAG,"Failed to store field value for "+field.getName(), iae);
            }
        }
    }

    public static String getPreferencesKey(Preferences.Value value, Field field, String prefix) {
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
