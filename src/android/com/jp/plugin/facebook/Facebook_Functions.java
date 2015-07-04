package com.jp.plugin.facebook;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by Jay Pratap Singh on 02-Jul-15.
 */
public class Facebook_Functions extends Activity  {

    private MessengerThreadParams mThreadParams;
    private boolean mPicking;
    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;
    CallbackManager callbackManager;
    String path_data;
    String method;
    CallbackContext callbackContext;
    String email_id;
    boolean is_share=false;
    String message_text;
    String file_path, mimetype, node;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        FacebookSdk.sdkInitialize(getApplicationContext());
        Intent intent = getIntent();

        Bundle b = getIntent().getExtras();
        method = b.getString("method");
        try {
            message_text = b.getString("message_text");
            file_path = b.getString("file_path");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        if (Intent.ACTION_PICK.equals(intent.getAction())) {
            mThreadParams = MessengerUtils.getMessengerThreadParamsForIntent(intent);
            mPicking = true;
        }


        if(method.equalsIgnoreCase("login")) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile",
                    "email"));
        }
        else if(method.equalsIgnoreCase("share"))
        {
            is_share = true;

            share_frm_native();
        }
        else if(method.equalsIgnoreCase("logout"))
        {
            try {
                LoginManager.getInstance().logOut();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", "logout_successfully");
                Facebook_Plugin fp = new Facebook_Plugin();
                fp.onActivityResult(0, 0, returnIntent);
                finish();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else if(method.equalsIgnoreCase("is_logged_in")) {
            try {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                Intent returnIntent = new Intent();
                if(accessToken != null)
                {
                    returnIntent.putExtra("result", "loggedin");
                    Facebook_Plugin fp = new Facebook_Plugin();
                    fp.onActivityResult(0, 0, returnIntent);
                }
                else
                {
                    returnIntent.putExtra("result", "notloggedin");
                    Facebook_Plugin fp = new Facebook_Plugin();
                    fp.onActivityResult(0, 0, returnIntent);

                }
                finish();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // login successful
                        GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject me, GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                        } else {
                                            email_id = me.optString("email");
                                            try {
                                                Intent returnIntent = new Intent();
                                                returnIntent.putExtra("result", email_id);
                                                Facebook_Plugin fp = new Facebook_Plugin();
                                                fp.onActivityResult(0, 0, returnIntent);
                                            }
                                            catch(Exception e)
                                            {
                                                e.printStackTrace();
                                            }
                                            if(is_share)
                                            {
                                                share_frm_native();
                                            }
                                            else {
                                                finish();
                                            }
                                        }
                                    }
                                }).executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        finish();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // login error
                        exception.printStackTrace();
                        finish();
                    }
                });
    }

    void login_frm_sm() {

        try {
            LoginManager.getInstance().logInWithPublishPermissions(Facebook_Functions.this, Arrays.asList("publish_actions"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    void share_frm_native() {
        try {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();

            if(accessToken != null)
            {

                if(file_path.indexOf("images")>0 || file_path.indexOf("png")>0
                        || file_path.indexOf("jpg")>0 || file_path.indexOf("jpeg")>0 || file_path.indexOf("gif")>0)
                {
                    mimetype="me/photos";
                    node="source";
                }
                else if(file_path.indexOf("mov")>0 || file_path.indexOf("avi")>0
                        || file_path.indexOf("mp4")>0 || file_path.indexOf("3gp")>0 || file_path.indexOf("gif")>0
                        || file_path.indexOf("wmv")>0 || file_path.indexOf("mpg")>0 || file_path.indexOf("mpeg")>0 )
                {
                    mimetype="me/videos";
                    node="video.mov";
                }
                else {
                    mimetype="me/feed";
                    node="message";
                }

                if(file_path.length()>0)
                {
                    file_path = file_path.trim().replace("file:///", "");
                }

                final Bundle bundle = new Bundle();

                if(node.contains("source")) {
                    try{
                        FileInputStream fis = new FileInputStream(new File(file_path.trim()));
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] buf = new byte[1024];
                        for (int readNum; (readNum = fis.read(buf)) != -1;)
                        {
                            bos.write(buf, 0, readNum);
                        }
                        byte[] data = bos.toByteArray();
                        bundle.putByteArray("source", data);
                        bundle.putString("message", message_text);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                else if(node.contains("video")) {
                    try{
                        InputStream is = new FileInputStream(file_path);
                        byte[] data = readBytes(is);
                        bundle.putByteArray("video.mov", data);
                        bundle.putString("description", message_text);
                        bundle.putString("contentType", "video/quicktime");
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                else{
                    bundle.putString("message", message_text);
                }

                GraphRequest request = GraphRequest.newPostRequest(accessToken,
                        mimetype, null, new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                Log.d("Graph_POST", "" + response);
                                finish();
                            }
                        });

                request.setParameters(bundle);
                request.executeAsync();
            }
            else
            {
                login_frm_sm();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public byte[] readBytes(InputStream inputStream) throws IOException {

        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
