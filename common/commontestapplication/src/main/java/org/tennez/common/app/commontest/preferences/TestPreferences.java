package org.tennez.common.app.commontest.preferences;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tennez.common.preferences.Preferences;
import org.tennez.common.preferences.PreferencesManager;

import java.util.Date;
import java.util.Set;

/**
 * Created by Tenne on 3/16/2016.
 */
@Preferences(name = "CommonTest")
public class TestPreferences {

    private static TestPreferences instance;

    public static TestPreferences getInstance(Context context) {
        return (TestPreferences)PreferencesManager.loadFromFile(context, TestPreferences.class);
    }

    @Preferences.Value(name = "byte")
    private byte primitiveByte;
    @Preferences.Value(name = "short")
    private short primitiveShort;
    @Preferences.Value(name = "int")
    private int primitiveInt;
    @Preferences.Value(name = "long")
    private long primitiveLong;
    @Preferences.Value(name = "double")
    private double primitiveDouble;
    @Preferences.Value(name = "float")
    private float primitiveFloat;
    @Preferences.Value(name = "boolean")
    private boolean primitiveBoolean;

    @Preferences.Value
    private Byte objectByte;
    @Preferences.Value
    private Short objectShort;
    @Preferences.Value
    private Integer objectInt;
    @Preferences.Value
    private Long objectLong;
    @Preferences.Value
    private Double objectDouble;
    @Preferences.Value
    private Float objectFloat;
    @Preferences.Value
    private Boolean objectBoolean;

    @Preferences.Value
    private String objectString;
    @Preferences.Value
    private Set<String> stringSet;
    @Preferences.Value
    private Date date;
    @Preferences.Value
    private JSONObject jsonObject;
    @Preferences.Value
    private JSONArray jsonArray;

    @Preferences.Value
    private TestContainer container;

    private String notAValue;

    public byte getPrimitiveByte() {
        return primitiveByte;
    }

    public short getPrimitiveShort() {
        return primitiveShort;
    }

    public int getPrimitiveInt() {
        return primitiveInt;
    }

    public long getPrimitiveLong() {
        return primitiveLong;
    }

    public double getPrimitiveDouble() {
        return primitiveDouble;
    }

    public float getPrimitiveFloat() {
        return primitiveFloat;
    }

    public boolean isPrimitiveBoolean() {
        return primitiveBoolean;
    }

    public Byte getObjectByte() {
        return objectByte;
    }

    public Short getObjectShort() {
        return objectShort;
    }

    public Integer getObjectInt() {
        return objectInt;
    }

    public Long getObjectLong() {
        return objectLong;
    }

    public Double getObjectDouble() {
        return objectDouble;
    }

    public Float getObjectFloat() {
        return objectFloat;
    }

    public Boolean getObjectBoolean() {
        return objectBoolean;
    }

    public String getObjectString() {
        return objectString;
    }

    public Set<String> getStringSet() {
        return stringSet;
    }

    public Date getDate() {
        return date;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public String getNotAValue() {
        return notAValue;
    }

    public TestContainer getContainer() {
        return container;
    }

    public void setContainer(TestContainer container) {
        this.container = container;
    }

    public static void setInstance(TestPreferences instance) {
        TestPreferences.instance = instance;
    }

    public void setPrimitiveByte(byte primitiveByte) {
        this.primitiveByte = primitiveByte;
    }

    public void setPrimitiveShort(short primitiveShort) {
        this.primitiveShort = primitiveShort;
    }

    public void setPrimitiveInt(int primitiveInt) {
        this.primitiveInt = primitiveInt;
    }

    public void setPrimitiveLong(long primitiveLong) {
        this.primitiveLong = primitiveLong;
    }

    public void setPrimitiveDouble(double primitiveDouble) {
        this.primitiveDouble = primitiveDouble;
    }

    public void setPrimitiveFloat(float primitiveFloat) {
        this.primitiveFloat = primitiveFloat;
    }

    public void setPrimitiveBoolean(boolean primitiveBoolean) {
        this.primitiveBoolean = primitiveBoolean;
    }

    public void setObjectByte(Byte objectByte) {
        this.objectByte = objectByte;
    }

    public void setObjectShort(Short objectShort) {
        this.objectShort = objectShort;
    }

    public void setObjectInt(Integer objectInt) {
        this.objectInt = objectInt;
    }

    public void setObjectLong(Long objectLong) {
        this.objectLong = objectLong;
    }

    public void setObjectDouble(Double objectDouble) {
        this.objectDouble = objectDouble;
    }

    public void setObjectFloat(Float objectFloat) {
        this.objectFloat = objectFloat;
    }

    public void setObjectBoolean(Boolean objectBoolean) {
        this.objectBoolean = objectBoolean;
    }

    public void setObjectString(String objectString) {
        this.objectString = objectString;
    }

    public void setStringSet(Set<String> stringSet) {
        this.stringSet = stringSet;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public void setNotAValue(String notAValue) {
        this.notAValue = notAValue;
    }

    @Override
    public String toString() {
        return "TestPreferences{" +
                "primitiveByte=" + primitiveByte +
                ", primitiveShort=" + primitiveShort +
                ", primitiveInt=" + primitiveInt +
                ", primitiveLong=" + primitiveLong +
                ", primitiveDouble=" + primitiveDouble +
                ", primitiveFloat=" + primitiveFloat +
                ", primitiveBoolean=" + primitiveBoolean +
                ", objectByte=" + objectByte +
                ", objectShort=" + objectShort +
                ", objectInt=" + objectInt +
                ", objectLong=" + objectLong +
                ", objectDouble=" + objectDouble +
                ", objectFloat=" + objectFloat +
                ", objectBoolean=" + objectBoolean +
                ", objectString='" + objectString + '\'' +
                ", stringSet=" + stringSet +
                ", date=" + date +
                ", jsonObject=" + jsonObject +
                ", jsonArray=" + jsonArray +
                ", container=" + container +
                ", notAValue='" + notAValue + '\'' +
                '}';
    }
}
