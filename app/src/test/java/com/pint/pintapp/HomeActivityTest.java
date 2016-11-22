package com.pint.pintapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenu;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.util.ActivityController;
import pintapp.pint.com.pint.BuildConfig;
import pintapp.pint.com.pint.HomeActivity;
import pintapp.pint.com.pint.PintNetworking.DefaultTokenProvider;
import pintapp.pint.com.pint.PintNetworking.ITokenProvider;
import pintapp.pint.com.pint.PintType;
import pintapp.pint.com.pint.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static junit.framework.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class HomeActivityTest {
    public static final String PINT_TYPE = "pintType";
    private ITokenProvider tokenProvider;
    private LocationListener mLocationListener = null;
    private double latitude;
    private double longitude;
    private String city;
    private String state;
    private Application application;
    private Context context;
    ViewPager mViewPager;
    HomeActivity.ListPagerAdapter adapter;
    final String[] TABS = {"Blood Drives", "Notifications"};

    @Mock
    Geocoder mockGeocoder;

    private ActivityController activityController;

    @InjectMocks
    private HomeActivity homeActivity;

    @Before
    public void setUp() {
        activityController = Robolectric.buildActivity(HomeActivity.class);
        //homeActivity = (HomeActivity) activityController.get();
        homeActivity = Robolectric.setupActivity(HomeActivity.class);
        application = RuntimeEnvironment.application;
        context = homeActivity.getBaseContext();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnCreate() throws Exception {
        assertNotNull(homeActivity);
    }

    @Test
    public void testActivitySetup() throws Exception {
        activityController.create().start();
        homeActivity = (HomeActivity) activityController.get();
        assertNotNull(homeActivity);
    }

    @Test
    public void testOnBackPressed() throws Exception {
        homeActivity.onBackPressed();
        assertNotNull(application);
    }

    @Test
    public void testOnCreateOptionsMenu() throws Exception{
        Menu menu = new RoboMenu();
        boolean value = homeActivity.onCreateOptionsMenu(menu);
        assertTrue(value);
    }

    @Test
    public void testOptionsSelected() throws  Exception {
        MenuItem item = new RoboMenuItem();
        boolean value = homeActivity.onOptionsItemSelected(item);
        assertFalse(value);
    }

    @Test
    public void testListPagerAdapterCreation() throws Exception {
        adapter = homeActivity.new
                ListPagerAdapter(homeActivity.getSupportFragmentManager());
        assertNotNull(adapter);
    }

    @Test
    public void testListPagerAdapterGetItemWithZero() throws Exception {
        adapter = homeActivity.new
                ListPagerAdapter(homeActivity.getSupportFragmentManager());
        int expected = PintType.BLOODDRIVE;
        Fragment fragment = adapter.getItem(0);
        int actual = fragment.getArguments().getInt(PINT_TYPE);
        assertEquals(expected, actual);
    }

    @Test
    public void testListPagerAdapterGetItemWithGreaterThanZero() throws Exception {
        adapter = homeActivity.new
                ListPagerAdapter(homeActivity.getSupportFragmentManager());
        int expected = PintType.USERNOTIFICATION;
        Fragment fragment = adapter.getItem(1);
        int actual = fragment.getArguments().getInt(PINT_TYPE);
        assertEquals(expected, actual);
    }

    @Test
    public void testListPagerAdapterGetItemWithLessThanZero() throws Exception {
        adapter = homeActivity.new
                ListPagerAdapter(homeActivity.getSupportFragmentManager());
        int expected = PintType.USERNOTIFICATION;
        Fragment fragment = adapter.getItem(-1);
        int actual = fragment.getArguments().getInt(PINT_TYPE);
        assertEquals(expected, actual);
    }

    @Test
    public void testListPagerAdapterGetCount() throws Exception {
        adapter = homeActivity.new
                ListPagerAdapter(homeActivity.getSupportFragmentManager());
        int expected = 2;
        int actual = adapter.getCount();
        assertEquals(expected, actual);
    }

    @Test
    public void testListPagerAdapterGetPageTitleWithValidZero() throws Exception {
        adapter = homeActivity.new
                ListPagerAdapter(homeActivity.getSupportFragmentManager());
        int position = 0;
        String expected = TABS[position];
        String actual = (String) adapter.getPageTitle(position);
        assertEquals(expected, actual);
    }

    @Test
    public void testListPagerAdapterGetPageTitleWithValidOne() throws Exception {
        adapter = homeActivity.new
                ListPagerAdapter(homeActivity.getSupportFragmentManager());
        int position = 1;
        String expected = TABS[position];
        String actual = (String) adapter.getPageTitle(position);
        assertEquals(expected, actual);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testListPagerAdapterGetPageTitleWithInvalidPosition() throws Exception {
        adapter = homeActivity.new
                ListPagerAdapter(homeActivity.getSupportFragmentManager());
        int position = 2;
        adapter.getPageTitle(position);
    }

    @Test
    public void testHomeActivityCreation() {
        ITokenProvider tokenProvider = mock(DefaultTokenProvider.class);
        LocationListener listener = mock(LocationListener.class);
        HomeActivity activity = new HomeActivity(tokenProvider, listener);
        assertNotNull(activity);
    }

    @Test
    public void testGetLocationFromAddress() throws Exception {
        Geocoder geocoder = mock(Geocoder.class);
        List<Address> addresses = new ArrayList<>();
        Address address = mock(Address.class);
        addresses.add(address);
        whenNew(Geocoder.class).withArguments(context).thenReturn(geocoder);
        // TODO: fix NoSuchMethodException. Might be paramters(?)
        when(geocoder.getFromLocationName(anyString(), anyInt())).thenReturn(addresses);
        homeActivity.getLocationFromAddress(context, "");
    }
}
