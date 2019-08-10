package com.ejsfbu.app_main;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ejsfbu.app_main.Activities.AddGoalActivity;
import com.ejsfbu.app_main.Models.Product;
import com.ejsfbu.app_main.Models.Request;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.parse.Parse.getApplicationContext;

public class BarcodeLookup {
    // Constant values for API requests
    public static final String BASE_URL = "https://api.barcodelookup.com/v2/products";
    public static final String API_KEY_PARAM = "key";
    public static final String API_KEY_BARCODE = "barcode";
    public static final String API_KEY_FORMAT = "formatted";
    // Tag for logging activity from api requests
    public static final String TAG = "BarcodeAPI";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                logError("Failed to get product information", throwable, true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                logError("Failed to get product information", throwable, true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
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

    // Send notification to firebase
    public static void sendNotification(String topic, String message) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        JSONObject payload = new JSONObject();
        JSONObject data = new JSONObject();
        JSONObject refresh = new JSONObject();

        try {
            refresh.put("action", "refresh");
            payload.put("title", "Penny");
            payload.put("body", message);
            payload.put("android_channel_id", "MyNotifications");
            payload.put("click_action","NOTIFICATION_OPEN");
            data.accumulate("notification", payload);
            data.accumulate("content_available", true);
            data.accumulate("to", "/topics/" + topic);
            data.accumulate("data", refresh);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity body = null;
        try {
            body = new StringEntity(data.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.addHeader("Authorization", "key=" + getApplicationContext().getResources().getString(R.string.legacy_firebase_key));
        client.addHeader("Content-Type", "application/json");
        client.post(getApplicationContext(), url, body, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(getApplicationContext(), "Parent notified for approval", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), "Failed to notify parent", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Toast.makeText(getApplicationContext(), "Failed to notify parent", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Toast.makeText(getApplicationContext(), "Failed to notify parent", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Failed to notify parent", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toast.makeText(getApplicationContext(), "something happened", Toast.LENGTH_LONG).show();
            }
        });
    }
}
