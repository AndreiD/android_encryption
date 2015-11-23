package com.androidadvance.encryptedapi;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidadvance.encryptedapi.security.AESCipher;
import com.androidadvance.encryptedapi.security.RSACipher;
import com.squareup.okhttp.ResponseBody;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewById Button button_aes;
    @ViewById Button button_rsa;
    @ViewById EditText editText_plain_data;
    @ViewById TextView textView_encrypted;
    @ViewById TextView textView_decrypted;
    @ViewById TextView textView_status;
    private MainActivity mContext;
    private long start_time;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = MainActivity.this;
    }

    @AfterViews void after__views() {
        editText_plain_data.setText("{\"id\": 4,\"email\":\"email@domain.com\",\"password\": \"a_secret_password\",\"firstName\": \"John\",\"phoneNumber\": \"11223344\", \"country\":\"España\" }");
    }

    @Click(R.id.button_aes) void aes_clicked() {

        //---- you can get it like this. delete this lines after you have the value.
//        String secret = AESCipher.HiddenKeys.encode("RFLDSKJ)U$#@FDSKJ@$#@#!@#E_This is a super secret key...");
//        Log.e("secret",secret);


        AESCipher myCipher = AESCipher.getInstance();
        String encrypted_s = myCipher.encrypt_string(editText_plain_data.getText().toString());

        textView_encrypted.setText(encrypted_s);
        textView_status.append("Encrypted" + "\n");


        textView_decrypted.setText(myCipher.decrypt_string(encrypted_s));

        start_time = System.currentTimeMillis();

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("time_on_device", System.currentTimeMillis());
        hm.put("encrypted_data", encrypted_s);

        Call<Object> call = mContext.getMyAPI().postToServer(hm);
        call.enqueue(new Callback<Object>() {
            @Override public void onResponse(Response<Object> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Object mPojo = response.body();
                    if (mPojo != null) {
                        textView_status.setText(mPojo.toString());
                    }
                    textView_status.append("\n done in " + String.valueOf(System.currentTimeMillis() - start_time) + " ms");
                } else {
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    Log.e("request failed", "Status code: " + String.valueOf(statusCode) + " ErrorBody:" + errorBody.toString());
                }
            }

            @Override public void onFailure(Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });


    }

    @Click(R.id.button_rsa) void rsa_clicked() {


        PublicKey pubKey = null;
        try {
            pubKey = new RSACipher.PublicKeyReader().get(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] encrypted_bytes = new byte[0];
        try {
            encrypted_bytes = RSACipher.encrypt(pubKey, editText_plain_data.getText().toString().getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        textView_encrypted.setText(new String(encrypted_bytes));

        PrivateKey privKey = null;
        try {
            privKey = new RSACipher.PrivateKeyReader().get(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        textView_decrypted.setText(new String(RSACipher.decrypt(privKey,encrypted_bytes)));



    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}

/***
 * Font: White Rabbit
 * Created By: Matthew Welch
 ***/