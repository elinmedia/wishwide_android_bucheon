package com.media.mobile.elin.wishwidemobile.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.media.mobile.elin.wishwidemobile.R;
import com.media.mobile.elin.wishwidemobile.SharedPreferencesConstant;

public class LoadingActivity extends AppCompatActivity implements SharedPreferencesConstant {
    private static final String TAG = "LoadingActivity";

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        mSharedPreferences = this.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);


        //로딩창 2초간 띄우기
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                String customerPhone = mSharedPreferences.getString(CUSTOMER_PHONE_KEY, "");

                //preferences shared 사용해 로그인 이력 있는지 확인
                Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                Log.d(TAG, "전화번호 확인: " + customerPhone);
                if (!(customerPhone.equals(""))) {
                    intent.putExtra("responseCode", "AUTO");
                    intent.putExtra("customerPhone", customerPhone);
                }
                else {
                    intent.putExtra("responseCode", "LOGIN");
                }

                startActivity(intent);
            }
        }, 1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
