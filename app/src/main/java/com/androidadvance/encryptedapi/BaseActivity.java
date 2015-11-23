package com.androidadvance.encryptedapi;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.androidadvance.encryptedapi.api.MyApi;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class BaseActivity extends AppCompatActivity {


    private MyApi myAPI;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        //-------- set app fonts ---------
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/whitrabt.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );


        //--------- retrofit shit ------
        Retrofit mretrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myAPI = mretrofit.create(MyApi.class);

    }

    public MyApi getMyAPI() {
        return myAPI;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}