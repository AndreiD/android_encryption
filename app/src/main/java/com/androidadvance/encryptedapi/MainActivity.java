package com.androidadvance.encryptedapi;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidadvance.encryptedapi.security.RSAHelper;
import com.squareup.okhttp.ResponseBody;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.androidadvance.encryptedapi.security.AESHelper;

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

        AESHelper myCipher = AESHelper.get_instance("fldksaj3rifldsfjasdfsafdlsfak"); // <-- keep this string secret.

        //---- AES Encryption -----
        byte[] bytes_to_be_encripted = new byte[0];
        try {
            bytes_to_be_encripted = editText_plain_data.getText().toString().getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] encrypted_bytes = myCipher.encrypt(bytes_to_be_encripted);
        textView_encrypted.setText(new String(encrypted_bytes));

        //--- AES Decryption -------
        byte[] decrypted_bytes = myCipher.decrypt(encrypted_bytes);
        textView_decrypted.setText(new String(decrypted_bytes));


        //---------- SERVER COMMUNICATION ----------
        start_time = System.currentTimeMillis();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("time_on_device", System.currentTimeMillis());
        hm.put("encrypted_data", new String(encrypted_bytes));

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
                    textView_status.setText("Status code: " + String.valueOf(statusCode) + " ErrorBody:" + errorBody.toString());
                }
            }

            @Override public void onFailure(Throwable t) {
                Log.e("onFailure", t.getMessage());
                textView_status.setText("server: " + t.getMessage());
            }
        });
        //---------- END SERVER PART ----------
    }


    @Click(R.id.button_rsa) void rsa_clicked() {

        byte[] bytes_to_be_encrypted = new byte[0];
        try {
            bytes_to_be_encrypted = editText_plain_data.getText().toString().getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //---- RSA Encryption -----
        byte[] encrypted_bytes = RSAHelper.encrypt(RSAHelper.get_instance().getPublicKey(mContext), bytes_to_be_encrypted);
        textView_encrypted.setText(new String(encrypted_bytes));

        //---- RSA Decrypt -----
        byte[] decrypted_bytes = RSAHelper.decrypt(RSAHelper.get_instance().getPrivateKey(mContext), encrypted_bytes);
        textView_decrypted.setText(new String(decrypted_bytes));


        //---------- SERVER COMMUNICATION ----------
        start_time = System.currentTimeMillis();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("time_on_device", System.currentTimeMillis());
        hm.put("encrypted_data", new String(encrypted_bytes));

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
                    textView_status.setText("Status code: " + String.valueOf(statusCode) + " ErrorBody:" + errorBody.toString());
                }
            }

            @Override public void onFailure(Throwable t) {
                Log.e("onFailure", t.getMessage());
                textView_status.setText("server: " + t.getMessage());
            }
        });
        //---------- END SERVER PART ----------

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