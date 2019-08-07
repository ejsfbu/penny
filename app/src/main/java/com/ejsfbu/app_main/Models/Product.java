package com.ejsfbu.app_main.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Product {

    private String name;
    private String imageUrl;
    private Double price;

    public Product(JSONObject object) throws JSONException {
        name = object.optString("product_name");
        if (name.isEmpty()) { name = "Item not found"; }
        JSONArray array = object.optJSONArray("images");
        if (array != null || array.length() != 0) {
            imageUrl = array.optString(0);
        }
        price = getPriceFromJson(object.optJSONArray("stores"));
    }

    private Double getPriceFromJson(JSONArray array) throws JSONException {
        if (array != null) {
            JSONObject store = array.getJSONObject(0);
            return store.optDouble("store_price");
        } else {
            return 0.0;
        }
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Double getPrice() {
        return price;
    }
}
