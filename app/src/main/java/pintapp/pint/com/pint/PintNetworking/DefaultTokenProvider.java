package pintapp.pint.com.pint.PintNetworking;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import pintapp.pint.com.pint.PintRequest;
import pintapp.pint.com.pint.R;

/**
 * Created by gregoryjean-baptiste on 11/27/15.
 */
public class DefaultTokenProvider implements ITokenProvider {

    public RequestQueue requestQueue;

    public DefaultTokenProvider(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public boolean hasToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.contains("token");
    }

    @Override
    public String getToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString("token", "");
    }

    @Override
    public void fetchToken(final String email, String password, final PintRequest pintRequest) {

        String url = "http://10.0.2.2:8181/api/m/login";
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json:charset=UTF-8");
        headers.put("Accept", "application/json, text/plain, */*");
        JSONObject body = new JSONObject();
        try {
            body.put("username", email);
            body.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            pintRequest.pintResponse(null);
        }

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, url, headers, body, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = (JSONObject) response;
                    String token = jsonObject.getString("X-AUTH-TOKEN");

                    if(token == "") {
                        pintRequest.pintResponse(null);
                    } else {
                        jsonObject.put("email", email);
                        pintRequest.pintResponse(jsonObject);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pintRequest.pintResponse(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    pintRequest.pintResponse(new JSONObject("{\"error\" : " + error.toString() + "}"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        requestQueue.add(customRequest);
    }

    @Override
    public void setToken(String token, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token", token);
        editor.commit();
    }

    @Override
    public void destroyToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("token");
        editor.commit();
    }

    @Override
    public String getEmail(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString("email", "");
    }

    @Override
    public void setEmail(String email, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", email);
        editor.commit();
    }

    @Override
    public void destroyEmail(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("email");
        editor.commit();
    }
}
