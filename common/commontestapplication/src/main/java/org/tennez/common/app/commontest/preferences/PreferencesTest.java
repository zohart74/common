package org.tennez.common.app.commontest.preferences;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tennez.common.app.commontest.Test;
import org.tennez.common.preferences.PreferencesManager;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tenne on 3/17/2016.
 */
public class PreferencesTest implements Test{

    private static final String TAG = "PreferencesTest";

    @Override
    public boolean test(Context context) {
        try {
            while(nextIteration(context)){}
            return true;
        } catch (Throwable t) {
            Log.d(TAG, "Failed preferences test", t);
            return false;
        }
    }

    @Override
    public String getName() {
        return "Test Preferences";
    }

    private boolean nextIteration(Context context) {
        boolean hasNext = true;
        TestPreferences testPreferences = TestPreferences.getInstance(context);
        Log.d("Test.Preferences", "Current State: " + testPreferences);
        if(testPreferences.getPrimitiveByte() == 0) {
            testPreferences.setPrimitiveByte(((byte) 1));
            testPreferences.setPrimitiveShort((short) 2);
            testPreferences.setPrimitiveInt(3);
            testPreferences.setPrimitiveLong(4);
            testPreferences.setPrimitiveDouble(5.1);
            testPreferences.setPrimitiveFloat((float) 6.2);
            testPreferences.setPrimitiveBoolean(false);

            testPreferences.setObjectByte(new Byte((byte) -1));
            testPreferences.setObjectShort(new Short((short) -2));
            testPreferences.setObjectInt(new Integer(-3));
            testPreferences.setObjectLong(new Long(-4));
            testPreferences.setObjectDouble(new Double(-5.1));
            testPreferences.setObjectFloat(new Float((float) -6.2));
            testPreferences.setObjectBoolean(Boolean.FALSE);

            testPreferences.setDate(new Date());
            testPreferences.setObjectString("S1");
            testPreferences.setStringSet(new HashSet<String>());
            testPreferences.setJsonObject(new JSONObject());
            testPreferences.setJsonArray(new JSONArray());
            testPreferences.setContainer(new TestContainer());
        } else if(testPreferences.getPrimitiveByte() == 1) {
            testPreferences.setPrimitiveByte(((byte) 2));
            testPreferences.setPrimitiveShort((short) 3);
            testPreferences.setPrimitiveInt(4);
            testPreferences.setPrimitiveLong(5);
            testPreferences.setPrimitiveDouble(6.1);
            testPreferences.setPrimitiveFloat((float) 7.2);
            testPreferences.setPrimitiveBoolean(true);

            testPreferences.setObjectByte(new Byte((byte) -2));
            testPreferences.setObjectShort(new Short((short) -3));
            testPreferences.setObjectInt(new Integer(-4));
            testPreferences.setObjectLong(new Long(-5));
            testPreferences.setObjectDouble(new Double(-6.1));
            testPreferences.setObjectFloat(new Float((float) -7.2));
            testPreferences.setObjectBoolean(Boolean.TRUE);

            Date date = testPreferences.getDate();
            date = new Date(date.getTime()+24*60*60*1000);
            testPreferences.setDate(date);
            testPreferences.setObjectString("S2");
            Set<String> stringSet = testPreferences.getStringSet();
            stringSet.add("STR1");
            testPreferences.setStringSet(stringSet);
            try {
                JSONObject jsonObject = testPreferences.getJsonObject();
                jsonObject.put("iter", 2);
                testPreferences.setJsonObject(jsonObject);
                JSONArray jsonArray = testPreferences.getJsonArray();
                jsonArray.put("iteration2");
                testPreferences.setJsonArray(jsonArray);
            } catch (JSONException json) {

            }

            TestContainer container = testPreferences.getContainer();
            container.setText("ctext");
            container.setNumber(10);
            container.setSubContainer(new TestSubContainer());
            container.setSubContainer2(new TestSubContainer());
        } else if(testPreferences.getPrimitiveByte() == 2) {
            testPreferences.setPrimitiveByte(((byte) 3));
            testPreferences.setPrimitiveShort((short) 4);
            testPreferences.setPrimitiveInt(5);
            testPreferences.setPrimitiveLong(6);
            testPreferences.setPrimitiveDouble(7.1);
            testPreferences.setPrimitiveFloat((float) 8.2);
            testPreferences.setPrimitiveBoolean(false);

            testPreferences.setObjectByte(new Byte((byte) -3));
            testPreferences.setObjectShort(new Short((short) -4));
            testPreferences.setObjectInt(new Integer(-5));
            testPreferences.setObjectLong(new Long(-6));
            testPreferences.setObjectDouble(new Double(-7.1));
            testPreferences.setObjectFloat(new Float((float) -8.2));
            testPreferences.setObjectBoolean(Boolean.FALSE);

            Date date = testPreferences.getDate();
            date = new Date(date.getTime()+24*60*60*1000);
            testPreferences.setDate(date);
            testPreferences.setObjectString("S3");
            Set<String> stringSet = testPreferences.getStringSet();
            stringSet.add("STR2");
            testPreferences.setStringSet(stringSet);
            try {
                JSONObject jsonObject = testPreferences.getJsonObject();
                jsonObject.put("iter", 3);
                jsonObject.put("next", true);
                testPreferences.setJsonObject(jsonObject);
                JSONArray jsonArray = testPreferences.getJsonArray();
                jsonArray.put("iteration3");
                testPreferences.setJsonArray(jsonArray);
            } catch (JSONException json) {

            }
            TestContainer container = testPreferences.getContainer();
            container.setText("ctext2");
            container.setNumber(11);
            TestSubContainer subContainer = container.getSubContainer();
            subContainer.setSubText("sctext");
            subContainer.setSubValue(-11);
            TestSubContainer subContainer2 = container.getSubContainer2();
            subContainer2.setSubText("sctext2");
            subContainer2.setSubValue(-111);
        } else if(testPreferences.getPrimitiveByte() == 3) {
            testPreferences.setPrimitiveByte(((byte) 0));
            testPreferences.setPrimitiveShort((short) 0);
            testPreferences.setPrimitiveInt(0);
            testPreferences.setPrimitiveLong(0);
            testPreferences.setPrimitiveDouble(0);
            testPreferences.setPrimitiveFloat((float) 0);
            testPreferences.setPrimitiveBoolean(false);

            testPreferences.setObjectByte(new Byte((byte) 0));
            testPreferences.setObjectShort(new Short((short) 0));
            testPreferences.setObjectInt(new Integer(0));
            testPreferences.setObjectLong(new Long(0));
            testPreferences.setObjectDouble(new Double(0));
            testPreferences.setObjectFloat(new Float((float) 0));
            testPreferences.setObjectBoolean(Boolean.FALSE);

            testPreferences.setDate(null);
            testPreferences.setObjectString(null);
            testPreferences.setStringSet(null);
            testPreferences.setJsonArray(null);
            testPreferences.setContainer(null);
            hasNext = false;
        }
        PreferencesManager.syncToFile(context, testPreferences);
        return hasNext;
    }
}
