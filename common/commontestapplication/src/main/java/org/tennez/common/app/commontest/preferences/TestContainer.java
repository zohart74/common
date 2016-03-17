package org.tennez.common.app.commontest.preferences;

import org.tennez.common.preferences.Preferences;

/**
 * Created by Tenne on 3/17/2016.
 */
@Preferences.Container
public class TestContainer {

    @Preferences.Value
    private String text;

    @Preferences.Value
    private int number;

    @Preferences.Value
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
