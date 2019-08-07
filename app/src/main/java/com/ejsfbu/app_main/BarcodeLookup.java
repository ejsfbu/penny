package com.ejsfbu.app_main;

import android.app.Application;
import android.content.Context;

import com.ejsfbu.app_main.Models.Product;
import com.ejsfbu.app_main.Models.Request;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

import java.util.Collections;

public class BarcodeLookup {
    // Constant values for API requests
    public static final String BASE_URL = "https://api.barcodelookup.com/v2/products";
    public static final String API_KEY_PARAM = "key";
    public static final String API_KEY_BARCODE = "barcode";
    public static final String API_KEY_FORMAT = "formatted";
    // Tag for logging activity from api requests
    public static final String TAG = "BarcodeAPI";

    private AsyncHttpClient client1;

//    public static Product lookUpItem(Context context, String barcode) throws JSONException {
//        AsyncHttpClient client = new AsyncHttpClient();
//        RequestParams params = new RequestParams();
//        params.add(API_KEY_BARCODE, barcode);
//        params.add(API_KEY_FORMAT, "y");
//        params.add(API_KEY_PARAM, context.getResources().getString(R.string.barcode_api_key));
//        // TODO
//
//    }
}
