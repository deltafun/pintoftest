package com.pint.pintapp;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pintapp.pint.com.pint.HomeActivity;
import pintapp.pint.com.pint.PintNetworking.IPintAPI;

import static org.mockito.Mockito.*;

public class HomeActivityListFragmentTest {

    @Mock
    private IPintAPI mockIPintAPI;

    private HomeActivity.ListFragment listFragment;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void setUp() {
    }

}
