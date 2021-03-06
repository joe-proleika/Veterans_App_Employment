package com.Senior_Proj_Fall_2015.Veterans_App_Employment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Joe on 10/17/2015.
 * <p/>
 * Login page. Users and Employers log in the same way.
 */
public class Login extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        Button submit_OK =
            (Button) findViewById(R.id.submit_OK);
        submit_OK.setOnClickListener(this);
    }

    /**
     * Verifies the username and password are a match. If not, clears the input zones and asks the user to try again. If
     * so, moves to MenuPage with the corresponding Profile logged in.
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_OK:
                final Handler h = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (StartPage.client.getUserID() == null) {
                            ((TextView) findViewById(R.id.login_status)).setText("Username or Password incorrect, try again.");
                            ((EditText) findViewById(R.id.login_username)).getText().clear();
                            ((EditText) findViewById(R.id.login_password)).getText().clear();
                        } else {
                            Intent i = new Intent(Login.this, MenuPage.class);
                            startActivity(i);
                        }
                    }
                };

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        StartPage.client.login(
                            ((EditText) findViewById(R.id.login_username)).getText().toString(),
                            ((EditText) findViewById(R.id.login_password)).getText().toString());
                        while (!StartPage.client.getIsTaskDone()) {
                            SystemClock.sleep(50);
                        }
                        h.sendEmptyMessage(0);
                    }
                });
                thread.start();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
