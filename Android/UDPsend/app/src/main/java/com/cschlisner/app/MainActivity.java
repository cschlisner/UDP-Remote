package com.cschlisner.app;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cengalabs.flatui.FlatUI;


public class MainActivity extends ActionBarActivity {
    EditText input;
    TextView visibleInput;
    Button rightClick, leftClick;
    View trackPad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalContext.globalContext = this;
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        FlatUI.setDefaultTheme(FlatUI.SNOW);
        trackPad = findViewById(R.id.touch_frag);
        rightClick = (Button) findViewById(R.id.right_click);
        rightClick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {
                Networking n = new Networking();
                String msg="";
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        msg = "RIGHTCLICKD";
                        break;
                    case MotionEvent.ACTION_UP:
                        msg = "RIGHTCLICKU";
                        break;
                }
                if (!msg.isEmpty())
                    n.execute(msg, GlobalContext.hostIP);
                return true;
            }
        });
        leftClick = (Button) findViewById(R.id.left_click);
        leftClick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {
                Networking n = new Networking();
                String msg="";
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        msg = "LEFTCLICKD";
                        break;
                    case MotionEvent.ACTION_UP:
                        msg = "LEFTCLICKU";
                        break;
                }
                if (!msg.isEmpty())
                    n.execute(msg, GlobalContext.hostIP);
                return true;
            }
        });
        input = (EditText) findViewById(R.id.hidden_input);
        visibleInput = (TextView) findViewById(R.id.disp_input);
        visibleInput.setText("Type...");
        input.setText("|");
        input.setSelection(1);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        GlobalContext.hostIP = sharedPref.getString(SettingsActivity.ADDRESS_PREFS, "");
        GlobalContext.mouseSensitivity = sharedPref.getString(SettingsActivity.SENSITIVITY_PREFS, "");
        System.setProperty("java.net.preferIPv4Stack" , "true");
        input.addTextChangedListener(new TextWatcher() {
            String curWord="", lastWord="";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (!s.equals("|")) {
                    Networking n = new Networking();
                    if (!s.equals("")) {
                        s = String.valueOf(s.charAt(1));
                        n.execute(s, GlobalContext.hostIP);
                        System.out.println(s);
                        if (!s.equals(" ")){
                            curWord += s;
                        }
                        else {
                            lastWord = curWord;
                            curWord = "";
                        }
                    } else {
                        n.execute("DEL", GlobalContext.hostIP);
                        System.out.println("DEL");
                        if (!curWord.equals(""))
                            curWord = String.valueOf(curWord.subSequence(0, curWord.length() - 1));
                        else {
                            if (!lastWord.equals(""))
                                curWord = lastWord;
                        }
                    }
                    input.setText("|");
                    input.setSelection(1);
                    visibleInput.setText(curWord);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.show_mbuttons:
                item.setChecked(!item.isChecked());
                rightClick.setEnabled(item.isChecked());
                rightClick.setVisibility(item.isChecked() ? View.VISIBLE : View.INVISIBLE);
                leftClick.setEnabled(item.isChecked());
                leftClick.setVisibility(item.isChecked() ? View.VISIBLE : View.INVISIBLE);
                try {
                    trackPad.getLayoutParams().height += item.isChecked()?-200:200;
                    trackPad.setLayoutParams(trackPad.getLayoutParams());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.restart_server:
                Networking n = new Networking();
                n.execute("RESTARTCASTSERVER", GlobalContext.hostIP);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public Boolean toggleKB=false;
    public void showKeyboard(View v){
        toggleKB = !toggleKB;
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
        if (toggleKB){
            imm.showSoftInput(input, 0);
            ((Button) v).setText("Hide Keyboard");
        }
        else{
            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            ((Button) v).setText("Show Keyboard");
        }
    }
}

