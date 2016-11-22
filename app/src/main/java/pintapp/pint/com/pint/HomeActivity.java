package pintapp.pint.com.pint;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pintapp.pint.com.pint.PintNetworking.DefaultPintAPI;
import pintapp.pint.com.pint.PintNetworking.DefaultTokenProvider;
import pintapp.pint.com.pint.PintNetworking.IPintAPI;
import pintapp.pint.com.pint.PintNetworking.ITokenProvider;
import pintapp.pint.com.pint.PintNetworking.JSONAdapter;

public class HomeActivity extends AppCompatActivity {

    private ITokenProvider tokenProvider;
    private LocationListener mLocationListener = null;
    private double latitude;
    private double longitude;
    private String city;
    private String state;
    ViewPager mViewPager;
    ListPagerAdapter listPagerAdapter;
    String[] tabs = {"Blood Drives", "Notifications"};

    public HomeActivity() {
        super();
    }

    public HomeActivity(ITokenProvider tokenProvider, LocationListener locationListener) {
        super();
        this.tokenProvider = tokenProvider;
        this.mLocationListener = locationListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (tokenProvider == null) {
            tokenProvider = new DefaultTokenProvider(this);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tokenProvider.destroyToken(getApplicationContext());
                tokenProvider.destroyEmail(getApplicationContext());
                Intent nextActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(nextActivity);
            }
        });

        //getLocationFromAddress(this, "3000 NE 150 st, Miami, FL");
        //distanceBetween(double startLatitude, double startLongitude, double endLatitude, double endLongitude, float[] results)

        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }

        if(mLocationListener == null)
            mLocationListener = new DefaultLocationListener();

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, mLocationListener);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        listPagerAdapter = new ListPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(listPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return;
            }
            Address location = address.get(0);
            Log.d("HomeActivity",location.getLatitude() + "");
            Log.d("HomeActivity", location.getLongitude() + "");

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //Do Nothing
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            tokenProvider.destroyToken(this);
            tokenProvider.destroyEmail(this);
            Intent nextActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(nextActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ListPagerAdapter extends FragmentPagerAdapter {

        public ListPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            android.support.v4.app.Fragment fragment = new ListFragment();
            Bundle args = new Bundle();
            // Our object is just an integer :-P

            if(position == 0) {
                args.putInt("pintType", PintType.BLOODDRIVE);
            } else {
                args.putInt("pintType", PintType.USERNOTIFICATION);
            }

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

    public static class ListFragment extends android.support.v4.app.Fragment {
        ListView pintList;
        ArrayList<JSONObject> pintObjects;
        Context context;
        JSONAdapter jsonAdapter;
        IPintAPI pintAPI;
        int pintType;

        public ListFragment() {
            super();
            this.pintAPI = new DefaultPintAPI();
        }

        public void OnAttach(Context context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                super.onAttach(context);
            }
            this.context = context;
        }

        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(
                    R.layout.tab_home, container, false);

            pintList = (ListView) rootView.findViewById(R.id.homeListViewList);
            pintObjects = new ArrayList<JSONObject>();
            Bundle args = getArguments();
            pintType = args.getInt("pintType", PintType.BLOODDRIVE);
            jsonAdapter = new JSONAdapter(getContext(), pintObjects, pintType);
            pintList.setAdapter(jsonAdapter);

            pintList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getContext(), DetailedActivity.class);
                    intent.putExtra("json", pintObjects.get(position).toString());

                    if(pintType == PintType.USERNOTIFICATION) {
                        try {
                            intent.putExtra("notification", pintObjects.get(position).getLong("sentTime"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    startActivity(intent);
                }
            });

            return rootView;
        }

        public void onStart() {
            super.onStart();

            DefaultTokenProvider defaultTokenProvider = new DefaultTokenProvider(getContext());
            String email = defaultTokenProvider.getEmail(getContext());

            if(pintType == PintType.BLOODDRIVE) {
                pintAPI.GetBloodDrivesByLocation(jsonAdapter, defaultTokenProvider, "Miami", "FL");
            } else {
                pintAPI.GetUserNotifications(jsonAdapter, defaultTokenProvider);
            }

            if(!jsonAdapter.jsonList.isEmpty())
                return;

            pintAPI.execute();
        }
    }

    private class DefaultLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            city = "Miami";
            state = "FL";
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
