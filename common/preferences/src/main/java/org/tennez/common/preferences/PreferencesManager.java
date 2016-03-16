package org.tennez.common.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;


public class PreferencesManager {

    private static final String TAG = "PreferencesManager";

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

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
                        setFieldValue(allPreferences, preferencesObject, field);
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
                        saveFieldValue(editor, preferencesObject, field);
                    }
                }
                editor.commit();
            }
        }
    }

    private static void setFieldValue(Map<String, ?> allPreferences, Object preferencesObject, Field field) {
        Preferences.Value value = field.getAnnotation(Preferences.Value.class);
        if(value != null) {
            String fieldName = value.name();
            if (fieldName.length() == 0) {
                fieldName = field.getName();
            }
            if (allPreferences.containsKey(fieldName)) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                try {
                    Object storedValue = allPreferences.get(fieldName);
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
                    } else if(field.getType() == Date.class) {
                        String dateStr = (String)storedValue;
                        try {
                            field.set(preferencesObject, DATE_FORMAT.parse(dateStr));
                        } catch (Exception e) {
                            Log.e(TAG,"Failed to parse date "+dateStr+" for field "+field.getName(), e);
                        }
                    } else if(field.getType() == JSONObject.class) {
                        String jsonStr = (String)storedValue;
                        try {
                            field.set(preferencesObject, new JSONObject(jsonStr));
                        } catch (Exception e) {
                            Log.e(TAG,"Failed to parse json "+jsonStr+" for field "+field.getName(), e);
                        }
                    } else if(field.getType() == JSONArray.class) {
                        String jsonArayStr = (String)storedValue;
                        try {
                            field.set(preferencesObject, new JSONArray(jsonArayStr));
                        } catch (Exception e) {
                            Log.e(TAG,"Failed to parse json array "+jsonArayStr+" for field "+field.getName(), e);
                        }
                    } else  {
                        field.set(preferencesObject, storedValue);
                    }
                } catch (IllegalAccessException iae) {
                    Log.e(TAG,"Failed to set field value for "+field.getName(), iae);
                }
            }
        }
    }

    private static void saveFieldValue(SharedPreferences.Editor editor, Object preferencesObject, Field field) {
        Preferences.Value value = field.getAnnotation(Preferences.Value.class);
        if(value != null) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            String fieldName = value.name();
            if (fieldName.length() == 0) {
                fieldName = field.getName();
            }
            try {
                Object fieldValue = field.get(preferencesObject);
                if(fieldValue == null) {
                    editor.remove(fieldName);
                } else {
                    if (fieldValue instanceof Byte ||
                            fieldValue instanceof Short ||
                            fieldValue instanceof Integer) {
                        editor.putInt(fieldName, ((Number) fieldValue).intValue());
                    } else if (fieldValue instanceof Long) {
                        editor.putLong(fieldName, ((Number) fieldValue).longValue());
                    } else if (fieldValue instanceof Float) {
                        editor.putFloat(fieldName, ((Number) fieldValue).floatValue());
                    } else if (fieldValue instanceof Double) {
                        editor.putString(fieldName, fieldValue.toString());
                    } else if (fieldValue instanceof Boolean) {
                        editor.putBoolean(fieldName, ((Boolean) fieldValue).booleanValue());
                    } else if (fieldValue instanceof String) {
                        editor.putString(fieldName, (String) fieldValue);
                    } else if (fieldValue instanceof Set && ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0] == String.class) {
                        editor.putStringSet(fieldName, (Set<String>) fieldValue);
                    } else if (field.getType() == Date.class) {
                        editor.putString(fieldName, DATE_FORMAT.format((Date) fieldValue));
                    } else if (field.getType() == JSONObject.class || field.getType() == JSONArray.class) {
                        editor.putString(fieldName, fieldValue.toString());
                    }
                }
            } catch (IllegalAccessException iae) {
                Log.e(TAG,"Failed to store field value for "+field.getName(), iae);
            }
        } else {
            Log.d(TAG,"Save Found non value field "+field.getName());
        }
    }
}
