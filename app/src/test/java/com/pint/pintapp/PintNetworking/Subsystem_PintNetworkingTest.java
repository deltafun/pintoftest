package com.pint.pintapp.PintNetworking;

import com.android.volley.Network;
import com.android.volley.ResponseDelivery;
import com.android.volley.toolbox.Volley;
import com.android.volley.utils.ImmediateResponseDelivery;
import com.pint.pintapp.PintNetworking.mock.MockActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import org.mockito.Mock;

import pintapp.pint.com.pint.PintNetworking.CustomRequest;
import pintapp.pint.com.pint.PintNetworking.DefaultTokenProvider;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class Subsystem_PintNetworkingTest extends MockActivity {

    private ResponseDelivery mDelivery;
    @Mock private Network mMockNetwork;

    @Mock
    private Volley volley;

    @InjectMocks
    DefaultTokenProvider dfp = new DefaultTokenProvider(mLoginActivity);

    @Before
    public void setUp() {
        mDelivery = new ImmediateResponseDelivery();
        initMocks(this);
    }

    @Test
    public void validateLogin() {
        String email = "email";
        String password = "password";
        //mRequestQueue = new RequestQueue(new NoCache(), mMockNetwork, 0, mDelivery);
        try {
            when(Volley.newRequestQueue(mLoginActivity)).thenReturn(mRequestQueue);
            mTokenProvider = new DefaultTokenProvider(mLoginActivity);

            mTokenProvider.fetchToken(email, password, mLoginActivity);
        } catch (Exception e) {}

        verify(mRequestQueue, atLeastOnce()).add(any(CustomRequest.class));
    }
}
