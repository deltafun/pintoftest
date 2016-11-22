package pintapp.pint.com.pint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pintapp.pint.com.pint.PintNetworking.DefaultTokenProvider;
import pintapp.pint.com.pint.PintNetworking.ITokenProvider;

public class MainActivity extends AppCompatActivity {
    private ITokenProvider tokenProvider;

    public MainActivity() {
        super();
    }

    public MainActivity(ITokenProvider tokenProvider) {
        super();
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent nextActivity;

        if(tokenProvider == null) {
            this.tokenProvider = new DefaultTokenProvider(this);
        }


        if(tokenProvider.hasToken(this)) {
            nextActivity = new Intent(this, HomeActivity.class);
            startActivity(nextActivity);
        } else {
            nextActivity = new Intent(this, LoginActivity.class);
            startActivity(nextActivity);
        }

    }
}
