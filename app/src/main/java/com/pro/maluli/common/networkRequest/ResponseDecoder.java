package com.pro.maluli.common.networkRequest;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponseDecoder {
    public static String decode(String response) {
        try {
            JSONObject jo = null;
            try {
                jo = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (null != jo.getString("data")) {
                String jsonObject = jo.getString("data");
                if (!TextUtils.isEmpty(jsonObject)) {
                    if (TextUtils.equals("{", jsonObject.substring(0, 1))) {
                        JSONObject jsonObject1 = new JSONObject(jsonObject);
                        jo.put("data", jsonObject1);
                    } else if (TextUtils.equals("[", jsonObject.substring(0, 1))) {
                        JSONArray jsonArray = new JSONArray(jsonObject);
                        jo.put("data", jsonArray);
                    } else {
                        jo.put("data", jsonObject);
                    }
                    response = jo.toString();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}

