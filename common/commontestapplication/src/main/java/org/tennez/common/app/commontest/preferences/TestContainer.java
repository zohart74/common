package org.tennez.common.app.commontest.preferences;

import org.json.JSONException;
import org.json.JSONObject;
import org.tennez.common.preferences.Preferences;
import org.tennez.common.preferences.ContainerType;

/**
 * Created by Tenne on 3/17/2016.
 */
@Preferences.Container
public class TestContainer {

    private static JSONObject createJSON() {
        try {
            return new JSONObject("{\"default\": true}");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final JSONObject DEFAULT_JSON = createJSON();



    @Preferences.Value(backwardCompatibility = "CTEXT")
    private String text;

    @Preferences.Value
    private int number;

    @Preferences.Value(useDefaultValue = true, defaultValue = ContainerType.NEW)
    private TestSubContainer subContainer;

    @Preferences.Value(name = "SCTR2", overridePrefix = true)
    private TestSubContainer subContainer2;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public TestSubContainer getSubContainer() {
        return subContainer;
    }

    public void setSubContainer(TestSubContainer subContainer) {
        this.subContainer = subContainer;
    }

    public TestSubContainer getSubContainer2() {
        return subContainer2;
    }

    public void setSubContainer2(TestSubContainer subContainer2) {
        this.subContainer2 = subContainer2;
    }

    @Override
    public String toString() {
        return "TestContainer{" +
                "text='" + text + '\'' +
                ", number=" + number +
                ", subContainer=" + subContainer +
                ", subContainer2=" + subContainer2 +
                '}';
    }
}
