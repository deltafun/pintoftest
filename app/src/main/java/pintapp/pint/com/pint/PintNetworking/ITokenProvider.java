package pintapp.pint.com.pint.PintNetworking;

import android.content.Context;

import pintapp.pint.com.pint.PintRequest;

/**
 * Created by gregoryjean-baptiste on 11/27/15.
 */
public interface ITokenProvider {
    public boolean hasToken(Context context);
    public String getToken(Context context);
    public void fetchToken(String email, String password, PintRequest pintRequest);
    public void setToken(String token, Context context);
    public void destroyToken(Context context);
    public String getEmail(Context context);
    public void setEmail(String email, Context context);
    public void destroyEmail(Context context);
}
