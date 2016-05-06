package com.hendiware.hellogcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mEmoZz on 4/30/16.
 * muhamed.gendy@gmail.com
 */
public class RegistrationService extends IntentService {

    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;


    public RegistrationService() {
        super("RegistrationService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("Registration Service :", "Hello .....");
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = preferences.edit();

        InstanceID instanceID = InstanceID.getInstance(this);
        Log.e("Registration Service :", "try get token  .....");

        try {
            String token = instanceID.getToken(getString(R.string.google_app_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.e("Registration Service :", "get Token is " + token);
            Log.i("Registration Service :", "GCM Registration Token: " + token);

            if (!preferences.getBoolean("token_sent", false))
                sendTokenToServer(token);
            else
                this.stopSelf();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Registration Service", "Error :get Token Failed !");

        }

    }


    private void sendTokenToServer(final String token) {
        String ADD_TOKEN_URL = "http://developerhendy.16mb.com/addnewtoken.php";
        StringRequest request = new StringRequest(Request.Method.POST, ADD_TOKEN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int responseCode = Integer.parseInt(response);
                if (responseCode == 1) {
                    prefEditor.putBoolean("token_sent", true).apply();
                    Log.e("Registration Service", "Response : Send Token Success");

                } else {
                    prefEditor.putBoolean("token_sent", false).apply();
                    Log.e("Registration Service", "Response : Send Token Failed");


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prefEditor.putBoolean("token_sent", false).apply();
                Log.e("Registration Service", "Error :Send Token Failed");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;

            }
        };

        Volley.newRequestQueue(this).add(request);

    }
}
