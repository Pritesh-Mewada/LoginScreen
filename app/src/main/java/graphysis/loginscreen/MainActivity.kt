package graphysis.loginscreen

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import android.R.attr.data
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.facebook.*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import com.facebook.login.LoginResult
import com.facebook.login.LoginManager
import com.facebook.login.widget.LoginButton
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    lateinit var gso:GoogleSignInOptions;
    lateinit var mGoogleSignInClient:GoogleSignInClient;
    lateinit var googleSignInButton:SignInButton;
    lateinit var callbackManager :CallbackManager;
    lateinit var loginButton:LoginButton;
    lateinit var signUpText:TextView;
    var RC_SIGN_IN:Int;
    var TAG:String;
    init {
        RC_SIGN_IN=91;
        TAG ="Google"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInButton = findViewById(R.id.google_sign_in) ;

        googleSignInButton.setOnClickListener(View.OnClickListener {
            var  signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        })

        callbackManager = CallbackManager.Factory.create();

        signUpText = findViewById(R.id.signup_text);

        signUpText.setOnClickListener {
            var intent:Intent = Intent(applicationContext,SignUpActivity::class.java);
            startActivity(intent)
        }


        loginButton = findViewById<View>(R.id.login_button) as LoginButton
        loginButton.setReadPermissions("email")
        // If using in a fragment



        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        // App code
                        var accessToken: AccessToken =loginResult.accessToken;
                        var profile: Profile = Profile.getCurrentProfile();

                        Log.d(TAG, profile.name);

                        var request :GraphRequest = GraphRequest.newMeRequest(accessToken,object :GraphRequest.GraphJSONObjectCallback{
                            override fun onCompleted(`object`: JSONObject?, response: GraphResponse?) {
                                Log.d(TAG, `object`?.get("email") as String?);
                            }

                        })
                        var parameters:Bundle = Bundle();
                        parameters.putString("fields","email");
                        request.parameters = parameters;
                        request.executeAsync();
                    }

                    override fun onCancel() {
                        // App code
                    }

                    override fun onError(exception: FacebookException) {
                        // App code
                    }
                });
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            Log.d(TAG,account.email);
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

        }

    }



}
