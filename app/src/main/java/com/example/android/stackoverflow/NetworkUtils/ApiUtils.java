package com.example.android.stackoverflow.NetworkUtils;

/**
 * Created by ashu on 06-04-2019.
 */

public class ApiUtils {

    public static final String BASE_URL = "https://api.stackexchange.com/";

    public static ApiInterface getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(ApiInterface.class);
    }
}
