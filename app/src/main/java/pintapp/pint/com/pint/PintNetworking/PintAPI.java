package pintapp.pint.com.pint.PintNetworking;

/**
 * Created by gregoryjean-baptiste on 11/28/15.
 */
public class PintAPI {

    public static final String GET_BLOODDRIVES_BY_LOCATION =
            "http://10.0.2.2:8181/api/donor/getBloodDrivesByLocation";

    public static final String GET_USER_NOTIFICATIONS = "http://10.0.2.2:8181/api/donor/getUserNotifications";

    public static final String GET_BLOODDRIVE_USER_NOTIFICATIONS = "http://10.0.2.2:8181/api/donor/getBloodDriveUserNotifications";

    public static final String GET_BLOODDRIVE = "http://10.0.2.2:8181/api/donor/getBloodDrive";

    public String url;
    public JSONAdapter jsonAdapter;
    public ITokenProvider tokenProvider;

    public PintAPI(JSONAdapter jsonAdapter, ITokenProvider tokenProvider) {
        this.jsonAdapter = jsonAdapter;
        this.tokenProvider = tokenProvider;
    }
}
