package pintapp.pint.com.pint.PintNetworking;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by gregoryjean-baptiste on 11/28/15.
 */
public class GeneralRequest<T> extends Request<T> {
    private final Map<String, String> headers;
    private final Listener<T> listener;
    private final JSONObject body;

    public GeneralRequest(int method, String url, Map<String, String> headers, JSONObject body,
                         Listener<T> listener, Response.ErrorListener elistener) {
        super(method, url, elistener);
        this.headers = headers;
        this.body = body;
        this.listener = listener;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        Map<String, String> responseHeaders = response.headers;

        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            JSONArray jsonArray;

            if(json.equalsIgnoreCase("")) {
                jsonArray = new JSONArray();
            } else {
                jsonArray = new JSONArray(json);
            }

            return Response.success(jsonArray, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        //return super.getBody();
        try {

            if(this.body == null) {
                return super.getBody();
            }

            byte[] bytes = this.body.toString().getBytes("utf-8");
            return bytes;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return super.getBody();
        }
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }
}
