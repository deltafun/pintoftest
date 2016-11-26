package com.pint.pintapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.test.mock.MockContext;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenu;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.RoboLayoutInflater;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;
import org.robolectric.util.ActivityController;
import org.robolectric.util.FragmentTestUtil;
import pintapp.pint.com.pint.*;
import pintapp.pint.com.pint.PintNetworking.DefaultTokenProvider;
import pintapp.pint.com.pint.PintNetworking.ITokenProvider;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.jar.Manifest;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

// TODO: refactor class
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

    private ActivityController activityController;

    private HomeActivity homeActivity;

    @Before
    public void setUp() {
        activityController = Robolectric.buildActivity(HomeActivity.class);
        //homeActivity = (HomeActivity) activityController.get();
        homeActivity = Robolectric.setupActivity(HomeActivity.class);
        application = RuntimeEnvironment.application;
        context = homeActivity.getBaseContext();
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
    public void testHomeActivityCreation() {
        ITokenProvider tokenProvider = mock(DefaultTokenProvider.class);
        LocationListener listener = mock(LocationListener.class);
        HomeActivity activity = new HomeActivity(tokenProvider, listener);
        assertNotNull(activity);
    }

    @Test
    public void testOnOptionItemsSelectedSelection() {
        MenuItem roboMenuItem = new RoboMenuItem(R.id.action_settings);
        boolean actual = homeActivity.onOptionsItemSelected(roboMenuItem);
        assertTrue(actual);
    }

    @Test (expected = NoSuchMethodError.class)
    public void testOnCreateHighSdk() {
        try {
            setFinalStatic(Build.VERSION.class.getField("SDK_INT"),
                    Build.VERSION_CODES.M);
        } catch (Exception e) {
            e.printStackTrace();
        }

        homeActivity = Robolectric.setupActivity(HomeActivity.class);
    }

    // ListPager **************************************************************

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

    // ListFragment ************************************************************

    @Test
    public void testListFragmentCreation() {
        HomeActivity.ListFragment fragment = new HomeActivity.ListFragment();
        assertNotNull(fragment);
    }

    @Test
    public void testListFragmentOnAttachWithSdkNotEqual23() {
        HomeActivity.ListFragment fragment = new HomeActivity.ListFragment();
        Context mockContext = new MockContext();

        fragment.OnAttach(mockContext);
        assertEquals(mockContext, fragment.getActivity());
    }

    @Test
    public void listFragmentOnAttachWithSdk23ShouldAttachContext() {
        // set sdk to 23 using reflection. would be better with robolectric
        try {
            setFinalStatic(Build.VERSION.class.getField("SDK_INT"),
                    Build.VERSION_CODES.M);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HomeActivity.ListFragment fragment = new HomeActivity.ListFragment();
        Context mockContext = new MockContext();

        fragment.OnAttach(mockContext);
        assertEquals(mockContext, fragment.getActivity());
    }

    @Test
    public void ListFragmentOnCreateViewShouldInitializeListenerThatStartedDetailedActivityIntent() throws Exception {
        HomeActivity.ListFragment fragment = new HomeActivity.ListFragment();

        // mocks
        View mockView = mock(View.class);
        JSONObject mockJSONObject = mock(JSONObject.class);

        // go thru fragment to lifecycle to start it
        fragment.onAttach(context);
        fragment.OnAttach(context);
        Bundle bundle = new Bundle();
        bundle.putInt("pintType", PintType.BLOODDRIVE);
        fragment.setArguments(bundle);
        fragment.onCreate(bundle);
        SupportFragmentTestUtil.startVisibleFragment(fragment);

        // gain access to private `pintList` member variable
        Field pintListField = fragment.getClass().getDeclaredField("pintList");
        pintListField.setAccessible(true);
        // gain access to private `pintObjects` member variable
        Field pintObjectsField = fragment.getClass().getDeclaredField("pintObjects");
        pintObjectsField.setAccessible(true);

        // get pintObjects. make pintObjects non-empty
        ArrayList<JSONObject> pintObjects =
                (ArrayList<JSONObject>) pintObjectsField.get(fragment);
        pintObjects.add(mockJSONObject);

        // get pintList so we can get method held in anonymous inner class
        ListView pintList = (ListView) pintListField.get(fragment);
        AdapterView.OnItemClickListener onItemClickListener =
                pintList.getOnItemClickListener();

        // Act
        onItemClickListener.onItemClick(pintList, mockView, 0, 0);

        // setup verfication intent was started
        ShadowActivity shadowActivity = shadowOf(homeActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);

        // Assert
        assertEquals(shadowIntent.getIntentClass(), DetailedActivity.class);
    }

    // DefaultLocationListener *************************************************

    @Test
    public void DefaultLocationListenerShouldCreateSuccessfully() throws Exception {
        HomeActivity activity = new HomeActivity();
        Object defaultLocationListener = getDefaultLocationListener(activity);
        assertNotNull(defaultLocationListener);
    }

    @Test
    public void DefaultLocationListenerOnLocationChangedShouldChangeCoordinates() throws Exception {
        HomeActivity activity = new HomeActivity();
        Object defaultLocationListener = getDefaultLocationListener(activity);
        Location location = new Location("GPS");

        double expectedLatitude = 25.7617;
        double expectedLongitude = 80.1918;
        location.setLatitude(expectedLatitude);
        location.setLongitude(expectedLongitude);

        // get private method via reflection (http://stackoverflow.com/q/34571)
        String methodName = "onLocationChanged";
        Method method = defaultLocationListener.getClass()
                        .getDeclaredMethod(methodName, Location.class);
        method.setAccessible(true);

        method.invoke(defaultLocationListener, location);

        // get private fields for HomeActivity class
        Field latitude = activity.getClass().getDeclaredField("latitude");
        Field longitude = activity.getClass().getDeclaredField("longitude");
        latitude.setAccessible(true);
        longitude.setAccessible(true);

        // get values from fields
        double actualLatitude = latitude.getDouble(activity);
        double actualLongitude = longitude.getDouble(activity);

        // Assert
        assertEquals(expectedLatitude, actualLatitude);
        assertEquals(expectedLongitude, actualLongitude);
    }

    @Test
    public void DefaultLocationListenerOnLocationChangedShouldChangeCity() throws Exception {
        HomeActivity activity = new HomeActivity();
        Object defaultLocationListener = getDefaultLocationListener(activity);

        // expected output
        String expectedCity = "Miami";
        String expectedState = "FL";

        // arrange mock location behavior
        Location location = mock(Location.class);
        when(location.getLatitude()).thenReturn(25.7617);
        when(location.getLongitude()).thenReturn(80.1918);

        // get private method via reflection (http://stackoverflow.com/q/34571)
        String methodName = "onLocationChanged";
        Method method = defaultLocationListener.getClass()
                .getDeclaredMethod(methodName, Location.class);
        method.setAccessible(true);

        method.invoke(defaultLocationListener, location);

        // get private fields for HomeActivity class
        Field city = activity.getClass().getDeclaredField("city");
        Field state = activity.getClass().getDeclaredField("state");
        city.setAccessible(true);
        state.setAccessible(true);

        // get values from fields
        String actualCity = (String) city.get(activity);
        String actualState = (String) state.get(activity);

        // Assert
        assertEquals(expectedCity, actualCity);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void DefaultLocationListenerShouldHaveOnStatusChanged() {
        HomeActivity activity = new HomeActivity();
        Object defaultLocationListener = getDefaultLocationListener(activity);

        String methodName = "onStatusChanged";
        Bundle mockBundle = mock(Bundle.class);

        try {
            Method method = defaultLocationListener.getClass()
                    .getDeclaredMethod(methodName, String.class, int.class, Bundle.class);
            method.setAccessible(true);

            try {
                method.invoke(defaultLocationListener, "", 0, mockBundle);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                fail();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                fail();
            } // end inner try-catch
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail();
        } // end try-catch
        return; // success
    }

    @Test
    public void DefaultLocationListenerShouldHaveOnProviderEnabled() {
        HomeActivity activity = new HomeActivity();
        Object defaultLocationListener = getDefaultLocationListener(activity);

        String methodName = "onProviderEnabled";

        try {
            Method method = defaultLocationListener.getClass()
                    .getDeclaredMethod(methodName, String.class);
            method.setAccessible(true);

            try {
                method.invoke(defaultLocationListener, "");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                fail();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                fail();
            } // end inner try-catch
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail();
        } // end try-catch
        return; // success
    }

    @Test
    public void DefaultLocationListenerShouldHaveOnProviderDisabled() {
        HomeActivity activity = new HomeActivity();
        Object defaultLocationListener = getDefaultLocationListener(activity);

        String methodName = "onProviderDisabled";

        try {
            Method method = defaultLocationListener.getClass()
                    .getDeclaredMethod(methodName, String.class);
            method.setAccessible(true);

            try {
                method.invoke(defaultLocationListener, "");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                fail();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                fail();
            } // end inner try-catch
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail();
        } // end try-catch
        return; // success
    }

    // Misc *******************************************************************

    /**
     * Get an instance of the private inner class DefaultLocationListener found
     * in HomeActivity.
     * Credit goes to: http://stackoverflow.com/a/14112262
     * @return an Object representing the DefaultLocationListener
     * @throws Exception
     */
    private static Object getDefaultLocationListener(HomeActivity outerObject) {
        Constructor<?> constructor = null;
        try {
            Class<?> innerClass = Class
                    .forName("pintapp.pint.com.pint.HomeActivity$DefaultLocationListener");
            constructor = innerClass
                    .getDeclaredConstructor(HomeActivity.class);
            constructor.setAccessible(true);
            return constructor.newInstance(outerObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(constructor);
        return constructor;
    }

    /**
     * Lets me change a static field through reflection.
     * Credit goes to toshkinl: http://stackoverflow.com/a/38074424
     * @param field field to be changed
     * @param newValue new value of field
     * @throws Exception
     */
    private static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }
}
