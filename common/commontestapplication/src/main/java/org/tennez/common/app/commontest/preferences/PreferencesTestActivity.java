package org.tennez.common.app.commontest.preferences;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.tennez.common.app.commontest.R;
import org.tennez.common.preferences.Preferences;
import org.tennez.common.preferences.PreferencesManager;

import java.util.LinkedList;
import java.util.List;

@Preferences(name = "CommonTest")
public class PreferencesTestActivity extends AppCompatActivity {

    @Preferences.Value(name = "subject")
    private EditText editText1;
    @Preferences.Value(name = "email")
    private EditText editText2;
    @Preferences.Value(name = "isHere")
    private CheckBox checkBox;
    @Preferences.Value(name = "isThere")
    private RadioButton radioButton;
    @Preferences.Value(name = "selectedNumber")
    private Spinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editText1 = (EditText)findViewById(R.id.editText1);
        editText2 = (EditText)findViewById(R.id.editText2);
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        radioButton = (RadioButton)findViewById(R.id.radioButton);
        spinner = (Spinner)findViewById(R.id.spinner);
        List<String> items = new LinkedList<String>();
        items.add("1");
        items.add("2");
        items.add("3");
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items));

        Button b = (Button)findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesManager.syncToFile(getApplicationContext(), PreferencesTestActivity.this);
                finish();
            }
        });
        PreferencesManager.syncFromFile(getApplicationContext(), this);

    }

}
