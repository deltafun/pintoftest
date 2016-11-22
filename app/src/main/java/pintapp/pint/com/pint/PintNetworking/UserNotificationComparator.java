package pintapp.pint.com.pint.PintNetworking;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

/**
 * Created by gregoryjean-baptiste on 11/28/15.
 */
public class UserNotificationComparator implements Comparator<JSONObject> {
    @Override
    public int compare(JSONObject lhs, JSONObject rhs) {
        try {
            long left = lhs.getLong("sentTime");
            long right = rhs.getLong("sentTime");

            if(left > right) {
                return -1;
            } else if(right > left) {
                return 1;
            } else {
                return 0;
            }

        } catch (JSONException e) {
            return 0;
        }
    }
}
