package com.pint.pintapp.PintNetworking.mock;

import com.android.volley.RequestQueue;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pintapp.pint.com.pint.DetailedActivity;
import pintapp.pint.com.pint.HomeActivity;
import pintapp.pint.com.pint.LoginActivity;
import pintapp.pint.com.pint.MainActivity;
import pintapp.pint.com.pint.PintNetworking.ITokenProvider;

@RunWith(MockitoJUnitRunner.class)
public class MockActivity {

    @Mock
    protected ITokenProvider mTokenProvider;

    @Mock
    protected RequestQueue mRequestQueue;

    @InjectMocks
    protected DetailedActivity mDetailedActivity = new DetailedActivity();

    @InjectMocks
    protected HomeActivity mHomeActivity = new HomeActivity();

    @InjectMocks
    protected LoginActivity mLoginActivity = new LoginActivity();

    @InjectMocks
    protected MainActivity mMainActivity = new MainActivity();

}
