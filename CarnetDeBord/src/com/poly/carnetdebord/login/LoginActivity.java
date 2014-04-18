package com.poly.carnetdebord.login;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;
import org.json.JSONObject;

import com.poly.carnetdebord.R;
import com.poly.carnetdebord.R.id;
import com.poly.carnetdebord.R.layout;
import com.poly.carnetdebord.R.menu;
import com.poly.carnetdebord.R.string;
import com.poly.carnetdebord.service.ILocalService;
import com.poly.carnetdebord.service.IWebService;
import com.poly.carnetdebord.service.LocalService;
import com.poly.carnetdebord.service.Response;
import com.poly.carnetdebord.service.WebService;
import com.poly.carnetdebord.service.WebService.RequestMethod;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world" };

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	private static final String FICHIER_PROPERTIES = "utils.properties";
	private static final String LOGIN_URL = "http://serveur10.lerb.polymtl.ca:8080/CarnetDeBord/webresources/login";
	public static final Logger logger = Logger.getLogger(LoginActivity.class.getName());

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	
	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	private String mEncryptedPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private InputStream fichierProperties;
	private IWebService webService = new WebService(this, WebService.RequestMethod.PUT);
	private ILocalService localService = LocalService.getInstance(this);
	
	private Encryption crypto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		 crypto = new Encryption(this);
		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		
		
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		
		findViewById(R.id.register).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						localService.goRegister();
					}
					
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}
/*
	private byte[] encryptPassword(String content) throws IOException
	{
		content = Base64.encodeToString(content.getBytes(), Base64.DEFAULT);
		
		//      KeyGenerator kg;
		//      try {
		//          kg = KeyGenerator.getInstance("AES");
		//      } catch (NoSuchAlgorithmException e) {
		//          logger.log(Level.SEVERE, "AES algorithm not supported", e);
		//          return null;
		//      }
		
		fichierProperties = getResources().getAssets().open(FICHIER_PROPERTIES);
		 Properties properties = new Properties();
		 if ( fichierProperties == null ) {
	            throw new IOException( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
	        }

		  properties.load(fichierProperties);
		  String strKey = properties.getProperty("keyAES");
		  SecretKey key = new SecretKeySpec(Base64.decode(strKey, Base64.DEFAULT), "AES");
		
		  try {
		      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		      cipher.init(Cipher.ENCRYPT_MODE, key);
		  return cipher.doFinal(content.getBytes("UTF8"));
		  } catch (NoSuchPaddingException e) {
			  logger.log(Level.SEVERE, "padding problem", e);
		  } catch (NoSuchAlgorithmException e) {
			  logger.log(Level.SEVERE, "algorithm problem", e);
		  } catch (InvalidKeyException e) {
			  logger.log(Level.SEVERE, "key invalid", e);
		  } catch (BadPaddingException e) {
			  logger.log(Level.SEVERE, "bad padding", e);
		  } catch (IllegalBlockSizeException e) {
			  logger.log(Level.SEVERE, "illegal block size", e);
		  } catch (UnsupportedEncodingException e) {
		      logger.log(Level.SEVERE, "Bad encoding", e);
		  }
		
		  return null;
	}*/
	
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			try {
				mEncryptedPassword = crypto.encode(mPassword);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JSONObject json = new JSONObject();
			try {
//				json.put("login", mEmail);
//				json.put("password", mEncryptedPassword);
				json.put("login", "random@wawa.com");
				json.put("password", "testtest");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Response res = webService.sendPutRequest(LOGIN_URL,json.toString());
			if(res.getStatus() != Response.BAD_REQUEST)
			{
				System.out.println("valid");
				//return true;
			}
			else
			{
				System.out.println("invalid");
				System.out.println(res.getStatus());
			}
			
			
			System.out.println(json.toString());
			/*new AlertDialog.Builder(getApplicationContext())
		    .setTitle("Affichage inputs")
		    .setMessage(json.toString()).show();*/
			// TODO: register the new account here.
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				finish();
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
