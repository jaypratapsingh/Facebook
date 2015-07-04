package com.jp.plugin.facebook;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.squareup.okhttp.internal.DiskLruCache;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class Facebook_Plugin extends CordovaPlugin {

    String file_path="", message_text="";
    CallbackManager callbackManager;
    static CallbackContext callbackContext;

    @Override
    public boolean execute(String actionString, JSONArray dataString, CallbackContext callbackContext) throws JSONException{
        try
        {
            try {
                JSONObject json_object = dataString.getJSONObject(0);
                file_path = json_object.getString("file_path");
                message_text = json_object.getString("message_text");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            this.callbackContext = callbackContext;

            if(actionString.equalsIgnoreCase("messenger")) {

                Intent call_class = new Intent(cordova.getActivity(),Facebook_Functions.class);
                Bundle b = new Bundle();
                b.putString("file_path", file_path);
                b.putString("method", "messenger");
                call_class.putExtras(b);
                cordova.getActivity().startActivity(call_class);

            }
            else if(actionString.equalsIgnoreCase("share")) {

                Intent call_class = new Intent(cordova.getActivity(),Facebook_Functions.class);
                Bundle b = new Bundle();
                b.putString("file_path", file_path);
                b.putString("message_text", message_text);
                b.putString("method", "share");
                call_class.putExtras(b);

                PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
                result.setKeepCallback(true);
                this.callbackContext.sendPluginResult(result);

                cordova.getActivity().startActivity(call_class);

            }
            else if(actionString.equalsIgnoreCase("login"))
            {
                Intent call_class = new Intent(cordova.getActivity(),Facebook_Functions.class);
                Bundle b = new Bundle();
                b.putString("method", "login");
                call_class.putExtras(b);

                PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
                result.setKeepCallback(true);
                this.callbackContext.sendPluginResult(result);

                cordova.getActivity().startActivityForResult(call_class, 0);
            }
            else if(actionString.equalsIgnoreCase("logout"))
            {
                Intent call_class = new Intent(cordova.getActivity(),Facebook_Functions.class);
                Bundle b = new Bundle();
                b.putString("method", "logout");
                call_class.putExtras(b);

                PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
                result.setKeepCallback(true);
                this.callbackContext.sendPluginResult(result);

                cordova.getActivity().startActivityForResult(call_class, 0);
            }
            else if(actionString.equalsIgnoreCase("check_logged_in"))
            {
                Log.d("aaaaaaaa",""+actionString);
                Intent call_class = new Intent(cordova.getActivity(),Facebook_Functions.class);
                Bundle b = new Bundle();
                b.putString("method", "is_logged_in");
                call_class.putExtras(b);

                PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
                result.setKeepCallback(true);
                this.callbackContext.sendPluginResult(result);

                cordova.getActivity().startActivityForResult(call_class, 0);
            }
            else
            {
                callbackContext.error("Please select correct method");
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PluginResult result = new PluginResult(PluginResult.Status.OK,""+data.getStringExtra("result"));
        result.setKeepCallback(false);
        if (callbackContext != null) {
            callbackContext.sendPluginResult(result);
            callbackContext = null;
        }
    }
}