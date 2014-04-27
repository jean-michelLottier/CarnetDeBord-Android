package com.poly.carnetdebord.login;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.security.acl.LastOwnerException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.carnetdebord.R;
import com.poly.carnetdebord.R.id;
import com.poly.carnetdebord.R.layout;
import com.poly.carnetdebord.R.menu;
import com.poly.carnetdebord.R.string;
import com.poly.carnetdebord.utilities.AppMode;
import com.poly.carnetdebord.utilities.DatePickerFragment;
import com.poly.carnetdebord.utilities.Encryption;
import com.poly.carnetdebord.utilities.User;
import com.poly.carnetdebord.webservice.IWebService;
import com.poly.carnetdebord.webservice.WebService;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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
public class RegisterActivity extends Activity {

	private static final String LOGIN_URL = "http://serveur10.lerb.polymtl.ca:8080/CarnetDeBord/webresources/login";
	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	private String mEncryptedPassword;
	private String mFirstname;
	private String mLastname;
	private String mConfirmPassword;
	private String mBirthday;

	private User user = new User();

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private EditText mFirstnameView;
	private EditText mLastnameView;
	private EditText mConfirmPasswordView;
	private EditText mBirthdayView;

	private Encryption crypto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register);
		AppMode.getInstance().setMode(AppMode.REGISTER);

		crypto = new Encryption(this);

		// Set up the login form.
		mEmailView = (EditText) findViewById(R.id.register_email);
		mEmailView.setText(mEmail);

		mFirstnameView = (EditText) findViewById(R.id.register_firstname);
		mLastnameView = (EditText) findViewById(R.id.register_lastname);
		mBirthdayView = (EditText) findViewById(R.id.register_birthday);
		mPasswordView = (EditText) findViewById(R.id.register_password);

		mConfirmPasswordView = (EditText) findViewById(R.id.register_confirmPassword);

		findViewById(R.id.register_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});

		mBirthdayView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getFragmentManager(), "datePicker");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);
		mConfirmPasswordView.setError(null);
		mLastnameView.setError(null);
		mFirstnameView.setError(null);
		mBirthdayView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		mConfirmPassword = mConfirmPasswordView.getText().toString();
		mLastname = mLastnameView.getText().toString();
		mFirstname = mFirstnameView.getText().toString();
		mBirthday = mBirthdayView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(mConfirmPassword)) {
			mConfirmPasswordView
					.setError(getString(R.string.error_field_required));
			focusView = mConfirmPasswordView;
			cancel = true;
		} else if (!mConfirmPassword.equals(mPassword)) {
			mConfirmPasswordView
					.setError(getString(R.string.error_confirm_password));
			focusView = mConfirmPasswordView;
			cancel = true;
		}

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

		if (TextUtils.isEmpty(mBirthday)) {
			mBirthdayView.setError(getString(R.string.error_field_required));
			focusView = mBirthdayView;
			cancel = true;
		}

		if (TextUtils.isEmpty(mFirstname)) {
			mFirstnameView.setError(getString(R.string.error_field_required));
			focusView = mFirstnameView;
			cancel = true;
		}

		if (TextUtils.isEmpty(mLastname)) {
			mLastnameView.setError(getString(R.string.error_field_required));
			focusView = mLastnameView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {

			try {
				sendData();
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void resetForm() {
		mPasswordView.setText("");
		mConfirmPasswordView.setText("");
		mEmailView.setText("");
		mEmailView.requestFocus();
	}

	protected void sendData() throws JsonGenerationException,
			JsonMappingException, IOException {
		// TODO: attempt authentication against a network service.

		mEncryptedPassword = crypto.encode(mPassword);

		user.setName(mLastname);
		user.setFirstname(mFirstname);
		user.setLogin(mEmail);
		// Date date = null;
		// SimpleDateFormat format = new
		// SimpleDateFormat("yy/mm/dd",Locale.CANADA_FRENCH);
		// try {
		// date = format.parse(mBirthday);
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		System.out.println(mBirthday);
		user.setStringBirthdate(mBirthday);
		System.out.println(user.getBirthdate());
		user.setPassword(mEncryptedPassword);

		ObjectMapper mapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		mapper.setDateFormat(df);
		Writer stringWriter = new StringWriter();
		mapper.writeValue(stringWriter, user);

		WebService webService = new WebService(RegisterActivity.this,
				WebService.RequestMethod.POST, stringWriter.toString());
		webService.execute(LOGIN_URL);

		System.out.println(stringWriter.toString());
		// TODO: register the new account here.
	}
}
