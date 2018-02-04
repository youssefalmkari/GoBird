package space.personal.youssefalmkari.goherp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

/**
 * Created by Yose on 1/23/2018.
 */

public class SignInActivity extends AppCompatActivity
        implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "Account Sign In: ";
    private LinearLayout displaySection;
    private ImageView userPhoto;
    private TextView name, email;
    private SignInButton signIn;
    private Button signOut;
    private Button btnContinue;
    private Button btnSkip;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int REQ_CODE_SIGN_IN = 101;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Bind
        displaySection = findViewById(R.id.display_section);
        userPhoto = findViewById(R.id.user_photo);
        name = findViewById(R.id.user_name);
        email = findViewById(R.id.email_display);
        btnContinue = findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(this);
        btnSkip = findViewById(R.id.btn_skip);
        btnSkip.setOnClickListener(this);
        signIn = findViewById(R.id.sign_in);
        signIn.setOnClickListener(this);
        signOut = findViewById(R.id.sign_out);
        signOut.setOnClickListener(this);

        //Hide sections
        displaySection.setVisibility(View.GONE);
        btnContinue.setVisibility(View.GONE);
        signOut.setVisibility(View.GONE);



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
            String userName = account.getDisplayName();
            String userEmail = account.getEmail();
            Uri imageURL = account.getPhotoUrl();
            Picasso.with(this).load(imageURL)
                    .resize(250, 250)
                    .into(userPhoto);
            name.setText(userName);
            email.setText(userEmail);
            signIn.setVisibility(View.GONE);
            btnSkip.setVisibility(View.GONE);
            displaySection.setVisibility(View.VISIBLE);
            btnContinue.setVisibility(View.VISIBLE);
            signOut.setVisibility(View.VISIBLE);
        } else {
            signIn.setVisibility(View.VISIBLE);
            btnSkip.setVisibility(View.VISIBLE);
            displaySection.setVisibility(View.GONE);
            btnContinue.setVisibility(View.GONE);
            signOut.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.sign_in:
                signIn();
                break;
            case R.id.btn_continue:
                goToNextActivity();
                break;
            case R.id.sign_out:
                signOut();
                break;
            case R.id.btn_skip:
                goToNextActivity();
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

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // respond to the event and trigger any appropriate
                        // logic in app or your back-end code
                        handleSignOutResult(task);
                    }
                });
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

    private void handleSignOutResult(Task<Void> completedTask) {
        if (completedTask.isSuccessful()) {
            updateUI(null);
        }
    }

    private void goToNextActivity() {
        Intent intent = new Intent(this, SpeechToTextActivity.class);
        startActivity(intent);
    }
}
