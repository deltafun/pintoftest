package pintapp.pint.com.pint.PintNetworking;

/**
 * Created by gregoryjean-baptiste on 11/28/15.
 */
public interface IPintAPI {

    public void GetBloodDrivesByLocation(JSONAdapter jsonAdapter, ITokenProvider tokenProvider, String city, String state);
    public void GetUserNotifications(JSONAdapter jsonAdapter, ITokenProvider tokenProvider);
    public void GetBloodDriveUserNotifications(JSONAdapter jsonAdapter, ITokenProvider tokenProvider, long bdid);
    public void GetBloodDrive(JSONAdapter jsonAdapter, ITokenProvider tokenProvider, long bdid);
    public void execute();
    public void executeOne();
}
