package com.Senior_Proj_Fall_2015.Veterans_App_Employment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Joe on 11/7/2015.
 * <p/>
 * Populates list of all employers registered in the database.
 */
public class EmployerListPage extends Activity {

    ArrayList<HashMap<String, String>> employerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_list);
        new EmployerListCreation().execute();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private class EmployerListCreation extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... p) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    StartPage.client.loadEmployers();
                    while (!StartPage.client.getIsTaskDone()) {
                        SystemClock.sleep(50);
                    }
                }
            });
            thread.start();
            return new String("");
        }

        /**
         * Where the list is generated, populated, and shown on-screen.
         *
         * @param string
         */
        @Override
        protected void onPostExecute(String string) {
            for (int i = 0; i < StartPage.dk.getEmployerList().length(); i++) {
                StartPage.dk.setEmployerProfile(i);
                String company = StartPage.dk.getEmployerDetail("company");
                String address = StartPage.dk.getEmployerDetail("address");
                String phone = StartPage.dk.getEmployerDetail("phone");
                String email = StartPage.dk.getEmployerDetail("email");

                HashMap<String, String> map = new HashMap<>();

                if (!company.equals("null")) {
                    map.put("company", company);
                } else {
                    map.put("company", "No company entered.");
                }
                if (!address.equals("null")) {
                    map.put("address", address);
                } else {
                    map.put("address", "No address entered.");
                }
                if (!phone.equals("null")) {
                    map.put("phone", phone);
                } else {
                    map.put("phone", "No phone number entered.");
                }
                if (!email.equals("null")) {
                    map.put("email", email);
                } else {
                    map.put("email", "No e-mail address entered.");
                }

                employerList.add(map);
                ListView list = (ListView) findViewById(R.id.list_employers);
                ListAdapter adapter = new SimpleAdapter(EmployerListPage.this,
                    employerList,
                    R.layout.activity_employer_list_item,
                    new String[]{"company", "address", "phone", "email"},
                    new int[]{R.id.textView_employer_list_company,
                        R.id.textView_employer_list_address,
                        R.id.textView_employer_list_phone_number,
                        R.id.textView_employer_list_email_address});

                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        StartPage.dk.setEmployerProfile(position);
                        Intent j = new Intent(EmployerListPage.this, EmployerProfilePage.class);
                        startActivity(j);
                    }
                });
            }
        }
    }
}
