package com.example.android.stackoverflow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    static final String CLIENT_ID = "14813";
    private static final String OAUTH_SCOPE = "no_expiry";
    //From StackExchange Console
    private static final String CODE = "code";
    private static final String REDIRECT_URI = "https://stackexchange.com/oauth/login_success";

    //Authorization
    static String AUTHORIZATION_CODE;

    public static void setSystemBarColor(Activity act, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(color));
        }
    }

    public static void setSystemBarLight(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = act.findViewById(android.R.id.content);
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        setSystemBarColor(this, android.R.color.white);
        setSystemBarLight(this);

        SharedPreferences sharedPref = getSharedPreferences("authInfo", Context.MODE_PRIVATE);
        if (!(sharedPref.getString("AuthCode", "").isEmpty())) {
            Intent in = new Intent(this, UserInterestActivity.class);
            startActivity(in);
            finish();
        }

        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://stackoverflow.com/oauth/dialog"
                        + "?client_id=" + CLIENT_ID + "&response_type=" + CODE + "&redirect_uri="
                        + REDIRECT_URI + "&scope=" + OAUTH_SCOPE))
                        .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_FROM_BACKGROUND);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                    finish();
                }

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri data = getIntent().getData();

        if (data != null && !TextUtils.isEmpty(data.getScheme())) {
            String url = data.toString();
            String code = url.substring(url.indexOf("access_token=") + 13);

            if (!TextUtils.isEmpty(code)) {

                //Success Toast
                Toast.makeText(LoginActivity.this, getString(R.string.success), Toast.LENGTH_LONG).show();
                AUTHORIZATION_CODE = code;
                saveData();
                Intent i = new Intent(LoginActivity.this, UserInterestActivity.class);
                startActivity(i);

                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(this, "Some Error occurred", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }


    public void saveData() {

        SharedPreferences.Editor sharedPref = getSharedPreferences("authInfo", Context.MODE_PRIVATE).edit();
        sharedPref.putString("AuthCode", AUTHORIZATION_CODE);
        sharedPref.apply();

    }
}
