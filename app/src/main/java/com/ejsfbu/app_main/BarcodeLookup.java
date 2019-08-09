package com.ejsfbu.app_main;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ejsfbu.app_main.Activities.AddGoalActivity;
import com.ejsfbu.app_main.Models.Product;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.parse.Parse.getApplicationContext;

public class BarcodeLookup {
    // Constant values for API requests
    public static final String BASE_URL = "https://api.barcodelookup.com/v2/products";
    public static final String API_KEY_PARAM = "key";
    public static final String API_KEY_BARCODE = "barcode";
    public static final String API_KEY_FORMAT = "formatted";
    // Tag for logging activity from api requests
    public static final String TAG = "BarcodeAPI";

    private AsyncHttpClient client1;

    public static void lookUpItem(String barcode, Context context) throws JSONException {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add(API_KEY_BARCODE, barcode);
        params.add(API_KEY_FORMAT, "y");
        params.add(API_KEY_PARAM, context.getResources().getString(R.string.barcode_api_key));
        client.get(BASE_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray array = response.optJSONArray("products");
                    Product product = new Product(array.optJSONObject(0));
                    ((AddGoalActivity) context).getProduct(product);
                } catch (JSONException e) {
                    logError("Failed to get product information", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                logError("Failed to get product information", throwable, true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONArray errorResponse) {
                logError("Failed to get product information", throwable, true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                logError("Failed to get product information", throwable, true);
            }
        });
    }

    // Handle errors: Log and alert user
    private static void logError(String message, Throwable error, Boolean alertUser) {
        Log.e(TAG, message, error);
        // Alert user
        if (alertUser) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
