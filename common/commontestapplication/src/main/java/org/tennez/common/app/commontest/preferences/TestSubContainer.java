package org.tennez.common.app.commontest.preferences;

import org.tennez.common.preferences.Preferences;

/**
 * Created by Tenne on 3/17/2016.
 */
@Preferences.Container
public class TestSubContainer {

    @Preferences.Value
    private String subText;

    @Preferences.Value(name = "OLD_KEY", overridePrefix = true)
    private long subValue;

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public long getSubValue() {
        return subValue;
    }

    public void setSubValue(long subValue) {
        this.subValue = subValue;
    }

    @Override
    public String toString() {
        return "TestSubContainer{" +
                "subText='" + subText + '\'' +
                ", subValue=" + subValue +
                '}';
    }
}
