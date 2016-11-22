package pintapp.pint.com.pint;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pintapp.pint.com.pint.PintNetworking.DefaultPintAPI;
import pintapp.pint.com.pint.PintNetworking.DefaultTokenProvider;
import pintapp.pint.com.pint.PintNetworking.IPintAPI;
import pintapp.pint.com.pint.PintNetworking.JSONAdapter;

public class DetailedActivity extends AppCompatActivity {

    JSONObject jsonObject = null;
    long bloodDriveId;
    ListView listView;
    ArrayList<JSONObject> jsonObjects;
    DetailAdapter detailAdapter;
    IPintAPI pintAPI;
    IPintAPI pintAPI2;
    long notif_id;
    String[] detailedView = {"title", "map", "address", "times", "desc"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        try {
            jsonObject = new JSONObject(intent.getStringExtra("json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView = (ListView) findViewById(R.id.detailList);
        this.jsonObjects = new ArrayList<JSONObject>();
        detailAdapter = new DetailAdapter(this, jsonObjects);

        notif_id = intent.getLongExtra("notification", 0);

        if(notif_id > 0) {
            detailAdapter.random = new JSONObject();
        } else {
            detailAdapter.random = jsonObject;
        }

        for(int i = 0; i < detailedView.length; i++) {
            jsonObjects.add(new JSONObject());
        }

        listView.setAdapter(detailAdapter);

        try {
            bloodDriveId = jsonObject.getLong("bloodDriveId");

            if(pintAPI == null) {
                pintAPI = new DefaultPintAPI();
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position >= detailedView.length) {
                        int real = position - detailedView.length;
                        JSONObject jsonObject = jsonObjects.get(real);
                        try {
                            long sent = jsonObject.getLong("sentTime");
                            notif_id = sent;
                            detailAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            pintAPI.GetBloodDriveUserNotifications(detailAdapter, new DefaultTokenProvider(this), bloodDriveId);
            pintAPI.execute();

            if(notif_id > 0) {
                if(pintAPI2 == null) {
                    pintAPI2 = new DefaultPintAPI();
                }

                pintAPI2.GetBloodDrive(detailAdapter, new DefaultTokenProvider(this), bloodDriveId);
                pintAPI2.executeOne();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class DetailAdapter extends JSONAdapter {

        public DetailAdapter(Context context, List<JSONObject> array) {
            super(context, array, PintType.BLOODDRIVEUSERNOTIFICATION);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = null;

            if(random == null) {
                random = new JSONObject();
            }

            if(position < detailedView.length) {
                String type = detailedView[position];

                switch (type) {
                    case "title" : {
                        rowView = layoutInflater.inflate(R.layout.detail_text, parent, false);
                        TextView textView = (TextView) rowView.findViewById(R.id.detailText);
                        try {
                            textView.setText(random.getString("title"));
                            textView.setTextSize(28);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "map" : {
                        rowView = layoutInflater.inflate(R.layout.detail_image, parent, false);
                        ImageView imageView = (ImageView) rowView.findViewById(R.id.detailImage);
                        int image = getResources().getIdentifier("map", "drawable",getPackageName());
                        imageView.setImageResource(image);
                        break;
                    }
                    case "address" : {
                        rowView = layoutInflater.inflate(R.layout.detail_text, parent, false);
                        TextView textView = (TextView) rowView.findViewById(R.id.detailText);
                        try {
                            textView.setText(random.getString("address"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "times" : {
                        rowView = layoutInflater.inflate(R.layout.detail_text, parent, false);
                        TextView textView = (TextView) rowView.findViewById(R.id.detailText);
                        try {
                            textView.setText(random.getString("startTime") + " - " + random.getString("endTime"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "desc" : {
                        rowView = layoutInflater.inflate(R.layout.detail_text, parent, false);
                        TextView textView = (TextView) rowView.findViewById(R.id.detailText);
                        try {
                            textView.setText(random.getString("description"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            } else {
                rowView = layoutInflater.inflate(R.layout.bd_notification, parent, false);
                JSONObject jsonObject = jsonList.get(position - detailedView.length);
                TextView title = (TextView) rowView.findViewById(R.id.bdunTitle);
                TextView hint = (TextView) rowView.findViewById(R.id.openhint);
                try {
                    title.setText(jsonObject.getString("title"));
                    title.setTextSize(22);
                    hint.setText("Open to View");
                    boolean viewed = jsonObject.getBoolean("hasSeen");

                    if(!viewed) {
                        title.setTextColor(Color.RED);
                    }

                    long sent = jsonObject.getLong("sentTime");

                    if(sent == notif_id) {
                        TextView full = (TextView) rowView.findViewById(R.id.bdunText);
                        full.setText(jsonObject.getString("longDescription"));
                        hint.setText("Opened");

                        if(!viewed) {
                            jsonObject.put("hasSeen", true);
                            title.setTextColor(Color.BLACK);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return rowView;
        }
    }

}
