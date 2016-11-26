package com.pint.pintapp;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import pintapp.pint.com.pint.HomeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Houses tests that try to use PowerMock
 */
@PrepareForTest(HomeActivity.class)
public abstract class HomeActivityPowerTest extends RobolectricTest {

//    @Test
//    public void testGetLocationFromAddress() throws Exception {
//        Geocoder geocoder = Mockito.mock(Geocoder.class);
//        List<Address> addresses = new ArrayList<>();
//        String strAddress = "123";
//        Address address = Mockito.mock(Address.class);
//        addresses.add(address);
//        Context context = Mockito.mock(Context.class);
//
//        // NullPointerException when `coder` in method calls a service.
//        // I.e. the `new` is being executed in getLocationFromAddress
//        // TODO: figure out why `whenNew` isn't doing anything.
//        PowerMockito.whenNew(Geocoder.class).withAnyArguments().thenReturn(geocoder);
//        Mockito.when(geocoder.getFromLocationName(strAddress, 5)).thenReturn(null);
//
//        HomeActivity homeActivity = new HomeActivity();
//
//        homeActivity.getLocationFromAddress(context, strAddress);
//    }
}
