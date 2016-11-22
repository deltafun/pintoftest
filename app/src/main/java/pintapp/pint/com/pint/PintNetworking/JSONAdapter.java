package pintapp.pint.com.pint.PintNetworking;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import pintapp.pint.com.pint.PintType;
import pintapp.pint.com.pint.R;

/**
 * Created by gregoryjean-baptiste on 11/28/15.
 */

public class JSONAdapter extends ArrayAdapter<JSONObject> {

    public Context context;
    public List<JSONObject> jsonList;
    public JSONObject random;
    public int pintType;

    public JSONAdapter(Context context, List<JSONObject> objects, int pintType) {
        super(context, -1, objects);
        this.context = context;
        this.jsonList = objects;
        this.pintType = pintType;
    }

    public JSONObject getItem(int position){

        return jsonList.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int resource = (pintType == PintType.BLOODDRIVE) ? R.layout.list_element_blooddrive : R.layout.list_element_usernotification;
        View rowView = layoutInflater.inflate(resource, parent, false);

        JSONObject jsonObject = jsonList.get(position);

        if(pintType == PintType.BLOODDRIVE) {
            TextView title = (TextView) rowView.findViewById(R.id.bdTitle);
            TextView desc = (TextView) rowView.findViewById(R.id.bdShort);
            TextView dist = (TextView) rowView.findViewById(R.id.bdDistance);
            try {
                title.setText(jsonObject.getString("title"));
                desc.setText(jsonObject.getString("description"));
                dist.setText("5 miles");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            TextView title = (TextView) rowView.findViewById(R.id.unTitle);
            TextView desc = (TextView) rowView.findViewById(R.id.unShort);
            TextView from = (TextView) rowView.findViewById(R.id.unFrom);
            try {
                title.setText(jsonObject.getString("title"));
                desc.setText(jsonObject.getString("shortDescription"));
                from.setText("From: " + jsonObject.getString("bloodDriveTitle"));
                boolean hasSeen = jsonObject.getBoolean("hasSeen");

                if(!hasSeen)
                    title.setTextColor(Color.RED);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return rowView;
    }
}
