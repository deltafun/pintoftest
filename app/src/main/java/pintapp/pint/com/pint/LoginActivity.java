package pintapp.pint.com.pint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import pintapp.pint.com.pint.PintNetworking.DefaultTokenProvider;
import pintapp.pint.com.pint.PintNetworking.ITokenProvider;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, PintRequest {

    private ITokenProvider tokenProvider;

    public LoginActivity() {
        super();
    }

    public LoginActivity(ITokenProvider tokenProvider) {
        super();
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(tokenProvider == null) {
            this.tokenProvider = new DefaultTokenProvider(this);
        }

        Button loginButtonLogin = (Button) findViewById(R.id.loginButtonLogin);
        loginButtonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d("LoginActivity", "Registers click");
        if(v.getId() == R.id.loginButtonLogin) {
            Log.d("LoginActivity", "Begins login process");
            EditText loginTextEmail = (EditText) findViewById(R.id.loginTextEmail);
            EditText loginTextPassword = (EditText) findViewById(R.id.loginTextPassword);

            String email = loginTextEmail.getText().toString();
            String password = loginTextPassword.getText().toString();

            tokenProvider.fetchToken(email, password, this);
        }
    }

    @Override
    public void pintResponse(JSONObject jsonObject) {
        if(jsonObject == null) {
            Toast.makeText(this, "Login Email/Password is incorrect", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d("LoginActivity", jsonObject.toString());

        try {
            tokenProvider.setToken(jsonObject.getString("X-AUTH-TOKEN"), this);
            tokenProvider.setEmail(jsonObject.getString("email"), this);
            Intent nextActivity = new Intent(this, HomeActivity.class);
            startActivity(nextActivity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
