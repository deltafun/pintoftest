package pintapp.pint.com.pint.PintNetworking;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;

/**
 * Created by gregoryjean-baptiste on 11/28/15.
 */
public class DefaultPintAPI implements IPintAPI {

    public PintAPI api;
    RequestQueue requestQueue;

    public void GetBloodDrivesByLocation(JSONAdapter jsonAdapter, ITokenProvider tokenProvider, String city, String state) {
        api = new PintAPI(jsonAdapter, tokenProvider);
        api.url = PintAPI.GET_BLOODDRIVES_BY_LOCATION + "/" + city + "/" + state;
    }

    public void GetUserNotifications(JSONAdapter jsonAdapter, ITokenProvider tokenProvider) {
        api = new PintAPI(jsonAdapter, tokenProvider);
        api.url = PintAPI.GET_USER_NOTIFICATIONS;
    }

    @Override
    public void GetBloodDriveUserNotifications(JSONAdapter jsonAdapter, ITokenProvider tokenProvider, long bdid) {
        api = new PintAPI(jsonAdapter, tokenProvider);
        api.url = PintAPI.GET_BLOODDRIVE_USER_NOTIFICATIONS + "/" + bdid;
    }

    @Override
    public void GetBloodDrive(JSONAdapter jsonAdapter, ITokenProvider tokenProvider, long bdid) {
        api = new PintAPI(jsonAdapter, tokenProvider);
        api.url = PintAPI.GET_BLOODDRIVE + "/" + bdid;
    }

    @Override
    public void execute() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("X-AUTH-TOKEN", api.tokenProvider.getToken(api.jsonAdapter.context));
        Log.d("DefaultPintAPI", headers.get("X-AUTH-TOKEN"));

        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(api.jsonAdapter.context);

        GeneralRequest customRequest = new GeneralRequest(Request.Method.GET, api.url, headers, null, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONArray jsonArray = (JSONArray) response;
                    Log.d("DefaultPintAPI", response.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        api.jsonAdapter.jsonList.add(i, jsonArray.getJSONObject(i));
                    }

                    Collections.sort(api.jsonAdapter.jsonList, new UserNotificationComparator());

                    api.jsonAdapter.notifyDataSetChanged();
                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DefaultPintAPI", error.toString());

            }
        });

        requestQueue.add(customRequest);
    }

    @Override
    public void executeOne() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("X-AUTH-TOKEN", api.tokenProvider.getToken(api.jsonAdapter.context));
        Log.d("DefaultPintAPI+1", headers.get("X-AUTH-TOKEN"));

        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(api.jsonAdapter.context);

        CustomRequest customRequest = new CustomRequest(Request.Method.GET, api.url, headers, null, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                JSONObject jsonObject = (JSONObject) response;
                Log.d("DefaultPintAPI", response.toString());

                api.jsonAdapter.random = jsonObject;
                api.jsonAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DefaultPintAPI", error.toString());

            }
        });

        requestQueue.add(customRequest);
    }
}
