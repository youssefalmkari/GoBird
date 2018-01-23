package space.personal.youssefalmkari.goherp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

/**
 * Created by Yose on 1/23/2018.
 */

public class SignInActivity extends AppCompatActivity
        implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "Account Sign In: ";
    private LinearLayout displaySection;
    private TextView name, email;
    private SignInButton signIn;
    private Button btnContinue;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int REQ_CODE_SIGN_IN = 101;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Bind
        displaySection = findViewById(R.id.display_section);
        name = findViewById(R.id.user_name);
        email = findViewById(R.id.email_display);
        btnContinue = findViewById(R.id.btn_continue);
        btnContinue.setVisibility(View.GONE);
        btnContinue.setOnClickListener(this);
        signIn = findViewById(R.id.sign_in);
        signIn.setOnClickListener(this);

        //Hide display section
        displaySection.setVisibility(View.GONE);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(getString(R.string.server_client_id))
                .requestProfile()
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {

        if(account != null) {
            displaySection.setVisibility(View.VISIBLE);
            String userName = account.getDisplayName();
            String userEmail = account.getEmail();
            name.setText(userName);
            email.setText(userEmail);
            signIn.setVisibility(View.GONE);
            btnContinue.setVisibility(View.VISIBLE);
        } else {

        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.sign_in:
                signIn();
                break;
            case R.id.btn_continue:
                Intent intent = new Intent(this, SpeechToTextActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQ_CODE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == REQ_CODE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
}
